package com.shixinke.utils.web.common;

import com.shixinke.utils.web.exception.CommonException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shixinke
 * @version 1.0
 * created 19-2-22 下午2:07
 */
@Data
@Slf4j
public class ResponseDTO<T> {
    private Integer code;
    private String message;
    private Boolean success;
    private T data;

    private static final int SUCCESS_CODE = 200;
    private static final String SUCCESS_MESSAGE = "success";
    private static final int ERROR_CODE = 500;
    private static final String ERROR_MESSAGE = "fail";

    public ResponseDTO() {
        this.code = SUCCESS_CODE;
        this.message = SUCCESS_MESSAGE;
        this.success = true;
        this.data = (T) new Object();
    }

    public ResponseDTO(int code) {
        this();
        this.code = code;
    }

    public ResponseDTO(String message) {
        this();
        this.message = message;
    }

    public ResponseDTO(T data) {
        this();
        this.data = data;
    }

    public ResponseDTO(int code, String message) {
        this();
        this.code = code;
        this.message = message;
    }

    public ResponseDTO(int code, T data) {
        this();
        this.code = code;
        this.data = data;
    }

    public ResponseDTO(int code, String message, T data) {
        this();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResponseDTO(int code, String message, boolean success, T data) {
        this.code = code;
        this.message = message;
        this.success = success;
        this.data = data;
    }

    public ResponseDTO setError(int code, String message) {
        this.setCode(code);
        this.setSuccess(false);
        this.setMessage(message);
        return this;
    }


    public ResponseDTO setError(CommonException ex) {
        this.setError(ex.getCode(), ex.getMessage());
        return this;
    }

    public ResponseDTO setError(Errors error) {
        return setError(error.getCode(), error.getMessage());
    }

    public ResponseDTO <T> setEmpty(String message, Class<T> clz) {
        this.setCode(SUCCESS_CODE);
        this.setMessage(message);
        this.setSuccess(false);
        try {
            this.setData(clz.newInstance());
        } catch (Exception e) {
            log.error("未找到{}的无参实例化方法", clz.getCanonicalName());
        }
        return this;
    }


    public static ResponseDTO success() {
        ResponseDTO responseDTO = new ResponseDTO(SUCCESS_CODE, SUCCESS_MESSAGE, true);
        return responseDTO;
    }

    public static ResponseDTO success(int code) {
        ResponseDTO responseDTO = new ResponseDTO(code, SUCCESS_MESSAGE, true);
        return responseDTO;
    }

    public static ResponseDTO success(String message) {
        ResponseDTO responseDTO = new ResponseDTO(SUCCESS_CODE, message, true);
        return responseDTO;
    }

    public static <T> ResponseDTO success(T data) {
        ResponseDTO responseDTO = new ResponseDTO(SUCCESS_CODE, SUCCESS_MESSAGE, true, data);
        return responseDTO;
    }

    public static ResponseDTO success(int code, String message) {
        ResponseDTO responseDTO = new ResponseDTO(code, message, true, null);
        return responseDTO;
    }

    public static <T> ResponseDTO success(int code, T data) {
        ResponseDTO responseDTO = new ResponseDTO(code, SUCCESS_MESSAGE, true, data);
        return responseDTO;
    }

    public static <T> ResponseDTO success(String message, T data) {
        ResponseDTO responseDTO = new ResponseDTO(SUCCESS_CODE, message, true, data);
        return responseDTO;
    }

    public static <T> ResponseDTO success(int code, String message, T data) {
        ResponseDTO responseDTO = new ResponseDTO(code, message, true, data);
        return responseDTO;
    }

    public static ResponseDTO error() {
        ResponseDTO responseDTO = new ResponseDTO(ERROR_CODE, ERROR_MESSAGE, false, null);
        return responseDTO;
    }

    public static ResponseDTO error(int code) {
        ResponseDTO responseDTO = new ResponseDTO(code, ERROR_MESSAGE, false, null);
        return responseDTO;
    }

    public static ResponseDTO error(String message) {
        ResponseDTO responseDTO = new ResponseDTO(ERROR_CODE, message, false, null);
        return responseDTO;
    }

    public static <T> ResponseDTO error(T data) {
        ResponseDTO responseDTO = new ResponseDTO(ERROR_CODE, ERROR_MESSAGE, false, data);
        return responseDTO;
    }

    public static ResponseDTO error(int code, String message) {
        ResponseDTO responseDTO = new ResponseDTO(code, message, false, null);
        return responseDTO;
    }

    public static <T> ResponseDTO error(int code, T data) {
        ResponseDTO responseDTO = new ResponseDTO(code, ERROR_MESSAGE, false, data);
        return responseDTO;
    }

    public static <T> ResponseDTO error(String message, T data) {
        ResponseDTO responseDTO = new ResponseDTO(ERROR_CODE, message, false, data);
        return responseDTO;
    }

    public static <T> ResponseDTO error(int code, String message, T data) {
        ResponseDTO<T> responseDTO = new ResponseDTO(code, message, false, data);
        return responseDTO;
    }

    public static <T> ResponseDTO error(Errors error) {
        return error(error.getCode(), error.getMessage());
    }



    @Override
    public String toString() {
        return "ResponseDTO{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", success=" + success +
                ", data=" + data +
                '}';
    }
}
