package com.lixp.exam.bean;

/**
 * 封装贴在桌子上的小纸条信息
 */
public class StudentNote {

    //private Integer sid;//考生id

    private String sCertNo;//准考证号

    private String sName;//考生姓名

    private String sPic;//考生照片

    private Integer sSex;//性别

    private String sCardNo;//证件号

    public String getsPic() {
        return sPic;
    }

    public void setsPic(String sPic) {
        this.sPic = sPic;
    }

    private String sRoomNo;//座位号

    private String sCollege;//所属院系

    private String sSubject;//专业

    private String sClass;//班级

//    public Integer getSid() {
//        return sid;
//    }
//
//    public void setSid(Integer sid) {
//        this.sid = sid;
//    }

    public String getsCertNo() {
        return sCertNo;
    }

    public void setsCertNo(String sCertNo) {
        this.sCertNo = sCertNo;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public Integer getsSex() {
        return sSex;
    }

    public void setsSex(Integer sSex) {
        this.sSex = sSex;
    }

    public String getsCardNo() {
        return sCardNo;
    }

    public void setsCardNo(String sCardNo) {
        this.sCardNo = sCardNo;
    }

    public String getsRoomNo() {
        return sRoomNo;
    }

    public void setsRoomNo(String sRoomNo) {
        this.sRoomNo = sRoomNo;
    }

    public String getsCollege() {
        return sCollege;
    }

    public void setsCollege(String sCollege) {
        this.sCollege = sCollege;
    }

    public String getsSubject() {
        return sSubject;
    }

    public void setsSubject(String sSubject) {
        this.sSubject = sSubject;
    }

    public String getsClass() {
        return sClass;
    }

    public void setsClass(String sClass) {
        this.sClass = sClass;
    }

    @Override
    public String toString() {
        return "StudentNote{" +
                "sCertNo='" + sCertNo + '\'' +
                ", sName='" + sName + '\'' +
                ", sPic='" + sPic + '\'' +
                ", sSex=" + sSex +
                ", sCardNo='" + sCardNo + '\'' +
                ", sRoomNo='" + sRoomNo + '\'' +
                ", sCollege='" + sCollege + '\'' +
                ", sSubject='" + sSubject + '\'' +
                ", sClass='" + sClass + '\'' +
                '}';
    }
}
