package com.lixp.exam.config;

import com.lixp.exam.utils.RsaUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;

@Component
@ConfigurationProperties(prefix = "exam.jwt")
public class JwtConfig {
    private String publicKeyFilename;  //公钥路径
    private String privateKeyFilename; //私钥路径
    private String secret;    //盐值
    private Integer expireMinutes; //过期时间,单位分钟
    private String cookieName; //cookie名称

    @PostConstruct //构造方法以后执行
    private void init(){
        File pubfile=new File(this.getPublicKeyFilename());//创建公钥的文件类
        File prifile=new File(this.getPrivateKeyFilename());//创建私钥的文件类

        //公钥或者私钥不存在，新建一套
        if (!pubfile.exists() || !prifile.exists())
        {
            try {
                RsaUtils.generateKey(this.getPublicKeyFilename(), this.getPrivateKeyFilename(), this.getSecret());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public String getCookieName() {
        return cookieName;
    }

    public void setCookieName(String cookieName) {
        this.cookieName = cookieName;
    }

    public String getPublicKeyFilename() {
        return publicKeyFilename;
    }

    public void setPublicKeyFilename(String publicKeyFilename) {
        this.publicKeyFilename = publicKeyFilename;
    }

    public String getPrivateKeyFilename() {
        return privateKeyFilename;
    }

    public void setPrivateKeyFilename(String privateKeyFilename) {
        this.privateKeyFilename = privateKeyFilename;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Integer getExpireMinutes() {
        return expireMinutes;
    }

    public void setExpireMinutes(Integer expireMinutes) {
        this.expireMinutes = expireMinutes;
    }
}
