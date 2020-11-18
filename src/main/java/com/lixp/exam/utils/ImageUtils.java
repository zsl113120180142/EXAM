package com.lixp.exam.utils;

import com.lixp.exam.bean.*;
import com.lixp.exam.config.PictureConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;

@Component
public class ImageUtils {

    @Autowired
    PictureConfig pictureConfig;

    public String createImage(Student student, ExamInfo examInfo, ExamRoomInfo examRoomInfo, RoomInfo roomInfo,String cardnote){

        int width = 1040;
        int height = 1400;
        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        //获取画笔
        Graphics2D g=image.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING , RenderingHints.VALUE_ANTIALIAS_ON);

        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        //设置画笔颜色为灰色
        g.setColor(Color.WHITE);
        //填充图片
        g.fillRect(0,0, width,height);

        //设置画笔颜色为黄色
        g.setColor(Color.black);
        //设置字体的小大
        g.setFont(new Font("宋体",Font.BOLD,30));
        //向图片上写入文字
        int length = examInfo.geteName().length();
        int xx=((width-length*30)/2);
        g.drawString(examInfo.geteName(),xx,120);

        g.drawLine(50,140,990,140);//画上线段
        g.setFont(new Font("宋体",Font.BOLD,25));
        g.drawString("身份信息",55,165);
        g.drawLine(50,180,990,180);//画上线段
        g.drawLine(50,1350,990,1350);//画下线段
        g.drawLine(50,140,50,1350);//画左线段
        g.drawLine(990,140,990,1350);//画右线段

        g.setFont(new Font("宋体",Font.PLAIN,23));
        //绘制正文
        g.drawString("准考证号：",70,210);       g.drawString(student.getsCertNo(),190,210);
        g.drawString("姓名：",70,245);         g.drawString(student.getsName(),160,245);
        g.drawString("性别：",70,280);         g.drawString(student.getsSex().toString(),160,280);
        g.drawString("证件类型：",70,315);       g.drawString(student.getsCardType(),190,315);
        g.drawString("证件号码：",70,350);       g.drawString(student.getsCardNo(),190,350);
        g.drawString("所属学校：",70,385);       g.drawString(student.getsSchool(),190,385);
        g.drawString("院系编辑：",70,420);       g.drawString(student.getsCollege()+" "+student.getsSubject(),190,420);
        g.drawString("学号：",70,455);         g.drawString(student.getsStudentNo(),160,455);
        g.drawString("身份证备注：",70,490);      g.drawString("无",210,490);

        File file1=new File(pictureConfig.getUrl()+student.getsCardNo()+".JPG");//考生头像,身份证号获取
        InputStream is= null;
        BufferedImage bi=null;
        try {
            is = new FileInputStream(file1);
            bi=ImageIO.read(is);
            Image im=(Image)bi;
            g.drawImage(im,750,200,210,290,null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        g.drawLine(50,520,990,520);//画上线段

        g.setFont(new Font("宋体",Font.BOLD,25));
        g.drawString("时间安排：",55,545);
        g.setFont(new Font("宋体",Font.PLAIN,23));

        g.drawLine(50,560,990,560);//画上线段

        g.drawString("考试名称",70,590);
        g.drawString("考试场次",270,590);
        g.drawString("考试日期",470,590);
        g.drawString("报道时间",670,590);
        g.drawString("考试时间",870,590);

        g.drawLine(100,610,940,610);//画上线段

        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sf1=new SimpleDateFormat("HH:mm");
        int si=0;
        for (Subject subject:student.getSubjectList()){
            g.drawString(subject.getSubjectName(),70,640+si*30);
            g.drawString("--",305,640+si*30);
            g.drawString(sf.format(subject.getsDate()),455,640+si*30);
            g.drawString("08:15",685,640+si*30);
            g.drawString(sf1.format(subject.getsStartTime())+"--"+sf1.format(subject.getsEndTime()),820,640+si*30);
            si++;
        }


        g.drawLine(50,850,990,850);//画上线段

        g.setFont(new Font("宋体",Font.BOLD,25));
        g.drawString("考试安排：",55,875);
        g.setFont(new Font("宋体",Font.PLAIN,23));

        g.drawLine(50,880,990,880);//画上线段

        g.drawString("考试",70,910);
        g.drawString("考试地点",290,910);
        g.drawString("考场号",670,910);
        g.drawString("座位号",870,910);

        g.drawLine(100,930,940,930);//画上线段

        g.drawString("期末考试",70,960);
        g.drawString(roomInfo.getrLocName()+" "+roomInfo.getrName(),290,960);
        g.drawString(examRoomInfo.getErNo(),670,960);
        g.drawString(student.getsRoomNo(),870,960);

        g.drawLine(50,980,990,980);//画上线段

        g.setFont(new Font("宋体",Font.BOLD,25));
        g.drawString("考生须知：",55,1010);
        g.setFont(new Font("宋体",Font.PLAIN,23));

        g.drawLine(50,1020,990,1020);//画上线段

        int row=cardnote.length()/38;
        for (int i=0;i<row;i++){
            if (i==row-1){
                g.drawString(cardnote.substring(i*38,(i+1)*38),70,1050+30*i);
                g.drawString(cardnote.substring((i+1)*38,cardnote.length()),70,1050+30*(i+1));
            }
            g.drawString(cardnote.substring(i*38,(i+1)*38),70,1050+30*i);
        }

        //将内存中的图片输出到浏览器
        //参数一：图片对象
        //参数二：图片的格式，如PNG,JPG,GIF
        //参数三：图片输出到哪里去
        String path=pictureConfig.getCardPicUrl()+student.getsCertNo()+".BMP";
        OutputStream out=null;
        try {
            File file=new File(path);
//            if (!file.exists()){
//                file.createNewFile();
//            }
            out = new FileOutputStream(file);
            ImageIO.write(image,"BMP",out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return student.getsCertNo();

    }
}
