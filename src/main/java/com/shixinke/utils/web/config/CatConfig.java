package com.shixinke.utils.web.config;

import com.dianping.cat.Cat;
import com.dianping.cat.servlet.CatFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author shixinke
 * crated 19-4-9 下午4:05
 * @version 1.0
 */
@Configuration
@Slf4j
public class CatConfig {


    @Autowired
    private Environment env;

    private static final String ENABLED = "cat.enabled";

    private static final String ENABLE_FLAG = "true";

    /**
     * @author  shixinke
     * @Description  自动给所有URL埋点
     * @method catFilter
     * @param
     * @return  org.springframework.boot.web.servlet.FilterRegistrationBean
     * @date  19-2-28 下午4:39
     */
    @Bean
    public FilterRegistrationBean catFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        if (getEnable()) {
            Cat.enable();
        } else {
            Cat.disable();
        }
        CatFilter filter = new CatFilter();
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        registration.setName("cat-filter");
        registration.setOrder(2);
        return registration;
    }

    public boolean getEnable() {
        String enable = env.getProperty(ENABLED);
        if (ENABLE_FLAG.equalsIgnoreCase(enable)) {
            return true;
        } else {
            return false;
        }
    }





}
