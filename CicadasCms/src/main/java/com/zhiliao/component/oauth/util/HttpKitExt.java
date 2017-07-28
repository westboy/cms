package com.zhiliao.component.oauth.util;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

public class HttpKitExt {
    /**
     * 构造请求参数
     * @param url url地址
     * @param params 参数集合
     * @return String 构造完成的url
     */
    public static String initParams(String url, Map<String, String> params) {
        if (null == params || params.isEmpty()) {
            return url;
        }
        StringBuilder sb = new StringBuilder(url);
        if (url.indexOf("?") == -1) {
            sb.append("?");
        }
        sb.append(map2Url(params));
        return sb.toString();
    }
    
    /**
     * map构造url
     * @param paramToMap 参数集合
     * @return String 构造完成的url
     */
    public static String map2Url(Map<String, String> paramToMap) {
        if (null == paramToMap || paramToMap.isEmpty()) {
            return null;
        }
        StringBuffer url = new StringBuffer();
        boolean isfist = true;
        for (Entry<String, String> entry : paramToMap.entrySet()) {
            if (isfist) {
                isfist = false;
            } else {
                url.append("&");
            }
            url.append(entry.getKey()).append("=");
            String value = entry.getValue();
            if (StrKit.notBlank(value))
                try {value = URLEncoder.encode(value, "UTF-8");} catch (UnsupportedEncodingException e) {throw new RuntimeException(e);}
            url.append(value);
        }
        return url.toString();
    }
    
}