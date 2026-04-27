package com.open.tool.utils;

import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * MinIO 通用工具类
 *
 * @author lhd
 * @since 2026/4/19 13:14
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MinioUtil {

    private final MinioClient minioClient;

    // 检查桶是否存在
    public boolean bucketExists(String bucketName) {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            log.error("检查桶失败", e);
            return false;
        }
    }

    // 创建桶
    public void createBucket(String bucketName) {
        if (!bucketExists(bucketName)) {
            try {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("桶创建成功：{}", bucketName);
            } catch (Exception e) {
                log.error("创建桶失败", e);
            }
        }
    }

    // 上传文件
    public void uploadFile(String bucketName, String objectName, MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(is, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            log.info("文件上传成功：{}", objectName);
        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw new RuntimeException("文件上传失败");
        }
    }


    // 获取文件临时预览/下载 URL
    public String getTempFileUrl(String bucketName, String objectName, int expiry, TimeUnit timeUnit) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(expiry, timeUnit)
                            .build()
            );
        } catch (Exception e) {
            log.error("获取文件URL失败", e);
            return null;
        }
    }

    // 获取文件简单URL,不携带参数，需要bucketName权限为public-read
    public String getSimpleUrl(String endpoint, String bucketName, String objectName) {
        try {
            return endpoint + "/" + bucketName + "/" + URLEncoder.encode(objectName, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("获取文件URL失败", e);
            return null;
        }
    }

    // 删除文件
    public void deleteFile(String bucketName, String objectName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            log.info("文件删除成功：{}", objectName);
        } catch (Exception e) {
            log.error("文件删除失败", e);
        }
    }

    // 下载文件
    public File downloadFile(String bucketName, String objectName) {
        //临时文件
        File minioFile = null;
        FileOutputStream outputStream = null;
        try {
            InputStream inputStream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
            //创建临时文件
            minioFile = File.createTempFile("minio", ".merge");
            outputStream = new FileOutputStream(minioFile);
            IOUtils.copy(inputStream, outputStream);
            return minioFile;
        } catch (Exception e) {
            log.error("文件下载失败", e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error("关闭输出流失败", e);
                }
            }
        }
        return null;
    }

    // 校验文件是否存在
    public boolean fileExists(String bucketName, String objectName) {
        try {
            minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
            return true;
        } catch (Exception e) {
            log.error("检查文件是否存在失败", e);
            return false;
        }
    }
}