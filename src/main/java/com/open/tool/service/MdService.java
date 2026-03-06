package com.open.tool.service;

import com.open.tool.config.MdProperties;
import com.open.tool.model.MdNode;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Base64Util;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

/**
 * @author lhd
 * @since 2026/1/15 14:44
 */

@Service
@RequiredArgsConstructor
public class MdService {

    private final MdProperties props;

    public List<MdNode> tree() {
        return walk(props.getRootPath(), "");
    }

    private List<MdNode> walk(Path dir, String relative) {
        try (var stream = Files.list(dir)) {
            return stream
                    .filter(p -> Files.isDirectory(p) || p.toString().endsWith(".md"))
                    .map(p -> {
                        String name = p.getFileName().toString();
                        if (props.getIgnorePaths().contains(name)) {
                            return null;
                        }
                        String rel = relative + name;
                        MdNode node = new MdNode(
                                name,
                                rel,
                                Files.isDirectory(p)
                        );
                        if (Files.isDirectory(p)) {
                            node.setChildren(walk(p, rel + "/"));
                        }
                        return node;
                    }).filter(Objects::nonNull).toList();
        } catch (IOException e) {
            throw new RuntimeException("读取目录失败", e);
        }
    }

    public String read(String path) {
        // URL 解码
        path = URLDecoder.decode(path, StandardCharsets.UTF_8);

        // 防路径穿越
        if (path.contains("..")) {
            throw new IllegalArgumentException("非法路径");
        }

        Path target = props.getRootPath()
                .resolve(path)
                .normalize()
                .toAbsolutePath();

        if (!target.startsWith(props.getRootPath())) {
            throw new SecurityException("非法访问");
        }

        if (!Files.exists(target) || !Files.isRegularFile(target)) {
            throw new IllegalArgumentException("文件不存在");
        }

        // 只允许 md
        if (!target.toString().endsWith(".md")) {
            throw new SecurityException("非法文件类型");
        }

        try {
            long size = Files.size(target);
            if (size > props.getMaxFileSize()) {
                throw new SecurityException("文件过大");
            }
            return Files.readString(target);
        } catch (IOException e) {
            throw new RuntimeException("读取文件失败", e);
        }
    }

}
