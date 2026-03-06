package com.open.tool.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 响应结果类
 *
 * @author lhd
 * @since 2026/1/7 17:20
 */

@Data
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResult<T> {
    // 响应码
    private Integer code;
    // 响应信息
    private String message;
    // 响应数据
    private T data;
    // 响应时间戳
    private Long timestamp;

    public CommonResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    public static <E> CommonResult<E> success(String message, E data) {
        return new CommonResult<>(200, message, data);
    }

    public static <E> CommonResult<E> success(E data) {
        return success("success", data);
    }

    public static <E> CommonResult<E> error(Integer code, String message) {
        return new CommonResult<>(code, message, null);
    }

}
