package com.lixp.exam.service;

import com.lixp.exam.bean.CourseAndSubject;
import com.lixp.exam.bean.CourseInfo;
import com.lixp.exam.bean.Subject;
import com.lixp.exam.dao.CourseAndSubjectMapper;
import com.lixp.exam.dao.CourseInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static java.lang.System.out;

/**
 * 考试专业相关的service
 */
@Service
public class CourseInfoService {

    @Autowired
    CourseInfoMapper courseInfoMapper;

    @Autowired
    CourseAndSubjectMapper courseAndSubjectMapper;

    /**
     * 批量插入考试专业
     * @param courseInfoList
     * @return
     */
    @Transactional
    public boolean batchInsertCourseInfo(List<CourseInfo> courseInfoList){
        int i = courseInfoMapper.insertList(courseInfoList);
        out.println("----------------->"+i);
        return i>0;
    }

    /**
     * 删除考试专业,专业下的考试也会被一并删除
     * @param cid
     * @return
     */
    @Transactional
    public boolean deleteCourseInfoByCid(Integer cid){
        if (cid==null){
            return false;
        }

        int i = courseInfoMapper.deleteByPrimaryKey(cid);//根据主键删除考试科目

        CourseAndSubject cs=new CourseAndSubject();
        cs.setCid(cid);
        courseAndSubjectMapper.delete(cs);//删除中间表的信息

        return true;
    }

    /**
     * 通过考试编号查询考试专业
     * @param eno
     * @return
     */
    public List<CourseInfo> selectCourseInfoByENo(String eno){
        Example example = new Example(CourseInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("eno",eno);
        List<CourseInfo> courseInfos = courseInfoMapper.selectByExample(example);
        return courseInfos;
    }

    /**
     * 根据cid查询出考试名
     * @param cid
     * @return
     */
    public String selectCourseByCid(Integer cid){
        CourseInfo courseInfo = courseInfoMapper.selectByPrimaryKey(cid);

        return courseInfo.getCourse();
    }

    /**
     * 根据考试名查询出对应考试信息
     */
    public CourseInfo selectCourseInfoByName(String eno,String courseName){//courseName 例:护理学
        CourseInfo courseInfo=new CourseInfo();

        courseInfo.setCourse(courseName);

        try {
            List<CourseInfo> courseInfoList =  selectCourseInfoByENoAndcourse(eno,courseName);
            courseInfo=courseInfoList.get(0);//查询出courseName对应的考试信息

        }catch (Exception e){
            out.println("专业名重复");
            e.printStackTrace();
            return null;
        }

        return courseInfo;
    }

    /**
     * 更新专业
     * @param courseInfo
     * @return
     */
    public boolean updateCourse(CourseInfo courseInfo) {
        Integer cid = courseInfo.getCid();
        if (cid==null){
            return false;
        }
        Example example=new Example(CourseInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cid",cid);
        int b = courseInfoMapper.updateByExampleSelective(courseInfo,example);
        return b==1;
    }
    /**
     * 单个新增考试科目
     */
    public boolean AddCourseInfo(CourseInfo courseInfo) {
        int i = courseInfoMapper.insertSelective(courseInfo);
        return i == 0;
    }

    public List<CourseInfo> selectCourseInfoByENoAndcourse(String eno, String course) {
        Example example = new Example(CourseInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("eno",eno);
        criteria.andEqualTo("course",course);
        List<CourseInfo> courseInfos = courseInfoMapper.selectByExample(example);
        return courseInfos;
    }
}
