package com.shixinke.utils.web.annotation;

/**
 * request content type
 * @author shixinke
 * @version 1.0
 * created 19-2-22 下午3:27
 */
public enum RequestContentType {
    /**
     * JSON
     */
    JSON,
    /**
     * www-form-urlencoded
     */
    FORM,
    /**
     * get the format by Content-Type in header
     */
    AUTO
}
