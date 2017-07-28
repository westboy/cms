package com.zhiliao.component.beetl.fun;

import com.zhiliao.common.db.kit.DbTableKit;
import org.beetl.core.Context;
import org.beetl.core.Function;

/**
 * Description:输出字段类型名称
 *
 * @author Jin
 * @create 2017-05-12
 **/
public class PrintModelFiledClassFunction implements Function {

    @Override
    public Object call(Object[] objects, Context context) {
        if (objects.length != 1){
            throw new RuntimeException("length of params must be 1 !");
        }
        if (objects[0].toString().length()!=0) {
            String var = (String) objects[0];
            for (String val : DbTableKit.MODEL_FILED_CLASS) {
                String[] tmp = val.split("#");
                System.out.println(tmp);
                if (tmp[0].equals(var))
                    return  tmp[1];
                continue;
            }
        }
        return null;
    }


}
