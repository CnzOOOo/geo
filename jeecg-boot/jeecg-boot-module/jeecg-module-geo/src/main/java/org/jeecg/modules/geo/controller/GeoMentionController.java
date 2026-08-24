package org.jeecg.modules.geo.controller;

import java.util.Arrays;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.geo.entity.GeoMention;
import org.jeecg.modules.geo.service.IGeoMentionService;
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
 * GEO mention controller.
 */
@Slf4j
@RestController
@RequestMapping("/geo/mention")
@Tag(name = "GEO mention")
public class GeoMentionController {

    @Autowired
    private IGeoMentionService geoMentionService;

    @Operation(summary = "Page list GEO mentions")
    @GetMapping("/list")
    public Result<?> list(GeoMention geoMention,
                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest request) {
        QueryWrapper<GeoMention> queryWrapper = QueryGenerator.initQueryWrapper(geoMention, request.getParameterMap());
        queryWrapper.orderByDesc("occurred_at");
        Page<GeoMention> page = new Page<>(pageNo, pageSize);
        IPage<GeoMention> pageList = geoMentionService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @Operation(summary = "Add GEO mention")
    @PostMapping("/add")
    public Result<?> add(@RequestBody GeoMention geoMention) {
        geoMentionService.save(geoMention);
        return Result.OK("Added");
    }

    @Operation(summary = "Edit GEO mention")
    @PostMapping("/edit")
    public Result<?> edit(@RequestBody GeoMention geoMention) {
        geoMentionService.updateById(geoMention);
        return Result.OK("Updated");
    }

    @Operation(summary = "Delete GEO mention")
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        geoMentionService.removeById(id);
        return Result.OK("Deleted");
    }

    @Operation(summary = "Batch delete GEO mentions")
    @DeleteMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        geoMentionService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("Deleted");
    }

    @Operation(summary = "Query GEO mention by id")
    @GetMapping("/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        return Result.OK(geoMentionService.getById(id));
    }
}
