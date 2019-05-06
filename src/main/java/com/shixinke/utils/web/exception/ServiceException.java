package com.shixinke.utils.web.exception;

/**
 * @author shixinke
 * crated 19-4-9 下午3:44
 * @version 1.0
 */
public class ServiceException extends CommonException {

    /**
     * service exception code
     */
    private static final int SERVICE_ERROR_CODE = 5001;

    public ServiceException() {
        super(SERVICE_ERROR_CODE);
    }

    public ServiceException(int code) {
        super(code);
    }

    public ServiceException(String message) {
        super(SERVICE_ERROR_CODE, message);
    }

    public ServiceException(Throwable cause) {
        super(cause);
        this.setCode(SERVICE_ERROR_CODE);
    }

    public ServiceException(String message, Throwable cause) {
        super(SERVICE_ERROR_CODE, message, cause);
    }

    public ServiceException(int code, String message) {
        super(code, message);
    }

    public ServiceException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

}
