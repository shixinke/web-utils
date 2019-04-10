package com.shixinke.utils.web.config;

import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import com.alibaba.csp.sentinel.config.SentinelConfig;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.apollo.ApolloDataSource;
import com.alibaba.csp.sentinel.log.LogBase;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.transport.config.TransportConfig;
import com.alibaba.csp.sentinel.util.AppNameUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author shixinke
 * crated 19-4-9 下午4:07
 * @version 1.0
 */
@Configuration
@Data
@Slf4j
public class SentinelRuleConfig {

    private List<String> flowRuleKeys = new ArrayList<>(0);
    private List<String> degradeRuleKeys = new ArrayList<>(0);
    private List<String> authorityRuleKeys = new ArrayList<>(0);
    private List<String> systemRuleKeys = new ArrayList<>(0);
    private List<String> paramFlowRuleKeys = new ArrayList<>(0);

    @Autowired
    private Environment env;

    private String flowRulePrefix;

    private String degradeRulePrefix;

    private String systemRulePrefix;

    private String authorityRulePrefix;

    private String paramFlowRulePrefix;

    private String apolloNamespace;

    private String sentinelLogDir;

    private String dashboardServer;

    private String projectName;

    @ApolloConfig
    private Config config;

    @Bean
    public SentinelResourceAspect sentinelResourceAspect() {
        initConfig();
        initRuleKeys();
        String defaultRules = "[]";
        if (!CollectionUtils.isEmpty(flowRuleKeys)) {
            for (String flowRuleKey : flowRuleKeys) {
                ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new ApolloDataSource<>(apolloNamespace, flowRuleKey, defaultRules, source -> {
                    log.info("rules source:{}", source);
                    return JSON.parseObject(source, new TypeReference<List<FlowRule>>() {});});
                FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
            }
            log.info("flow rules:{}", FlowRuleManager.getRules());
        }
        return new SentinelResourceAspect();
    }


    private void initRuleKeys() {
        Set<String> properties = config.getPropertyNames();
        if (!CollectionUtils.isEmpty(properties)) {
            for (String key : properties) {
                if (!StringUtils.isEmpty(flowRulePrefix) && key.startsWith(flowRulePrefix) && !key.equals(flowRulePrefix)) {
                    flowRuleKeys.add(key);
                } else if (!StringUtils.isEmpty(degradeRulePrefix) && key.startsWith(degradeRulePrefix) && !key.equals(degradeRulePrefix)) {
                    degradeRuleKeys.add(key);
                } else if (!StringUtils.isEmpty(systemRulePrefix) && key.startsWith(systemRulePrefix) && !key.equals(systemRulePrefix)) {
                    systemRuleKeys.add(key);
                } else if (!StringUtils.isEmpty(authorityRulePrefix) && key.startsWith(authorityRulePrefix) && !key.equals(authorityRulePrefix)) {
                    authorityRuleKeys.add(key);
                } else if (!StringUtils.isEmpty(paramFlowRulePrefix) && key.startsWith(paramFlowRulePrefix) && !key.equals(paramFlowRulePrefix)) {
                    paramFlowRuleKeys.add(key);
                }
            }
        }
    }

    private void initConfig() {
        flowRulePrefix = env.getProperty("sentinel.flow.rules.prefix");
        degradeRulePrefix = env.getProperty("sentinel.degrade.rules.prefix");
        systemRulePrefix = env.getProperty("sentinel.system.rules.prefix");
        authorityRulePrefix = env.getProperty("sentinel.authority.rules.prefix");
        paramFlowRulePrefix = env.getProperty("sentinel.paramFlow.rules.prefix");
        apolloNamespace = env.getProperty("apollo.sentinel.rules.namespace");
        sentinelLogDir = env.getProperty("csp.sentinel.log.dir");
        dashboardServer = env.getProperty("csp.sentinel.dashboard.server");
        projectName = env.getProperty("project.name");
        if (!StringUtils.isEmpty(sentinelLogDir)) {
            System.setProperty(LogBase.LOG_DIR, sentinelLogDir);
        }
        if (!StringUtils.isEmpty(projectName)) {
            System.setProperty(AppNameUtil.APP_NAME, projectName);
        }
        if (!StringUtils.isEmpty(dashboardServer)) {
            SentinelConfig.setConfig(TransportConfig.CONSOLE_SERVER, dashboardServer);
        }

    }

}
