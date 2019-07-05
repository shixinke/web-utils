package com.shixinke.utils.web.common;

import lombok.Data;

/**
 * 追踪日志ID
 * @author shixinke
 */
@Data
public class TraceLogId {
    private String ip;
    private Long lastId;
    private Long createTime;

    public TraceLogId() {
        this.ip = "";
        this.lastId = 0L;
        this.createTime = 0L;
    }
}
