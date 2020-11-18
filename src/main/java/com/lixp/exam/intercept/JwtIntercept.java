package com.lixp.exam.intercept;

import com.lixp.exam.bean.UserInfo;
import com.lixp.exam.config.AllowUrlConfig;
import com.lixp.exam.config.JwtConfig;
import com.lixp.exam.utils.JwtUtils;
import com.lixp.exam.utils.RsaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.PublicKey;

@Component
@EnableConfigurationProperties({JwtConfig.class, AllowUrlConfig.class})
public class JwtIntercept implements HandlerInterceptor {

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private AllowUrlConfig allowUrlConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //String token = CookieUtils.getCookie(request, jwtConfig.getCookieName());//获取请求中的token
        String token = request.getHeader("USERTOKEN");
        String url=request.getRequestURL().toString();//获取请求接口的url

        //System.out.println("拦截器启动了");

        if (token==null || token.length()==0){//没有token

            for (String allowUrl:allowUrlConfig.getAllowUrl())
            {
                if (url.contains(allowUrl)){//请求地址为不需要权限的路径,直接放行
                    return true;
                }
            }
            response.setStatus(HttpStatus.UNAUTHORIZED.value());//返回未认证401
            return false;
        }
        boolean b = this.jurisdictionAnalysis(request, response, handler, token);
        return b;
    }

    private  boolean jurisdictionAnalysis(HttpServletRequest request, HttpServletResponse response, Object handler,String token){
        //cookie中携带token
        try {
            PublicKey publicKey = RsaUtils.getPublicKey(jwtConfig.getPublicKeyFilename());//获取公钥

            UserInfo userInfo = JwtUtils.getInfoFromToken(token, publicKey);//解析用户信息，这里没报错的话

            String role = userInfo.getRole();//获取当前用户的角色信息

            HandlerMethod handlerMethod = (HandlerMethod) handler;//对handler进行强制类型转换

            PostMapping postannotation = handlerMethod.getMethodAnnotation(PostMapping.class);//获取访问方法的那个注解

            if (postannotation!=null){//如果不为空
                String name = postannotation.name();//获取controller方法注解的name值

                if (role.equals(name)){
                    return true;//放行
                }
            }
            GetMapping getannotation=null;
            if (postannotation==null){//如果为空，证明是getmapping，
                getannotation = handlerMethod.getMethodAnnotation(GetMapping.class);//获取访问方法的那个注解
                String name = getannotation.name();//获取controller方法注解的name值
                if (role.equals(name)){
                    return true;//放行
                }
            }

        } catch (Exception e) {

            response.setStatus(HttpStatus.NOT_FOUND.value());//返回404

            e.printStackTrace();
            return false;
        }
        response.setStatus(HttpStatus.UNAUTHORIZED.value());//返回未认证401
        return false;

    }
}
