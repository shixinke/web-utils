package com.shixinke.utils.web.example;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * spring boot
 * @author shixinke
 */
//@EnableApolloConfig
@SpringBootApplication(scanBasePackages = {"com.shixinke.utils.web"})
@MapperScan(basePackages = "com.shixinke.utils.web.example.mapper")
public class WebUtilsExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebUtilsExampleApplication.class, args);
    }
}
