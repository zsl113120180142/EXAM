package com.lixp.exam.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.lixp.exam.config.PictureConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class PdfUtils {

    @Autowired
    PictureConfig pictureConfig;

    public  File Pdf(String picurl, String mOutputPdfFileName) {
        Document doc = new Document(PageSize.A4, 20, 20, 20, 20); //new一个pdf文档
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(mOutputPdfFileName)); //pdf写入
            doc.open();//打开文档
                Image png1 = Image.getInstance(picurl); //通过文件路径获取image
                float heigth = png1.getHeight();
                float width = png1.getWidth();
                int percent = getPercent2(heigth, width);
                png1.setAlignment(Image.MIDDLE);
                png1.scalePercent(percent + 3);// 表示是原来图像的比例;
                doc.add(png1);
            doc.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File mOutputPdfFile = new File(mOutputPdfFileName);  //输出流
        if (!mOutputPdfFile.exists()) {
            mOutputPdfFile.deleteOnExit();
            return null;
        }

        File file=new File(picurl);
        file.delete();//用完以后删除照片

        return mOutputPdfFile; //反回文件输出流
    }

    public static int getPercent(float h, float w) {
        int p = 0;
        float p2 = 0.0f;
        if (h > w) {
            p2 = 297 / h * 100;
        } else {
            p2 = 210 / w * 100;
        }
        p = Math.round(p2);
        return p;
    }

    public static int getPercent2(float h, float w) {
        int p = 0;
        float p2 = 0.0f;
        p2 = 530 / w * 100;
        p = Math.round(p2);
        return p;
    }


    //String filepath, HttpServletRequest request
    public void imgOfPdf(String certNo) {
        boolean result = false;
        try {

            String picurl=pictureConfig.getCardPicUrl()+certNo+".BMP";  //添加图片文件路径

            //String fles = filepath.substring(0, filepath.lastIndexOf("."));

            String pdfUrl = pictureConfig.getCardPdfUrl()+certNo+".pdf";  //输出pdf文件路径
            result = true;
            if (result == true) {
                File file = this.Pdf(picurl, pdfUrl);//生成pdf
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

