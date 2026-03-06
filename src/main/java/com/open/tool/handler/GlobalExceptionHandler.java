package com.open.tool.handler;


import com.open.tool.common.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理类
 *
 * @author lhd
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 兜底异常
     */
    @ExceptionHandler(Exception.class)
    public CommonResult<String> handleException(Exception ex) {
        // 记录日志
        log.error(ex.getMessage(), ex);
        // 发送消息给运维
        // 发送邮件给开发人员,ex对象发送给开发人员
        return CommonResult.error(-1, "系统异常，请稍后重试！");
    }

}
