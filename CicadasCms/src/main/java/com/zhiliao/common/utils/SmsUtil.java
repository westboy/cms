package com.zhiliao.common.utils;


import com.alibaba.fastjson.JSONArray;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SmsUtil {

    private static final Logger log = LoggerFactory.getLogger(SmsUtil.class);

    private static  final String url = "https://api.netease.im/sms/sendtemplate.action";
    private static  final String appKey = "cc58c823717404e2bb009fb055f659cf";
    private static  final String appSecret = "e4a1120b04c5";
    private static  final String nonce =  "12345";

    public static void sendTemplateMsg(String mobile,String templateId,String... params){

        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumUtil.getCheckSum(appSecret, nonce ,curTime);

        JSONArray paramArray = new JSONArray();
        for(String param : params){
            paramArray.add(param);
        }
        Map<String,String> queryMap = new HashMap<>();
        queryMap.put("templateid",templateId);
        queryMap.put("mobiles","[\""+mobile+"\"]");
        queryMap.put("params",paramArray.toJSONString());

        HttpResponse response = HttpRequest
                .post(url)
                .header("charset","utf-8")
                .header("contentType", "application/x-www-form-urlencoded")
                .header("AppKey", appKey)
                .header("Nonce", nonce)
                .header("CurTime", curTime)
                .header("CheckSum", checkSum)
                .query(queryMap)
                .send();
        log.info(response.bodyText());
    }

}
