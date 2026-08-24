package org.jeecg.modules.geo.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.jeecg.modules.geo.ai.GeoAiClient;
import org.jeecg.modules.geo.entity.GeoKnowledgeItem;
import org.jeecg.modules.geo.entity.GeoMerchant;
import org.jeecg.modules.geo.entity.GeoQuestionBank;
import org.jeecg.modules.geo.entity.GeoArticle;
import org.jeecg.modules.geo.mapper.GeoArticleMapper;
import org.jeecg.modules.geo.service.IGeoArticleService;
import org.jeecg.modules.geo.service.IGeoKnowledgeItemService;
import org.jeecg.modules.geo.service.IGeoMerchantService;
import org.jeecg.modules.geo.service.IGeoQuestionBankService;
import org.jeecg.modules.geo.vo.GeoArticleGenerateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * GEO article service implementation.
 */
@Service
public class GeoArticleServiceImpl extends ServiceImpl<GeoArticleMapper, GeoArticle> implements IGeoArticleService {

    @Autowired
    private IGeoMerchantService geoMerchantService;

    @Autowired
    private IGeoQuestionBankService geoQuestionBankService;

    @Autowired
    private IGeoKnowledgeItemService geoKnowledgeItemService;

    @Autowired
    private GeoAiClient geoAiClient;

    @Override
    public void submitForReview(String id) {
        GeoArticle article = getById(id);
        if (article == null) {
            return;
        }
        article.setStatus(1);
        article.setReviewStatus(1);
        updateById(article);
    }

    @Override
    public void publish(String id) {
        GeoArticle article = getById(id);
        if (article == null) {
            return;
        }
        article.setStatus(2);
        article.setReviewStatus(2);
        article.setPublishedAt(new Date());
        updateById(article);
    }

    @Override
    public void offline(String id) {
        GeoArticle article = getById(id);
        if (article == null) {
            return;
        }
        article.setStatus(3);
        updateById(article);
    }

    @Override
    public GeoArticle generateDraft(GeoArticleGenerateRequest request) {
        if (request.getMerchantId() == null || request.getMerchantId().isBlank()) {
            throw new IllegalArgumentException("merchantId is required");
        }
        GeoMerchant merchant = geoMerchantService.getById(request.getMerchantId());
        if (merchant == null) {
            throw new IllegalArgumentException("merchant not found");
        }
        GeoQuestionBank question = null;
        if (request.getQuestionId() != null && !request.getQuestionId().isBlank()) {
            question = geoQuestionBankService.getById(request.getQuestionId());
        }

        List<GeoKnowledgeItem> knowledge = geoKnowledgeItemService.lambdaQuery()
                .eq(GeoKnowledgeItem::getMerchantId, request.getMerchantId())
                .eq(GeoKnowledgeItem::getStatus, 1)
                .list();

        String title = request.getTitle();
        if (title == null || title.isBlank()) {
            title = question == null ? merchant.getMerchantName() + " 选购指南" : question.getQuestion();
        }

        String systemPrompt = "你是一名 GEO 内容策略师。你的任务是基于真实商家知识库生成符合 E-E-A-T 和三段式结构的文章草稿。"
                + "不能编造地址、价格、资质、评价或案例。所有事实必须来自提供的知识库。";
        String userPrompt = buildUserPrompt(merchant, question, knowledge, title, request.getTitleType());
        String content = geoAiClient.chat(systemPrompt, userPrompt);

        GeoArticle article = new GeoArticle();
        article.setMerchantId(merchant.getId());
        article.setQuestionId(request.getQuestionId());
        article.setTitle(title);
        article.setTitleType(request.getTitleType());
        article.setSummary(firstLine(content));
        article.setContentMd(content);
        article.setStatus(0);
        article.setReviewStatus(0);
        save(article);
        return article;
    }

    private String buildUserPrompt(GeoMerchant merchant, GeoQuestionBank question, List<GeoKnowledgeItem> knowledge,
                                   String title, String titleType) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("商家名称：").append(merchant.getMerchantName()).append("\n");
        prompt.append("所在城市：").append(merchant.getCity()).append(merchant.getDistrict()).append("\n");
        if (merchant.getAddress() != null) {
            prompt.append("地址：").append(merchant.getAddress()).append("\n");
        }
        prompt.append("标题：").append(title).append("\n");
        if (titleType != null) {
            prompt.append("标题类型：").append(titleType).append("\n");
        }
        if (question != null) {
            prompt.append("用户问题：").append(question.getQuestion()).append("\n");
        }
        prompt.append("知识库事实：\n");
        if (knowledge.isEmpty()) {
            prompt.append("（暂无已核验知识条目，请只写通用框架，不要编造具体事实）\n");
        } else {
            String facts = knowledge.stream()
                    .map(item -> "- " + item.getFact() + ": " + item.getValue())
                    .collect(Collectors.joining("\n"));
            prompt.append(facts).append("\n");
        }
        prompt.append("要求：开头第一句直接给答案，中间使用 1.2.3 分层，结尾给行动建议；"
                + "只使用知识库中的事实；不得虚构来源、评价、销量、资质和案例。");
        return prompt.toString();
    }

    private String firstLine(String content) {
        if (content == null) {
            return null;
        }
        String[] lines = content.split("\n");
        for (String line : lines) {
            if (line != null && !line.isBlank()) {
                return line.trim();
            }
        }
        return content;
    }
}
