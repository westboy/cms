package com.zhiliao.common.db.kit;

import com.zhiliao.common.db.impl.M;

import static com.zhiliao.common.db.impl.M.*;

/**
 * Description:Mysql db table filed utils
 *
 * @author Jin
 * @create 2017-05-11
 **/
public class MysqlFiledUtil {

    public static boolean isAutoIncrementFiled(M FiledType){
        if(FiledType==BIG_INT_TYPE)
            return true;
         if(FiledType==INT_TYPE)
             return true;
         if(FiledType==TNY_INT_TYPE)
             return true;
         if(FiledType==SMALL_INT_TYPE)
             return true;
         if(FiledType==MEDIUM_INT_TYPE)
             return true;
         return false;
    }

    public static boolean isCharTextFiled(M FiledType){

        if(FiledType==DATE_TYPE)
            return false;
        if(FiledType==TIMESTAMP_TYPE)
            return false;
        if(FiledType==TEXT_TYPE)
            return false;
        if(FiledType==MEDIUM_TEXT_TYPE)
            return false;
        if(FiledType==LONG_TEXT_TYPE)
            return false;
        return true;
    }

    public static boolean isNotDefaultValue(M FiledType){
        if(FiledType==TEXT_TYPE)
            return true;
        if(FiledType==MEDIUM_TEXT_TYPE)
            return true;
        if(FiledType==LONG_TEXT_TYPE)
            return true;
        return false;
    }

    public static void main(String[] ARGS){
        System.out.println(isAutoIncrementFiled(INT_TYPE));

        System.out.println( isCharTextFiled(TEXT_TYPE));
    }

}
