package org.jeecg.modules.geo.controller;

import java.util.Arrays;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.geo.entity.GeoQuestionBank;
import org.jeecg.modules.geo.service.IGeoQuestionBankService;
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
 * GEO question bank controller.
 */
@Slf4j
@RestController
@RequestMapping("/geo/questionBank")
@Tag(name = "GEO question bank")
public class GeoQuestionBankController {

    @Autowired
    private IGeoQuestionBankService geoQuestionBankService;

    @Operation(summary = "Page list GEO questions")
    @GetMapping("/list")
    public Result<?> list(GeoQuestionBank geoQuestionBank,
                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                          HttpServletRequest request) {
        QueryWrapper<GeoQuestionBank> queryWrapper = QueryGenerator.initQueryWrapper(geoQuestionBank, request.getParameterMap());
        queryWrapper.orderByDesc("create_time");
        Page<GeoQuestionBank> page = new Page<>(pageNo, pageSize);
        IPage<GeoQuestionBank> pageList = geoQuestionBankService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @Operation(summary = "Add GEO question")
    @PostMapping("/add")
    public Result<?> add(@RequestBody GeoQuestionBank geoQuestionBank) {
        geoQuestionBankService.save(geoQuestionBank);
        return Result.OK("Added");
    }

    @Operation(summary = "Edit GEO question")
    @PostMapping("/edit")
    public Result<?> edit(@RequestBody GeoQuestionBank geoQuestionBank) {
        geoQuestionBankService.updateById(geoQuestionBank);
        return Result.OK("Updated");
    }

    @Operation(summary = "Delete GEO question")
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        geoQuestionBankService.removeById(id);
        return Result.OK("Deleted");
    }

    @Operation(summary = "Batch delete GEO questions")
    @DeleteMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        geoQuestionBankService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("Deleted");
    }

    @Operation(summary = "Query GEO question by id")
    @GetMapping("/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        return Result.OK(geoQuestionBankService.getById(id));
    }
}
