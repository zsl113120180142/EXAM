package com.lixp.exam.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OtherMapper{

    @Select("SELECT MAX(s_cert_no) FROM (SELECT *  FROM tbl_student WHERE erid=#{erid}) a")
    String findMaxCardNoByErid(Integer erid);

    @Select("SELECT MIN(s_cert_no) FROM (SELECT *  FROM tbl_student WHERE erid=#{erid}) a")
    String findMinCardNoByErid(Integer erid);


}
