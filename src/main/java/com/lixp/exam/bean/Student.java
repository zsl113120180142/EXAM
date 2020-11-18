package com.lixp.exam.bean;

import tk.mybatis.mapper.annotation.Order;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "tbl_student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sid;//考生id 自增长主键，无意义

    @Column(name = "s_cert_no")
    private String sCertNo;//准考证号

    @NotNull
    @Column(name = "s_name")
    private String sName;//考生姓名

    @NotNull
    @Column(name = "s_sex")
    private Integer sSex;//性别

    @Column(name = "s_card_type")
    private String sCardType;//证件类型

    @Column(name = "s_card_no")
    private String sCardNo;//证件号

    private Integer erid;//考场id 关联教室

    @Column(name = "s_room_no")
    private String sRoomNo;//座位号

    @Column(name = "s_pic")
    private String sPic;//考生照片

    private Integer eid;//考试id 关联examinfo表

    @NotEmpty
    private String eno;//考试编号

    @Column(name = "s_status")
    private Integer sStatus;//考生状态

    @Order
    private String course;//考试科目

    @Transient
    private List<Subject> subjectList;//考试科目信息的集合

    @NotEmpty
    @Column(name = "s_school")
    private String sSchool;//所属学校

    @NotEmpty
    @Column(name = "s_college")
    private String sCollege;//所属院系

    @NotEmpty
    @Column(name = "s_subject")
    private String sSubject;//专业

    @NotEmpty
    @Column(name = "s_class")
    private String sClass;//班级

    @NotEmpty
    @Column(name = "s_student_no")
    private String sStudentNo;//学号

    @Column(name = "s_uuid")
    @Order
    private String sUuid;//随机码

    /**
     * @return sid
     */
    public Integer getSid() {
        return sid;
    }

    /**
     * @param sid
     */
    public void setSid(Integer sid) {
        this.sid = sid;
    }

    /**
     * @return s_cert_no
     */
    public String getsCertNo() {
        return sCertNo;
    }

    /**
     * @param sCertNo
     */
    public void setsCertNo(String sCertNo) {
        this.sCertNo = sCertNo == null ? null : sCertNo.trim();
    }

    /**
     * @return s_name
     */
    public String getsName() {
        return sName;
    }

    /**
     * @param sName
     */
    public void setsName(String sName) {
        this.sName = sName == null ? null : sName.trim();
    }

    /**
     * @return s_sex
     */
    public Integer getsSex() {
        return sSex;
    }

    /**
     * @param sSex
     */
    public void setsSex(Integer sSex) {
        this.sSex = sSex;
    }

    /**
     * @return s_card_type
     */
    public String getsCardType() {
        return sCardType;
    }

    /**
     * @param sCardType
     */
    public void setsCardType(String sCardType) {
        this.sCardType = sCardType == null ? null : sCardType.trim();
    }

    /**
     * @return s_card_no
     */
    public String getsCardNo() {
        return sCardNo;
    }

    /**
     * @param sCardNo
     */
    public void setsCardNo(String sCardNo) {
        this.sCardNo = sCardNo == null ? null : sCardNo.trim();
    }

    /**
     * @return erid
     */
    public Integer getErid() {
        return erid;
    }

    /**
     * @param erid
     */
    public void setErid(Integer erid) {
        this.erid = erid;
    }

    /**
     * @return s_room_no
     */
    public String getsRoomNo() {
        return sRoomNo;
    }

    /**
     * @param sRoomNo
     */
    public void setsRoomNo(String sRoomNo) {
        this.sRoomNo = sRoomNo == null ? null : sRoomNo.trim();
    }

    /**
     * @return s_pic
     */
    public String getsPic() {
        return sPic;
    }

    /**
     * @param sPic
     */
    public void setsPic(String sPic) {
        this.sPic = sPic == null ? null : sPic.trim();
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
     * @return s_status
     */
    public Integer getsStatus() {
        return sStatus;
    }

    /**
     * @param sStatus
     */
    public void setsStatus(Integer sStatus) {
        this.sStatus = sStatus;
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

    /**
     * @return s_school
     */
    public String getsSchool() {
        return sSchool;
    }

    /**
     * @param sSchool
     */
    public void setsSchool(String sSchool) {
        this.sSchool = sSchool == null ? null : sSchool.trim();
    }

    /**
     * @return s_college
     */
    public String getsCollege() {
        return sCollege;
    }

    /**
     * @param sCollege
     */
    public void setsCollege(String sCollege) {
        this.sCollege = sCollege == null ? null : sCollege.trim();
    }

    /**
     * @return s_subject
     */
    public String getsSubject() {
        return sSubject;
    }

    /**
     * @param sSubject
     */
    public void setsSubject(String sSubject) {
        this.sSubject = sSubject == null ? null : sSubject.trim();
    }

    /**
     * @return s_class
     */
    public String getsClass() {
        return sClass;
    }

    /**
     * @param sClass
     */
    public void setsClass(String sClass) {
        this.sClass = sClass == null ? null : sClass.trim();
    }

    /**
     * @return s_student_no
     */
    public String getsStudentNo() {
        return sStudentNo;
    }

    /**
     * @param sStudentNo
     */
    public void setsStudentNo(String sStudentNo) {
        this.sStudentNo = sStudentNo == null ? null : sStudentNo.trim();
    }

    /**
     * @return s_uuid
     */
    public String getsUuid() {
        return sUuid;
    }

    /**
     * @param sUuid
     */
    public void setsUuid(String sUuid) {
        this.sUuid = sUuid == null ? null : sUuid.trim();
    }

    public List<Subject> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<Subject> subjectList) {
        this.subjectList = subjectList;
    }

    @Override
    public String toString() {
        return "Student{" +
                "sid=" + sid +
                ", sCertNo='" + sCertNo + '\'' +
                ", sName='" + sName + '\'' +
                ", sSex=" + sSex +
                ", sCardType='" + sCardType + '\'' +
                ", sCardNo='" + sCardNo + '\'' +
                ", erid=" + erid +
                ", sRoomNo='" + sRoomNo + '\'' +
                ", sPic='" + sPic + '\'' +
                ", eid=" + eid +
                ", eno='" + eno + '\'' +
                ", sStatus=" + sStatus +
                ", course='" + course + '\'' +
                ", subjectList=" + subjectList +
                ", sSchool='" + sSchool + '\'' +
                ", sCollege='" + sCollege + '\'' +
                ", sSubject='" + sSubject + '\'' +
                ", sClass='" + sClass + '\'' +
                ", sStudentNo='" + sStudentNo + '\'' +
                ", sUuid='" + sUuid + '\'' +
                '}';
    }
}