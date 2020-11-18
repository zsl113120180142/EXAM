package com.lixp.exam.dao;

import com.lixp.exam.bean.User;
import com.lixp.exam.utils.MyMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends MyMapper<User> {
}