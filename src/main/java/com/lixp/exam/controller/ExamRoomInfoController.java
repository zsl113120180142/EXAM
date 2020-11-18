package com.lixp.exam.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lixp.exam.bean.*;
import com.lixp.exam.service.ExamInfoService;
import com.lixp.exam.service.ExamRoomInfoService;
import com.lixp.exam.service.RoomInfoService;
import com.lixp.exam.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ExamRoomInfoController {

    @Autowired
    StudentService studentService;

    @Autowired
    RoomInfoService roomInfoService;

    @Autowired
    ExamRoomInfoService examRoomInfoService;

    @Autowired
    ExamInfoService examInfoService;

    /**
     * 根据传过来的考试号自动生成考场信息
     *
     * @param eno
     * @return
     */
    @PostMapping(value = "/CreatExamRoom", name = "admin")
    public Msg createExamRoom(@RequestParam("eno") String eno) {
        /*
         * map
         * sum:300 总的考试人数
         * studentCountList:
         * StudentCount {courseString coursecount}
         * StudentCount {courseString coursecount}
         * StudentCount {courseString coursecount}
         * */
        Map map = studentService.getStudentCountByENoAndCourse(eno);

        List<StudentCount> studentCountList = (List<StudentCount>) map.get("studentCountList");//查询出当前考试号下的所有课程及对应的人数

        List<RoomInfo> roomInfoList = roomInfoService.selectAllRoomInfo();//查询所有教室，这里是所有教室都可用

        //自动生成考场
        if (examRoomInfoService.batchInsertExamRoomInfo(roomInfoList, eno, studentCountList)) {
            return Msg.success();
        } else {
            return Msg.fail();
        }

    }

    /**
     * 查询全部考场
     *
     * @return
     */
    @PostMapping(value = "selectAllExamRoom", name = "admin")
    public Msg selectAllExamRoom(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                 @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ExamRoomInfo> examRoomInfos = examRoomInfoService.selectAllExamRoomInfoAndStudent();
        PageInfo page = new PageInfo(examRoomInfos, 5);
        List<ExamInfo> examInfos = examInfoService.selectAllExamInfoByStatus(1);//查询出当前已开始的考试
        Integer eStatus;
        if (examInfos != null) {
            eStatus = 1;
        } else {
            eStatus = 0;
        }
        return Msg.success().add("examRoomInfos", page).add("eStatus", eStatus);
    }

    /**
     * 模糊查询考场
     *
     * @param search
     * @return
     */
    @PostMapping(value = "selectExamRoomByerNo", name = "admin")
    public Msg selectExamRoomByerNo(@RequestParam("value") String search,
                                    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                    @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        if (search.equals("")) {
            return Msg.fail();
        } else {
            PageHelper.startPage(pageNum, pageSize);
            String NoBlankSearch = search.replace(" ", "");
            List<ExamRoomInfo> examRoomInfoList = examRoomInfoService.selectExamRoomByerNo(NoBlankSearch);
            PageInfo page = new PageInfo(examRoomInfoList, 5);
            return Msg.success().add("examRoomInfoList", page);
        }
    }

    /**
     * @Description: deleteExamRoom方法是删除单个考场信息
     * @param:
     * @return:
     * @auther: zsl
     * @date: 2020/8/19 15:06
     */

    @PostMapping(value = "deleteExamRoom", name = "admin")
    public Msg deleteExamRoom(@RequestParam("erid") Integer erid) {
        if (erid == null) {
            return Msg.fail();
        }
        examRoomInfoService.deleteExamRoom(erid);
        return Msg.success();
    }


}
