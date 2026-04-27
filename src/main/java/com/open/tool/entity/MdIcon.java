package com.open.tool.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author lhd
 * @since 2026/4/18 20:47
 */
@Data
@TableName("md_icon")
public class MdIcon {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * icon
     */
    private String icon;
}
