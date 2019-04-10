package com.shixinke.utils.web.annotation.cat;

/**
 * Message type in CAT
 * @author shixinke
 * created 19-4-9 下午3:17
 * @version 1.0
 */
public enum CatMessageType {
    /**
     *
     */
    TRANSACTION("TRANSACTION"),
    /**
     * trace message
     */
    TRACE("TRACE"),
    /**
     * metric message(contain business metric)
     */
    METRIC("METRIC"),
    /**
     * event message
     */
    EVENT("EVENT"),
    /**
     * transaction message with duration
     */
    TRANSACTION_WITH_DURATION("TRANSACTION_WITH_DURATION");
    private String value;
    CatMessageType(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
