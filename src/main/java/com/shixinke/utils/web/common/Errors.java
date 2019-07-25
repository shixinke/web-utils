package com.shixinke.utils.web.common;

/**
 * error enum
 * @author shixinke
 */
public interface Errors {
    /**
     * 获取错误码
     * @return
     */
    public int getCode();

    /**
     * 获取错误信息
     * @return
     */
    public String getMessage();

}
