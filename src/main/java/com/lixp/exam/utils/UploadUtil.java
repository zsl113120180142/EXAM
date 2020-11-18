package com.lixp.exam.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 解析压缩包的工具类
 */
public class UploadUtil {

    /**
     * 31      * 解析上传的压缩文件
     * 32      * @param request 请求
     * 33      * @param file 上传文件
     * 34      * @return
     * 35      * @throws Exception
     * 36
     */
    public static String resolveCompressUploadFile(HttpServletRequest request, MultipartFile file, String path) throws Exception {

        /* 截取后缀名 */
        if (file.isEmpty()) {
            throw new Exception("文件不能为空");
        }

        String fileName = file.getOriginalFilename();//获取文件名

        int pos = fileName.lastIndexOf(".");//获取.的下标

        String extName = fileName.substring(pos + 1).toLowerCase();//获取后缀名

        //判断上传文件必须是zip或者是rar否则不允许上传
        if (!extName.equals("zip") && !extName.equals("rar")) {
            throw new Exception("上传文件格式错误，请重新上传");
        }

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//格式化当前日期

        //日期作为文件夹的名称
        String saveFileName = sdf.format(d);

        //上传的zip文件保存位置
        File pushFile = new File(path + saveFileName + "/" + fileName);
        //解压后的文件的保存位置
        File descFile = new File(path + saveFileName);

        if (!descFile.exists()) {
            descFile.mkdirs();
        }
        //解压目的文件
        String descDir = path + saveFileName + "/";

        file.transferTo(pushFile);//把压缩文件写入过去

        //开始解压zip
        if (extName.equals("zip")) {
            //传入压缩文件保存地址，及解压到哪里
            CompressFileUtils.unZipFiles(pushFile, descDir);

        } else if (extName.equals("rar")) {
            //开始解压rar
            CompressFileUtils.unRarFile(pushFile.getAbsolutePath(), descDir);
        } else {
            throw new Exception("文件格式不正确不能解压");
        }

        return saveFileName;
    }


}
