package org.jeecg.modules.geo.controller;

import java.util.Arrays;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.geo.entity.GeoChannel;
import org.jeecg.modules.geo.service.IGeoChannelService;
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
 * GEO channel controller.
 */
@Slf4j
@RestController
@RequestMapping("/geo/channel")
@Tag(name = "GEO channel")
public class GeoChannelController {

    @Autowired
    private IGeoChannelService geoChannelService;

    @Operation(summary = "Page list GEO channels")
    @GetMapping("/list")
    public Result<?> list(GeoChannel geoChannel,
                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest request) {
        QueryWrapper<GeoChannel> queryWrapper = QueryGenerator.initQueryWrapper(geoChannel, request.getParameterMap());
        queryWrapper.orderByDesc("create_time");
        Page<GeoChannel> page = new Page<>(pageNo, pageSize);
        IPage<GeoChannel> pageList = geoChannelService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @Operation(summary = "Add GEO channel")
    @PostMapping("/add")
    public Result<?> add(@RequestBody GeoChannel geoChannel) {
        geoChannelService.save(geoChannel);
        return Result.OK("Added");
    }

    @Operation(summary = "Edit GEO channel")
    @PostMapping("/edit")
    public Result<?> edit(@RequestBody GeoChannel geoChannel) {
        geoChannelService.updateById(geoChannel);
        return Result.OK("Updated");
    }

    @Operation(summary = "Delete GEO channel")
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        geoChannelService.removeById(id);
        return Result.OK("Deleted");
    }

    @Operation(summary = "Batch delete GEO channels")
    @DeleteMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        geoChannelService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("Deleted");
    }

    @Operation(summary = "Query GEO channel by id")
    @GetMapping("/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        return Result.OK(geoChannelService.getById(id));
    }
}
