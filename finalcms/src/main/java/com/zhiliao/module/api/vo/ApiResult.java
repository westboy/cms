package com.zhiliao.module.api.vo;

/**
 * Description:json
 *
 * @author Jin
 * @create 2017-04-26
 **/
public class ApiResult<T> {

    private int statusCode;
    private String msg;
    private T data;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
