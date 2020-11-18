package com.lixp.exam.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "exam.picture")
public class PictureConfig {

    private String url;

    private String cardPicUrl;

    private String cardPdfUrl;

    private String ip;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCardPicUrl() {
        return cardPicUrl;
    }

    public void setCardPicUrl(String cardPicUrl) {
        this.cardPicUrl = cardPicUrl;
    }

    public String getCardPdfUrl() {
        return cardPdfUrl;
    }

    public void setCardPdfUrl(String cardPdfUrl) {
        this.cardPdfUrl = cardPdfUrl;
    }
}
