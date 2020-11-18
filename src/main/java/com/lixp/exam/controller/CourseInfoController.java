package com.lixp.exam.controller;

import com.lixp.exam.bean.CourseInfo;
import com.lixp.exam.bean.ExamInfo;
import com.lixp.exam.bean.Msg;
import com.lixp.exam.bean.Subject;
import com.lixp.exam.dao.CourseAndSubjectMapper;
import com.lixp.exam.service.CourseInfoService;
import com.lixp.exam.service.ExamInfoService;
import com.lixp.exam.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class CourseInfoController {

    @Autowired
    CourseInfoService courseInfoService;



    @Autowired
    ExamInfoService examInfoService;

    @Autowired
    SubjectService subjectService;
    /**
     *添加专业
     * @param courseInfoList
     * @return
     */
    @PostMapping(value = "/AddCourse",name="admin")
    public Msg addCourse(@RequestBody List<CourseInfo> courseInfoList){
        boolean b = courseInfoService.batchInsertCourseInfo(courseInfoList);
        if(b){
            return Msg.success();
        }else {
            return Msg.fail();
        }
    }

    /**
     * 更新专业
     * @param courseInfo
     * @return
     */
    @PostMapping(value = "/UpdateCourse",name="admin")
    public Msg UpdateCourse(CourseInfo courseInfo){
        if (courseInfo==null){
            return Msg.fail();
        }
        boolean b =courseInfoService.updateCourse(courseInfo);
        if (!b){
            return Msg.fail();
        }

        return Msg.success();
    }
    /**
     *删除专业,专业下的考试信息也会被一并删除
     * @param cid
     * @return
     */
    @PostMapping(value = "/deleteCourse",name="admin")
    public Msg deleteCourse(@RequestParam("cid") Integer cid){

        boolean b = courseInfoService.deleteCourseInfoByCid(cid);
        if (!b){
            return Msg.fail();
        }

        return Msg.success();
    }


    /**
     * 查询已开始的考试的全部专业,及专业下对应的考试
     * @return
     */
    @PostMapping(value = "getCourseAndSubject",name="admin")
    public Msg getCourseAndSubject(@RequestParam("eno") String eno){
      /*  List<ExamInfo> examInfos = examInfoService.selectAllExamInfoByStatus(1);//查询出当前已开始的考试
        if (CollectionUtils.isEmpty(examInfos)){
            return Msg.fail().add("info","当前无考试信息");
        }
        String eno = examInfos.get(0).getEno();*/

        List<CourseInfo> courseInfos = courseInfoService.selectCourseInfoByENo(eno);//查询出全部的考试专业

        for (CourseInfo courseInfo:courseInfos){
            System.out.println(courseInfo);
            List<Subject> subjects = subjectService.selectSubjectByCourseName(eno,courseInfo.getCourse());
            courseInfo.setSubjects(subjects);
        }
        Integer eStatus;
        if(courseInfos!=null){
             eStatus = 1;
        }else {
            eStatus = 0;
        }
        return Msg.success().add("courseInfos",courseInfos).add("eStatus",eStatus);
    }
}
