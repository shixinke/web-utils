package com.shixinke.utils.web.common;

/**
 * error enum
 * @author shixinke
 */
public enum Errors {
    /**
     * validate error
     */
    VALIDATE_ERROR(4001, "参数错误", "validated failed"),
    /**
     * server error
     */
    SERVER_ERROR(5000, "服务器开小差", "server internal error"),
    /**
     * database error
     */
    DB_ERROR(5100, "服务器开小差", "database error"),
    /**
     * database operation failed
     */
    DB_FAILED(5101, "操作失败", "database operation failed"),
    /**
     * cache operation failed
     */
    CACHE_ERROR(5200, "服务器在开小差", "cache operation failed"),
    /**
     *
     */
    FREQUENTLY_ERROR(5300, "操作过于频繁", "frequent operation"),
    /**
     * flow error
     */
    FLOW_ERROR(5301, "服务拥挤，请稍候重试", "service busy"),
    /**
     * not login
     */
    NOT_LOGIN(5600, "用户未登录", "not login"),
    /**
     * ID生成失败
     */
    ID_ERROR(5400, "创建失败", "id created failed"),
    /**
     * 签名验证失败
     */
    INVALID_SIGNATURE(9007, "签名验证失败", "sign check failed"),
    ;
    private int code;
    private String message;
    private String originMessage;
    Errors(int code, String message, String originMessage) {
        this.code = code;
        this.message = message;
        this.originMessage = originMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getOriginMessage() {
        return originMessage;
    }
}
