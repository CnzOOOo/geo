package org.jeecg.modules.geo.seo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.jeecg.modules.geo.entity.GeoArticle;
import org.jeecg.modules.geo.entity.GeoMerchant;
import org.jeecg.modules.geo.service.IGeoArticleService;
import org.jeecg.modules.geo.service.IGeoMerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

/**
 * Generates AI-readable SEO artifacts from merchant data.
 */
@Service
public class GeoSeoArtifactService {

    @Autowired
    private IGeoMerchantService geoMerchantService;

    @Autowired
    private IGeoArticleService geoArticleService;

    @Autowired
    private GeoSeoProperties geoSeoProperties;

    public String generateLlmsTxt(String merchantId) {
        GeoMerchant merchant = requireMerchant(merchantId);
        List<GeoArticle> articles = publishedArticles(merchantId);

        StringBuilder content = new StringBuilder();
        content.append("# ").append(merchant.getMerchantName()).append("\n");
        content.append("> ").append(valueOrEmpty(merchant.getDescription())).append("\n\n");
        content.append("## 商家信息\n");
        appendLine(content, "地址", merchant.getAddress());
        appendLine(content, "电话", merchant.getPhone());
        appendLine(content, "营业时间", merchant.getOpeningHours());
        appendLine(content, "服务范围", merchant.getServiceArea());
        content.append("\n## 核心内容\n");
        if (articles.isEmpty()) {
            content.append("- 暂无已发布文章\n");
        } else {
            for (GeoArticle article : articles) {
                content.append("- ").append(article.getTitle()).append(": ").append(article.getCanonicalUrl()).append("\n");
            }
        }
        return content.toString();
    }

    public String generateSitemap(String merchantId) {
        GeoMerchant merchant = requireMerchant(merchantId);
        requireBaseUrl();
        List<GeoArticle> articles = publishedArticles(merchantId);

        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");
        xml.append("  <url>\n");
        xml.append("    <loc>").append(baseUrl()).append("</loc>\n");
        xml.append("    <lastmod>").append(today()).append("</lastmod>\n");
        xml.append("    <priority>1.0</priority>\n");
        xml.append("  </url>\n");
        for (GeoArticle article : articles) {
            String loc = article.getCanonicalUrl();
            if (loc == null || loc.isBlank()) {
                loc = baseUrl() + "/articles/" + article.getId();
            }
            xml.append("  <url>\n");
            xml.append("    <loc>").append(escapeXml(loc)).append("</loc>\n");
            xml.append("    <lastmod>").append(today()).append("</lastmod>\n");
            xml.append("    <priority>0.8</priority>\n");
            xml.append("  </url>\n");
        }
        xml.append("</urlset>\n");
        return xml.toString();
    }

    public JSONObject generateLocalBusinessSchema(String merchantId) {
        GeoMerchant merchant = requireMerchant(merchantId);
        JSONObject schema = new JSONObject();
        schema.put("@context", "https://schema.org");
        schema.put("@type", "LocalBusiness");
        schema.put("name", merchant.getMerchantName());
        if (merchant.getDescription() != null) {
            schema.put("description", merchant.getDescription());
        }
        schema.put("telephone", merchant.getPhone());
        schema.put("openingHours", merchant.getOpeningHours());
        schema.put("url", merchant.getWebsite());

        JSONObject address = new JSONObject();
        address.put("@type", "PostalAddress");
        address.put("addressRegion", merchant.getProvince());
        address.put("addressLocality", merchant.getCity());
        address.put("streetAddress", merchant.getAddress());
        schema.put("address", address);

        if (merchant.getLat() != null && merchant.getLng() != null) {
            JSONObject geo = new JSONObject();
            geo.put("@type", "GeoCoordinates");
            geo.put("latitude", merchant.getLat());
            geo.put("longitude", merchant.getLng());
            schema.put("geo", geo);
        }
        return schema;
    }

    private GeoMerchant requireMerchant(String merchantId) {
        if (merchantId == null || merchantId.isBlank()) {
            throw new IllegalArgumentException("merchantId is required");
        }
        GeoMerchant merchant = geoMerchantService.getById(merchantId);
        if (merchant == null) {
            throw new IllegalArgumentException("merchant not found");
        }
        return merchant;
    }

    private List<GeoArticle> publishedArticles(String merchantId) {
        return geoArticleService.lambdaQuery()
                .eq(GeoArticle::getMerchantId, merchantId)
                .eq(GeoArticle::getStatus, 2)
                .orderByDesc(GeoArticle::getPublishedAt)
                .list();
    }

    private void requireBaseUrl() {
        if (baseUrl() == null || baseUrl().isBlank()) {
            throw new IllegalStateException("geo.site.base-url is not configured");
        }
    }

    private String baseUrl() {
        String value = geoSeoProperties.getBaseUrl();
        return value == null ? "" : value.replaceAll("/+$", "");
    }

    private String today() {
        return LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    private void appendLine(StringBuilder content, String label, String value) {
        if (value != null && !value.isBlank()) {
            content.append("- ").append(label).append(": ").append(value).append("\n");
        }
    }

    private String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }

    private String escapeXml(String value) {
        return value == null ? "" : value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
