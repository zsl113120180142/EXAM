package com.lixp.exam.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Table(name = "tbl_courseinfo")
public class CourseInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cid;

    private String eno;

    private String course;

    @Transient
    private List<Subject> subjects;

//    @Column(name = "c_date")
//    @DateTimeFormat(pattern="yyyy-MM-dd")
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    private Date cData;//考试日期
//
//    @Column(name = "e_start_time")
//    @DateTimeFormat(pattern = "HH:mm:ss")
//    @JsonFormat(pattern = "HH:mm:ss")
//    private Date eStartTime;//考试开始时间 13:00:00
//
//    @Column(name = "e_end_time")
//    @DateTimeFormat(pattern = "HH:mm:ss")
//    @JsonFormat(pattern = "HH:mm:ss")
//    private Date eEndTime;//结束时间 时分秒

    /**
     * @return cid
     */
    public Integer getCid() {
        return cid;
    }

    /**
     * @param cid
     */
    public void setCid(Integer cid) {
        this.cid = cid;
    }

    /**
     * @return eno
     */
    public String getEno() {
        return eno;
    }

    /**
     * @param eno
     */
    public void setEno(String eno) {
        this.eno = eno == null ? null : eno.trim();
    }

    /**
     * @return course
     */
    public String getCourse() {
        return course;
    }

    /**
     * @param course
     */
    public void setCourse(String course) {
        this.course = course == null ? null : course.trim();
    }

    public CourseInfo(String eno, String course) {
        this.eno = eno;
        this.course = course;
    }

    public CourseInfo() {
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    //    public Date getcData() {
//        return cData;
//    }
//
//    public void setcData(Date cData) {
//        this.cData = cData;
//    }
//
//    public Date geteStartTime() {
//        return eStartTime;
//    }
//
//    public void seteStartTime(Date eStartTime) {
//        this.eStartTime = eStartTime;
//    }
//
//    public Date geteEndTime() {
//        return eEndTime;
//    }
//
//    public void seteEndTime(Date eEndTime) {
//        this.eEndTime = eEndTime;
//    }

    @Override
    public String toString() {
        return "CourseInfo{" +
                "cid=" + cid +
                ", eno='" + eno + '\'' +
                ", course='" + course + '\'' +
                '}';
    }
}