package com.lixp.exam.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lixp.exam.bean.*;
import com.lixp.exam.config.JwtConfig;
import com.lixp.exam.service.*;
import com.lixp.exam.utils.JwtUtils;
import com.lixp.exam.utils.RsaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static java.lang.System.out;

@RestController
public class ExamInfoController {
    @Autowired
    ExamInfoService examInfoService;

    @Autowired
    StudentService studentService;

    @Autowired
    RoomInfoService roomInfoService;

    @Autowired
    StudentUserService studentUserService;

    @Autowired
    CourseInfoService courseInfoService;

    /**
     * 查询可以报名的考试
     * @return
     */
    @GetMapping(value = "startingExam",name="user")
    public Msg startingExam(){
        Date date = new Date();//获取当前时间
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");//转换
        String formatdate = simpleDateFormat.format(date);

        List<StartExamInfo> startExamInfo=examInfoService.selectStartingExam(formatdate);//查询出当前正在进行的考试

        if (startExamInfo==null)
        {
            return Msg.fail().add("info","当前没有正在进行的考试");
        }

        return Msg.success().add("startExamInfo",startExamInfo);
    }

    /**
     * 报名考试
     * @return
     */
    @PostMapping(value = "signUpExam",name="user")
    public Msg signUp(@RequestParam("eno") String eno,@RequestParam("cid") Integer cid,HttpServletRequest request){
        Date date = new Date();//获取当前时间
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");//转换
        String formatdate = simpleDateFormat.format(date);

        List<StartExamInfo> startExamInfos=examInfoService.selectStartingExam(formatdate);//查询出当前正在进行的考试
        boolean b=false;
        for (StartExamInfo startExamInfo:startExamInfos){//查询正在进行的考试有没有报考的这一门,防止过期以后还在报考
            if (startExamInfo.getEno().equals(eno)){
                b=true;
            }
        }

        if (!b){
            return Msg.fail().add("info","报名已结束");
        }
        try{
            Student student = studentUserService.getStudentByToken(request, eno);//根据当前账户查询出考生的信息

            if (StringUtils.isEmpty(student.getsName())){//没名字
                return Msg.fail().add("info","请完善个人信息").add("eno",eno);
            }
            if (!StringUtils.isEmpty(student.getCourse())){//报名课程不为空
                return Msg.fail().add("info","请勿重复报名");
            }
            String course = courseInfoService.selectCourseByCid(cid);
            student.setCourse(course);
            studentService.updateStudent(student);//更新学生
        } catch (Exception e) {
            e.printStackTrace();
            return  Msg.fail().add("info","报名异常,请稍后再试");
        }
        return Msg.success().add("info","报名成功");
    }



    /**
     * 完善报名的学生信息
     * @return
     */
    @PostMapping(value = "completeInfo",name="user")
    public Msg completeInfo(@Valid Student student, HttpServletRequest request){

        try {
            Student student1 = studentUserService.getStudentByToken(request, student.getEno());//根据当前账户查询出考生的信息

              student.setsCardNo(student1.getsCardNo());
//            student.setEno(student1.getEno());
//            student.setSid(student1.getSid());
//            student.setsUuid(student1.getsUuid());

            studentService.updateStudent(student);//更新学生
        } catch (Exception e) {
            e.printStackTrace();
            return  Msg.fail().add("info","完善信息异常,请稍后再试");
        }

        return Msg.success().add("info","完善信息成功,赶快去报名吧");
    }

    /**
     * @Description: selectByNameorCardno方法是模糊查询
     * @param:
     * @return:
     * @auther: zsl
     * @date: 2020/8/16 19:24
     */
    @PostMapping(value = "/selectByNameorCardno",name="admin")
    public Msg selectByNameorCardno(@RequestParam("value")String search){
        if (search.equals("")){
            return Msg.fail();
        }else {
            String NoBlankSearch = search.replace(" ", "");
            List<Student> studentList = studentService.selectByNameorCardno(NoBlankSearch);
            return Msg.success().add("students",studentList);
        }
    }

    /**
     * 查询所有考试
     * @return
     */
    @PostMapping(value = "/AllExamInfo",name="admin")
    public Msg getAllExamInfo(){
        List<ExamInfo> examInfoList = examInfoService.selectAllExamInfo();
        return Msg.success().add("examInfoList",examInfoList);
    }

    /**
     * 查询所有考试及其底下的考试科目
     * @return
     */
    @PostMapping(value = "/AllExamInfoAndCourse",name="admin")
    public Msg AllExamInfoAndCourse(){
        List<ExamInfo> examInfoList = examInfoService.selectAllExamInfoAndCourse();
        return Msg.success().add("examInfoList",examInfoList);
    }

    /**
     * 添加考试
     * @param examInfo
     * @return
     */
    @PostMapping(value = "/AddExam",name="admin")
    public Msg addExamInfo(ExamInfo examInfo){
        out.println(examInfo.getEno());
        boolean b = examInfoService.addExamInfo(examInfo);
        if(b){
            return Msg.success();
        }else {
            return Msg.fail();
        }
    }

    /**
     * 修改考试
     * @param examInfo
     * @return
     */
    @PostMapping(value = "/UpdateExam",name="admin")
    public Msg updateExamInfo(ExamInfo examInfo){
        if (examInfo==null){
            return Msg.fail();
        }
        boolean b =examInfoService.updateExamInfo(examInfo);
        if (!b){
            return Msg.fail();
        }

        return Msg.success();
    }

    /**
     *删除大考试,大考试下的考试科目也会被一并删除
     * @param eid
     * @return
     */
    @PostMapping(value = "/deleteExam",name="admin")
    public Msg deleteExam(@RequestParam("eid") Integer eid){

        boolean b = examInfoService.deleteExamInfoByEid(eid);
        if (!b){
            return Msg.fail();
        }

        return Msg.success();
    }



    @GetMapping(value = "/NewExamInfo/{eid}",name="admin")
    public Msg getExamInfo(@PathVariable("eid")Integer eid){
        ExamInfo examInfo = examInfoService.selectExamInfoById(eid);//查询出单个考试信息

        //通过考试编号，获取参加该考试的考生数量及对应科目下的考试人数
        Map<String, Object> studentCount = studentService.getStudentCountByENoAndCourse(examInfo.getEno());
        //查询出可用教室
        List<RoomInfo> roomInfoList = roomInfoService.selectAllRoomInfoByStatus(1);
        //获取人数信息
        int persons = roomInfoService.getPersons(roomInfoList);
        return Msg.success().add("studentCount",studentCount)
                            .add("examInfo",examInfo)
                            .add("roomInfoList",roomInfoList)
                            .add("roompersons",persons);

    }


}
