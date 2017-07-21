package com.zhiliao.common.utils;

import com.google.common.collect.Maps;
import com.zhiliao.mybatis.model.master.TCmsContent;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

/**
 * Description:pojo 转 map
 *
 * @author Jin
 * @create 2017-06-15
 **/
public class Pojo2MapUtil {

    public static Map<String, ?> toMap(Object o) throws Exception {
        Map<String, Object> values = Maps.newHashMap();
        BeanInfo info = Introspector.getBeanInfo(o.getClass());
        for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
            /*This will access public properties through getters*/
            Method getter = pd.getReadMethod();
            if (getter != null) {
                System.out.println(pd.getName() + "==>" + getter.invoke(o));
                values.put(pd.getName(), getter.invoke(o));
            }
            else System.out.println("null getter"+getter);
        }
        System.out.println("---");
        return values;
    }


    public static void main(String[] args) throws Exception {
        TCmsContent content = new TCmsContent();
        content.setContentId(999999l);
        content.setModelId(11111);
        content.setUpdatedate(new Date());
        Map m = toMap(content);
        m.forEach((key,value)->
          System.out.println(key+"-->>"+value)
        );
    }
}
