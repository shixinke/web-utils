package com.shixinke.utils.web.common;

/**
 * 通用错误码
 * @author shixinke
 */
public enum CommonErrors implements Errors {
    NONE(2000, "无错误", "success"),
    /**
     * validate error
     */
    VALIDATE_ERROR(4000, "参数验证失败", "validated failed"),
    /**
     * parameter error
     */
    NOT_AUTHORITY(4001, "没有授权", "not authority"),
    /**
     * parameter error
     */
    PARAMETER_EMPTY(4002, "参数不能为空", "parameter must not be empty"),
    /**
     * forbidden
     */
    FORBIDDEN(4003, "没有权限", "forbidden"),
    /**
     * data not exists
     */
    NOT_FOUND(4004, "请求资源不存在", "not exists"),
    /**
     * parameter structure error
     */
    PARAMETER_STRUCTURE_ERROR(4005, "参数结构不正确", "parameter structure error"),
    /**
     * parameter parse error
     */
    PARAMETER_PARSE_ERROR(4006, "参数解析出错", "parameter parse error"),
    /**
     * signature error
     */
    INVALID_SIGNATURE(4007, "签名验证失败", "sign check failed"),
    /**
     * request method error
     */
    REQUEST_METHOD_ERROR(4008, "请求方式不正确", "request method error"),
    /**
     * CSRF token error
     */
    CSRF_TOKEN_ERROR(4009, "csrfToken错误", "csrf token error"),
    /**
     * expired
     */
    EXPIRED(4010, "失效", "expired"),
    /**
     * cookie expired
     */
    COOKIE_EXPIRED(4011, "cookie失效", "COOKIE expired"),
    /**
     * server error
     */
    SERVER_ERROR(5000, "服务器开小差", "server internal error"),
    /**
     * io exception
     */
    IO_EXCEPTION(5001, "服务繁忙", "server busy"),
    /**
     * service exception
     */
    SERVICE_EXCEPTION(5002, "服务异常", "service exception"),
    /**
     * facade exception
     */
    FACADE_EXCEPTION(5003, "服务异常", "facade exception"),
    /**
     * data type conversion exception
     */
    CONVERSION_EXCEPTION(5004, "数据格式转换异常", "data type conversion exception"),
    /**
     * empty content
     */
    EMPTY_CONTENT(5005, "内容为空", "empty content"),
    /**
     * not login
     */
    NOT_LOGIN(5006, "未登录", "not login"),
    /**
     * not enabled
     */
    NOT_ENABLED(5007, "功能未开启", "not enabled"),
    /**
     * database error
     */
    DB_ERROR(5100, "服务器开小差", "database error"),
    /**
     * database operation failed
     */
    DB_FAILED(5101, "操作失败", "database operation failed"),
    /**
     * cache error
     */
    CACHE_ERROR(5200, "服务器在开小差", "cache error"),
    /**
     * cache operation failed
     */
    CACHE_FAILED(5201, "操作失败", "cache operation failed"),
    /**
     * frequently error
     */
    FREQUENTLY_ERROR(5300, "操作过于频繁", "frequent operation"),
    /**
     * flow error
     */
    FLOW_ERROR(5301, "服务拥挤，请稍候重试", "service busy"),
    /**
     * network error
     */
    NETWORK_ERROR(5400, "网络错误", "network error"),
    /**
     * connect timeout
     */
    CONNECT_TIMEOUT(5401, "网络连接超时", "network connect timeout"),
    /**
     * network bad gateway
     */
    NETWORK_BAD_GATEWAY(5402, "网关异常", "network gateway error"),
    /**
     * network service error
     */
    NETWORK_SERVICE_ERROR(5403, "网络依赖服务异常", "network service error"),
    /**
     * network gateway timeout
     */
    NETWORK_GATEWAY_TIMEOUT(5404, "网络网关错误", "network gateway timeout"),
    /**
     * network read timeout
     */
    READ_TIMEOUT(5405, "网络读取超时", "network read timeout"),
    /**
     * socket timeout
     */
    SOCKET_TIMEOUT(5406, "网络超时", "socket timeout"),
    /**
     * retry exception
     */
    RETRY_EXCEPTION(5405, "网络重试异常", "retry exception"),
    /**
     * execution exception
     */
    EXECUTION_EXCEPTION(5406, "执行出错", "execution exception"),
    /**
     * http client exception
     */
    HTTP_CLIENT_EXCEPTION(5407, "http客户端异常", "http client exception"),
    /**
     * config error
     */
    CONFIG_ERROR(5500, "配置异常", "config error"),
    /**
     * config read error
     */
    CONFIG_READ_ERROR(5501, "配置读取错误", "config read error"),
    /**
     * dependency exception
     */
    SEARCH_ERROR(5600, "搜索服务异常", "search error"),
    /**
     * search index not exists
     */
    SEARCH_INDEX_NOT_EXISTS(5601, "索引不存在", "index not exists"),
    /**
     * query dsl error
     */
    SEARCH_QUERY_DSL_ERROR(5602, "查询语句错误", "query dsl error"),
    /**
     * parse result error
     */
    SEARCH_PARSE_RESULT_ERROR(5603, "解析查询结果出错", "parse result error"),
    /**
     * pagination error
     */
    SEARCH_PAGINATION_ERROR(5604, "分页出错", "pagination error"),
    /**
     * mongo error
     */
    MONGO_ERROR(5700, "MongoDB服务异常", "mongoDB error"),
    /**
     * component error
     */
    COMPONENT_ERROR(5800, "组件异常", "component error"),
    /**
     * dependency exception
     */
    DEPENDENCY_EXCEPTION(5801, "依赖服务异常", "dependency exception"),
    /**
     * degrade exception
     */
    DEGRADE_EXCEPTION(5802, "降级异常", "degrade exception"),
    /**
     * 业务异常
     */
    BUSINESS_ERROR(5900, "业务异常", "business error"),
    /**
     * unknown error
     */
    UNKNOWN_EXCEPTION(5999, "操作失败", "server busy")
    ;
    private int code;
    private String message;
    private String originMessage;
    CommonErrors(int code, String message, String originMessage) {
        this.code = code;
        this.message = message;
        this.originMessage = originMessage;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getOriginMessage() {
        return originMessage;
    }
}
