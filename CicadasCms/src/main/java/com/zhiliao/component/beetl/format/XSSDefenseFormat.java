package com.zhiliao.component.beetl.format;

import org.apache.commons.lang.StringEscapeUtils;
import org.beetl.core.Format;
import org.owasp.validator.html.*;

public class XSSDefenseFormat implements Format {

    @Override
    public Object format(Object data, String pattern) {
        if (null == data) {
            return null;
        } else {

            try {
                if(data instanceof String) {
                    Policy policy = Policy.getInstance(getClass().getResourceAsStream("/antisamy.xml"));
                    AntiSamy as = new AntiSamy(policy);

                    String content = (String) data;
                    if ("escape".equals(pattern)) {
                        content = StringEscapeUtils.escapeHtml(content);
                    }
                    CleanResults cr = as.scan(content);
                    content = cr.getCleanHTML();
                    return content;
                }
                return data;

            } catch (ScanException e) {
                return "系统错误";
            } catch (PolicyException e) {
                return "系统错误";
            }

        }
    }

    public static void main(String[] args){
        String js =  "中文<script>hi</script><h5></h5>";
        System.out.println(js);
        js = (String) new XSSDefenseFormat().format(js, "escape");
        System.out.println(js);
    }
}