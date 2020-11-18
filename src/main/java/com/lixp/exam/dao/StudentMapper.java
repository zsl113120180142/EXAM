package com.lixp.exam.dao;

import com.lixp.exam.bean.Student;
import com.lixp.exam.utils.MyMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentMapper extends MyMapper<Student> {

}