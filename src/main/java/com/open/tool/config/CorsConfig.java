package com.open.tool.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域配置
 *
 * @author lhd
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 设置允许跨域请求的域名
                .allowedOriginPatterns("*")
                // 是否发送Cookie
                .allowCredentials(true)
                // 放行哪些原始域 SpringBoot 3.x 里不允许和 `allowCredentials(true)` 一起用
                //.allowedOrigins("*")
                // 放行哪些请求方式
                .allowedMethods("GET", "POST")
                // 设置允许的header属性
                .allowedHeaders("*")
                // 暴露哪些头部信息
                .exposedHeaders("*")
                // 跨域允许时间
                .maxAge(3600);
    }
}
