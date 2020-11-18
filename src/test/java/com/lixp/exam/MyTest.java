package com.lixp.exam;

import com.lixp.exam.bean.*;
import com.lixp.exam.config.PictureConfig;
import com.lixp.exam.dao.SpecialitiesMapper;
import com.lixp.exam.dao.StudentMapper;
import com.lixp.exam.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MyTest {
    @Autowired
    private StudentService studentService;

    @Autowired
    private UserService userService;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    PictureConfig pictureConfig;

    @Autowired
    SpecialitiesMapper specialitiesMapper;


    @Autowired
    ExamInfoService examInfoService;
    @Autowired
    ExamRoomInfoService examRoomInfoService;
    @Autowired
    SubjectService subjectService;
    @Autowired
    CourseInfoService courseInfoService;

    @Test
    public void testuserService(){
        UserInfo userInfo = userService.selectUser("lishi","123456","1");
        System.out.println(userInfo);
    }


    @Test
    public void test(){
        List<ExamInfo> examInfos = examInfoService.selectAllExamInfoByStatus(1);//查询出当前已开始的考试
        if (CollectionUtils.isEmpty(examInfos)){
            System.out.println("当前无考试信息");
        }
        String eno = examInfos.get(0).getEno();

        List<CourseInfo> courseInfos = courseInfoService.selectCourseInfoByENo(eno);//查询出全部的考试专业

        for (CourseInfo courseInfo:courseInfos){
            List<Subject> subjects = subjectService.selectSubjectByCourseName(courseInfo.getEno(),courseInfo.getCourse());
            courseInfo.setSubjects(subjects);
        }

        System.out.println(courseInfos);
    }


    @Test
    public void test1(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-mm-dd");
        Date date=null;
        try {
            date=simpleDateFormat.parse("2020-07-09");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        examInfoService.selectStartingExam("2020-07-09");
    }

    @Test
    public void test02(){
        List<Student> students = studentMapper.selectAll();
        for (int i=0;i<students.size();i++){
            String picture =  "http://"+pictureConfig.getIp()+":8080/EXAM/static/"+students.get(i).getsCardNo()+".jpg";
            System.out.println(picture);
            students.get(i).setsPic(picture);
            studentService.updateStudent(students.get(i));


        }
    }



}
