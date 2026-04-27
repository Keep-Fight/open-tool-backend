package com.open.tool.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author lhd
 * @since 2026/1/15 14:43
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreeNode {

    private String title;
    private String path;
    private String icon;
    @JsonProperty("isOpen")
    private boolean open;
    private boolean active;
    private boolean directory;
    private List<TreeNode> children;


    public TreeNode(String title, String path, String icon, boolean directory) {
        this.title = title;
        this.path = path;
        this.icon = icon;
        this.directory = directory;
        this.open = false;
        this.active = false;
    }
}

