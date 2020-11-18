package com.lixp.exam.bean;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName: Specialities
 * @Description: TODO
 * @Author: zsl
 * @Date: 2020/10/1 18:15
 * @Version: v1.0
 */
@Table(name="tbl_specialities")
public class Specialities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pid;

    private String specialities;

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getSpecialities() {
        return specialities;
    }

    public void setSpecialities(String specialities) {
        this.specialities = specialities;
    }

    @Override
    public String toString() {
        return "Specialities{" +
                "pid=" + pid +
                ", specialities='" + specialities + '\'' +
                '}';
    }
}
