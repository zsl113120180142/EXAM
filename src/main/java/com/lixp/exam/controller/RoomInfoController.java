package com.lixp.exam.controller;

import com.lixp.exam.bean.Msg;
import com.lixp.exam.bean.RoomInfo;
import com.lixp.exam.service.RoomInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
public class RoomInfoController {

    @Autowired
    RoomInfoService roomInfoService;

    /**
     * 更新教室信息 ，删除教室即传入状态码为0
     * @param roomInfo
     * @return
     */
    @PostMapping(value = "updateRoomInfo",name = "admin")
    public Msg updateRoomInfo(@Valid RoomInfo roomInfo){
        if (roomInfo.getRid() == null){
            return Msg.fail();
        }
        try {
            this.roomInfoService.updateRoomInfo(roomInfo);
            return Msg.success();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return Msg.fail();
    }
    /**
     * 更新教室信息 ，删除教室即传入状态码为0
     * @param roomInfo
     * @return
     */
    @PostMapping(value = "updateRoomInfo1",name = "admin")
    public void updateRoomInfo(RoomInfo roomInfo,HttpServletResponse response){
      response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * 保存教室
     * @param roomInfo
     * @return
     */
    @PostMapping( value = "/SaveRoom",name="admin" )
    public Msg saveRoomInfo(@Valid RoomInfo roomInfo) {

        boolean b = roomInfoService.saveRoomInfo(roomInfo);
        if (b) {
            return Msg.success().add("roomInfo",roomInfo);
        } else {
            return Msg.fail();
        }
    }

    /**
     * 查询所有教室
     * @param response
     * @return
     */
    @PostMapping(value="/AllRoom",name="admin")
    public Msg getAllRoom(HttpServletResponse response){

        List<RoomInfo> roomInfoList = roomInfoService.selectAllRoomInfo();
        return Msg.success().add("roomInfoList",roomInfoList);
    }

    /**
     * 查询所有可用教室
     * @return
     */
    @PostMapping(value="/AllUseRoom",name="admin")
    public Msg getAllUseRoom(){
        List<RoomInfo> roomInfoList = this.roomInfoService.selectAllRoomInfoByStatus(1);
        return Msg.success().add("roomInfoList",roomInfoList);
    }


    /**
     * 上传教室excel表格完成批量导入
     * @param file
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping(value="/RoomInfoUpload",name="admin")
    public Msg fileUpload(@RequestParam("file") MultipartFile file,
                          HttpServletRequest request) throws IOException {

        InputStream is = file.getInputStream();
        try {
            List<RoomInfo> list = roomInfoService.getRoomListByExcel(is,file.getOriginalFilename());
            if(roomInfoService.batchInsertRoomInfo(list)){
                return Msg.success();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  Msg.fail();
    }


}
