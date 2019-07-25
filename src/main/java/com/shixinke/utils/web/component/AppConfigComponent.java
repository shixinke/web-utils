package com.shixinke.utils.web.component;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 配置读取组件
 * @author shixinke
 */
@Component
public class AppConfigComponent {

    @Resource
    private Environment environment;

    private static final String PROPERTY_VALUE_SEPARATOR = ",";

    /**
     * 获取配置
     * @param key
     * @return
     */
    public String getProperty(String key) {
        return environment.getProperty(key);
    }

    /**
     * 获取配置
     * @param key
     * @param defaults
     * @return
     */
    public String getProperty(String key, String defaults) {
        return environment.getProperty(key, defaults);
    }

    /**
     * 获取配置
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getProperty(String key, Class<T> clazz) {
        return environment.getProperty(key, clazz);
    }

    /**
     * 获取配置
     * @param key
     * @param clazz
     * @param defaults
     * @param <T>
     * @return
     */
    public <T> T getProperty(String key, Class<T> clazz, T defaults) {
        return environment.getProperty(key, clazz, defaults);
    }

    /**
     * 获取属性值列表
     * @param key
     * @return
     */
    public List<String> getPropertyList(String key) {
        return getPropertyList(key, PROPERTY_VALUE_SEPARATOR);
    }

    /**
     * 获取属性值列表
     * @param key
     * @param separator
     * @return
     */
    public List<String> getPropertyList(String key, String separator) {
        return getPropertyList(key, separator, String.class);
    }

    /**
     * 获取属性值列表
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> getPropertyList(String key, Class<T> clazz) {
        return getPropertyList(key, PROPERTY_VALUE_SEPARATOR, clazz);
    }

    /**
     * 获取属性值列表
     * @param key
     * @param separator
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> getPropertyList(String key, String separator, Class<T> clazz) {
        String value = getProperty(key);
        List<T> dataList = new ArrayList<>(5);
        if (!StringUtils.isEmpty(value)) {
            String[] values = value.split(separator);
            if (clazz == String.class) {
                return (List<T>) Arrays.asList(values);
            }
            for (String v : values) {
                Object o = new Object();
                if (clazz == Integer.class) {
                    o = Integer.parseInt(v);
                } else if (clazz == Boolean.class) {
                    o = Boolean.valueOf(v);
                } else if (clazz == Short.class) {
                    o = Short.valueOf(v);
                } else if (clazz == Byte.class) {
                    o = Byte.valueOf(v);
                } else if (clazz == Long.class) {
                    o = Long.valueOf(v);
                } else if (clazz == Float.class) {
                    o = new Float(v);
                } else if (clazz == Double.class) {
                    o = new Double(v);
                }
                dataList.add( (T) o);
            }
        }
        return dataList;
    }
}
