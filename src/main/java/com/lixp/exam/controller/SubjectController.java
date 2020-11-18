package com.lixp.exam.controller;

import com.lixp.exam.bean.CourseInfo;
import com.lixp.exam.bean.Msg;
import com.lixp.exam.bean.Specialities;
import com.lixp.exam.bean.Subject;
import com.lixp.exam.service.CourseInfoService;
import com.lixp.exam.service.SpecialitiesService;
import com.lixp.exam.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@RestController
public class SubjectController {

    @Autowired
    SubjectService subjectService;
    @Autowired
    SpecialitiesService specialitiesService;
    @Autowired
    CourseInfoService courseInfoService;


    /**
     * 更新考试科目信息
     * @param subject
     * @return
     */
    @PostMapping(value ="updateSubject",name="admin")
    public Msg updateSubject(Subject subject){
        try{
            subjectService.updataSubject(subject);

        }catch (Exception e){

            return Msg.fail().add("info","更新失败");

        }
        return Msg.success().add("subject",subject);
    }

    /**
     * 新增考试科目信息
     * @param subject
     * @param pid
     * @return
     */
    @PostMapping(value ="addSubject",name="admin")
    public Msg addSubject(Subject subject,
                          @RequestParam("pid") Integer pid
                          ){
        if (subject==null){
            return Msg.fail();
        }
        Specialities specialities = specialitiesService.selectSpecialitiesByPid(pid);
        List<CourseInfo> courseInfoList0 = courseInfoService.selectCourseInfoByENoAndcourse(subject.getEno(),specialities.getSpecialities());
        if (CollectionUtils.isEmpty(courseInfoList0)) {
            CourseInfo courseInfo = new CourseInfo();
            courseInfo.setEno(subject.getEno());
            courseInfo.setCourse(specialities.getSpecialities());
            courseInfoService.AddCourseInfo(courseInfo);
        }
       List<CourseInfo> courseInfoList = courseInfoService.selectCourseInfoByENoAndcourse(subject.getEno(),specialities.getSpecialities());
       Integer cid = courseInfoList.get(0).getCid();
       boolean b = subjectService.addSubject(subject,cid);
        if (b){
            return Msg.success();
        }else {
            return Msg.fail();
        }
    }
    /**
     * 删除考试科目信息，删除中间表的科目信息（有cid）
     * @param subId
     * @return
     */
    @PostMapping(value ="deleteSubject",name="admin")
    public Msg deleteSubject(@RequestParam("subId") Integer subId,
                             @RequestParam("cid") Integer cid
                             ){
        if (subId==null){
            return Msg.fail();
        }

            subjectService.deleteSubjectAndCid(subId,cid);
            return Msg.success();

    }


}
