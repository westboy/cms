package com.zhiliao.common.exception;

/**
 * Description:自定义Exception,主要是为了返回JSON信息
 *
 * @author Jin
 * @create 2017-04-09
 **/
public class ApiException extends RuntimeException{

    public ApiException(String message) {
        super(message);
    }

}
