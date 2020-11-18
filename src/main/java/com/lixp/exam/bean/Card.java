package com.lixp.exam.bean;

public class Card {

    private Student student;
    private ExamInfo examInfo;
    private String cErNo;
    private String cRName;
    private String cRLocName;



    public Student getStudent() {
        return student;
    }

    public Card setStudent(Student student) {
        this.student = student;
        return this;
    }

    public ExamInfo getExamInfo() {
        return examInfo;
    }

    public Card setExamInfo(ExamInfo examInfo) {
        this.examInfo = examInfo;
        return this;
    }

    public String getcErNo() {
        return cErNo;
    }

    public Card setcErNo(String cErNo) {
        this.cErNo = cErNo;
        return this;
    }

    public String getcRName() {
        return cRName;
    }

    public Card setcRName(String cRName) {
        this.cRName = cRName;
        return this;
    }

    public String getcRLocName() {
        return cRLocName;
    }

    public Card setcRLocName(String cRLocName) {
        this.cRLocName = cRLocName;
        return this;
    }

    @Override
    public String toString() {
        return "Card{" +
                "student=" + student +
                ", examInfo=" + examInfo +
                ", cErNo='" + cErNo + '\'' +
                ", cRName='" + cRName + '\'' +
                ", cRLocName='" + cRLocName + '\'' +
                '}';
    }
}
