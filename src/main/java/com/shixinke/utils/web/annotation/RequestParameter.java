package com.shixinke.utils.web.annotation;

import java.lang.annotation.*;

/**
 * @author shixinke
 * @version 1.0
 * @Description request parameter annotation
 * @Date 19-2-22 下午2:46
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RequestParameter {
    /**
     * the request
     * @return
     */
    NameStyle source() default NameStyle.UNDERLINE;

    /**
     *
     * @return
     */
    RequestContentType type() default RequestContentType.AUTO;

    /**
     *
     * @return
     */
    boolean withHeader() default false;

    /**
     * 请求的参数名称
     * @return
     */
    String key() default "";
}
