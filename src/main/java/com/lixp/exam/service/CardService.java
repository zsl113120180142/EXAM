package com.lixp.exam.service;

import com.lixp.exam.bean.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CardService {

    @Autowired
    StudentService studentService;

    @Autowired
    ExamRoomInfoService examRoomInfoService;

    @Autowired
    ExamInfoService examInfoService;

    @Autowired
    RoomInfoService roomInfoService;

    /**
     * 通过考试编号生成准考证信息
     *
     * @param eno
     * @return
     */
    @Transactional
    public boolean generatedCards(String eno) {
        //查询出考试信息
        List<ExamInfo> examInfoList = examInfoService.selectAllExamInfoByENo(eno);
        //查询出所有考场信息
        List<ExamRoomInfo> examRoomInfoList = examRoomInfoService.selectAllExamRoomInfoByENo(eno);
        //查询出所有学生信息
        List<Student> studentList = studentService.selectAllStudent(eno);
        int studentCount = studentList.size();//统计学生个数
        int i = 0;
        for (ExamInfo examInfo : examInfoList) {
            for (ExamRoomInfo examRoomInfo : examRoomInfoList) {
                for (int j = 1; j <= 30 && i < studentCount; j++) {
                    if (studentList.get(i).getCourse().equals(examRoomInfo.getCourse())) {
                        studentList.get(i).setEno(eno);
                        studentList.get(i).setEid(examInfo.getEid());
                        studentList.get(i).setErid(examRoomInfo.getErid());
                        String eSession = examInfo.geteSessions();
                        String erNo = examRoomInfo.getErNo();
                        String sRoomNo = String.format(String.format("%03d", j));
                        String sCertNo = eno + eSession + erNo + sRoomNo;
                        studentList.get(i).setsCertNo(sCertNo);
                        studentList.get(i).setsRoomNo(sRoomNo);
                        studentService.saveStudent(studentList.get(i));
                        i++;
                    }else{ break;}
                }
            }
        }
        return i == studentCount;
    }


    public Card getCardBySid(int sid) {
        Card card = new Card();
        Student student = studentService.selectStudentById(sid);
        card.setStudent(student);
        ExamInfo examInfo = examInfoService.selectExamInfoById(student.getEid());
        card.setExamInfo(examInfo);
        ExamRoomInfo examRoomInfo = examRoomInfoService.selectExamRoomInfoById(student.getErid());
        card.setcErNo(examRoomInfo.getErNo());
        RoomInfo roomInfo = roomInfoService.selectRoomInfoById(examRoomInfo.getRid());
        card.setcRLocName(roomInfo.getrLocName());
        card.setcRName(roomInfo.getrName());
        return card;

    }
}
