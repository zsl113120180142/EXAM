package com.lixp.exam.bean;

import tk.mybatis.mapper.annotation.Order;

import javax.persistence.*;
import java.util.List;

@Table(name = "tbl_examroominfo")
public class ExamRoomInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer erid;

    private Integer rid;

    private String eno;

    @Column(name = "er_No")
    private String erNo;

    @Order
    private String course;

    @Transient
    private List<Student> studentList;//考试科目信息的集合

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
     * @return rid
     */
    public Integer getRid() {
        return rid;
    }

    /**
     * @param rid
     */
    public void setRid(Integer rid) {
        this.rid = rid;
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
     * @return er_No
     */
    public String getErNo() {
        return erNo;
    }

    /**
     * @param erNo
     */
    public void setErNo(String erNo) {
        this.erNo = erNo == null ? null : erNo.trim();
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
        this.course = course;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    @Override
    public String toString() {
        return "ExamRoomInfo{" +
                "erid=" + erid +
                ", rid=" + rid +
                ", eno='" + eno + '\'' +
                ", erNo='" + erNo + '\'' +
                ", course='" + course + '\'' +
                ", studentList=" + studentList +
                '}';
    }
}