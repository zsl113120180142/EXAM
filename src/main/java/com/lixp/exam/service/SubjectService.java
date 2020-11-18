package com.lixp.exam.service;

import com.lixp.exam.bean.CourseAndSubject;
import com.lixp.exam.bean.CourseInfo;
import com.lixp.exam.bean.Subject;
import com.lixp.exam.dao.CourseAndSubjectMapper;
import com.lixp.exam.dao.SubjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 考试科目相关的service
 */
@Service
public class SubjectService {

    @Autowired
    SubjectMapper subjectMapper;

    @Autowired
    CourseInfoService courseInfoService;

    @Autowired
    CourseAndSubjectMapper courseAndSubjectMapper;

    /**
     * 根据专业名得到所有的考试科目
     * @param course
     * @return
     */
    public List<Subject> selectSubjectByCourseName(String eno,String course) {

        CourseInfo courseInfo = courseInfoService.selectCourseInfoByName(eno,course);//根据报考的专业名得到专业信息

        Integer cid = courseInfo.getCid();//得到专业的cid

        List<Subject> subjects = subjectMapper.selectSubjectsByCid(cid);//通过cid查询到全部的科目信息

        return subjects;
    }


    /**
     * 更新考试科目
     * @param subject
     * @return
     */
    public Subject updataSubject(Subject subject){
        Integer subId=subject.getSubId();
        if (subId==null){
            return null;
        }
        Example example=new Example(Subject.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("subId",subId);
        int i = subjectMapper.updateByExampleSelective(subject, example);
        if (i>0){
            return subject;
        }
        return null;
    }

    /**
     * 删除考试科目,那么所有考试专业下都不会有这个科目
     * @param subId
     * @return
     */
   /* public boolean deleteSubject(Integer subId){
        int i = subjectMapper.deleteByPrimaryKey(subId);//删除考试科目

        CourseAndSubject cs=new CourseAndSubject();
        cs.setSubId(subId);
        courseAndSubjectMapper.delete(cs);//删除中间表的信息
        return i>0;
    }*/
    /**
     * 删除考试科目,中间表的考试内容被删除
     * @param subId
     * @param cid
     * @return
     */
    public boolean deleteSubjectAndCid(Integer subId, Integer cid) {
        CourseAndSubject cs=new CourseAndSubject();
        cs.setSubId(subId);
        cs.setCid(cid);
        int i =courseAndSubjectMapper.delete(cs);//删除中间表的信息
        return i==1;
    }

    public boolean addSubject(Subject subject,Integer cid) {
        int i = subjectMapper.selectCount(subject);
        if (i>0){
            return false;
        }else {
            subjectMapper.insertSelective(subject);
            List<Subject> subjects = subjectMapper.select(subject);
            Integer subId = subjects.get(0).getSubId();
            CourseAndSubject cs=new CourseAndSubject();
            cs.setSubId(subId);
            cs.setCid(cid);
            int b =  courseAndSubjectMapper.insert(cs);
            return b==1;
        }


    }

    public List<Subject> selectSubjectByEno(String eno) {
        Example example=new Example(Subject.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("eno",eno);
        List<Subject> subjectList = subjectMapper.selectByExample(example);
        return subjectList;
    }
}
