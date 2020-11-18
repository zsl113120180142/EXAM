package com.lixp.exam.utils;

import com.lixp.exam.bean.SysLog;
import com.lixp.exam.bean.UserInfo;
import com.lixp.exam.config.JwtConfig;
import com.lixp.exam.dao.SysLogMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.security.PublicKey;
import java.util.Date;

/**
 * 写入日志的工具类
 */
@Aspect
@Component("aopSysLogUtil")
@EnableConfigurationProperties(JwtConfig.class)
public class AopSysLogUtil {

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SysLogMapper sysLogMapper;

    @Pointcut("execution(* com.lixp.exam.controller.*.*(..))")
    public void Log(){}//切入点

    private Class clazz;
    private Method executionmethod;//访问的方法

    @Before("Log()")
    public void LogBefore(JoinPoint joinPoint) throws Exception {

        Class<?> executionclass = joinPoint.getTarget().getClass();//获取当前类
        clazz=executionclass;
        String methodName = joinPoint.getSignature().getName();//获取方法名
        Object[] args = joinPoint.getArgs();
        Class[] classes;
        if (args==null||args.length==0){
            executionmethod=executionclass.getMethod(methodName);//获取方法
        }else{
            classes=new Class[args.length];
            for (int i=0;i<args.length;i++) {
                Class arg=args[i].getClass();
                if (arg==Class.forName("org.apache.catalina.connector.RequestFacade") ||
                        arg==Class.forName("org.springframework.web.multipart.support.StandardMultipartHttpServletRequest")
                ){
                    classes[i]=Class.forName("javax.servlet.http.HttpServletRequest");
                    continue;
                }
                if (arg==Class.forName("com.alibaba.druid.support.http.WebStatFilter$StatHttpServletResponseWrapper")){
                    classes[i]=Class.forName("javax.servlet.http.HttpServletResponse");
                    continue;
                }

                if (arg==Class.forName("org.apache.catalina.session.StandardSessionFacade")){
                    classes[i]=Class.forName("javax.servlet.http.HttpSession");
                    continue;
                }

                if (arg==Class.forName("org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile"))
                {
                    classes[i]=Class.forName("org.springframework.web.multipart.MultipartFile");
                    continue;
                }

                if (arg==Class.forName("org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile"))
                {
                    classes[i]=Class.forName("org.springframework.web.multipart.MultipartFile");
                    continue;
                }


                classes[i]=arg;
            }
            executionmethod=executionclass.getMethod(methodName,classes);//获取方法
        }

    }

    @AfterReturning("Log()")
    public void LogAfter(){
        String ip = request.getRemoteAddr();//获取访问者的ip地址
        String token = request.getHeader("USERTOKEN");//获取token
        if (token==null){
            return;
        }
        PublicKey publicKey;
        UserInfo userInfo=null;
        try {
             publicKey = RsaUtils.getPublicKey(jwtConfig.getPublicKeyFilename());//获取公钥
             userInfo = JwtUtils.getInfoFromToken(token, publicKey);//解析用户信息，这里没报错的话
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        if (clazz!=null&&executionmethod!=null){
            RequestMapping classannotation = (RequestMapping)clazz.getAnnotation(RequestMapping.class);//获取类上的注解
            String[] classValue=null;
            if (classannotation!=null){//类上注解不为空
                 classValue = classannotation.value();
            }

            String[] methodvalue=null;
            PostMapping methodannotation=(PostMapping)executionmethod.getAnnotation(PostMapping.class);//获取方法上的PostMapping注解
            if (methodannotation==null){//如果postmapping为空
                GetMapping Getannotation=(GetMapping)executionmethod.getAnnotation(GetMapping.class);//获取方法上的GetMapping注解
                methodvalue=Getannotation.value();
            }else{
                methodvalue= methodannotation.value();
            }

            String url;
            if (classValue!=null)
            {
                url =classValue[0]+methodvalue[0];//获取url地址
            }else {
                url =methodvalue[0];//获取url地址
            }

            String methodname=clazz.getName()+executionmethod.getName();//获取方法名

            String username = userInfo.getUsername();

            SysLog sysLog=new SysLog();//封装日志类
            sysLog.setVisitTime(new Date());
            sysLog.setUsername(username);
            sysLog.setIp(ip);
            sysLog.setUrl(url);
            sysLog.setMethod(methodname);
            sysLogMapper.insertSelective(sysLog);//保存日志类
        }


    }


}
