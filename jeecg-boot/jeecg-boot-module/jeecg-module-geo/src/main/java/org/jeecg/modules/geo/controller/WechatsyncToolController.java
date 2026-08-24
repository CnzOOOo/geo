package org.jeecg.modules.geo.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.geo.publish.WechatsyncToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * Wechatsync environment tool controller.
 */
@Slf4j
@RestController
@RequestMapping("/geo/wechatsync")
@Tag(name = "Wechatsync tool")
public class WechatsyncToolController {

    @Autowired
    private WechatsyncToolService wechatsyncToolService;

    @Operation(summary = "Check Wechatsync environment")
    @GetMapping("/status")
    public Result<?> status() {
        return Result.OK(wechatsyncToolService.status());
    }

    @Operation(summary = "Install Wechatsync CLI")
    @PostMapping("/install")
    public Result<?> install() {
        return Result.OK(wechatsyncToolService.install());
    }

    @Operation(summary = "Check Wechatsync CLI update")
    @GetMapping("/check-update")
    public Result<?> checkUpdate() {
        return Result.OK(wechatsyncToolService.checkUpdate());
    }

    @Operation(summary = "Update Wechatsync CLI")
    @PostMapping("/update")
    public Result<?> update() {
        return Result.OK(wechatsyncToolService.update());
    }

    @Operation(summary = "Get Wechatsync plugin install info")
    @GetMapping("/plugin-info")
    public Result<?> pluginInfo() {
        return Result.OK(wechatsyncToolService.pluginInfo());
    }

    @Operation(summary = "Check Wechatsync platform login status")
    @GetMapping("/platform-status")
    public Result<?> platformStatus() {
        return Result.OK(wechatsyncToolService.platformStatus());
    }
}
