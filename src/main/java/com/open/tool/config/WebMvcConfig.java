package com.open.tool.config;


import com.open.tool.interceptor.SecretKeyInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lhd
 * @since 2026/4/19 21:58
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private SecretKeyInterceptor secretKeyInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器，拦截所有请求
        registry.addInterceptor(secretKeyInterceptor)
                .addPathPatterns("/**");
    }
}