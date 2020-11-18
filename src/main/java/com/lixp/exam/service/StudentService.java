package com.lixp.exam.service;

import com.lixp.exam.bean.*;
import com.lixp.exam.config.PictureConfig;
import com.lixp.exam.dao.StudentMapper;
import com.lixp.exam.utils.FileUtils;
import com.lixp.exam.utils.MyUUID;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.System.out;

@Service
public class StudentService {
    @Autowired
    StudentMapper studentMapper;

    @Autowired
    CourseInfoService courseInfoService;

    @Autowired
    ExamRoomInfoService examRoomInfoService;

    @Autowired
    PictureConfig pictureConfig;

    @Autowired
    RoomInfoService roomInfoService;

    @Autowired
    ExamInfoService examInfoService;

    /**
     * 添加单个考生信息
     *
     * @param student
     * @return
     */
    public boolean saveStudent(Student student) {
        int i;
        if (student.getSid() == null) {
            student.setsUuid(MyUUID.getUUID());
            i = studentMapper.insertSelective(student);
        } else {
            i = studentMapper.updateByPrimaryKeySelective(student);
            out.println("---------" + i);
        }
        return i > 0;
    }

    /**
     * 更新学生信息
     *
     * @param student
     * @return
     */
    public Boolean updateStudent(Student student) {
        Integer sid = student.getSid();
        Example example = new Example(Student.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sid", sid);
        studentMapper.updateByExampleSelective(student, example);//更新
        return true;
    }

    /**
     * 批量添加考生
     *
     * @param studentList
     * @return
     */
    @Transactional
    public boolean batchInsertStudent(List<Student> studentList) {
        for (Student student : studentList) {
            student.setsUuid(MyUUID.getUUID());
        }
        int i = studentMapper.insertList(studentList);

        return i > 0;
    }


    /**
     * 删除考生信息
     *
     * @param sid
     * @return
     */
    public boolean delStudent(int sid) {
        int i = studentMapper.deleteByPrimaryKey(sid);
        return i > 0;

    }


    /**
     * 查询单个考生信息
     *
     * @param sid
     * @return
     */

    public Student selectStudentById(int sid) {
        Student student = studentMapper.selectByPrimaryKey(sid);
        return student;
    }

    /**
     * 通过身份证信息和考试编号查找考生信息
     *
     * @param sCardNo
     * @param eno
     * @return
     */
    public Integer selectStudentSidByCardNo(String sCardNo, String eno) {
        Example example = new Example(Student.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sCardNo", sCardNo);
        criteria.andEqualTo("eno", eno);
        Student student = studentMapper.selectOneByExample(example);
        if (student == null) {
            return null;
        }
        Integer sid = student.getSid();
        return sid;
    }

    /**
     * 通过身份证查找考生信息
     *
     * @param sCardNo
     * @return
     */
    public Student selectStudentByCardNo(String sCardNo, String eno) {//传入学生注册时的身份证,和eno
        Example example = new Example(Student.class);

        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("sCardNo", sCardNo);
        if (eno != null) {
            criteria.andEqualTo("eno", eno);
        }


        Student student = studentMapper.selectOneByExample(example);

        if (student == null) {//如果查不出来
            Student student1 = new Student();
            student1.setsCardNo(sCardNo);
            student1.setEno(eno);
            student1.setsPic("http://" + pictureConfig.getIp() + "/EXAM/static/" + sCardNo + ".jpg");
            saveStudent(student1);//插入一个
            return student1;
        }

        return student;
    }

    /**
     * 根据考试编号获取参加该考试的所有考生信息
     *
     * @return
     */
    public List<Student> selectAllStudent(String eno) {
        Example example = new Example(Student.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("eno", eno);
        List<Student> studentList = studentMapper.selectByExample(example);
        return studentList;
    }

    /**
     * 获取考生数量，根据考试编号获取考生数量
     *
     * @param eno
     * @return
     */
    public int getStudentCountByENo(String eno) {
        Example example = new Example(Student.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("eno", eno);
        int i = studentMapper.selectCountByExample(example);
        return i;
    }

    /**
     * 通过考试编号，获取参加该考试的考生数量及对应科目下的考试人数
     *
     * @param eno
     * @return
     */
    public Map<String, Object> getStudentCountByENoAndCourse(String eno) {
        Map<String, Object> countMap = new HashMap<>();
        //查询出考试号下，所有的科目信息，一个考生号对应多门科目1001下 护理 语文 数学
        List<CourseInfo> courseInfoList = courseInfoService.selectCourseInfoByENo(eno);

        //存放各个科目下的的考试名及考试人数信息
        List<StudentCount> studentCountList = new ArrayList<>();
        //获取学生表下所有考试号为1001的学生人数
        int allCount = getStudentCountByENo(eno);//300

        //sum=300
        countMap.put("sum", allCount);
        //返回各个科目下的人数
        studentCountList = getStudentCountList(courseInfoList, eno);
        countMap.put("studentCountList", studentCountList);
        /*
         * countMap
         * sum:300 总的考试人数
         * studentCountList:
         * StudentCount {courseString coursecount}
         * StudentCount {courseString coursecount}
         * StudentCount {courseString coursecount}
         * */
        return countMap;
    }


    public List<StudentCount> getStudentCountList(List<CourseInfo> courseInfoList, String eno) {
        List<StudentCount> studentCountList = new ArrayList<>();
        for (CourseInfo courseInfo : courseInfoList) {
            Example example = new Example(Student.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("eno", eno);
            criteria.andEqualTo("course", courseInfo.getCourse());
            int i = studentMapper.selectCountByExample(example);
            StudentCount studentCount = new StudentCount(courseInfo.getCourse(), i);
            studentCountList.add(studentCount);
        }
        return studentCountList;
    }

    /**
     * 查询出全部的学生小纸条信息
     *
     * @return
     *//*
    public Map<String, List<StudentNote>> getStudentNotes(Integer erid) {
        Map<String, List<StudentNote>> map = new HashMap<>();

        Example example = new Example(Student.class);
        example.orderBy("sRoomNo").asc();//按座位排序

        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("erid", erid);
        //Example.Criteria criteria = example.createCriteria();
        //criteria.andEqualTo("s_status",1);
        List<Student> students = studentMapper.selectByExample(example);//查询出考场id为erid的学生

        List<StudentNote> noteList = students.stream().map(student -> {
            StudentNote studentNote = new StudentNote();
            studentNote.setsCardNo(student.getsCardNo());
            studentNote.setsName(student.getsName());
            studentNote.setsSex(student.getsSex());
            studentNote.setsCardNo(student.getsCardNo());
            studentNote.setsRoomNo(student.getsRoomNo());
            studentNote.setsCollege(student.getsCollege());
            studentNote.setsSubject(student.getsSubject());
            studentNote.setsClass(student.getsClass());
            studentNote.setsPic(student.getsPic());
            return studentNote;
        }).collect(Collectors.toList());

        map.put(erid.toString(), noteList);


        return map;
    }*/


    /**
     * 批量导出考生
     */

    public List<Object> getStudentExcel(String eno, Integer erid) {
        Example example = new Example(Student.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("eno", eno);
        if (erid != 0) {
            criteria.andEqualTo("erid", erid);
        }
        example.orderBy("erid").asc().orderBy("sRoomNo").asc();//先按考场再按座位排序

        List<Student> students = studentMapper.selectByExample(example);//查询出考场id为erid的学生
        List<Object> StudentExcelList = students.stream().map(student -> {
            StudentExcel studentExcel = new StudentExcel();
            studentExcel.setsCertNo(student.getsCertNo());//准考证号
            studentExcel.setsName(student.getsName());//考生姓名
            studentExcel.setsSex(student.getsSex());//性别
            studentExcel.setsCardType(student.getsCardType());//证件类型
            studentExcel.setsCardNo(student.getsCardNo());//证件号
            studentExcel.setsCollege(student.getsCollege());//证件号
            studentExcel.setsSubject(student.getsSubject());//专业
            studentExcel.setsClass(student.getsClass());//班级
            studentExcel.setsStudentNo(student.getsStudentNo());//学号

            ExamRoomInfo examRoomInfo = examRoomInfoService.selectExamRoomInfoById(student.getErid());
            if (examRoomInfo != null) {
                RoomInfo roomInfo = roomInfoService.selectRoomInfoById(examRoomInfo.getRid());
                if (roomInfo != null) {
                    studentExcel.setrLocName(roomInfo.getrLocName());//地点名称
                    studentExcel.setrName(roomInfo.getrName());////教室名
                    studentExcel.setsRoomNo(student.getsRoomNo());//座位号
                }
            }
            ExamInfo examInfo = examInfoService.selectExamInfoById(student.getEid());
            studentExcel.seteName(examInfo.geteName());//考试名

            studentExcel.setCourse(student.getCourse());//考生科目
            return studentExcel;
        }).collect(Collectors.toList());
        //Example.Criteria criteria = example.createCriteria();
        //criteria.andEqualTo("s_status",1);
        return StudentExcelList;
    }


    /**
     * 从Excel中导入学生数据
     *
     * @param in
     * @param fileName
     * @return
     * @throws Exception
     */

    public List<Student> getListByExcel(InputStream in, String fileName) throws Exception {
        List<Student> list = new ArrayList<Student>();
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

                Student student = new Student();

                if (row.getCell(2) != null) {
                    student.setsName(row.getCell(2).getStringCellValue());
                }
                if (row.getCell(3) != null) {
                    student.setsSex(Integer.parseInt(row.getCell(3).getStringCellValue()));
                }

                if (row.getCell(4) != null) {
                    student.setsCardType(row.getCell(4).getStringCellValue());
                }

                if (row.getCell(5) != null) {
                    student.setsCardNo(row.getCell(5).getStringCellValue());
                }

                if (row.getCell(8) != null) {
                    student.setsPic(row.getCell(8).getStringCellValue());
                }

                if (row.getCell(9) != null) {
                    row.getCell(9).setCellType(CellType.STRING);
                    student.setEid(Integer.parseInt(row.getCell(9).getStringCellValue()));
                }

                if (row.getCell(10) != null) {
                    row.getCell(10).setCellType(CellType.STRING);
                    student.setEno(row.getCell(10).getStringCellValue());
                }

                if (row.getCell(12) != null) {
                    student.setCourse(row.getCell(12).getStringCellValue());
                }

                if (row.getCell(13) != null) {
                    student.setsSchool(row.getCell(13).getStringCellValue());
                }

                if (row.getCell(14) != null) {
                    student.setsCollege(row.getCell(14).getStringCellValue());
                }

                if (row.getCell(15) != null) {
                    student.setsSubject(row.getCell(15).getStringCellValue());
                }

                if (row.getCell(16) != null) {
                    student.setsClass(row.getCell(16).getStringCellValue());
                }

                if (row.getCell(17) != null) {
                    student.setsStudentNo(row.getCell(17).getStringCellValue());
                }

                list.add(student);

            }
        }
        work.close();
        return list;
    }

    /**
     * 刷新全部学生的照片地址
     *
     * @return
     */
    public boolean flushPic() {
        Example example = new Example(Student.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sPic", "");
        criteria.orIsNull("sPic");
        List<Student> students = studentMapper.selectByExample(example);//查询出全部照片没关联的学生

        for (Student student : students) {
            student.setsPic("http://" + pictureConfig.getIp() + "/EXAM/static/" + student.getsCardNo() + ".jpg");
            studentMapper.updateByPrimaryKey(student);
        }

        return true;
    }

    /**
     * @Description: selectByNameorCardno方法是模糊查询
     * @param:
     * @return:
     * @auther: zsl
     * @date: 2020/8/16 20:00
     */

    /**
     * 方法：推荐，速度最快
     * 判断是否为整数
     *
     * @param str 传入的字符串
     * @return 是整数返回true, 否则返回false
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public List<Student> selectByNameorCardno(String search) {
        if (isInteger(search)) {
            Example example = new Example(Student.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andLike("sCardNo", "%" + search + "%");
            List<Student> students = studentMapper.selectByExample(example);
            return students;
        } else {
            Example example = new Example(Student.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andLike("sName", "%" + search + "%");
            List<Student> students = studentMapper.selectByExample(example);
            return students;
        }
    }
}
