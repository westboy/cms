package com.zhiliao.common.utils;

import com.zhiliao.common.annotation.ExcelFiled;
import com.zhiliao.common.exception.SystemException;
import com.zhiliao.mybatis.model.master.TCmsContent;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import javax.servlet.ServletOutputStream;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description:
 *
 * @author Jin
 * @create 2017-07-27
 **/
public class ExcelUtil {

    public static void exports(ServletOutputStream out,List<?> list) {

        if(CmsUtil.isNullOrEmpty(list)) throw new SystemException("导出excel失败！");
        try {
            WritableWorkbook wwb = Workbook.createWorkbook(out);
            WritableSheet sheet = wwb.createSheet("sheet1123123132", 0);
            Label label ;
            int flag = 0;
            if(flag==0) {
                Object obj = list.get(0);
                Class clazz = obj.getClass();
                Field[] fields = clazz.getDeclaredFields();//获得属性
                for (int i = 0;i<fields.length;i++) {
                    ExcelFiled excelFiled = fields[i].getAnnotation(ExcelFiled.class);
                    if(excelFiled==null) continue;
                    /* Label(x,y,z) 代表单元格的第x+1列，第y+1行, 内容z*/
                    /* 在Label对象的子对象中指明单元格的位置和内容*/
                    label = new Label(i,0,excelFiled.value());
                    /* 将定义好的单元格添加到工作表中*/
                    sheet.addCell(label);
                }
                flag=1;
            }
            for (int i= 0;i<list.size();i++){
                Object obj = list.get(i);
                Class clazz = obj.getClass();
                Field[] fields = clazz.getDeclaredFields();/*获得属性*/
                for (int j = 0;j<fields.length;j++) {
                    ExcelFiled excelFiled = fields[j].getAnnotation(ExcelFiled.class);
                    if(excelFiled==null) continue;
                    PropertyDescriptor pd = new PropertyDescriptor(fields[j].getName(),clazz);
                    Method getMethod = pd.getReadMethod();/*获得get方法*/
                    Object o = getMethod.invoke(obj);
                    if(excelFiled.isNumber()) {
                        jxl.write.Number number = new jxl.write.Number(0,j,Double.valueOf(o.toString()));
                        sheet.addCell(number);
                        continue;
                    }
                    if(!StrUtil.isBlank(excelFiled.dateFormat()))
                        label = new Label(j, i, DateUtil.format((Date)o,excelFiled.dateFormat()));
                    else
                        label = new Label(j, i, o.toString());
                    sheet.addCell(label);
                }
            }
            wwb.write();
            wwb.close();
            out.flush();
            out.close();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }


    public static void main(String [] args) throws Exception {
        List list = new ArrayList<>();
        TCmsContent content = new TCmsContent();
        content.setCategoryId(111l);
        list.add(content);
        Object obj = list.get(0);
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();//获得属性
        for (Field field : fields) {
            ExcelFiled filedName = field.getAnnotation(ExcelFiled.class);
            if(filedName!=null) System.out.println(filedName.value());
            System.out.println(field.getName());
            PropertyDescriptor pd = new PropertyDescriptor(field.getName(),clazz);
            Method getMethod = pd.getReadMethod();//获得get方法
            Object o = getMethod.invoke(obj);//执行get方法返回一个Object
            System.out.println(o);
        }

    }
}
