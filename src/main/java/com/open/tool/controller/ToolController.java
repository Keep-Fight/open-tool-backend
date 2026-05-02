package com.open.tool.controller;

import com.open.tool.common.CommonResult;
import com.open.tool.service.ToolService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 工具类接口
 *
 * @author lhd
 * @since 2026/4/27
 */
@RestController
@RequestMapping("/tool")
@RequiredArgsConstructor
public class ToolController {

    private final ToolService toolService;

    /**
     * 获取IP地址
     *
     * @param domain 域名
     * @return IP地址
     */
    @GetMapping("/ipAddress")
    public CommonResult<String> getIpAddress(@RequestParam(required = false) String domain, HttpServletRequest request) {
        return CommonResult.success(toolService.getIpAddress(domain,request));
    }
}
