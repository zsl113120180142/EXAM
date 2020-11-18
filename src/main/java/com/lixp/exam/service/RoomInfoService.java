package com.lixp.exam.service;

import com.lixp.exam.bean.RoomInfo;
import com.lixp.exam.dao.RoomInfoMapper;
import com.lixp.exam.utils.FileUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

@Service
public class RoomInfoService {

    @Autowired
    RoomInfoMapper roomInfoMapper;

    public boolean isNotExitRoom(RoomInfo roomInfo) {
        Example example = new Example(RoomInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("rLocName", roomInfo.getrLocName());
        criteria.andEqualTo("rName", roomInfo.getrName());
        int i = roomInfoMapper.selectCountByExample(example);
        return i == 0;
    }

    /**
     * 单个保存教室
     *
     * @param roomInfo 传入教室信息
     * @return
     */
    public boolean saveRoomInfo(RoomInfo roomInfo) {
        roomInfo.setRid(null);
        int i = 0;
            if (isNotExitRoom(roomInfo)) {
                i = roomInfoMapper.insertSelective(roomInfo);
            }
        return i > 0;
    }

    /**
     * 更新教室
     * @param roomInfo
     */
    public void updateRoomInfo(RoomInfo roomInfo){

        Example example=new Example(RoomInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("rid",roomInfo.getRid());
        this.roomInfoMapper.updateByExampleSelective(roomInfo,example);
    }

    /**
     * 批量添加教室
     *
     * @param roomInfoList
     * @return
     */
    //@Transactional
    public boolean batchInsertRoomInfo(List<RoomInfo> roomInfoList) {
        int i = roomInfoMapper.insertList(roomInfoList);
        out.println("----------------->" + i);
        return i > 0;
    }

    public boolean delRoomInfo(int rid) {
        int i = roomInfoMapper.deleteByPrimaryKey(rid);
        return i > 0;
    }

    /**
     * 查询单个教室信息
     *
     * @param rid
     * @return
     */
    public RoomInfo selectRoomInfoById(int rid) {
        RoomInfo roomInfo = roomInfoMapper.selectByPrimaryKey(rid);
        return roomInfo;
    }

    /**
     * 查询所有教室信息
     *
     * @return
     */
    public List<RoomInfo> selectAllRoomInfo() {
        List<RoomInfo> roomInfoList = roomInfoMapper.selectAll();
        return roomInfoList;
    }


    /**
     * 查询可用教室
     * @param rStatus
     * @return
     */
    public List<RoomInfo> selectAllRoomInfoByStatus(int rStatus) {
        Example example = new Example(RoomInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("rStatus",rStatus);
        List<RoomInfo> roomInfoList = roomInfoMapper.selectByExample(example);
        return roomInfoList;
    }
    /**
     * 获取人数信息
     *
     * @param roomInfoList
     * @return
     */
    public int getPersons(List<RoomInfo> roomInfoList) {
        int persons = 0;
        for (RoomInfo roomInfo : roomInfoList) {
            persons += roomInfo.getrCapacity();
        }
        return persons;
    }

    /**
     * 根据Excel文件导入教室
     * @param in
     * @param fileName
     * @return
     * @throws Exception
     */

    public List<RoomInfo> getRoomListByExcel(InputStream in, String fileName) throws Exception {
        List<RoomInfo> list = new ArrayList<RoomInfo>();
        //创建Excel工作薄
        Workbook work = FileUtils.getWorkbook(in, fileName);
        if (null == work) {
            throw new Exception("创建Excel工作薄为空！");
        }
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;

        //获取工作表
        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            if (sheet == null) {
                continue;
            }
            //从第二行开始导数据
            for (int j = 1; j <= sheet.getLastRowNum(); j++) {
                row = sheet.getRow(j);
                if (row == null || row.getFirstCellNum() == j) {
                    continue;
                }

                RoomInfo roomInfo  = new RoomInfo();

                if(row.getCell(1)!=null){
                    roomInfo.setrName(row.getCell(1).getStringCellValue());
                }

                if(row.getCell(2)!=null){
                    row.getCell(2).setCellType(CellType.STRING);
                    roomInfo.setrCapacity(Integer.parseInt(row.getCell(2).getStringCellValue()));
                }

                if(row.getCell(3)!=null){
                    roomInfo.setrLocName(row.getCell(3).getStringCellValue());
                }

                if(row.getCell(4)!=null){
                    row.getCell(4).setCellType(CellType.STRING);
                    roomInfo.setrStatus(Integer.parseInt(row.getCell(4).getStringCellValue()));
                }

                list.add(roomInfo);

            }
        }
        work.close();
        return list;
    }


}
