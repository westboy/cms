package com.zhiliao.component.beetl.fun;
import org.beetl.core.Context;
import org.beetl.core.Function;

import java.text.SimpleDateFormat;
import java.util.Date;


public class PrintTimeFunction implements Function {

    private final static SimpleDateFormat TIME_STAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final static SimpleDateFormat MY_DATE_FORMAT = new SimpleDateFormat("yyyy年MM月dd日");

    @Override
    public String call(Object[] params, Context context) {
        if (params.length != 1){
            throw new RuntimeException("length of params must be 1 !");
        }
        if (params[0].toString().length()==0){
            return null;
        }
    
        return getNiceDate((Date)params[0]);
    }

    public static String getNiceDate( Date date) {
        if (null == date) return "";
        String result = null;
        long currentTime = new Date().getTime() - date.getTime();
        int time = (int)(currentTime / 1000);
        if(time < 60) {
            result = "刚刚";
        } else if(time >= 60 && time < 3600) {
            result = time/60 + "分钟前";
        } else if(time >= 3600 && time < 86400) {
            result = time/3600 + "小时前";
        } else if(time >= 86400 && time < 864000) {
            result = time/86400 + "天前";
        } else{
            result = MY_DATE_FORMAT.format(date);
        }
        return result;
    }
}
