package com.shixinke.utils.web.config;

import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import com.alibaba.csp.sentinel.config.SentinelConfig;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.apollo.ApolloDataSource;
import com.alibaba.csp.sentinel.log.LogBase;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
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
import java.util.HashSet;
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

    @Autowired
    private Environment env;

    private List<String> flowRuleKeys = new ArrayList<>(0);
    private List<String> degradeRuleKeys = new ArrayList<>(0);
    private List<String> authorityRuleKeys = new ArrayList<>(0);
    private List<String> systemRuleKeys = new ArrayList<>(0);
    private List<String> paramFlowRuleKeys = new ArrayList<>(0);


    private String flowRulePrefix;

    private String degradeRulePrefix;

    private String systemRulePrefix;

    private String authorityRulePrefix;

    private String paramFlowRulePrefix;

    private String apolloNamespace;

    private String sentinelLogDir;

    private String dashboardServer;

    private String projectName;


    private static final String FLOW_RULE_PREFIX_KEY =  "sentinel.flow.rules.prefix";
    private static final String DEGRADE_RULE_PREFIX_KEY =  "sentinel.degrade.rules.prefix";
    private static final String AUTHORITY_RULE_PREFIX_KEY =  "sentinel.authority.rules.prefix";
    private static final String SYSTEM_RULE_PREFIX_KEY =  "sentinel.system.rules.prefix";
    private static final String PARAM_FLOW_RULE_PREFIX_KEY =  "sentinel.paramFlow.rules.prefix";
    private static final String SENTINEL_APOLLO_RULE_NAMESPACE_KEY = "apollo.sentinel.rules.namespace";
    private static final String SENTINEL_LOG_DIR_KEY = "csp.sentinel.log.dir";
    private static final String SENTINEL_SERVER_KEY = "csp.sentinel.dashboard.server";
    private static final String PROJECT_NAME_KEY = "project.name";

    @ApolloConfig
    private Config config;

    @Bean
    public SentinelResourceAspect sentinelResourceAspect() {
        initConfig();
        initRuleKeys();
        String defaultRules = "[]";
        loadFlowRules(defaultRules);
        loadDegradeRules(defaultRules);
        loadAuthorityRules(defaultRules);
        loadSystemRules(defaultRules);
        loadParamFlowRules(defaultRules);
        return new SentinelResourceAspect();
    }

    private void loadFlowRules(String defaultRules) {
        if (!CollectionUtils.isEmpty(flowRuleKeys)) {
            for (String flowRuleKey : flowRuleKeys) {
                ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new ApolloDataSource<>(apolloNamespace, flowRuleKey, defaultRules, source -> {
                    log.info("flow rules source:{}", source);
                    return JSON.parseObject(source, new TypeReference<List<FlowRule>>() {});});
                FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
            }
            log.info("flow rules:{}", FlowRuleManager.getRules());
        }
    }

    private void loadDegradeRules(String defaultRules) {
        if (!CollectionUtils.isEmpty(degradeRuleKeys)) {
            for (String degradeRuleKey : degradeRuleKeys) {
                ReadableDataSource<String, List<DegradeRule>> degradeRuleDataSource = new ApolloDataSource<>(apolloNamespace, degradeRuleKey, defaultRules, source -> {
                    log.info("degrade rules source:{}", source);
                    return JSON.parseObject(source, new TypeReference<List<DegradeRule>>() {});});
                DegradeRuleManager.register2Property(degradeRuleDataSource.getProperty());
            }
            log.info("degrade rules:{}", DegradeRuleManager.getRules());
        }
    }

    private void loadAuthorityRules(String defaultRules) {
        if (!CollectionUtils.isEmpty(authorityRuleKeys)) {
            for (String authorityRuleKey : authorityRuleKeys) {
                ReadableDataSource<String, List<AuthorityRule>> authorityRuleDataSource = new ApolloDataSource<>(apolloNamespace, authorityRuleKey, defaultRules, source -> {
                    log.info("authority rules source:{}", source);
                    return JSON.parseObject(source, new TypeReference<List<AuthorityRule>>() {});});
                AuthorityRuleManager.register2Property(authorityRuleDataSource.getProperty());
            }
            log.info("authority rules:{}", AuthorityRuleManager.getRules());
        }
    }

    private void loadSystemRules(String defaultRules) {
        if (!CollectionUtils.isEmpty(systemRuleKeys)) {
            for (String systemRuleKey : systemRuleKeys) {
                ReadableDataSource<String, List<SystemRule>> systemRuleDataSource = new ApolloDataSource<>(apolloNamespace, systemRuleKey, defaultRules, source -> {
                    log.info("system rules source:{}", source);
                    return JSON.parseObject(source, new TypeReference<List<SystemRule>>() {});});
                SystemRuleManager.register2Property(systemRuleDataSource.getProperty());
            }
            log.info("system rules:{}", SystemRuleManager.getRules());
        }
    }

    private void loadParamFlowRules(String defaultRules) {
        if (!CollectionUtils.isEmpty(paramFlowRuleKeys)) {
            for (String paramFlowRuleKey : paramFlowRuleKeys) {
                ReadableDataSource<String, List<ParamFlowRule>> paramFlowRuleDataSource = new ApolloDataSource<>(apolloNamespace, paramFlowRuleKey, defaultRules, source -> {
                    log.info("param flow rules source:{}", source);
                    return JSON.parseObject(source, new TypeReference<List<ParamFlowRule>>() {});});
                ParamFlowRuleManager.register2Property(paramFlowRuleDataSource.getProperty());
            }
            log.info("param flow rules:{}", ParamFlowRuleManager.getRules());
        }
    }


    private void initRuleKeys() {
        Set<String> properties = new HashSet<>(5);
        if (config != null) {
            properties = config.getPropertyNames();
        } else {
            log.debug("not enabled apollo");
        }

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
        flowRulePrefix = env.getProperty(FLOW_RULE_PREFIX_KEY);
        degradeRulePrefix = env.getProperty(DEGRADE_RULE_PREFIX_KEY);
        systemRulePrefix = env.getProperty(SYSTEM_RULE_PREFIX_KEY);
        authorityRulePrefix = env.getProperty(AUTHORITY_RULE_PREFIX_KEY);
        paramFlowRulePrefix = env.getProperty(PARAM_FLOW_RULE_PREFIX_KEY);
        apolloNamespace = env.getProperty(SENTINEL_APOLLO_RULE_NAMESPACE_KEY);
        sentinelLogDir = env.getProperty(SENTINEL_LOG_DIR_KEY);
        dashboardServer = env.getProperty(SENTINEL_SERVER_KEY);
        projectName = env.getProperty(PROJECT_NAME_KEY);
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
