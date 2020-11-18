package com.lixp.exam.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Table(name = "tbl_examinfo")
public class ExamInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer eid;//考试id，主键自增长无意义

    @Column(name="eno")
    private String eno;//考试编号1001

    @Column(name = "e_name")
    private String eName;//考试名

    @Column(name = "e_sessions")
    private String eSessions;//考试场次

    @Column(name = "e_date")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date eDate;//考试日期

    @Column(name = "e_status")
    private Integer eStatus;//考试状态 0未启用,1 启用

    @Column(name = "e_sign_start_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date eSignStartTime;//开始报名时间

    @Column(name = "e_sign_end_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date eSignEndTime;//报名结束时间

    @Column(name = "e_loc_name")
    private String eLocName;//考试地点

    private List<Subject> subjectList;

    public List<Subject> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<Subject> subjectList) {
        this.subjectList = subjectList;
    }

    /**
     * @return eid
     */
    public Integer getEid() {
        return eid;
    }

    /**
     * @param eid
     */
    public void setEid(Integer eid) {
        this.eid = eid;
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
     * @return e_name
     */
    public String geteName() {
        return eName;
    }

    /**
     * @param eName
     */
    public void seteName(String eName) {
        this.eName = eName == null ? null : eName.trim();
    }

    /**
     * @return e_sessions
     */
    public String geteSessions() {
        return eSessions;
    }

    /**
     * @param eSessions
     */
    public void seteSessions(String eSessions) {
        this.eSessions = eSessions == null ? null : eSessions.trim();
    }

    /**
     * @return e_data
     */
    public Date geteDate() {
        return eDate;
    }

    /**
     * @param eDate
     */
    public void seteDate(Date eDate) {
        this.eDate = eDate;
    }


    /**
     * @return e_status
     */
    public Integer geteStatus() {
        return eStatus;
    }

    /**
     * @param eStatus
     */
    public void seteStatus(Integer eStatus) {
        this.eStatus = eStatus;
    }

    /**
     * @return e_sign_start_time
     */
    public Date geteSignStartTime() {
        return eSignStartTime;
    }

    /**
     * @param eSignStartTime
     */
    public void seteSignStartTime(Date eSignStartTime) {
        this.eSignStartTime = eSignStartTime;
    }

    /**
     * @return e_sign_end_time
     */
    public Date geteSignEndTime() {
        return eSignEndTime;
    }

    /**
     * @param eSignEndTime
     */
    public void seteSignEndTime(Date eSignEndTime) {
        this.eSignEndTime = eSignEndTime;
    }



    /**
     * @return e_loc_name
     */
    public String geteLocName() {
        return eLocName;
    }

    /**
     * @param eLocName
     */
    public void seteLocName(String eLocName) {
        this.eLocName = eLocName == null ? null : eLocName.trim();
    }

    @Override
    public String toString() {
        return "ExamInfo{" +
                "eid=" + eid +
                ", eno='" + eno + '\'' +
                ", eName='" + eName + '\'' +
                ", eSessions='" + eSessions + '\'' +
                ", eDate=" + eDate +
                ", eStatus=" + eStatus +
                ", eSignStartTime=" + eSignStartTime +
                ", eSignEndTime=" + eSignEndTime +
                ", eLocName='" + eLocName + '\'' +
                ", subjectList=" + subjectList +
                '}';
    }
}