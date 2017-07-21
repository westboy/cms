package com.zhiliao.common.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.zhiliao.module.api.vo.ApiResult;

import java.util.List;
import java.util.Map;

public class JsonUtil {

    private final static Integer DEFAULT_ERROR_CODE = 1;

    private final static Integer DEFAULT_SUCCESS_CODE = 0;


    /*后台框架请求成功JSON*/
    public static String toSUCCESS(String message,String tabId,boolean closeCurrent){
        JSONObject obj = new JSONObject();
        obj.put("statusCode","200");
        obj.put("message",message);
        obj.put("tabid",tabId);
        obj.put("closeCurrent",closeCurrent);
        obj.put("forward","");
        obj.put("forwardConfirm","");
        return obj.toJSONString();
    }

    /*后台框架请求成功JSON*/
    public static String toSUCCESS(String message,String tabId,String dialogid,boolean closeCurrent){
        JSONObject obj = new JSONObject();
        obj.put("statusCode","200");
        obj.put("message",message);
        obj.put("tabid",tabId);
        obj.put("dialogid",dialogid);
        obj.put("closeCurrent",closeCurrent);
        obj.put("forward","");
        obj.put("forwardConfirm","");
        return obj.toJSONString();
    }

    /*后台框架请求成功JSON*/
    public static String toSUCCESS(String message,String tabId,String dialogid,String divid,boolean closeCurrent){
        JSONObject obj = new JSONObject();
        obj.put("statusCode","200");
        obj.put("message",message);
        obj.put("tabid",tabId);
        obj.put("dialogid",dialogid);
        obj.put("closeCurrent",closeCurrent);
        obj.put("divid",divid);
        obj.put("forwardConfirm","");
        return obj.toJSONString();
    }

    /*后台框架请求成功JSON*/
    public static String toSUCCESS(String message,String tabId,String dialogid,String divid,String forward,boolean closeCurrent){
        JSONObject obj = new JSONObject();
        obj.put("statusCode","200");
        obj.put("message",message);
        obj.put("tabid",tabId);
        obj.put("dialogid",dialogid);
        obj.put("closeCurrent",closeCurrent);
        obj.put("divid",divid);
        obj.put("forward",forward);
        obj.put("forwardConfirm","");
        return obj.toJSONString();
    }

    /*后台框架请求成功JSON*/
    public static String toSUCCESS(String message){
        JSONObject obj = new JSONObject();
        obj.put("statusCode","200");
        obj.put("message",message);
        obj.put("tabid","");
        obj.put("closeCurrent",false);
        obj.put("forward","");
        obj.put("forwardConfirm","");
        return obj.toJSONString();
    }


    /*后台框架文件上传成功JSON*/
    public static String toUploadSUCCESS(String message,String fileName){
        JSONObject obj = new JSONObject();
        obj.put("statusCode","200");
        obj.put("message",message);
        obj.put("filename",fileName);
        return obj.toJSONString();
    }

    /*后台框架文件上传错误JSON*/
    public static String toUploadRROR(String message){
        JSONObject obj = new JSONObject();
        obj.put("statusCode","300");
        obj.put("message",message);
        return obj.toJSONString();
    }

    /*后台框架请求错误JSON*/
    public static String toERROR(String message){
        JSONObject obj = new JSONObject();
        obj.put("statusCode","300");
        obj.put("message",message);
        obj.put("tabid","");
        obj.put("closeCurrent",false);
        obj.put("forward","");
        obj.put("forwardConfirm","");
        return obj.toJSONString();
    }

    /*后台框架回话超时JSON*/
    public static String toTIMEOUT(){
        JSONObject obj = new JSONObject();
        obj.put("statusCode","301");
        obj.put("message","会话超时");
        obj.put("closeCurrent",false);
        obj.put("forward","");
        obj.put("forwardConfirm","");
        return obj.toJSONString();
    }

    /*后台框架回话超时JSON*/
    public static String toTIMEOUT(String info){
        JSONObject obj = new JSONObject();
        obj.put("statusCode","301");
        obj.put("message",info);
        obj.put("closeCurrent",false);
        obj.put("forward","");
        obj.put("forwardConfirm","");
        return obj.toJSONString();
    }


    /*通用信息提示JSON*/
    public static Map toMAP(boolean success, String msg){
        Map obj = Maps.newHashMap();
        obj.put("success",success);
        obj.put("message",msg);
        return obj;
    }

    /* API接口请求成功返回JSON */
    public static String toSuccessResultJSON(String msg,List list){
        ApiResult result = new ApiResult<List>();
        result.setStatusCode(DEFAULT_SUCCESS_CODE);
        result.setMsg(msg);
        result.setData(list);
        return JSON.toJSONString(result);
    }

    /* API接口请求成功返回JSON */
    public static String toSuccessResultJSON(String msg,Object object){
        ApiResult result = new ApiResult();
        result.setStatusCode(DEFAULT_SUCCESS_CODE);
        result.setMsg(msg);
        if(!CmsUtil.isNullOrEmpty(object))
             result.setData(object);
        return JSON.toJSONString(result);
    }
    /* API接口请求错误返回JSON */
    public static String toErrorResultJSON(String msg){
        ApiResult result = new ApiResult();
        result.setStatusCode(DEFAULT_ERROR_CODE);
        result.setMsg(msg);
        return JSON.toJSONString(result);
    }

    /*创建一个新的JSONObject*/
    public static JSONObject newJSONObject(){
        return new JSONObject();
    }
}
