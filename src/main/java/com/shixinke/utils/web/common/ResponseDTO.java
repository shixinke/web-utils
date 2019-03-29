package com.shixinke.utils.web.common;

import lombok.Data;

/**
 * @author shixinke
 * @version 1.0
 * created 19-2-22 下午2:07
 */
@Data
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
        ResponseDTO responseDTO = new ResponseDTO(code, message, true);
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
        ResponseDTO responseDTO = new ResponseDTO(ERROR_CODE, ERROR_MESSAGE, false);
        return responseDTO;
    }

    public static ResponseDTO error(int code) {
        ResponseDTO responseDTO = new ResponseDTO(code, ERROR_MESSAGE, false);
        return responseDTO;
    }

    public static ResponseDTO error(String message) {
        ResponseDTO responseDTO = new ResponseDTO(ERROR_CODE, message, false);
        return responseDTO;
    }

    public static <T> ResponseDTO error(T data) {
        ResponseDTO responseDTO = new ResponseDTO(ERROR_CODE, ERROR_MESSAGE, false, data);
        return responseDTO;
    }

    public static ResponseDTO error(int code, String message) {
        ResponseDTO responseDTO = new ResponseDTO(code, message, false);
        return responseDTO;
    }

    public static <T> ResponseDTO error(int code, T data) {
        ResponseDTO responseDTO = new ResponseDTO(code, ERROR_MESSAGE, true, data);
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
