package com.lixp.exam.bean;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Table(name = "tbl_roominfo")
public class RoomInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rid;//教室编号

    @NotEmpty
    @Column(name = "r_name")
    private String rName;//教室名例如604

    @NotNull
    @Column(name = "r_capacity")
    private Integer rCapacity;//可容纳人数

    @NotEmpty
    @Column(name = "r_loc_name")
    private String rLocName;//地点名称，第一教学楼

    @NotNull
    @Column(name = "r_status")
    private Integer rStatus;//教室状态0 ,1

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
     * @return r_name
     */
    public String getrName() {
        return rName;
    }

    /**
     * @param rName
     */
    public void setrName(String rName) {
        this.rName = rName == null ? null : rName.trim();
    }

    /**
     * @return r_capacity
     */
    public Integer getrCapacity() {
        return rCapacity;
    }

    /**
     * @param rCapacity
     */
    public void setrCapacity(Integer rCapacity) {
        this.rCapacity = rCapacity;
    }

    /**
     * @return r_loc_name
     */
    public String getrLocName() {
        return rLocName;
    }

    /**
     * @param rLocName
     */
    public void setrLocName(String rLocName) {
        this.rLocName = rLocName == null ? null : rLocName.trim();
    }

    /**
     * @return r_status
     */
    public Integer getrStatus() {
        return rStatus;
    }

    /**
     * @param rStatus
     */
    public void setrStatus(Integer rStatus) {
        this.rStatus = rStatus;
    }

    @Override
    public String toString() {
        return "RoomInfo{" +
                "rid=" + rid +
                ", rName='" + rName + '\'' +
                ", rCapacity=" + rCapacity +
                ", rLocName='" + rLocName + '\'' +
                ", rStatus=" + rStatus +
                '}';
    }
}