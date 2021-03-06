package com.shixinke.utils.web.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.shixinke.utils.web.annotation.NameStyle;
import com.shixinke.utils.web.annotation.RequestContentType;
import com.shixinke.utils.web.annotation.RequestParameter;
import com.shixinke.utils.web.util.Converters;
import com.shixinke.utils.web.util.NameStyleUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * request parameter resolver
 * @author shixinke
 * @version 1.0
 * created 19-2-26 12:48
 */
@Slf4j
public class RequestParameterResolver implements HandlerMethodArgumentResolver {

    private static final String NULL_STRING = "null";

    private static final String METHOD_GET = "GET";

    private static final String LEFT_BRACE = "{";

    private static final String RIGHT_BRACE = "}";

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(RequestParameter.class);
    }

    /**
     * resolve argument
     * @param methodParameter the object of method parameter
     * @param modelAndViewContainer the object of model and view container
     * @param nativeWebRequest the object of native web request
     * @param webDataBinderFactory the web data binding factory
     * @return Object
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory)  {
        RequestParameter requestParameter = methodParameter.getParameterAnnotation(RequestParameter.class);
        if (requestParameter == null) {
            return null;
        }
        HttpServletRequest servletRequest = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        String contentType = servletRequest.getContentType();
        String jsonContentType = "application/json";

        RequestContentType requestContentType = RequestContentType.FORM;
        boolean isJson = requestParameter.type().equals(RequestContentType.AUTO) && jsonContentType.equalsIgnoreCase(contentType) || requestParameter.type().equals(RequestContentType.JSON);
        if (isJson) {
            requestContentType = RequestContentType.JSON;
        }
        if (requestContentType.equals(RequestContentType.FORM)) {
            return resolveFormArgument(methodParameter, nativeWebRequest, requestParameter);
        } else {
            return resolveJsonArgument(methodParameter, nativeWebRequest, requestParameter);
        }
    }

    /**
     * resolve form argument
     * @param parameter the object of method parameter
     * @param webRequest the object of web request
     * @return Object
     */
    private Object resolveFormArgument(MethodParameter parameter, NativeWebRequest webRequest, RequestParameter requestParameter) {
        Object obj = BeanUtils.instantiateClass(parameter.getParameterType());
        BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(obj);
        Iterator<String> paramNames = webRequest.getParameterNames();
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        List<String> paramNameList = null;
        if (!requestParameter.withHeader()) {
            paramNameList = Converters.iteratorToList(paramNames);
        } else {
            paramNameList = Converters.iteratorMergeToList(paramNames, webRequest.getHeaderNames());
        }
        for (String paramName : paramNameList) {
            Object o = webRequest.getParameter(paramName);
            if (requestParameter.withHeader() && o == null) {
                o = webRequest.getHeader(paramName);
            }
            if (METHOD_GET.equalsIgnoreCase(servletRequest.getMethod()) && isJson(paramName)) {
                return resolveJsonArgument(parameter, webRequest, requestParameter);
            }
            String key = paramName;
            if (requestParameter.source().equals(NameStyle.UNDERLINE)) {
                key = NameStyleUtil.underlineToCamel(paramName);
            }
            Class<?> propertyType = wrapper.getPropertyType(key);
            if (propertyType != null && !propertyType.isPrimitive() && !isBasicDataTypes(propertyType) && propertyType != String.class) {

                if (isArray(propertyType, o)) {
                    o = splitStringToArray(o.toString(), requestParameter.delimiter());
                } else if (isList(propertyType, o)) {
                    o = splitStringToList(o.toString(), requestParameter.delimiter());
                } else if (isSet(propertyType, o)) {
                    o = splitStringToSet(o.toString(), requestParameter.delimiter());
                } else {
                    o = JSON.parseObject(o.toString(), propertyType);
                }
            } else {
                o = parseValue(o, propertyType);
            }
            if (wrapper.isWritableProperty(key)) {
                wrapper.setPropertyValue(key, o);
            } else {
                log.error("property [{}] is not writable", key);
            }

        }
        return obj;
    }

    /**
     * resolve json argument
     * @param parameter the object of method parameter
     * @param webRequest the object of web request
     * @return Object
     */
    private Object resolveJsonArgument(MethodParameter parameter, NativeWebRequest webRequest, RequestParameter requestParameter) {
        String key = requestParameter.key();
        Class<?> parameterType = parameter.getParameterType();
        String jsonBody = getRequestBody(key, webRequest);
        JSONObject jsonObject = JSON.parseObject(jsonBody);
        Object value;
        if (jsonObject == null) {
            try {
                return parameterType.newInstance();
            } catch (Exception ex) {
                return new Object();
            }
        }
        if (!StringUtils.isEmpty(key)) {
            value = jsonObject.get(key);
        } else {
            key = parameter.getParameterName();
            value = jsonObject.get(key);
            if (value == null) {
                value = jsonObject;
            }
        }

        return parseValue(value, parameterType);
    }

    /**
     * parse value
     * @param value the value of parameter
     * @param parameterType the type of parameter
     * @return Object
     */
    private Object parseValue(Object value, Class<?> parameterType) {
        if (value != null && parameterType != null) {
            if (value instanceof String) {
                return parseStringWrapper(parameterType, value);
            }
            /**
             * basic data type
             */
            if (parameterType.isPrimitive()) {
                return parsePrimitive(parameterType.getName(), value);
            }
            /**
             * basic package type
             */
            if (isBasicDataTypes(parameterType)) {
                return parseBasicTypeWrapper(parameterType, value);

            } else if (parameterType == String.class) {
                /**
                 * string
                 */
                return value.toString();
            }
            /**
             * other data type,like object
             */
            Object result = JSON.parseObject(value.toString(), parameterType);
            if (result == null) {
                try {
                    result = parameterType.newInstance();
                } catch (Exception e) {
                    result = new Object();
                }
            }
            return result;
        }
        return null;
    }

    /**
     * basic data type parse
     * @param parameterTypeName  the type of parameter
     * @param value the value of parameter
     * @return Object
     */
    private Object parsePrimitive(String parameterTypeName, Object value) {
        final String booleanTypeName = "boolean";
        if (booleanTypeName.equals(parameterTypeName)) {
            return Boolean.valueOf(value.toString());
        }
        final String intTypeName = "int";
        if (intTypeName.equals(parameterTypeName)) {
            return Integer.valueOf(value.toString());
        }
        final String charTypeName = "char";
        if (charTypeName.equals(parameterTypeName)) {
            return value.toString().charAt(0);
        }
        final String shortTypeName = "short";
        if (shortTypeName.equals(parameterTypeName)) {
            return Short.valueOf(value.toString());
        }
        final String longTypeName = "long";
        if (longTypeName.equals(parameterTypeName)) {
            return Long.valueOf(value.toString());
        }
        final String floatTypeName = "float";
        if (floatTypeName.equals(parameterTypeName)) {
            return Float.valueOf(value.toString());
        }
        final String doubleTypeName = "double";
        if (doubleTypeName.equals(parameterTypeName)) {
            return Double.valueOf(value.toString());
        }
        final String byteTypeName = "byte";
        if (byteTypeName.equals(parameterTypeName)) {
            return Byte.valueOf(value.toString());
        }
        return null;
    }

    /**
     * basic package type parse
     * @param parameterType the type of parameter
     * @param value the value of parameter
     * @return Object
     */
    private Object parseBasicTypeWrapper(Class<?> parameterType, Object value) {
        if (Number.class.isAssignableFrom(parameterType)) {
            Number number = (Number) value;
            if (parameterType == Integer.class) {
                return number.intValue();
            } else if (parameterType == Short.class) {
                return number.shortValue();
            } else if (parameterType == Long.class) {
                return number.longValue();
            } else if (parameterType == Float.class) {
                return number.floatValue();
            } else if (parameterType == Double.class) {
                return number.doubleValue();
            } else if (parameterType == Byte.class) {
                return number.byteValue();
            }
        } else if (parameterType == Boolean.class) {
            return value.toString();
        } else if (parameterType == Character.class) {
            return value.toString().charAt(0);
        }
        return null;
    }

    /**
     * string parse
     * @param parameterType the type of parameter
     * @param value the value of parameter
     * @return Object
     */
    private Object parseStringWrapper(Class<?> parameterType, Object value) {
        String val = (String) value;
        if (value == null || NULL_STRING.equals(val)) {
            return null;
        }
        if (parameterType == String.class) {
            return val;
        }
        if (Number.class.isAssignableFrom(parameterType)) {
            if (parameterType == Integer.class) {
                return Integer.parseInt(val);
            } else if (parameterType == Short.class) {
                return Short.valueOf(val);
            } else if (parameterType == Long.class) {
                return Long.valueOf(val);
            } else if (parameterType == Float.class) {
                return Float.valueOf(val);
            } else if (parameterType == Double.class) {
                return Double.valueOf(val);
            } else if (parameterType == Byte.class) {
                return Byte.valueOf(val);
            }
        } else if (parameterType == Boolean.class) {
            return Boolean.valueOf(val);
        } else if (parameterType == Character.class) {
            return val.charAt(0);
        }
        return null;
    }



    /**
     * is basic data type
     * @param clazz the class of the parameter
     * @return boolean
     */
    private boolean isBasicDataTypes(Class clazz) {
        Set<Class> classSet = new HashSet<Class>(8);
        classSet.add(Integer.class);
        classSet.add(Long.class);
        classSet.add(Short.class);
        classSet.add(Float.class);
        classSet.add(Double.class);
        classSet.add(Boolean.class);
        classSet.add(Byte.class);
        classSet.add(Character.class);
        return classSet.contains(clazz);
    }

    /**
     * is list
     * @param clazz the class of parameter
     * @param o the value of parameter
     * @return
     */
    private boolean isList(Class clazz, Object o) {
        return (clazz == List.class || Arrays.asList(clazz.getInterfaces()).contains(List.class)) && o instanceof String;
    }

    /**
     * is array
     * @param clazz the class of parameter
     * @param o the value of parameter
     * @return
     */
    private boolean isArray(Class clazz, Object o) {
       return clazz.isArray() && o instanceof String;
    }

    /**
     * is set
     * @param clazz the class of parameter
     * @param o the value of parameter
     * @return
     */
    private boolean isSet(Class clazz, Object o) {
        return (clazz == Set.class || Arrays.asList(clazz.getInterfaces()).contains(Set.class)) && o instanceof String;
    }

    /**
     * split string to array
     * @param val the value of parameter
     * @param delimiter the delimiter of string
     * @return
     */
    private String[] splitStringToArray(String val, String delimiter) {
        return val.split(delimiter);
    }

    /**
     * split string to list
     * @param val the value of parameter
     * @param delimiter the delimiter of string
     * @return
     */
    private List splitStringToList(String val, String delimiter) {
        String[] arr = val.split(delimiter);
        return Arrays.asList(arr);
    }

    /**
     * split string to set
     * @param val the value of parameter
     * @param delimiter the delimiter of string
     * @return
     */
    private Set splitStringToSet(String val, String delimiter) {
        String[] arr = val.split(delimiter);
        HashSet set = new HashSet();
        set.addAll(Arrays.asList(arr));
        return set;
    }


    /**
     * get request body
     * @param key the key of the parameter
     * @param webRequest  the object of web request
     * @return String
     */
    private String getRequestBody(String key, NativeWebRequest webRequest) {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        String jsonBody = null;
        if (!StringUtils.isEmpty(key)) {
            jsonBody = (String) webRequest.getAttribute(key, NativeWebRequest.SCOPE_REQUEST);
        }
        if (jsonBody == null) {
            try {
                jsonBody = IOUtils.toString(servletRequest.getReader());
                webRequest.setAttribute(key, jsonBody, NativeWebRequest.SCOPE_REQUEST);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (jsonBody == null || METHOD_GET.equalsIgnoreCase(servletRequest.getMethod())) {
            Map<String, String[]> params = servletRequest.getParameterMap();
            if (params != null && params.size() > 0) {
                for (String k : params.keySet()) {
                    jsonBody = k;
                    break;
                }
            }
        }
        return jsonBody;
    }

    /**
     * is json format
     * @param text the value of the parameter
     * @return boolean
     */
    private boolean isJson(String text) {
        if (text.startsWith(LEFT_BRACE) && text.endsWith(RIGHT_BRACE)) {
            return true;
        }
        return false;
    }
}

