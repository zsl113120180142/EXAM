package com.lixp.exam.bean;

import javax.persistence.*;
import java.util.Date;

@Table(name="tbl_syslog")
public class SysLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;              //id自增长无意义

    @Column(name="visitTime")
    private Date visitTime;         //访问时间

    @Transient
    private String visitTimeStr;    //访问时间的字符串形式

    private String username;        //访问的用户名

    private String ip;              //访问者的ip地址

    private String url;             //访问的资源路径

    private String method;          //执行的方法



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(Date visitTime) {
        this.visitTime = visitTime;
    }

    public String getVisitTimeStr() {
        return visitTimeStr;
    }

    public void setVisitTimeStr(String visitTimeStr) {
        this.visitTimeStr = visitTimeStr;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

}
