package com.simple.boot.exception;

import com.simple.boot.common.model.CommonResult;
import com.simple.boot.common.model.ResultCode;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
     * Spring Security 认证异常
     */
    @ExceptionHandler(AuthenticationException.class)
    public CommonResult<String> handleAuthenticationException(AuthenticationException ex) {
        // 记录日志
        log.error(ex.getMessage(), ex);
        // 发送消息给运维
        // 发送邮件给开发人员,ex对象发送给开发人员
        return CommonResult.error(ResultCode.UNAUTHORIZED.code(), ex.getMessage());
    }

    /**
     * Spring Security 授权异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public CommonResult<String> handleAuthenticationException(AccessDeniedException ex) {
        // 记录日志
        log.error(ex.getMessage(), ex);
        // 发送消息给运维
        // 发送邮件给开发人员,ex对象发送给开发人员
        return CommonResult.error(ResultCode.FORBIDDEN.code(), ex.getMessage());
    }

    /**
     * 方法参数校验(处理 @RequestBody 参数校验失败)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        // 记录日志
        log.error(ex.getMessage(), ex);
        // 发送消息给运维
        // 发送邮件给开发人员,ex对象发送给开发人员
        String msg = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return CommonResult.error(ResultCode.PARAM_ERROR.code(), msg);
    }

    /**
     * 约束违反异常(处理 @RequestParam 参数校验失败)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public CommonResult<String> handleConstraintViolationException(ConstraintViolationException ex) {
        // 记录日志
        log.error(ex.getMessage(), ex);
        // 发送消息给运维
        // 发送邮件给开发人员,ex对象发送给开发人员
        return CommonResult.error(ResultCode.PARAM_ERROR.code(), ex.getMessage());
    }

    /**
     * 绑定异常(处理 @ModelAttribute 参数校验失败)
     */
    @ExceptionHandler(BindException.class)
    public CommonResult<String> handleBindException(BindException ex) {
        // 记录日志
        log.error(ex.getMessage(), ex);
        // 发送消息给运维
        // 发送邮件给开发人员,ex对象发送给开发人员
        String msg = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return CommonResult.error(ResultCode.PARAM_ERROR.code(), msg);
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public CommonResult<String> handleBusinessException(BusinessException ex) {
        // 记录日志
        log.error(ex.getMessage(), ex);
        // 发送消息给运维
        // 发送邮件给开发人员,ex对象发送给开发人员
        return CommonResult.error(ResultCode.BUSINESS_ERROR.code(), ex.getMessage());
    }

    /**
     * 服务异常
     */
    @ExceptionHandler(ServiceException.class)
    public CommonResult<String> handleServiceException(ServiceException ex) {
        // 记录日志
        log.error(ex.getMessage(), ex);
        // 发送消息给运维
        // 发送邮件给开发人员,ex对象发送给开发人员
        return CommonResult.error(ResultCode.SERVER_ERROR.code(), ex.getMessage());
    }

    /**
     * 兜底异常
     */
    @ExceptionHandler(Exception.class)
    public CommonResult<String> handleException(Exception ex) {
        // 记录日志
        log.error(ex.getMessage(), ex);
        // 发送消息给运维
        // 发送邮件给开发人员,ex对象发送给开发人员
        return CommonResult.error(ResultCode.FAIL);
    }

}
