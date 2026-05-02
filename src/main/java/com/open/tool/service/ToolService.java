package com.open.tool.service;

import com.open.tool.utils.IPUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 工具类服务
 *
 * @author lhd
 * @since 2026/4/27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ToolService {

    public String getIpAddress(String domain, HttpServletRequest request) {
        if (StringUtils.isBlank(domain)) {
            // 返回当前请求的IP地址
            return IPUtil.getClientIp(request);
        }

        try {
            InetAddress[] addresses = InetAddress.getAllByName(domain);
            if (addresses.length > 0) {
                return addresses[0].getHostAddress();
            }
        } catch (UnknownHostException e) {
            log.error("域名解析失败: {}", domain, e);
        }
        return null;
    }

}
