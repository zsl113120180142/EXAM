package com.lixp.exam.controller;

import com.lixp.exam.bean.*;
import com.lixp.exam.config.PictureConfig;
import com.lixp.exam.service.*;
import com.lixp.exam.utils.Downutils;
import com.lixp.exam.utils.ImageUtils;
import com.lixp.exam.utils.PdfUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@RestController
public class CardController {

    @Autowired
    PictureConfig pictureConfig;

    @Autowired
    StudentService studentService;

    @Autowired
    CardService cardService;

    @Autowired
    StudentUserService studentUserService;

    @Autowired
    ExamInfoService examInfoService;

    @Autowired
    SubjectService subjectService;

    @Autowired
    RoomInfoService roomInfoService;

    @Autowired
    ExamRoomInfoService examRoomInfoService;

    @Autowired
    ImageUtils imageUtils;

    @Autowired
    PdfUtils pdfUtils;

    @Autowired
    CardNoteService cardNoteService;
    /**
     * 学生查询自己的准考证
     * @param request
     * @return
     */
    @PostMapping(value = "/SGetCard",name="user")
    public Msg getCardForStudent(HttpServletRequest request){
        List<ExamInfo> examInfos = examInfoService.selectAllExamInfoByStatus(1);//查询出当前已开始的考试

        ExamInfo examInfo=examInfos.get(0);//考试信息

        String eno = examInfo.getEno();//获取第一个已开始的考试

        Student student = studentUserService.getStudentByToken(request, eno);//根据eno和token查询对应的学生

        String sCertNo = student.getsCertNo();//得到准考证号

        String course=student.getCourse();//得到要考专业

        if (StringUtils.isEmpty(sCertNo) || StringUtils.isEmpty(course)){//学生没有准考证,或者没有报考专业
            return Msg.fail().add("info","暂无准考证信息");
        }

        List<Subject> subjects=subjectService.selectSubjectByCourseName(student.getEno(),course);//根据专业名称得到所有科目信息

        student.setSubjectList(subjects);//查询出完整的学生信息和考试科目信息
        ExamRoomInfo examRoomInfo = examRoomInfoService.selectExamRoomInfoById(student.getErid());//考场信息
        RoomInfo roomInfo = roomInfoService.selectRoomInfoById(examRoomInfo.getRid());//教室信息
        CardNote cardNote = cardNoteService.selectAllCardNote();//提示信息
        File file =new File(pictureConfig.getCardPdfUrl()+ student.getsCertNo()+".pdf");

        if (!file.exists()){//准考证不存在,生成准考证
            String cardNo = imageUtils.createImage(student, examInfo, examRoomInfo, roomInfo,cardNote.getcValue());
            pdfUtils.imgOfPdf(cardNo);
        }

        return Msg.success().add("student",student).
                add("examInfo",examInfo).add("examRoomInfo",examRoomInfo)
                .add("roomInfo",roomInfo).add("cardNote",cardNote);

    }

    /**
     * 下载准考证
     * @param request
     * @param response
     */
    @GetMapping(value = "/downLoadStuPdf",name="user")
    public void downLoadStuPdf(HttpServletRequest request, HttpServletResponse response,@RequestParam("USERTOKEN") String usertoken){

        if (StringUtils.isEmpty(usertoken)){
            return;
        }

        List<ExamInfo> examInfos = examInfoService.selectAllExamInfoByStatus(1);//查询出当前已开始的考试

        ExamInfo examInfo=examInfos.get(0);//考试信息

        String eno = examInfo.getEno();//获取第一个已开始的考试

        Student student = studentUserService.getStudentByToken(usertoken, eno);//根据eno和token查询对应的学生

        String sCertNo = student.getsCertNo();//得到准考证号

        String course=student.getCourse();//得到要考专业

        if (StringUtils.isEmpty(sCertNo) || StringUtils.isEmpty(course)){//学生没有准考证号,或者没有报考专业
            return ;
        }

        File file =new File(pictureConfig.getCardPdfUrl()+ student.getsCertNo()+".pdf");//

        if (!file.exists()){//判断准考证pdf文件是否存在
            return ;
        }

        String filename=student.getsName()+"准考证.pdf";
        String agent = request.getHeader("user-agent");//获取用户浏览器版本
        InputStream inputStream=null;
        ServletOutputStream outputStream=null;
        try {
            inputStream=new FileInputStream(file);//得到pdf的输入流

            filename= Downutils.filenameEncoding(filename, agent);//把下载的文件名按照用户浏览器版本重新编码,防止中文乱码

            response.addHeader("Content-Disposition", "attchement;filename=/" + filename);

            response.setCharacterEncoding("utf-8");

            response.setContentType("application/pdf");//设置mime类型

            outputStream=null;
            response.setStatus(HttpStatus.OK.value());
            outputStream = response.getOutputStream();
            byte[] b=new byte[1024];
            int len;
            while((len=inputStream.read(b))!=-1){
                outputStream.write(b,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                outputStream.close();
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return;

    }

    /**
     * 老师获取学生信息
     * @param sid
     * @return
     */
    @GetMapping(value="/AGetCard/{sid}",name="admin")
    public Msg getCardForAdmin(@PathVariable("sid")Integer sid){
        Card card = cardService.getCardBySid(sid);
        //
        if (card==null){
            return Msg.fail();
        }
        return Msg.success().add("card",card);
    }

    /**
     * 自动生成准考证
     * @param eno
     * @return
     */
    @PostMapping(value = "/GeneratedCards",name="admin")
    public Msg generatedCards(@RequestParam("eno") String eno){

        if(cardService.generatedCards(eno)){

            return  Msg.success();
        }else {
            return Msg.fail();
        }

    }

    /**
     * 编辑准考证上的考生须知
     */
    @PostMapping(value = "editCardInfo",name="admin")
    public Msg editCardInfo(@RequestParam("value") String value){
        String regex="\\<p\\>{0,}|\\</p\\>{0,}|\\<br\\>{0,}";
        value=value.replaceAll(regex,"");
       // System.out.println(value);
        boolean b = cardNoteService.saveCardNote(value);
        if (!b){
            return Msg.fail();
        }
        return Msg.success();
    }
}
