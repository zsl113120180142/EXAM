package com.lixp.exam.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lixp.exam.bean.Msg;
import com.lixp.exam.bean.Student;
import com.lixp.exam.config.PictureConfig;
import com.lixp.exam.service.StudentService;
import com.lixp.exam.utils.UploadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class StudentController {

    @Autowired
    StudentService studentService;

    @Autowired
    private PictureConfig pictureConfig;

    /**
     * 根据考试号获取全部的考生信息
     *
     * @param eno
     * @param pageNum
     * @param pageSize
     * @return
     */
    @PostMapping(value = "/AllStudent/{eno}", name = "admin")
    public Msg getAllStudent(@PathVariable("eno") String eno,
                             @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Student> studentList = studentService.selectAllStudent(eno);
        PageInfo page = new PageInfo(studentList, 5);
        return Msg.success().add("studentList", page);
    }


    /**
     * 添加,更新学生信息
     *
     * @return
     */
    @PostMapping(value = "changeStudent", name = "admin")
    public Msg updateStudent(Student student) {
        Integer sid = student.getSid();
        try {
            if (sid == null) {//sid为空,新增
                studentService.saveStudent(student);//添加考生
            } else {
                studentService.updateStudent(student);//更新考生信息
            }
        } catch (Exception e) {
            return Msg.fail();
        }
        return Msg.success();
    }

    /**
     * @Description: onePictureUpload方法是单个图片上传
     * @param:
     * @return:
     * @auther: zsl
     * @date: 2020/8/17 16:40
     */
    Logger logger = LoggerFactory.getLogger(StudentController.class);

    @PostMapping(value = "/onePictureUpload", name = "admin")
    public Msg onePictureUpload(@RequestParam("file") MultipartFile file, HttpSession session, HttpServletRequest request) {
        if (file == null) {
            return Msg.fail();
        } else {
            String targetFile = this.pictureConfig.getUrl();//文件存放地址
            File fileName = new File(targetFile+File.separator+file.getOriginalFilename());
            if (fileName.exists()){
                fileName.delete();
            }
            try (FileOutputStream out = new FileOutputStream(targetFile + file.getOriginalFilename());) {
                out.write(file.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("文件上传失败!");
                return Msg.fail();
            }
            logger.info("文件上传成功!");
            studentService.flushPic();//刷新学生的spic地址
            return Msg.success()
                    .add("filePath", "http://"+pictureConfig.getIp()+":8080/EXAM/static/"+ file.getOriginalFilename())
                    .add("fileName", file.getOriginalFilename());
        }
    }


    /**
     * 解析学生图片压缩包
     *
     */
    @PostMapping(value = "/pictureUpload",name = "admin")
    public Msg uploadPushContent(@RequestParam("file")MultipartFile file, HttpSession session, HttpServletRequest request) {
        if (file == null) {

        } else {
            try {
                //获取存储文件的目录
                String path=this.pictureConfig.getUrl();//文件存放地址

                String saveFileName = UploadUtil.resolveCompressUploadFile(request, file, path);

               studentService.flushPic();//刷新学生的spic地址

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return Msg.success();
    }

    /**
     * 解析excel文件
     */
    @PostMapping(value="/StudentUpload",name="admin")
    public Msg fileUpload(@RequestParam("file") MultipartFile file,
                          HttpServletRequest request) throws IOException {

        InputStream is = file.getInputStream();
        try {
            List<Student> list = studentService.getListByExcel(is,file.getOriginalFilename());
            if(studentService.batchInsertStudent(list)){
                studentService.flushPic();//刷新学生的spic地址
                return Msg.success();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  Msg.fail();
    }
    /**
     * 刷新全部学生的照片信息,将照片路径和身份证关联起来
     */
    @GetMapping(value = "flushStuPic",name="admin")
    public Msg flushStuPic(){

        boolean b = studentService.flushPic();
        if (!b){
            return Msg.fail();
        }
        return Msg.success();
    }

}
