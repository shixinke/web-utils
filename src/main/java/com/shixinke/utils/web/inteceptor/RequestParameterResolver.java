package com.shixinke.utils.web.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.util.*;

/**
 * @author jiangfangtao
 * @version 1.0
 * @Description 请求参数解析器
 * @Date 19-2-26 下午12:48
 */
@Slf4j
public class RequestParameterResolver implements HandlerMethodArgumentResolver {

    private static final String NULL_STRING = "null";

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(RequestParameter.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
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
     * 解析form类型的参数
     * @param parameter
     * @param webRequest
     * @return
     */
    private Object resolveFormArgument(MethodParameter parameter, NativeWebRequest webRequest, RequestParameter requestParameter) {
        Object obj = BeanUtils.instantiateClass(parameter.getParameterType());
        BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(obj);
        Iterator<String> paramNames = webRequest.getParameterNames();
        List<String> paramNameList = new ArrayList<String>(5);
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
            String key = paramName;
            if (requestParameter.source().equals(NameStyle.UNDERLINE)) {
                key = NameStyleUtil.underlineToCamel(paramName);
            }
            Class<?> propertyType = wrapper.getPropertyType(key);
            if (propertyType != null && !propertyType.isPrimitive() && !isBasicDataTypes(propertyType) && propertyType != String.class) {
                o = JSON.parseObject(o.toString(), propertyType);
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
     * 解析JSON格式的参数
     * @param parameter
     * @param webRequest
     * @return
     */
    private Object resolveJsonArgument(MethodParameter parameter, NativeWebRequest webRequest, RequestParameter requestParameter) {
        String key = requestParameter.key();
        String jsonBody = getRequestBody(key, webRequest);
        JSONObject jsonObject = JSON.parseObject(jsonBody);
        Object value;
        if (jsonObject == null) {
            return null;
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

        Class<?> parameterType = parameter.getParameterType();
        return parseValue(value, parameterType);
    }

    private Object parseValue(Object value, Class<?> parameterType) {
        if (value != null) {
            if (value instanceof String) {
                return parseStringWrapper(parameterType, value);
            }
            /**
             * 基本类型
             */
            if (parameterType.isPrimitive()) {
                return parsePrimitive(parameterType.getName(), value);
            }
            /**
             * 基本包装类型
             */
            if (isBasicDataTypes(parameterType)) {
                return parseBasicTypeWrapper(parameterType, value);

            } else if (parameterType == String.class) {
                /**
                 * 字符串类型
                 */
                return value.toString();
            }
            /**
             * 其他类型
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
     * 基本类型解析
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
     * 基本类型包装类解析
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
     * 判断是否为基本数据类型包装类
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
     * 获取请求体JSON字符串
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
        return jsonBody;
    }
}

