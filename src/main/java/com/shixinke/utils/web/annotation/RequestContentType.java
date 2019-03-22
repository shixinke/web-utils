package com.shixinke.utils.web.annotation;

/**
 * @author shixinke
 * @version 1.0
 * @description request format
 * @date 19-2-22 下午3:27
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
     * get the format by header
     */
    AUTO
}
