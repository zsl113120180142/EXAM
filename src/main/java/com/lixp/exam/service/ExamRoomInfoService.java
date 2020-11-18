package com.lixp.exam.service;

import com.lixp.exam.bean.*;
import com.lixp.exam.dao.ExamRoomInfoMapper;
import com.lixp.exam.dao.OtherMapper;
import com.lixp.exam.dao.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExamRoomInfoService {

    @Autowired
    ExamRoomInfoMapper examRoomInfoMapper;

    @Autowired
    StudentService studentService;

    @Autowired
    RoomInfoService roomInfoService;

    @Autowired
    ExamInfoService examInfoService;

    @Autowired
    StudentMapper studentMapper;

    @Autowired
    @SuppressWarnings("all")
    OtherMapper otherMapper;

    /**
     * 批量生成考场
     * 在生成的过程中，要注意有的教室容纳的是30个人，所以要先判断教室容纳人数
     * @param roomInfoList：可用的教室信息
     * @param eno：考试编号
     * @return
     */
    @Transactional
    public boolean batchInsertExamRoomInfo(List<RoomInfo> roomInfoList, String eno, List<StudentCount> studentCountList) {
        //考场信息集合，用来存放自动生成的考场
        List<ExamRoomInfo> examRoomInfoList = new ArrayList<>();

        Iterator it = roomInfoList.iterator();
        int erNo = 1;
        //根据不同的科目生成考场
        for (StudentCount studentCount : studentCountList) {
            //获取当前科目的报考人数
            int coursePersons = studentCount.getCourseCount();//320人
            //获取当前科目所需考场数量
            int couresRooms = (int) Math.ceil((double)coursePersons/30);//11个考场
            //数量从1开始计数
            int courseCount = 1;
            //使用迭代器的方式，如果安排了一个教师，就从列表中删除该对象，不能用for循环
            while (it.hasNext()){//遍历考场
                //如果当前courseCount为0，表示该科目已经安排完成，可以跳出循环
                if(courseCount==0){
                    break;
                }
                RoomInfo roomInfo = (RoomInfo)it.next();//指向下一个
                //获取当前教室最大容纳人数，一个考场只能有30人，因此一个教室可能有两个考场
                int capacity = roomInfo.getrCapacity() / 30;//考场假设可用容纳90
                for (int j = 1; j <= capacity; j++) {
                    ExamRoomInfo examRoomInfo = new ExamRoomInfo();
                    examRoomInfo.setEno(eno);//设置考试编号
                    examRoomInfo.setRid(roomInfo.getRid());//设置教室id
                    examRoomInfo.setErNo(String.format("%03d", erNo));//初始为1
                    examRoomInfo.setCourse(studentCount.getCourseString());//设置考试科目
                    examRoomInfoList.add(examRoomInfo);
                    //没安排完一个考场，courseCount就加1
                    courseCount++;
                    //考场编号+1
                    erNo++;
                    //如果courseCount大于当前科目所需的最大考场数量couresRooms，就跳出循环
                    if(courseCount > couresRooms){
                        courseCount = 0;
                        break;
                    }
                }
                //安排完一个教室，就移除该教室，这样一个教室就能保证只有一个科目的考试
                it.remove();
            }
        }
        int i = examRoomInfoMapper.insertList(examRoomInfoList);
        return i > 0;
    }

   /* public boolean batchInsertExamRoomInfo(List<RoomInfo> roomInfoList, String eno,String course){
        List<ExamRoomInfo> examRoomInfoList = new ArrayList<>();
        int erNo=1;
        for (RoomInfo roomInfo : roomInfoList){
            int capacity = roomInfo.getrCapacity()/30;
            for(int j=1;j<=capacity;j++){
                ExamRoomInfo examRoomInfo = new ExamRoomInfo();
                examRoomInfo.setEno(eno);
                examRoomInfo.setRid(roomInfo.getRid());
                examRoomInfo.setErNo(String.format("%03d", erNo));
                examRoomInfo.setCourse(course);
                examRoomInfoList.add(examRoomInfo);
                erNo++;
            }
        }
        int i = examRoomInfoMapper.insertList(examRoomInfoList);
        return i>0;
    }*/

    /**
     * 根据考试编号查找当前考试的考场信息
     *
     * @param eno
     * @return
     */
    public List<ExamRoomInfo> selectAllExamRoomInfoByENo(String eno) {
        Example example = new Example(ExamRoomInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("eno", eno);
        List<ExamRoomInfo> examRoomInfoList = examRoomInfoMapper.selectByExample(example);
        return examRoomInfoList;
    }

    /**
     * 根据考场id查询考场
     *
     * @param erid
     * @return
     */
    public ExamRoomInfo selectExamRoomInfoById(int erid) {
        ExamRoomInfo examRoomInfo = examRoomInfoMapper.selectByPrimaryKey(erid);
        return examRoomInfo;
    }

    /**
     * 查询全部考场
     * @return
     */
    public List<ExamRoomInfo> selectAllExamRoomInfo(){

        Example example=new Example(ExamRoomInfo.class);
        example.orderBy("erid").asc();//按考场号从小到大排序
        List<ExamRoomInfo> examRoomInfos = examRoomInfoMapper.selectByExample(example);
        return examRoomInfos;
    }

    public List<ExamRoomInfo> selectAllExamRoomInfoAndStudent() {

        List<ExamRoomInfo> examRoomInfos = selectAllExamRoomInfo();
        for (int i =0;i<examRoomInfos.size();i++){
            Integer erid = examRoomInfos.get(i).getErid();

            Example example = new Example(Student.class);
            example.orderBy("sRoomNo").asc();//按座位排序
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("erid", erid);
            List<Student> students = studentMapper.selectByExample(example);//查询出考场id为erid的学生

            examRoomInfos.get(i).setStudentList(students);
        }
        return examRoomInfos;
    }


    /**
     * 查询全部考场生成小纸条
     * @return
     */
    public Map<String, ExamNote> getExamNotes() {
        Map<String, ExamNote> map=new HashMap<>();
        List<ExamRoomInfo> allexamRoomInfo = selectAllExamRoomInfo();//查询出全部考场

        for (ExamRoomInfo examRoomInfo:allexamRoomInfo){
            ExamNote examNote=new ExamNote();
            String min = otherMapper.findMinCardNoByErid(examRoomInfo.getErid());//查询出次考场最小考号
            String max = otherMapper.findMaxCardNoByErid(examRoomInfo.getErid());//查询出次考场最大考号

            examNote.setErNo(examRoomInfo.getErNo());//设置考场号
            examNote.setCourse(examRoomInfo.getCourse());//设置考试科目
            examNote.setCardNoRange(min+"--"+max);//设置准考证范围

            map.put(examRoomInfo.getErNo(),examNote);
        }
        return map;
    }

    /**
     * 删除单个考场信息
     * @param erid
     */
    public void deleteExamRoom(Integer erid) {
        examRoomInfoMapper.deleteByPrimaryKey(erid);
    }

    /**
     *
     * @param examRoomInfo
     * @return
     */
    public boolean updateExamRoom(ExamRoomInfo examRoomInfo) {
        int b = examRoomInfoMapper.updateByPrimaryKeySelective(examRoomInfo);
        return b==1;
    }

    /**
     * 模糊查询考场
     * @param noBlankSearch
     * @return
     */
    public List<ExamRoomInfo> selectExamRoomByerNo(String noBlankSearch) {
        Example example = new Example(ExamRoomInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike("erNo","%"+noBlankSearch+"%");
        List<ExamRoomInfo> examRoomInfos = examRoomInfoMapper.selectByExample(example);
        for (int i =0;i<examRoomInfos.size();i++){
            Integer erid = examRoomInfos.get(i).getErid();

            Example studentexample = new Example(Student.class);
            studentexample.orderBy("sRoomNo").asc();//按座位排序
            Example.Criteria studentcriteria = studentexample.createCriteria();
            studentcriteria.andEqualTo("erid", erid);

            List<Student> students = studentMapper.selectByExample(studentexample);//查询出考场id为erid的学生
            examRoomInfos.get(i).setStudentList(students);
        }
        return examRoomInfos;
    }


}
