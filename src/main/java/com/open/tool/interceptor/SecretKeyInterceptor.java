package com.open.tool.interceptor;

import com.open.tool.annotations.CheckSecretKey;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;


/**
 * @author lhd
 * @since 2026/4/19 21:57
 */
@Component
public class SecretKeyInterceptor implements HandlerInterceptor {

    @Value("${open-tool.secret-key}")
    private String correctSecretKey;

    /**
     * 请求进入Controller之前执行
     */
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        // 1. 判断是否是Controller方法请求
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        // 2. 判断方法/类上是否带有 @CheckSecretKey 注解
        CheckSecretKey checkSecretKey = handlerMethod.getMethodAnnotation(CheckSecretKey.class);
        if (checkSecretKey == null) {
            // 没有注解，直接放行
            return true;
        }

        // 3. 获取注解中配置的请求头key
        String headerKey = checkSecretKey.headerKey();
        // 4. 从请求头中获取密钥
        String requestSecretKey = request.getHeader(headerKey);

        // 5. 校验密钥
        if (requestSecretKey == null || !requestSecretKey.equals(correctSecretKey)) {
            // 密钥错误/不存在，返回错误信息
            response.setContentType("application/json;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write("{\"code\":401,\"msg\":\"密钥无效或不存在，请检查请求头\"}");
            writer.flush();
            return false;
        }

        // 校验通过，放行
        return true;
    }
}