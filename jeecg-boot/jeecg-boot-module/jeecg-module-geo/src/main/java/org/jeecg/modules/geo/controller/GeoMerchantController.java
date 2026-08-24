package org.jeecg.modules.geo.controller;

import java.util.Arrays;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.geo.entity.GeoMerchant;
import org.jeecg.modules.geo.service.IGeoMerchantService;
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
 * GEO merchant controller.
 */
@Slf4j
@RestController
@RequestMapping("/geo/merchant")
@Tag(name = "GEO merchant")
public class GeoMerchantController {

    @Autowired
    private IGeoMerchantService geoMerchantService;

    @Operation(summary = "Page list GEO merchants")
    @GetMapping("/list")
    public Result<?> list(GeoMerchant geoMerchant,
                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest request) {
        QueryWrapper<GeoMerchant> queryWrapper = QueryGenerator.initQueryWrapper(geoMerchant, request.getParameterMap());
        queryWrapper.orderByDesc("create_time");
        Page<GeoMerchant> page = new Page<>(pageNo, pageSize);
        IPage<GeoMerchant> pageList = geoMerchantService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @Operation(summary = "Add GEO merchant")
    @PostMapping("/add")
    public Result<?> add(@RequestBody GeoMerchant geoMerchant) {
        geoMerchantService.save(geoMerchant);
        return Result.OK("Added");
    }

    @Operation(summary = "Edit GEO merchant")
    @PostMapping("/edit")
    public Result<?> edit(@RequestBody GeoMerchant geoMerchant) {
        geoMerchantService.updateById(geoMerchant);
        return Result.OK("Updated");
    }

    @Operation(summary = "Delete GEO merchant")
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        geoMerchantService.removeById(id);
        return Result.OK("Deleted");
    }

    @Operation(summary = "Batch delete GEO merchants")
    @DeleteMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        geoMerchantService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("Deleted");
    }

    @Operation(summary = "Query GEO merchant by id")
    @GetMapping("/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        return Result.OK(geoMerchantService.getById(id));
    }
}
