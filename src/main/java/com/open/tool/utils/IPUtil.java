package com.open.tool.utils;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;

/**
 * @author lhd
 * @since 2026/5/1 16:39
 */
@Slf4j
public class IPUtil {

    private static final String UNKNOWN = "unknown";

    public static String getClientIp(HttpServletRequest request) {
        String ip = null;

        String[] headers = {
                "X-Forwarded-For",
                "X-Real-IP",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR"
        };

        for (String header : headers) {
            ip = request.getHeader(header);
            if (isValid(ip)) {
                break;
            }
        }

        if (!isValid(ip)) {
            ip = request.getRemoteAddr();
        }

        // 多级代理取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        // 本地访问处理
        if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
            ip = getLocalHostIp();
        }

        return ip;
    }

    private static boolean isValid(String ip) {
        return ip != null && !ip.isEmpty() && !UNKNOWN.equalsIgnoreCase(ip);
    }

    private static String getLocalHostIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "127.0.0.1";
        }
    }
}