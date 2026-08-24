package org.jeecg.modules.geo.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.geo.seo.GeoSeoArtifactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * GEO SEO artifact controller.
 */
@Slf4j
@RestController
@RequestMapping("/geo/seo")
@Tag(name = "GEO SEO artifact")
public class GeoSeoController {

    @Autowired
    private GeoSeoArtifactService geoSeoArtifactService;

    @Operation(summary = "Generate llms.txt")
    @GetMapping("/llms-txt")
    public Result<?> llmsTxt(@RequestParam(name = "merchantId", required = true) String merchantId) {
        try {
            return Result.OK(geoSeoArtifactService.generateLlmsTxt(merchantId));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "Generate XML sitemap")
    @GetMapping("/sitemap")
    public Result<?> sitemap(@RequestParam(name = "merchantId", required = true) String merchantId) {
        try {
            return Result.OK(geoSeoArtifactService.generateSitemap(merchantId));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "Generate LocalBusiness JSON-LD")
    @GetMapping("/local-business-schema")
    public Result<?> localBusinessSchema(@RequestParam(name = "merchantId", required = true) String merchantId) {
        try {
            JSONObject schema = geoSeoArtifactService.generateLocalBusinessSchema(merchantId);
            return Result.OK(schema);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
