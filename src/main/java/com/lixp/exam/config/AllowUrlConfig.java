package com.lixp.exam.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 不需要权限的路径
 */
@Component
@ConfigurationProperties(prefix = "exam")
public class AllowUrlConfig {

    private List<String> allowUrl;

    public List<String> getAllowUrl() {
        return allowUrl;
    }

    public void setAllowUrl(List<String> allowUrl) {
        this.allowUrl = allowUrl;
    }
}
