package com.shixinke.utils.web.exception;

/**
 * @author shixinke
 * crated 19-4-9 下午3:43
 * @version 1.0
 */
public class ValidateException extends CommonException {
    /**
     * validate exception code
     */
    private static final int VALIDATE_ERROR_CODE = 4001;
    public ValidateException() {
        super();
        this.setCode(VALIDATE_ERROR_CODE);
    }

    public ValidateException(int code) {
        super(code);
    }

    public ValidateException(String message) {
        super(VALIDATE_ERROR_CODE, message);
    }

    public ValidateException(Throwable cause) {
        super(cause);
        this.setCode(VALIDATE_ERROR_CODE);
    }

    public ValidateException(String message, Throwable cause) {
        super(VALIDATE_ERROR_CODE, message, cause);
    }

    public ValidateException(int code, String message) {
        super(code, message);
    }

    public ValidateException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }
}

