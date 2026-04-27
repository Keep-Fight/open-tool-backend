package com.open.tool.controller;

import com.open.tool.common.CommonResult;
import com.open.tool.model.TreeNode;
import com.open.tool.service.MdService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Markdown 相关接口
 *
 * @author lhd
 * @since 2026/1/15 14:56
 */
@RestController
@RequestMapping("/md")
@RequiredArgsConstructor
public class MdController {

    private final MdService service;

    @GetMapping("/tree")
    public CommonResult<List<TreeNode>> tree() {
        return CommonResult.success(service.tree());
    }

    @GetMapping("/content")
    public CommonResult<String> content(@RequestParam String path) {
        return CommonResult.success(service.read(path));
    }
}