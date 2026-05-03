package com.open.tool.service;

import com.open.tool.config.MdProperties;
import com.open.tool.entity.MdIcon;
import com.open.tool.mapper.MdIconMapper;
import com.open.tool.model.TreeNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lhd
 * @since 2026/1/15 14:44
 */

@Service
@RequiredArgsConstructor
public class MdService {

    private final MdProperties props;
    private final MdIconMapper iconMapper;

    public List<TreeNode> tree() {
        // 加载icon映射表 md_icon
        List<MdIcon> mdIcons = iconMapper.selectList(null);
        Map<String, String> iconMap = mdIcons.stream()
                .collect(Collectors.toMap(
                        MdIcon::getFileName,
                        MdIcon::getIcon));
        // 递归遍历目录，构建树状结构
        List<TreeNode> nodes = walk(props.getRootPath(), "", iconMap);
        // 只保留 directories 中配置的顶层目录
        nodes = filterTopLevelNodes(nodes);
        // 按 directories 的顺序进行排序
        nodes = sortTopLevelNodes(nodes);
        return nodes;
    }

    private List<TreeNode> filterTopLevelNodes(List<TreeNode> topLevelNodes) {
        List<String> dirs = props.getDirectories();
        if (dirs == null || dirs.isEmpty()) {
            return topLevelNodes;
        }
        Set<String> dirSet = new HashSet<>(dirs);
        return topLevelNodes.stream()
                .filter(node -> dirSet.contains(node.getTitle()))
                .collect(Collectors.toList());
    }

    private List<TreeNode> walk(Path dir, String relative, Map<String, String> iconMap) {
        try (var stream = Files.list(dir)) {
            return stream
                    .filter(p -> Files.isDirectory(p) || p.toString().endsWith(".md"))
                    .map(p -> {
                        String name = p.getFileName().toString();
                        if (props.getIgnorePaths().contains(name)) {
                            return null;
                        }
                        String rel = relative + name;
                        TreeNode node = new TreeNode(
                                name,
                                rel,
                                iconMap.get(name),
                                Files.isDirectory(p)
                        );
                        if(props.getActiveFileName().concat(".md").equals(name)) {
                            node.setActive(true);
                        }
                        if (props.getActiveFileName().equals(name)) {
                            node.setOpen(true);
                        }
                        if (Files.isDirectory(p)) {
                            node.setChildren(walk(p, rel + "/", iconMap));
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

    // 按 directories 的顺序进行排序
    public List<TreeNode> sortTopLevelNodes(List<TreeNode> topLevelNodes) {
        // 1. 先转换为可变列表（关键修复：避免不可变列表排序报错）
        List<TreeNode> mutableNodes = new ArrayList<>(topLevelNodes);

        // 2. 获取排序基准列表
        List<String> sortBaseList = props.getDirectories();
        if (sortBaseList == null || sortBaseList.isEmpty()) {
            return mutableNodes;
        }

        // 3. 预构建索引Map（提升大列表排序性能）
        Map<String, Integer> sortIndexMap = new HashMap<>();
        for (int i = 0; i < sortBaseList.size(); i++) {
            sortIndexMap.put(sortBaseList.get(i), i);
        }

        // 4. 定制比较器排序（基于title匹配基准列表）
        mutableNodes.sort(Comparator.comparingInt(node ->
                sortIndexMap.getOrDefault(node.getTitle(), sortBaseList.size())
        ));

        return mutableNodes;
    }

}
