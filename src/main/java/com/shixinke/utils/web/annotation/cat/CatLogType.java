package com.shixinke.utils.web.annotation.cat;

/**
 * Log type in CAT
 * @author shixinke
 * crated 19-4-9 下午3:21
 * @version 1.0
 */
public enum CatLogType {
    /**
     * database operation
     */
    SQL("SQL"),
    /**
     * HTTP Application Program Interface
     */
    API("API"),
    /**
     * Business Logic Service
     */
    SERVICE("SERVICE"),
    /**
     * Cache Operation
     */
    CACHE("CACHE"),
    /**
     * Search Component(Like ElasticSearch)
     */
    SEARCH("SEARCH"),
    /**
     * Component (Like Apollo)
     */
    COMPONENT("COMPONENT"),
    /**
     * Others
     */
    COMMON("COMMON");
    private String value;
    CatLogType(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
