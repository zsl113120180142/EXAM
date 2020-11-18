package com.lixp.exam.bean;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Table(name="tbl_student_user")
public class StudentUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; //学生用户id，无意义主键自增长

    @Email
    private String sUsername;//学生的邮箱号
    @NotEmpty
    private String sPassword;//学生密码
    @NotEmpty
    private String sCardType;//学生证件类型
    @NotEmpty
    private String sCardNo;//学生身份证号

    private Date created; //创建时间

    private String sRole;//学生角色，默认user


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getsUsername() {
        return sUsername;
    }

    public void setsUsername(String sUsername) {
        this.sUsername = sUsername;
    }

    public String getsPassword() {
        return sPassword;
    }

    public void setsPassword(String sPassword) {
        this.sPassword = sPassword;
    }

    public String getsCardType() {
        return sCardType;
    }

    public void setsCardType(String sCardType) {
        this.sCardType = sCardType;
    }

    public String getsCardNo() {
        return sCardNo;
    }

    public void setsCardNo(String sCardNo) {
        this.sCardNo = sCardNo;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getsRole() {
        return sRole;
    }

    public void setsRole(String sRole) {
        this.sRole = sRole;
    }

    @Override
    public String toString() {
        return "StudentUser{" +
                "id=" + id +
                ", sUsername='" + sUsername + '\'' +
                ", sPassword='" + sPassword + '\'' +
                ", sCardType='" + sCardType + '\'' +
                ", sCardNo='" + sCardNo + '\'' +
                ", created=" + created +
                ", sRole='" + sRole + '\'' +
                '}';
    }
}
