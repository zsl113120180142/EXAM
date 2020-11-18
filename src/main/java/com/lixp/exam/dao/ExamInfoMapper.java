package com.lixp.exam.dao;

import com.lixp.exam.bean.CourseInfo;
import com.lixp.exam.bean.ExamInfo;
import com.lixp.exam.bean.StartExamInfo;
import com.lixp.exam.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ExamInfoMapper extends MyMapper<ExamInfo> {

//    /**
//     * 根据考试号查询出考试科目,string类型
//     * @param eno
//     * @return
//     */
//    @Select("SELECT course FROM tbl_courseinfo WHERE eno=#{eno}")
//    List<String> selectStartingExam(String eno);

    /**
     * 根据当前时间查询可以报名的考试
     * @param date
     * @return
     */
    @Select("SELECT * FROM tbl_examinfo WHERE e_status=1 AND e_sign_start_time<#{date} AND e_sign_end_time>#{date}")
    List<StartExamInfo> selectExamInfo(@Param("date") String date);

}