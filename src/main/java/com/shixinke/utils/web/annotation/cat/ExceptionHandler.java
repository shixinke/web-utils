package com.shixinke.utils.web.annotation.cat;


/**
 * Exception handler
 * @author shixinke
 */
public interface ExceptionHandler<T> {

    /**
     * handle
     * @param ex the object of exception
     * @param message the defined message
     * @param clz the class of the parameter
     * @return T 
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