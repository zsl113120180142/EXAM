package com.lixp.exam.bean;

import java.util.HashMap;
import java.util.Map;

//设置返回信息对象
public class Msg {
    private int code; //设置返回码
    private String message; //设置返回的文本消息
    private Map<String, Object> data = new HashMap<>();
    ; //附加数据

    //成功的话返回码为20000
    public static Msg success() {
        Msg result = new Msg();
        result.setCode(20000);
        result.setMessage("OK");
        return result;
    }

    //失败的话返回码为10000
    public static Msg fail() {
        Msg result = new Msg();
        result.setCode(10000);
        result.setMessage("err");
        return result;
    }

    //添加返回数据
    public Msg add(String key, Object object) {
        this.getData().put(key, object);
        return this;
    }

    public int getCode() {
        return code;
    }

    public Msg setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Msg setMessage(String message) {
        this.message = message;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Msg setData(Map<String, Object> data) {
        this.data = data;
        return this;
    }
}