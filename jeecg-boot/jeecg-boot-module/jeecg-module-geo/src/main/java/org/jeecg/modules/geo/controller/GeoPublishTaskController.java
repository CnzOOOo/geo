package org.jeecg.modules.geo.controller;

import java.util.Arrays;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.geo.entity.GeoPublishTask;
import org.jeecg.modules.geo.service.IGeoPublishTaskService;
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
 * GEO publish task controller.
 */
@Slf4j
@RestController
@RequestMapping("/geo/publishTask")
@Tag(name = "GEO publish task")
public class GeoPublishTaskController {

    @Autowired
    private IGeoPublishTaskService geoPublishTaskService;

    @Operation(summary = "Page list GEO publish tasks")
    @GetMapping("/list")
    public Result<?> list(GeoPublishTask geoPublishTask,
                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest request) {
        QueryWrapper<GeoPublishTask> queryWrapper = QueryGenerator.initQueryWrapper(geoPublishTask, request.getParameterMap());
        queryWrapper.orderByDesc("create_time");
        Page<GeoPublishTask> page = new Page<>(pageNo, pageSize);
        IPage<GeoPublishTask> pageList = geoPublishTaskService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @Operation(summary = "Add GEO publish task")
    @PostMapping("/add")
    public Result<?> add(@RequestBody GeoPublishTask geoPublishTask) {
        geoPublishTaskService.save(geoPublishTask);
        return Result.OK(geoPublishTask);
    }

    @Operation(summary = "Create and execute GEO publish task")
    @PostMapping("/createAndExecute")
    public Result<?> createAndExecute(@RequestBody GeoPublishTask geoPublishTask) {
        try {
            geoPublishTaskService.save(geoPublishTask);
            return Result.OK(geoPublishTaskService.execute(geoPublishTask.getId()));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "Execute GEO publish task")
    @PostMapping("/execute")
    public Result<?> execute(@RequestParam(name = "id", required = true) String id) {
        try {
            return Result.OK(geoPublishTaskService.execute(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "Edit GEO publish task")
    @PostMapping("/edit")
    public Result<?> edit(@RequestBody GeoPublishTask geoPublishTask) {
        geoPublishTaskService.updateById(geoPublishTask);
        return Result.OK("Updated");
    }

    @Operation(summary = "Delete GEO publish task")
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        geoPublishTaskService.removeById(id);
        return Result.OK("Deleted");
    }

    @Operation(summary = "Batch delete GEO publish tasks")
    @DeleteMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        geoPublishTaskService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("Deleted");
    }

    @Operation(summary = "Query GEO publish task by id")
    @GetMapping("/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        return Result.OK(geoPublishTaskService.getById(id));
    }
}
