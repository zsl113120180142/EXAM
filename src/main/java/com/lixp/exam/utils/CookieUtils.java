package com.lixp.exam.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 解析cookie的工具类
 */
public class CookieUtils {

    public static String getCookie(HttpServletRequest request, String cookieName){
        Cookie[] cookies = request.getCookies();//获取全部cookie

        //未携带cookie
        if (cookies==null||cookies.length==0){
            return null;
        }

        //遍历
        for (Cookie cookie:cookies){
            if (cookie.getName().equals(cookieName))
            {
                return cookie.getValue();//返回指定cookie
            }
        }

        return null;
    }
}
