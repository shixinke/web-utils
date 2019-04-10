package com.shixinke.utils.web.annotation.cat;

import lombok.extern.slf4j.Slf4j;

/**
 * Exception handler
 * @param <T>
 */
public interface ExceptionHandler<T> {

    /**
     * handle
     * @param ex
     * @param message
     * @param clz
     * @return
     */
    public default T handle(Exception ex, String message, Class<T> clz) {
        try {
            T obj = clz.newInstance();
            return obj;
        } catch (Exception e) {
            return null;
        }
    }
}