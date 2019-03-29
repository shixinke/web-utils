package com.shixinke.utils.web.annotation;

import java.lang.annotation.*;

/**
 * request parameter annotation
 * @author shixinke
 * @version 1.0
 * created 19-2-22 下午2:46
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RequestParameter {
    /**
     * the naming style of client
     * @return NameStyle
     */
    NameStyle source() default NameStyle.UNDERLINE;

    /**
     * content type of the request
     * @return RequestContentType
     */
    RequestContentType type() default RequestContentType.AUTO;

    /**
     * get the parameters from the header
     * @return boolean
     */
    boolean withHeader() default false;

    /**
     * the name of the parameter
     * @return String
     */
    String key() default "";
}
