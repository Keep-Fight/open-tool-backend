package com.open.tool.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.util.List;

/**
 * @author lhd
 * @since 2026/1/15 14:45
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "md")
public class MdProperties {
    private Path rootPath;
    private long maxFileSize;
    private List<String> ignorePaths;

    public Path getRootPath() {
        return rootPath.normalize().toAbsolutePath();
    }
}
