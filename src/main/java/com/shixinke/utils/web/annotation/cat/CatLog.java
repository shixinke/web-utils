package com.shixinke.utils.web.annotation.cat;

import java.lang.annotation.*;

/**
 * @author shixinke
 * crated 19-4-9 下午3:25
 * @version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CatLog {
    CatMessageType messageType() default CatMessageType.TRANSACTION_WITH_DURATION;
    CatLogType methodType() default CatLogType.API;
    String type() default "";
    String page() default "";
    Class<?> exceptionClass() default ExceptionHandler.class;
    String exceptionHandler() default "";
    Class empty() default Nullable.class;
    String remark() default "";
}