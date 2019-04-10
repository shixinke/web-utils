package com.shixinke.utils.web.exception;

/**
 * @author shixinke
 * crated 19-4-9 下午3:42
 * @version 1.0
 */
public class CommonException extends Exception {
    /**
     * status code(contain success code and error code)
     */
    private int code;
    /**
     * original message
     */
    private String originalMessage;
    /**
     * default error code
     */
    private static final int EXCEPTION_CODE = 500;
    /**
     * default error message
     */
    private static final String EXCEPTION_MESSAGE = "fail";

    public CommonException(int code, String message, String originalMessage) {
        super(message);
        this.code = code;
        this.originalMessage = originalMessage;
    }

    public CommonException(int code, String message, Throwable ex) {
        super(message, ex);
        this.code = code;
        this.originalMessage = ex.getMessage();
    }

    public CommonException() {
        this(EXCEPTION_CODE, EXCEPTION_MESSAGE, EXCEPTION_MESSAGE);
    }

    public CommonException(Throwable t) {
        super(t);
        this.originalMessage = t.getMessage();
    }

    public CommonException(int code) {
        this();
        this.code = code;
    }

    public CommonException(String message) {
        super(message);
    }

    public CommonException(int code, String message) {
        super(message);
        this.code = code;
    }

    public CommonException(String message, Throwable t) {
        super(message, t);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }



    public String getOriginalMessage() {
        return originalMessage;
    }

    public void setOriginalMessage(String originalMessage) {
        this.originalMessage = originalMessage;
    }
}
