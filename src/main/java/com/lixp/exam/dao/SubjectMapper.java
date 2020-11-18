package com.lixp.exam.dao;

import com.lixp.exam.bean.Subject;
import com.lixp.exam.utils.MyMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectMapper extends MyMapper<Subject> {

    /**
     * 根据cid查询出对应的考试科目信息
     * @return
     */
    @Select("SELECT * FROM tbl_subject WHERE sub_id IN(SELECT sub_id FROM tbl_course_subject WHERE cid=#{cid})")
    List<Subject> selectSubjectsByCid(Integer cid);
}
