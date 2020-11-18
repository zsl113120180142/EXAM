package com.lixp.exam.service;

import com.lixp.exam.bean.Student;
import com.lixp.exam.bean.StudentUser;
import com.lixp.exam.bean.UserInfo;
import com.lixp.exam.config.JwtConfig;
import com.lixp.exam.dao.StudentUserMapper;
import com.lixp.exam.utils.JwtUtils;
import com.lixp.exam.utils.RsaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.PublicKey;
import java.util.List;

@Service
public class StudentUserService {

    @Autowired
    private StudentUserMapper studentUserMapper;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    StudentService studentService;


    /**
     * 根据邮箱查询账户信息
     * @param email
     * @return
     */
    public StudentUser selectStudentUserByEmail(String email){
        StudentUser studentUser=new StudentUser();
        studentUser.setsUsername(email);
        List<StudentUser> studentUsers = studentUserMapper.select(studentUser);//查询出email为这个的账户信息
        if (CollectionUtils.isEmpty(studentUsers)){
            return null;
        }
        return studentUsers.get(0);
    }

    /**
     * 根据账户查询出考生的信息
     * @param request
     * @param eno
     * @return
     */
    public Student getStudentByToken(HttpServletRequest request,String eno){

        String token = request.getHeader(jwtConfig.getCookieName());//这里token不会为空，不然已经被拦截器拦截了
        return getStudentByToken(token,eno);
    }

    /**
     * 根据token查询出考生的信息
     * @param token
     * @param eno
     * @return
     */
    public Student getStudentByToken(String token,String eno){

        PublicKey publicKey = null;//获取公钥
        Student student=null;
        try {
            publicKey = RsaUtils.getPublicKey(jwtConfig.getPublicKeyFilename());//获取公钥

            UserInfo userInfo = JwtUtils.getInfoFromToken(token, publicKey);//解析用户信息，这里没报错的话

            String email = userInfo.getUsername();//从token中获取到报考学生的邮箱

            StudentUser studentUser = this.selectStudentUserByEmail(email);//查询出学生账户信息

            String sCardNo = studentUser.getsCardNo();//得到身份证

            student = studentService.selectStudentByCardNo(sCardNo, eno);//根据身份证查询出考生信息

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return student;
    }



}
