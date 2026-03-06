package com.open.tool.model;

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
public class MdNode {

    private String name;
    private String path;
    private boolean directory;
    private List<MdNode> children;


    public MdNode(String name, String path, boolean directory) {
        this.name = name;
        this.path = path;
        this.directory = directory;
    }
}

