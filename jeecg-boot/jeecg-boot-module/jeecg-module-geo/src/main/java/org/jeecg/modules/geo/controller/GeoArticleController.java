package org.jeecg.modules.geo.controller;

import java.util.Arrays;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.geo.entity.GeoArticle;
import org.jeecg.modules.geo.service.IGeoArticleService;
import org.jeecg.modules.geo.vo.GeoArticleGenerateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * GEO article controller.
 */
@Slf4j
@RestController
@RequestMapping("/geo/article")
@Tag(name = "GEO article")
public class GeoArticleController {

    @Autowired
    private IGeoArticleService geoArticleService;

    @Operation(summary = "Page list GEO articles")
    @GetMapping("/list")
    public Result<?> list(GeoArticle geoArticle,
                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest request) {
        QueryWrapper<GeoArticle> queryWrapper = QueryGenerator.initQueryWrapper(geoArticle, request.getParameterMap());
        queryWrapper.orderByDesc("create_time");
        Page<GeoArticle> page = new Page<>(pageNo, pageSize);
        IPage<GeoArticle> pageList = geoArticleService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @Operation(summary = "Add GEO article")
    @PostMapping("/add")
    public Result<?> add(@RequestBody GeoArticle geoArticle) {
        geoArticleService.save(geoArticle);
        return Result.OK("Added");
    }

    @Operation(summary = "Generate GEO article draft by AI")
    @PostMapping("/generate")
    public Result<?> generate(@RequestBody GeoArticleGenerateRequest request) {
        try {
            return Result.OK(geoArticleService.generateDraft(request));
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("GEO article generate failed", e);
            return Result.error("AI 初稿生成失败：" + e.getMessage());
        }
    }

    @Operation(summary = "Edit GEO article")
    @PostMapping("/edit")
    public Result<?> edit(@RequestBody GeoArticle geoArticle) {
        geoArticleService.updateById(geoArticle);
        return Result.OK("Updated");
    }

    @Operation(summary = "Submit GEO article for review")
    @PostMapping("/submitReview")
    public Result<?> submitReview(@RequestParam(name = "id", required = true) String id) {
        geoArticleService.submitForReview(id);
        return Result.OK("Submitted");
    }

    @Operation(summary = "Publish GEO article")
    @PostMapping("/publish")
    public Result<?> publish(@RequestParam(name = "id", required = true) String id) {
        geoArticleService.publish(id);
        return Result.OK("Published");
    }

    @Operation(summary = "Offline GEO article")
    @PostMapping("/offline")
    public Result<?> offline(@RequestParam(name = "id", required = true) String id) {
        geoArticleService.offline(id);
        return Result.OK("Offline");
    }

    @Operation(summary = "Delete GEO article")
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        geoArticleService.removeById(id);
        return Result.OK("Deleted");
    }

    @Operation(summary = "Batch delete GEO articles")
    @DeleteMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        geoArticleService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("Deleted");
    }

    @Operation(summary = "Query GEO article by id")
    @GetMapping("/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        return Result.OK(geoArticleService.getById(id));
    }
}
