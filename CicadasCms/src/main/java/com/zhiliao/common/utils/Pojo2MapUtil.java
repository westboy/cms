package com.zhiliao.common.utils;

import com.google.common.collect.Maps;
import com.zhiliao.mybatis.model.TCmsContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
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

    private static final Logger log = LoggerFactory.getLogger(Pojo2MapUtil.class);

    public static Map<String, ?> toMap(Object o) throws Exception {
        Map<String, Object> values = Maps.newHashMap();
        BeanInfo info = Introspector.getBeanInfo(o.getClass());
        log.debug(">>>>>>>>>> pojo to map [begin]");
        for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
            Method getter = pd.getReadMethod();
            if (getter != null) {
                log.debug(pd.getName() + "-->" + getter.invoke(o));
                values.put(pd.getName(), getter.invoke(o));
            }
            else log.debug(">>>>>>>>>> null getter"+getter);
        }
        log.debug(">>>>>>>>>> pojo to map [end]");
        return values;
    }

    public static <T> T toObj(Class<T> clazz, Map map)
            throws IntrospectionException, IllegalAccessException,
            InstantiationException, InvocationTargetException, ClassNotFoundException {
        T obj = clazz.newInstance();
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
        log.debug(">>>>>>>>>> map to  pojo [begin]");
        for (int i = 0; i< propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();

            if (map.containsKey(propertyName)) {
                Object value = map.get(propertyName);
                Object[] args = new Object[1];
                args[0] = value;
                descriptor.getWriteMethod().invoke(obj, args);
                log.debug(">>>>>>>>>> "+propertyName+ "==>" + map.get(propertyName));
            }
        }
        log.debug(">>>>>>>>>> map to pojo [end]");
        return obj;
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
