package com.zhiliao.component.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

public class DefaultUsernamePasswordToken  extends UsernamePasswordToken {

    public static  final String ADMIN = "admin";
    public static  final String USER = "user";

    public DefaultUsernamePasswordToken() { }

    public DefaultUsernamePasswordToken(String username, String password) {
        super(username, password);
    }



    /**
     * 判断登录类型
     */
    private String loginType;

    public String getLoginType() {
        return this.loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;

    }


}
