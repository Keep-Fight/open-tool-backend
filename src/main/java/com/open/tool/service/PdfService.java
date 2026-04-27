package com.open.tool.service;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.open.tool.config.MinioConfig;
import com.open.tool.entity.PdfUrl;
import com.open.tool.mapper.PdfUrlMapper;
import com.open.tool.model.TreeNode;
import com.open.tool.utils.MinioUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lhd
 * @since 2026/4/18 20:57
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PdfService {

    private final MinioUtil minioUtil;
    private final MinioConfig minioConfig;
    private final PdfUrlMapper pdfUrlMapper;

    public List<TreeNode> tree() {
        // 1. 查询路径列表
        List<PdfUrl> pdfUrlList = pdfUrlMapper.selectList(null);
        Map<String, List<PdfUrl>> folderGroup = pdfUrlList.stream()
                .collect(Collectors.groupingBy(PdfUrl::getFolder));

        // 2. 构建一级目录节点
        List<TreeNode> rootNodes = new ArrayList<>();
        for (Map.Entry<String, List<PdfUrl>> entry : folderGroup.entrySet()) {
            String folderName = entry.getKey();
            List<PdfUrl> pdfUrls = entry.getValue();

            // 构建一级目录节点
            TreeNode folderNode = new TreeNode();
            folderNode.setTitle(folderName);
            folderNode.setPath("");
            folderNode.setIcon("");
            folderNode.setDirectory(true);

            // 3. 构建二级文件节点
            List<TreeNode> fileNodes = new ArrayList<>();
            for (PdfUrl pdfUrl : pdfUrls) {
                TreeNode fileNode = new TreeNode();
                fileNode.setTitle(pdfUrl.getName());
                fileNode.setPath(pdfUrl.getUrl());
                fileNode.setIcon("");
                fileNode.setDirectory(false);
                fileNode.setChildren(null);
                fileNodes.add(fileNode);
            }

            // 将二级文件节点设置为目录节点的子节点
            folderNode.setChildren(fileNodes);
            rootNodes.add(folderNode);
        }

        return rootNodes;
    }

    public void upload(String folder, MultipartFile[] files) {
        if (files == null || files.length == 0) {
            return;
        }
        // 获取默认桶
        String bucket = minioConfig.getBucketName();

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }

            String originalFilename = file.getOriginalFilename();
            // 拼接 MinIO 里的文件路径（保持和上传一致）
            String objectName = folder + "/" + originalFilename;

            // 获取文件路径，判断 MinIO 是否存在
            String fileUrl;
            if (!minioUtil.fileExists(bucket, objectName)) {
                log.info("MinIO 中不存在，开始上传：{}", objectName);
                minioUtil.uploadFile(bucket, objectName, file);
            }
            fileUrl = minioUtil.getSimpleUrl(minioConfig.getEndpoint(), bucket, objectName);

            // ====================== 保存/更新数据库 ======================
            // 先查是否存在
            LambdaQueryWrapper<PdfUrl> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(PdfUrl::getFolder, folder);
            wrapper.eq(PdfUrl::getName, originalFilename);
            PdfUrl exist = pdfUrlMapper.selectOne(wrapper);

            if (exist == null) {
                // 不存在 -> 插入
                PdfUrl pdfUrl = new PdfUrl();
                pdfUrl.setFolder(folder);
                pdfUrl.setName(originalFilename);
                pdfUrl.setUrl(fileUrl);
                pdfUrlMapper.insert(pdfUrl);
            } else {
                // 已存在 -> 更新URL
                exist.setUrl(fileUrl);
                pdfUrlMapper.updateById(exist);
            }
        }
    }
}
