package org.jeecg.modules.geo.controller;

import java.util.Arrays;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.geo.entity.GeoMonitorTask;
import org.jeecg.modules.geo.service.IGeoMonitorTaskService;
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
 * GEO monitor task controller.
 */
@Slf4j
@RestController
@RequestMapping("/geo/monitorTask")
@Tag(name = "GEO monitor task")
public class GeoMonitorTaskController {

    @Autowired
    private IGeoMonitorTaskService geoMonitorTaskService;

    @Operation(summary = "Page list GEO monitor tasks")
    @GetMapping("/list")
    public Result<?> list(GeoMonitorTask geoMonitorTask,
                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest request) {
        QueryWrapper<GeoMonitorTask> queryWrapper = QueryGenerator.initQueryWrapper(geoMonitorTask, request.getParameterMap());
        queryWrapper.orderByDesc("create_time");
        Page<GeoMonitorTask> page = new Page<>(pageNo, pageSize);
        IPage<GeoMonitorTask> pageList = geoMonitorTaskService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @Operation(summary = "Add GEO monitor task")
    @PostMapping("/add")
    public Result<?> add(@RequestBody GeoMonitorTask geoMonitorTask) {
        geoMonitorTaskService.save(geoMonitorTask);
        return Result.OK("Added");
    }

    @Operation(summary = "Run GEO monitor task now")
    @PostMapping("/runNow")
    public Result<?> runNow(@RequestParam(name = "id", required = true) String id) {
        try {
            return Result.OK(geoMonitorTaskService.runNow(id));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "Edit GEO monitor task")
    @PostMapping("/edit")
    public Result<?> edit(@RequestBody GeoMonitorTask geoMonitorTask) {
        geoMonitorTaskService.updateById(geoMonitorTask);
        return Result.OK("Updated");
    }

    @Operation(summary = "Delete GEO monitor task")
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        geoMonitorTaskService.removeById(id);
        return Result.OK("Deleted");
    }

    @Operation(summary = "Batch delete GEO monitor tasks")
    @DeleteMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        geoMonitorTaskService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("Deleted");
    }

    @Operation(summary = "Query GEO monitor task by id")
    @GetMapping("/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        return Result.OK(geoMonitorTaskService.getById(id));
    }
}
