package com.open.tool.annotations;


import java.lang.annotation.*;

/**
 * @author lhd
 * @since 2026/4/19 21:55
 */
// 可作用在方法、类上
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME) // 运行时生效
@Documented
public @interface CheckSecretKey {

    /**
     * 请求头中密钥的key名称，默认：secret-key
     */
    String headerKey() default "secret-key";

}
