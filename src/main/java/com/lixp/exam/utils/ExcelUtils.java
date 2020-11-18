package com.lixp.exam.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 解析excel的工具类
 */
public class ExcelUtils {

    /**
     * 导出excel
     * @param sheetTitle sheet名称
     * @param title //列名
     * @param list //要插入的数据
     * @return
     */
    public static byte[] export(String sheetTitle, String[] title, List<Object> list) {
        XSSFWorkbook wb = new XSSFWorkbook();//创建excel表
        Sheet sheet = wb.createSheet(sheetTitle);
        sheet.setDefaultColumnWidth(20);//设置默认行宽



        //创建表头
        Row row = sheet.createRow(0);
        row.setHeightInPoints(20);//行高

        Cell cell = row.createCell(0);
        cell.setCellValue(sheetTitle);
        //创建标题
        Row rowTitle = sheet.createRow(1);
        rowTitle.setHeightInPoints(20);

        Cell hc;
        for (int i = 0; i < title.length; i++) {
            hc = rowTitle.createCell(i);
            hc.setCellValue(title[i]);
        }

        byte result[] = null;

        ByteArrayOutputStream out = null;

        try {
            //创建表格数据
            Field[] fields;
            int i = 2;

            for (Object obj : list) {
                fields = obj.getClass().getDeclaredFields();

                Row rowBody = sheet.createRow(i);
                rowBody.setHeightInPoints(20);
                int j = 0;
                for (Field f : fields) {

                    f.setAccessible(true);

                    Object va = f.get(obj);
                    if (null == va) {
                        va = "";
                    }

                    hc = rowBody.createCell(j);
                    hc.setCellValue(va.toString());
                    j++;
                }

                i++;
            }
            out = new ByteArrayOutputStream();
            wb.write(out);
            result =  out.toByteArray();
        } catch (Exception ex) {
            Logger.getLogger(ExcelUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                if(null != out){
                    out.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(ExcelUtils.class.getName()).log(Level.SEVERE, null, ex);
            } finally{
                try {
                    wb.close();
                } catch (IOException ex) {
                    Logger.getLogger(ExcelUtils.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return result;
    }


    /**
     * 传入excel文件地址解析excel文件，提取出信息
     * @param filepath excel文件地址
     * @return
     */
    public static List<StringBuffer> readExcel(String filepath){

        Row row = null;
        List<StringBuffer> list = new ArrayList<StringBuffer>();
        Workbook workbook=createWorkbookByExcelType(filepath);
        Sheet sheet = workbook.getSheetAt(0);
        //获取最大行数
        int rownum = sheet.getLastRowNum();
        //获取第一行
        row = sheet.getRow(0);
        //获取最大列数
        int colnumnum = row.getLastCellNum();
        for (int i = 1; i<rownum; i++) {
            Map<String,String> map = new LinkedHashMap<String,String>();
            row = sheet.getRow(i);
            if(row !=null){
               StringBuffer stringBuffer=new StringBuffer();
                for (int j=0;j<colnumnum;j++){
                    String cellData = (String) getCellFormatValue(row.getCell(j));
                    stringBuffer.append(cellData+"-");
                }
                list.add(stringBuffer);
            }
        }
            return list;

    }

    //根据传入的文件名后缀判断是xls还是xlsx
    public static Workbook createWorkbookByExcelType(String filePath){
        Workbook wb = null;
        if(filePath==null){
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            if(".xls".equals(extString)){
                return wb = new HSSFWorkbook(is);
            }else if(".xlsx".equals(extString)){
                return wb = new XSSFWorkbook(is);
            }else{
                return wb = null;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return wb;
    }


    public static Object getCellFormatValue(Cell cell){
        Object cellValue = null;
        if(cell!=null){
            //判断cell类型
            switch(cell.getCellType()){
                case NUMERIC:{
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                }
                case FORMULA:{
                    //判断cell是否为日期格式
                    if(DateUtil.isCellDateFormatted(cell)){
                        //转换为日期格式YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    }else{
                        //数字
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case STRING:{
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        }else{
            cellValue = "";
        }
        return cellValue;
    }
}
