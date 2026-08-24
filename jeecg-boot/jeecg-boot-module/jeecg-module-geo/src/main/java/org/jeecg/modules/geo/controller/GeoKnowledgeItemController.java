package org.jeecg.modules.geo.controller;

import java.util.Arrays;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.geo.entity.GeoKnowledgeItem;
import org.jeecg.modules.geo.service.IGeoKnowledgeItemService;
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
 * GEO knowledge item controller.
 */
@Slf4j
@RestController
@RequestMapping("/geo/knowledge")
@Tag(name = "GEO knowledge")
public class GeoKnowledgeItemController {

    @Autowired
    private IGeoKnowledgeItemService geoKnowledgeItemService;

    @Operation(summary = "Page list GEO knowledge")
    @GetMapping("/list")
    public Result<?> list(GeoKnowledgeItem geoKnowledgeItem,
                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest request) {
        QueryWrapper<GeoKnowledgeItem> queryWrapper = QueryGenerator.initQueryWrapper(geoKnowledgeItem, request.getParameterMap());
        queryWrapper.orderByDesc("create_time");
        Page<GeoKnowledgeItem> page = new Page<>(pageNo, pageSize);
        IPage<GeoKnowledgeItem> pageList = geoKnowledgeItemService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @Operation(summary = "Add GEO knowledge")
    @PostMapping("/add")
    public Result<?> add(@RequestBody GeoKnowledgeItem geoKnowledgeItem) {
        geoKnowledgeItemService.save(geoKnowledgeItem);
        return Result.OK("Added");
    }

    @Operation(summary = "Edit GEO knowledge")
    @PostMapping("/edit")
    public Result<?> edit(@RequestBody GeoKnowledgeItem geoKnowledgeItem) {
        geoKnowledgeItemService.updateById(geoKnowledgeItem);
        return Result.OK("Updated");
    }

    @Operation(summary = "Delete GEO knowledge")
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        geoKnowledgeItemService.removeById(id);
        return Result.OK("Deleted");
    }

    @Operation(summary = "Batch delete GEO knowledge")
    @DeleteMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        geoKnowledgeItemService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("Deleted");
    }

    @Operation(summary = "Query GEO knowledge by id")
    @GetMapping("/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        return Result.OK(geoKnowledgeItemService.getById(id));
    }
}
