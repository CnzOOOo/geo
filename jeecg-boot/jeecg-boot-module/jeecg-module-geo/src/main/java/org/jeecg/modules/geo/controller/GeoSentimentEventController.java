package org.jeecg.modules.geo.controller;

import java.util.Arrays;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.geo.entity.GeoSentimentEvent;
import org.jeecg.modules.geo.service.IGeoSentimentEventService;
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
 * GEO sentiment event controller.
 */
@Slf4j
@RestController
@RequestMapping("/geo/sentimentEvent")
@Tag(name = "GEO sentiment event")
public class GeoSentimentEventController {

    @Autowired
    private IGeoSentimentEventService geoSentimentEventService;

    @Operation(summary = "Page list GEO sentiment events")
    @GetMapping("/list")
    public Result<?> list(GeoSentimentEvent geoSentimentEvent,
                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest request) {
        QueryWrapper<GeoSentimentEvent> queryWrapper = QueryGenerator.initQueryWrapper(geoSentimentEvent, request.getParameterMap());
        queryWrapper.orderByDesc("create_time");
        Page<GeoSentimentEvent> page = new Page<>(pageNo, pageSize);
        IPage<GeoSentimentEvent> pageList = geoSentimentEventService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @Operation(summary = "Add GEO sentiment event")
    @PostMapping("/add")
    public Result<?> add(@RequestBody GeoSentimentEvent geoSentimentEvent) {
        geoSentimentEventService.save(geoSentimentEvent);
        return Result.OK("Added");
    }

    @Operation(summary = "Edit GEO sentiment event")
    @PostMapping("/edit")
    public Result<?> edit(@RequestBody GeoSentimentEvent geoSentimentEvent) {
        geoSentimentEventService.updateById(geoSentimentEvent);
        return Result.OK("Updated");
    }

    @Operation(summary = "Delete GEO sentiment event")
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        geoSentimentEventService.removeById(id);
        return Result.OK("Deleted");
    }

    @Operation(summary = "Batch delete GEO sentiment events")
    @DeleteMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        geoSentimentEventService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("Deleted");
    }

    @Operation(summary = "Query GEO sentiment event by id")
    @GetMapping("/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        return Result.OK(geoSentimentEventService.getById(id));
    }
}
