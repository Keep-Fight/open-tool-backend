package com.open.tool.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lhd
 * @since 2026/4/18 20:40
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("pdf_url")
public class PdfUrl {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 文件夹名称
     */
    private String folder;

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件url
     */
    private String url;

}
