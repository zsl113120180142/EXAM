package com.lixp.exam.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 不需要权限的路径
 */
@Component
@ConfigurationProperties(prefix = "exam.email")
public class EmailConfig {
    public static String address;
    public static String authorizationCode;
    public static String expirationTime;

    public String getAddress() {
        return address;
    }

    @Value("exam.email.address")
    public void setAddress(String address) {
        this.address = address;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }
    @Value("exam.email.authorizationCode")
    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getExpirationTime() {
        return expirationTime;
    }

    @Value("exam.email.expirationTime")
    public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
    }
}
