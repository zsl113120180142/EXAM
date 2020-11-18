package com.lixp.exam.bean;

/**
 * 贴在门口的考场小纸条
 */
public class ExamNote {

    private String erNo;//考场号

    private String course;//考试科目

    private String cardNoRange;//考号范围

    public String getErNo() {
        return erNo;
    }

    public void setErNo(String erNo) {
        this.erNo = erNo;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getCardNoRange() {
        return cardNoRange;
    }

    public void setCardNoRange(String cardNoRange) {
        this.cardNoRange = cardNoRange;
    }

    @Override
    public String toString() {
        return "ExamNote{" +
                "erNo='" + erNo + '\'' +
                ", course='" + course + '\'' +
                ", cardNoRange='" + cardNoRange + '\'' +
                '}';
    }
}
