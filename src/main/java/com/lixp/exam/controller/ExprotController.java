package com.lixp.exam.controller;


import com.lixp.exam.bean.*;
import com.lixp.exam.config.JwtConfig;
import com.lixp.exam.service.*;
import com.lixp.exam.utils.ExcelUtils;
import com.lixp.exam.utils.JwtUtils;
import com.lixp.exam.utils.RsaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RestController
public class ExprotController {
    @Autowired
    JwtConfig jwtConfig;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ExamRoomInfoService examRoomInfoService;

    @Autowired
    ExamInfoService examInfoService;

    @Autowired
    StudentUserService studentUserService;

    @Autowired
    SubjectService subjectService;
    /**
     * 导出小纸条和考场签到表 合二为一
     * @return
     */
   /* @PostMapping(value = "exportNote",name = "admin")
    public Msg exportNote(@RequestParam("erid") Integer erid ){
        Map<String,List<StudentNote>> notemap=this.studentService.getStudentNotes(erid);//查询出全部的学生小纸条信息
        return Msg.success().add("notemap",notemap);
    }*/

    /**
     * 导出考场门口的纸条
     * @return
     */
    @PostMapping(value = "exportExamNote",name="admin")
    public Msg exportExamNote() {
        Map<String, ExamNote> ExamNotemap=this.examRoomInfoService.getExamNotes();

        return Msg.success().add("ExamNotemap",ExamNotemap);
    }

    /**
     *导出考试信息的excel表格
     */
    @GetMapping(value = "exportExamExcel/{eno}/{erid}", name="admin")
    public void exportExamExcel(HttpServletResponse response,
                                @RequestParam("USERTOKEN") String usertoken,
                                @PathVariable(value = "eno") String eno,
                                @PathVariable(value = "erid") Integer erid){
        PublicKey publicKey;
        try {
            publicKey = RsaUtils.getPublicKey(jwtConfig.getPublicKeyFilename());//获取公钥

            UserInfo userInfo = JwtUtils.getInfoFromToken(usertoken, publicKey);//解析用户信息，这里没报错的话
            if (!userInfo.getRole().equals("admin")){//角色为admin
                return;
            }
        }catch (Exception e){
            return;
        }
        List<Object> studentExcel = this.studentService.getStudentExcel(eno,erid);
        Class<? extends Object> aClass = studentExcel.get(0).getClass();
        Field[] fields = aClass.getDeclaredFields();//获取全部属性
        String[] titles=new String[fields.length];
        for (int i=0;i<fields.length;i++){
            String f=fields[i].toString();
            int index=fields[i].toString().lastIndexOf('.');
            titles[i]=f.substring(index+1,f.length());
        }
        byte[] body= ExcelUtils.export("考生信息", titles, studentExcel);//
        response.addHeader("Content-Disposition", "attchement;filename=/" + "student.xlsx");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/vnd.ms-excel");

        ServletOutputStream outputStream=null;
        try {
            response.setStatus(HttpStatus.OK.value());
            outputStream = response.getOutputStream();
            outputStream.write(body);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return;
    }

    /**
     *导出学生准考证,已废弃
     */
//    @GetMapping(value = "exportSCard", name="user")
//    public Msg exportSCard(HttpServletRequest request,HttpServletResponse response) {
//
//        List<ExamInfo> examInfos = examInfoService.selectAllExamInfoByStatus(1);//查询出当前已开始的考试
//
//        String eno = examInfos.get(0).getEno();//获取第一个已开始的考试
//
//        Student student = studentUserService.getStudentByToken(request, eno);//根据eno和身份证查询对应的学生
//
//        String sCertNo = student.getsCertNo();//得到准考证号
//
//        String course=student.getCourse();//得到要考专业
//
//        if (StringUtils.isEmpty(sCertNo) || StringUtils.isEmpty(course)){//学生没有准考证,或者没有报考专业
//            return Msg.fail().add("info","暂无准考证信息");
//        }
//
//        List<Subject> subjects=subjectService.selectSubjectByCourseName(course);//根据专业名称得到所有科目信息
//
//        student.setSubjectList(subjects);
//
//
//        return Msg.success().add("student",student);
//
//    }



}
