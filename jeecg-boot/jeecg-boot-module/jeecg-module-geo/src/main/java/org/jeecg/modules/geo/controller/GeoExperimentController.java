package org.jeecg.modules.geo.controller;

import java.util.Arrays;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.geo.entity.GeoExperiment;
import org.jeecg.modules.geo.service.IGeoExperimentService;
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
 * GEO experiment controller.
 */
@Slf4j
@RestController
@RequestMapping("/geo/experiment")
@Tag(name = "GEO experiment")
public class GeoExperimentController {

    @Autowired
    private IGeoExperimentService geoExperimentService;

    @Operation(summary = "Page list GEO experiments")
    @GetMapping("/list")
    public Result<?> list(GeoExperiment geoExperiment,
                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest request) {
        QueryWrapper<GeoExperiment> queryWrapper = QueryGenerator.initQueryWrapper(geoExperiment, request.getParameterMap());
        queryWrapper.orderByDesc("create_time");
        Page<GeoExperiment> page = new Page<>(pageNo, pageSize);
        IPage<GeoExperiment> pageList = geoExperimentService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @Operation(summary = "Add GEO experiment")
    @PostMapping("/add")
    public Result<?> add(@RequestBody GeoExperiment geoExperiment) {
        geoExperimentService.save(geoExperiment);
        return Result.OK("Added");
    }

    @Operation(summary = "Edit GEO experiment")
    @PostMapping("/edit")
    public Result<?> edit(@RequestBody GeoExperiment geoExperiment) {
        geoExperimentService.updateById(geoExperiment);
        return Result.OK("Updated");
    }

    @Operation(summary = "Delete GEO experiment")
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        geoExperimentService.removeById(id);
        return Result.OK("Deleted");
    }

    @Operation(summary = "Batch delete GEO experiments")
    @DeleteMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        geoExperimentService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("Deleted");
    }

    @Operation(summary = "Query GEO experiment by id")
    @GetMapping("/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        return Result.OK(geoExperimentService.getById(id));
    }
}
