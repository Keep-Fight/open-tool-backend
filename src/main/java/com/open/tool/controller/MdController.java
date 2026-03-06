package com.open.tool.controller;

import com.open.tool.common.CommonResult;
import com.open.tool.model.MdNode;
import com.open.tool.service.MdService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lhd
 * @since 2026/1/15 14:56
 */
@RestController
@RequestMapping("/md")
@RequiredArgsConstructor
public class MdController {

    private final MdService service;

    @GetMapping("/tree")
    public CommonResult<List<MdNode>> tree() {
        return CommonResult.success(service.tree());
    }

    @GetMapping("/file")
    public CommonResult<String> file(@RequestParam String path) {
        return CommonResult.success(service.read(path));
    }
}