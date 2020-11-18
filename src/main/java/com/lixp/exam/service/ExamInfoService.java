package com.lixp.exam.service;

import com.lixp.exam.bean.*;
import com.lixp.exam.dao.ExamInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class ExamInfoService {
    @Autowired
    ExamInfoMapper examInfoMapper;

    @Autowired
    CourseInfoService courseInfoService;
    @Autowired
    ExamRoomInfoService examRoomInfoService;
    @Autowired
    StudentService studentService;
    @Autowired
    SubjectService subjectService;

    /**
     * 根据当前时间查询可以报名的考试
     * @param date 当前时间
     * @return
     */
    public List<StartExamInfo> selectStartingExam(String date) {

        List<StartExamInfo> startExamInfos = examInfoMapper.selectExamInfo(date);//查询出当前正在进行的考试

        if (startExamInfos==null){//没有考试
            return null;
        }

        for (StartExamInfo startExamInfo:startExamInfos){

            String eno = startExamInfo.getEno();//得到这个考试id
            List<CourseInfo> courseInfos = courseInfoService.selectCourseInfoByENo(eno);//得到当前考试下的所有课程
            startExamInfo.setCourse(courseInfos);//设置给startExamInfo

        }
        return startExamInfos;
    }

    /**
     * 判断考试编号是否存在，用于一开始新增考试的时候
     * @param eno
     * @return
     */
    public boolean selectExamInfoByENO(String eno){
        Example example = new Example(ExamInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("eno",eno);
        int i = examInfoMapper.selectCountByExample(example);
        return i==0;
    }

    /**
     * 用于新增考试场次
     * @param eno
     * @return
     */
    public boolean selectExamInfoByENOAndESessions(String eno,String eSessions){
        Example example = new Example(ExamInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("eno",eno);
        criteria.andEqualTo("e_sessions",eSessions);
        int i = examInfoMapper.selectCountByExample(example);
        return i==0;
    }

    /**
     * 添加考试信息
     * @param examInfo
     * @return
     */
    public boolean addExamInfo(ExamInfo examInfo){
        int i = 0;
        if(selectExamInfoByENO(examInfo.getEno())){
            i = examInfoMapper.insertSelective(examInfo);
        }
        return i > 0;
    }

    /**
     * 更新考试信息
     * @param examInfo
     * @return
     */
    public boolean updataExamInfo(ExamInfo examInfo){
        int i = examInfoMapper.updateByPrimaryKeySelective(examInfo);
        return i>0;
    }

    /**
     * 删除考试信息
     * @param eid
     * @return
     */
    public boolean delExamInfo(int eid){
        int i = examInfoMapper.deleteByPrimaryKey(eid);
        return i>0;
    }

    /**
     * 查询单个考试信息
     * @param eid
     * @return
     */
    public ExamInfo selectExamInfoById(int eid){
        ExamInfo examInfo = examInfoMapper.selectByPrimaryKey(eid);
        return  examInfo;
    }

    /**
     * 查询所有考试信息
     * @return
     */
    public List<ExamInfo> selectAllExamInfo(){

        List<ExamInfo> examInfoList = examInfoMapper.selectAll();

        return examInfoList;

    }

    /**
     * 查询当前考试所有考试场次信息
     * @param eno
     * @return
     */
    public List<ExamInfo> selectAllExamInfoByENo(String eno){
        Example example = new Example(ExamInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("eno",eno);
        List<ExamInfo> examInfoList = examInfoMapper.selectByExample(example);
        return examInfoList;
    }

    /**
     * 根据考试状态码查询考试信息
     * @param status
     * @return
     */
    public List<ExamInfo> selectAllExamInfoByStatus(Integer status){
        Example example = new Example(ExamInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("eStatus",status);
        List<ExamInfo> examInfoList = examInfoMapper.selectByExample(example);
        return examInfoList;
    }


    /**
     * 查询所有考试及其底下的考试科目
     * @return
     */
    public List<ExamInfo> selectAllExamInfoAndCourse() {
        List<ExamInfo> examInfoList = examInfoMapper.selectAll();
        for (ExamInfo examInfo : examInfoList){
            List<Subject> subjectList = subjectService.selectSubjectByEno(examInfo.getEno());
            examInfo.setSubjectList(subjectList);
        }
        return examInfoList;
    }

    /**
     * 修改考试信息,和所有与eno有关的表的信息
     * @param examInfo
     * @return
     */
    public boolean updateExamInfo(ExamInfo examInfo) {
        ExamInfo examInfo1= selectExamInfoById(examInfo.getEid());
        String eno = examInfo1.getEno();
        if (eno!=examInfo.getEno()){
            //修改course中的eno
            List<CourseInfo> courseInfoList = courseInfoService.selectCourseInfoByENo(eno);
            for (CourseInfo courseInfo : courseInfoList){
                courseInfo.setEno(examInfo.getEno());
                courseInfoService.updateCourse(courseInfo);
            }
            //修改ExamRoom
            List<ExamRoomInfo> examRoomInfoList = examRoomInfoService.selectAllExamRoomInfoByENo(eno);
            for (ExamRoomInfo examRoomInfo : examRoomInfoList){
                examRoomInfo.setEno(examInfo.getEno());
                examRoomInfoService.updateExamRoom(examRoomInfo);
            }
            //修改student
            List<Student> studentList = studentService.selectAllStudent(eno);
            for (Student student : studentList){
                student.setEno(examInfo.getEno());
                studentService.updateStudent(student);
            }
            //修改subject
            List<Subject> subjectList = subjectService.selectSubjectByEno(eno);
            for (Subject subject : subjectList){
                subject.setEno(examInfo.getEno());
                subjectService.updataSubject(subject);
            }
        }
        int b = examInfoMapper.updateByPrimaryKeySelective(examInfo);
        return b==1;
    }

    /**
     * 删除所有考试信息给予考试有关的信息
     * @param eid
     * @return
     */
    public boolean deleteExamInfoByEid(Integer eid) {
        if (eid==null){
            return false;
        }
        ExamInfo examInfo = examInfoMapper.selectByPrimaryKey(eid);
        String eno = examInfo.getEno();
        List<CourseInfo> courseInfoList = courseInfoService.selectCourseInfoByENo(eno);
        for (CourseInfo courseInfo : courseInfoList){
            Integer cid = courseInfo.getCid();
            courseInfoService.deleteCourseInfoByCid(cid);
        }
        examInfoMapper.deleteByPrimaryKey(eid);
        return true;
    }
}
