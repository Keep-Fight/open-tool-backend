package com.open.tool.controller;


import com.open.tool.annotations.CheckSecretKey;
import com.open.tool.common.CommonResult;
import com.open.tool.model.TreeNode;
import com.open.tool.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * PDF 相关接口
 *
 * @author lhd
 * @since 2026/4/18 20:39
 */

@RestController
@RequestMapping("/pdf")
@RequiredArgsConstructor
public class PdfController {

    private final PdfService service;

    @GetMapping("/tree")
    public CommonResult<List<TreeNode>> tree() {
        return CommonResult.success(service.tree());
    }

    @CheckSecretKey
    @PostMapping("/upload")
    public CommonResult<String> upload(@RequestParam("folder") String folder,
                                       @RequestParam("files") MultipartFile[] files) {
        service.upload(folder, files);
        return CommonResult.success("上传成功");
    }

}
