package com.zhiliao.component.beetl.format;

import org.beetl.core.Format;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateFormat implements Format {

    private ThreadLocal<Map<String, SimpleDateFormat>> threadlocal = new ThreadLocal();

    public DateFormat() {
    }

    public Object format(Object data, String pattern) {
        if (data == null) {
            return null;
        } else {
            Date date;
            if (Date.class.isAssignableFrom(data.getClass())) {
                SimpleDateFormat date1;
                if (pattern == null) {
                    date1 = this.getDateFormat("default");
                } else {
                    date1 = this.getDateFormat(pattern);
                }

                return date1.format((Date) data);
            } else if (data.getClass() == Long.class) {
                date = new Date(((Long) data).longValue());
                SimpleDateFormat sdf;
                if (pattern == null) {
                    sdf = this.getDateFormat("default");
                } else {
                    sdf = this.getDateFormat(pattern);
                }

                return sdf.format(date);
            } else {
                return data;
            }
        }
    }

    private SimpleDateFormat getDateFormat(String pattern) {
        Object map;
        if ((map = this.threadlocal.get()) == null) {
            map = new HashMap(4, 0.65F);
            this.threadlocal.set((Map<String, SimpleDateFormat>) map);
        }

        SimpleDateFormat format = (SimpleDateFormat) ((Map) map).get(pattern);
        if (format == null) {
            if ("default".equals(pattern)) {
                format = new SimpleDateFormat();
            } else {
                format = new SimpleDateFormat(pattern);
            }

            ((Map) map).put(pattern, format);
        }

        return format;
    }
}
