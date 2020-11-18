package com.lixp.exam.bean;

/**
 * 导出考生信息
 */
public class StudentExcel extends StudentNote{
    private String sCertNo;//准考证号
    private String sName;//考生姓名
    private Integer sSex;//性别
    private String sCardType;//证件类型
    private String sCardNo;//证件号
    private String sCollege;//所属院系
    private String sSubject;//专业
    private String sClass;//班级
    private String sStudentNo;//学号
    private String rLocName;//地点名称，第一教学楼
    private String rName;//教室名例如604
    private String sRoomNo;//座位号
    private String eName;//考试名
    private String course;//考生科目

    @Override
    public String getsCertNo() {
        return sCertNo;
    }

    @Override
    public void setsCertNo(String sCertNo) {
        this.sCertNo = sCertNo;
    }

    @Override
    public String getsName() {
        return sName;
    }

    @Override
    public void setsName(String sName) {
        this.sName = sName;
    }

    @Override
    public Integer getsSex() {
        return sSex;
    }

    @Override
    public void setsSex(Integer sSex) {
        this.sSex = sSex;
    }

    @Override
    public String getsCardNo() {
        return sCardNo;
    }

    @Override
    public void setsCardNo(String sCardNo) {
        this.sCardNo = sCardNo;
    }

    @Override
    public String getsRoomNo() {
        return sRoomNo;
    }

    @Override
    public void setsRoomNo(String sRoomNo) {
        this.sRoomNo = sRoomNo;
    }

    @Override
    public String getsCollege() {
        return sCollege;
    }

    @Override
    public void setsCollege(String sCollege) {
        this.sCollege = sCollege;
    }

    @Override
    public String getsSubject() {
        return sSubject;
    }

    @Override
    public void setsSubject(String sSubject) {
        this.sSubject = sSubject;
    }

    @Override
    public String getsClass() {
        return sClass;
    }

    @Override
    public void setsClass(String sClass) {
        this.sClass = sClass;
    }


    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getsCardType() {
        return sCardType;
    }

    public void setsCardType(String sCardType) {
        this.sCardType = sCardType;
    }

    public String getsStudentNo() {
        return sStudentNo;
    }

    public void setsStudentNo(String sStudentNo) {
        this.sStudentNo = sStudentNo;
    }

    public String getrLocName() {
        return rLocName;
    }

    public void setrLocName(String rLocName) {
        this.rLocName = rLocName;
    }

    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    @Override
    public String toString() {
        return "StudentExcel{" +
                "sCertNo='" + sCertNo + '\'' +
                ", sName='" + sName + '\'' +
                ", sSex=" + sSex +
                ", sCardType='" + sCardType + '\'' +
                ", sCardNo='" + sCardNo + '\'' +
                ", sCollege='" + sCollege + '\'' +
                ", sSubject='" + sSubject + '\'' +
                ", sClass='" + sClass + '\'' +
                ", sStudentNo='" + sStudentNo + '\'' +
                ", rLocName='" + rLocName + '\'' +
                ", rName='" + rName + '\'' +
                ", sRoomNo='" + sRoomNo + '\'' +
                ", eName='" + eName + '\'' +
                ", course='" + course + '\'' +
                '}';
    }
}
