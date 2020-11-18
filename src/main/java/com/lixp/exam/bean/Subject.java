package com.lixp.exam.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Table(name="tbl_subject")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer subId;

    private String subjectName;

    @Column(name = "s_date")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date sDate;//考试日期

    @Column(name = "s_start_time")
    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(pattern = "HH:mm:ss")
    private Date sStartTime;//考试开始时间 13:00:00

    @Column(name = "s_end_time")
    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(pattern = "HH:mm:ss")
    private Date sEndTime;//结束时间 时分秒

    private String eno;

    public Integer getSubId() {
        return subId;
    }

    public void setSubId(Integer subId) {
        this.subId = subId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Date getsDate() {
        return sDate;
    }

    public void setsDate(Date sDate) {
        this.sDate = sDate;
    }

    public Date getsStartTime() {
        return sStartTime;
    }

    public void setsStartTime(Date sStartTime) {
        this.sStartTime = sStartTime;
    }

    public Date getsEndTime() {
        return sEndTime;
    }

    public void setsEndTime(Date sEndTime) {
        this.sEndTime = sEndTime;
    }

    public String getEno() {
        return eno;
    }

    public void setEno(String eno) {
        this.eno = eno;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "subId=" + subId +
                ", subjectName='" + subjectName + '\'' +
                ", sDate=" + sDate +
                ", sStartTime=" + sStartTime +
                ", sEndTime=" + sEndTime +
                ", eno='" + eno + '\'' +
                '}';
    }
}
