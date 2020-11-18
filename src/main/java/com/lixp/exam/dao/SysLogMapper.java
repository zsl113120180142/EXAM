package com.lixp.exam.dao;

import com.lixp.exam.bean.SysLog;
import com.lixp.exam.utils.MyMapper;
import org.springframework.stereotype.Repository;


/**
 * 操作日志数据表的dao接口
 */
@Repository
public interface SysLogMapper extends MyMapper<SysLog> {
}
