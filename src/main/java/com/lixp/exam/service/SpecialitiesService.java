package com.lixp.exam.service;

import com.lixp.exam.bean.Specialities;
import com.lixp.exam.dao.SpecialitiesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @ClassName: SpecialitiesService
 * @Description: TODO
 * @Author: zsl
 * @Date: 2020/10/1 18:35
 * @Version: v1.0
 */
@Service
public class SpecialitiesService {

    @Autowired
    SpecialitiesMapper specialitiesMapper;

    public List<Specialities> selectSpecialities() {
        return specialitiesMapper.selectAll();
    }

    public boolean addSpecialities(Specialities specialities) {
        int b = specialitiesMapper.insertSelective(specialities);
        return b==1;
    }


    public Specialities updataSpecialities(Specialities specialities) {
        Integer pid = specialities.getPid();
        if (pid==null){
            return null;
        }
        Example example=new Example(Specialities.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("pid",pid);
        int i = specialitiesMapper.updateByExampleSelective(specialities, example);
        if (i>0){
            return specialities;
        }
        return null;
    }

    public boolean deleteSpecialities(Integer pid) {
        int i =specialitiesMapper.deleteByPrimaryKey(pid);
        return i==1;
    }

    public Specialities selectSpecialitiesByPid(Integer pid) {
        return specialitiesMapper.selectByPrimaryKey(pid);
    }
}
