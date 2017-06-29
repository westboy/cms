package com.zhiliao.common.utils;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * CheckSum 工具类(可以用于API接口signature生成)
 */
public class CheckSumUtil {

    private CheckSumUtil() {
        throw new Error("工具类不能实例化！");
    }

    /*计算并获取CheckSum*/
    public static String getCheckSum(String appSecrt, String nonce, String timestamp) {
        return encode("sha1", appSecrt + nonce + timestamp);
    }

    /*获取当前日期*/
    public static String getTimestamp(){
        SimpleDateFormat sdf = new  SimpleDateFormat("yyyyMMddHHmmss");
        return  sdf.format(new Date());
    }

    public static void  main(String [] args ){

//        String s = getTimestamp();
//        System.out.println(getCheckSum("jdeFDS89HFassdsfFDNDS73FDJK", "11", s));
        System.out.println(Long.parseLong("20170608164200")-Long.parseLong("20170608164000"));

    }

    /* 计算并获取md5值*/
    public static String getMD5(String str) {
        return encode("md5", str);
    }

    /*加密*/
    private static String encode(String algorithm, String value) {
        if (value == null) {
            return null;
        }
        try {
            MessageDigest messageDigest
                    = MessageDigest.getInstance(algorithm);
            messageDigest.update(value.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }
    private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

 }