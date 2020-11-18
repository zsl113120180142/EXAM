package com.lixp.exam.controller;

import com.lixp.exam.bean.Msg;
import com.lixp.exam.bean.StudentUser;
import com.lixp.exam.bean.UserInfo;
import com.lixp.exam.config.JwtConfig;
import com.lixp.exam.service.UserService;
import com.lixp.exam.utils.JwtUtils;
import com.lixp.exam.utils.MailUtils;
import com.lixp.exam.utils.RsaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class UserController {

    @Autowired
    private JwtConfig jwtConfig;
    
    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @param usertype 用户类型 1为学生，2为老师
     * @param request   
     * @param response
     * @return
     */
    @PostMapping("/login")
    public Msg userlogin(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "usertype") String usertype,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        if (username==null || password == null || usertype==null){
            return Msg.fail();
        }
        //查询封装的用户信息
        UserInfo userInfo = this.userService.selectUser(username, password, usertype);

        if (userInfo.getUsername()!=null){
            try {
                String token = JwtUtils.generateToken(userInfo, RsaUtils.getPrivateKey(jwtConfig.getPrivateKeyFilename()), jwtConfig.getExpireMinutes());
                Cookie tokencookie=new Cookie(jwtConfig.getCookieName(),token);//创建token的cookie
                //tokencookie.setDomain("192.168.1.101");
                //tokencookie.setMaxAge(jwtConfig.getExpireMinutes()*60);//设置cookie的生存时间
                //response.addCookie(tokencookie);//添加cookie
                response.setStatus(HttpStatus.OK.value());

                if (usertype.equals("1")){

                    return Msg.success().add("USERTOKEN",tokencookie.getValue())
                            .add("eno",userInfo.getId())
                            .add("sCardNo",userInfo.getUsername());

                }
                return Msg.success().add("USERTOKEN",tokencookie.getValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return Msg.fail();
    }

    /**
     * 注册和找回密码
     * @param studentUser   用户信息
     * @param code          验证码
     * @param status        状态码 0 注册 1找回
     * @param session
     * @return
     */
    @PostMapping(value = "userRegister")//注册不需要权限
    public Msg userRegister(@Valid StudentUser studentUser,@RequestParam("code")String code,
                            Integer status,HttpSession session,HttpServletResponse response){

        StringBuilder sessioncode = (StringBuilder)session.getAttribute("code");//从session中获取验证码

        System.out.println("session中的验证码"+sessioncode);
        System.out.println("studentUser"+studentUser);
        System.out.println("code"+code);
        System.out.println("status"+status);
        //验证码为空，session中验证码为空，或者session中没有验证码,或者没有status

        if (code==null ||sessioncode==null || status==null || !code.equals(sessioncode.toString()))
        {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return Msg.fail().add("info","数据不合法");
        }

        if (status==0){
            studentUser.setCreated(new Date());//设置注册时间

            Boolean b=this.userService.addStudentUser(studentUser);//添加用户
            session.removeAttribute("code");//让验证码失效
            if (!b){//添加失败
                return Msg.fail().add("info","账号已注册");
            }
            return Msg.success().add("info","注册成功");
        }
        else{

            Boolean b=this.userService.findStudentUserPs(studentUser);//更新
            session.removeAttribute("code");//让验证码失效
            if (!b){//添加失败
                return Msg.fail().add("info","邮箱未被使用");
            }
        }

        return Msg.success().add("info","修改密码成功");
    }

    /**
     * 发送验证码的接口
     * @return
     */
    @PostMapping("sendCodeEmail")
    public Msg sendCodeEmail(@Valid StudentUser studentUser, HttpSession session,HttpServletRequest request,HttpServletResponse response){
        String email = studentUser.getsUsername();//获取用户的email

        StringBuilder code=new StringBuilder();
        for (int i=0;i<6;i++){
            double d=(Math.random()*10);
            Integer ii=(int)Math.floor(d);
            String s = ii.toString();
            code.append(s);
        }

        String text="您的验证码是："+code+",请不要泄露给他人。";

        MailUtils.sendMail(email,text,"【赣南医学院】");

        session.setAttribute("code",code);
        session.setMaxInactiveInterval(120);//设置过期时间

        String url = request.getHeader("Origin");
        System.out.println("url----->"+url);
        if (!StringUtils.isEmpty(url)) {
            response.setHeader("Access-Control-Allow-Origin", url);
            response.setHeader("Access-Control-Allow-Credentials", "true");
        }

        return Msg.success();
    }




}
