package com.lixp.exam.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 正在进行的考试
 */
public class StartExamInfo extends ExamInfo {


    private List<CourseInfo> course;//可报名的科目


    public List<CourseInfo> getCourse() {
        return course;
    }

    public void setCourse(List<CourseInfo> course) {
        this.course = course;
    }

    @Override
    public String toString() {
                return "ExamInfo{" +
                "eid=" + getEid() +
                ", eno='" + getEno() + '\'' +
                ", eName='" + geteName() + '\'' +
                ", eSessions='" + geteSessions() + '\'' +
                ", eData=" + geteDate() +
                ", eStatus=" + geteStatus() +
                ", eSignStartTime=" + geteSignStartTime() +
                ", eSignEndTime=" + geteSignEndTime() +
                ", eLocName='" + geteLocName() + '\'' +
                '}'+
                "StartExamInfo{" +
                "course=" + course +
                '}';
    }
}