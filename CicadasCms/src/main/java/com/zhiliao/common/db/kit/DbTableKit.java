package com.zhiliao.common.db.kit;

import com.zhiliao.common.db.impl.M;
import com.zhiliao.common.db.vo.FiledTypeVo;
import com.zhiliao.common.exception.SystemException;
import com.zhiliao.common.utils.StrUtil;

/**
 * Description:cms 工具类
 *
 * @author Jin
 * @create 2017-05-12
 **/
public class DbTableKit {

     public static final String[] MODEL_FILED_CLASS ={"input#单文本","file#文件","image#单图上传","editor#编辑器","textarea#多文本框","radio#单选","select#列表","checkbox#多选","dateInput#日期输入"};

     public static final String[] PREPARED_MODEL_FILED_NAME={"title","site_id","siteId","user_id","userId","category_id","categoryId","model_id","modelId","keywords","description","top","recommend","thumb","status","inputdate","updatedate","url","author","view_num","viewNum"};


     public static FiledTypeVo getFiledTypeVo(String filedClass, String filedType,Integer length,String defaultValue){
          FiledTypeVo f = new FiledTypeVo();

          if (filedClass.equals("input")){
               if(!StrUtil.isBlank(filedType)&&length!=null){
                    f.setM(M.valueOf(filedType));
                    f.setLength(length);
               }else {
                    f.setM(M.VARCHAR_TYPE);
                    f.setLength(255);
               }
               f.setDefaultValue(defaultValue);
          }

          if (filedClass.equals("file")){
               if(!StrUtil.isBlank(filedType)&&length!=null){
                    f.setM(M.valueOf(filedType));
                    f.setLength(length);
               }else {
                    f.setM(M.VARCHAR_TYPE);
                    f.setLength(128);
               }
               f.setDefaultValue(null);
          }

          if (filedClass.equals("image")){
               if(!StrUtil.isBlank(filedType)&&length!=null){
                    f.setM(M.valueOf(filedType));
                    f.setLength(length);
               }else {
                    f.setM(M.TEXT_TYPE);
                    f.setLength(null);
               }
               f.setDefaultValue(defaultValue);
          }

          if (filedClass.equals("editor")){
               if(!StrUtil.isBlank(filedType)&&length!=null){
                    if(MysqlFiledUtil.isAutoIncrementFiled(M.valueOf(filedType)))
                         throw new SystemException("请选择非数字的字段类型类型！");
                    f.setM(M.valueOf(filedType));
                    f.setLength(length);

               }else {
                    f.setM(M.MEDIUM_TEXT_TYPE);
                    f.setLength(null);
               }
               f.setDefaultValue(defaultValue);
          }

          if (filedClass.equals("textarea")){
               if(!StrUtil.isBlank(filedType)&&length!=null){
                    if(MysqlFiledUtil.isAutoIncrementFiled(M.valueOf(filedType)))
                         throw new SystemException("请选择非数字的字段类型类型！");
                    f.setM(M.valueOf(filedType));
                    f.setLength(length);
               }else {
                    f.setM(M.TEXT_TYPE);
                    f.setLength(null);
               }
               f.setDefaultValue(defaultValue);
          }

          if (filedClass.equals("radio")){
               if(!StrUtil.isBlank(filedType)&&length!=null){
                    f.setM(M.valueOf(filedType));
                    f.setLength(length);
               }else {
                    f.setM(M.VARCHAR_TYPE);
                    f.setLength(200);
               }
               f.setDefaultValue(" ");
          }

          if (filedClass.equals("checkbox")){
               if(!StrUtil.isBlank(filedType)&&length!=null){
                    f.setM(M.valueOf(filedType));
                    f.setLength(length);
               }else {
                    f.setM(M.VARCHAR_TYPE);
                    f.setLength(200);
               }
               f.setDefaultValue(" ");
          }

          if (filedClass.equals("select")){
               if(!StrUtil.isBlank(filedType)&&length!=null){
                    f.setM(M.valueOf(filedType));
                    f.setLength(length);
               }else {
                    f.setM(M.VARCHAR_TYPE);
                    f.setLength(200);
               }
               f.setDefaultValue(null);
          }
          if (filedClass.equals("dateInput")){
               if(!StrUtil.isBlank(filedType)&&length!=null){
                    f.setM(M.valueOf(filedType));
                    f.setLength(length);
               }else {
                    f.setM(M.CHAR_TYPE);
                    f.setLength(32);
               }
               f.setDefaultValue(" ");
          }

      return f;
     }

}
