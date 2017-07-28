package com.zhiliao.common.utils;

import com.zhiliao.common.annotation.ExcelField;
import com.zhiliao.common.exception.SystemException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

/**
 * Description:
 *
 * @author Jin
 * @create 2017-07-27
 **/
public class ExcelUtil {

    public static void exports(String sheetName,List<?> list) {

        if(CmsUtil.isNullOrEmpty(list)) throw new SystemException("导出excel失败！");
        try {
            HttpServletResponse response = ControllerUtil.getHttpServletResponse();
            ServletOutputStream out = response.getOutputStream();
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet=wb.createSheet(sheetName);
            for (int i= 0;i<list.size();i++) {
                Object obj = list.get(i);
                Class clazz = obj.getClass();
                if(i== 0) {
                    HSSFRow row=sheet.createRow(i);
                    Field[] fields = clazz.getDeclaredFields();
                    int tmp = 0;
                    for (Field field :fields) {
                        HSSFCell cell = row.createCell(tmp);
                        ExcelField excelFiled = field.getAnnotation(ExcelField.class);
                        if (excelFiled == null) continue;
                        cell.setCellValue(excelFiled.value());
                        tmp++;
                    }
                }
                HSSFRow row=sheet.createRow(i+1);
                Field[] fields = clazz.getDeclaredFields();
                int tmp = 0;
                for (Field field :fields) {
                    ExcelField excelFiled = field.getAnnotation(ExcelField.class);
                    if (excelFiled == null){continue;}
                    HSSFCell cell = row.createCell(tmp);
                    PropertyDescriptor pd = new PropertyDescriptor(field.getName(),clazz);
                    Method getMethod = pd.getReadMethod();
                    Object o = getMethod.invoke(obj);
                    if(!StrUtil.isBlank(excelFiled.dateFormat())&& o instanceof Date) cell.setCellValue(DateUtil.format((Date)o,excelFiled.dateFormat()));
                    else cell.setCellValue(String.valueOf(o));
                    tmp++;
                }
            }
            response.reset();
            response.setHeader("Content-disposition", "attachment;filename="+sheetName+".xls");
            response.setContentType("application/msexcel");
            wb.write(out);
            out.close();

        } catch (Exception e) {
            throw new SystemException("导出excel失败,["+e.getMessage()+"]");
        }
    }

    public static void imports(){}

}
