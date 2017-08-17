package com.zhiliao.mybatis.model;

import com.zhiliao.common.annotation.ExcelField;

import java.io.Serializable;
import java.util.Date;

public class TSysUser implements Serializable {

    @ExcelField(value = "用户编号")
    private Integer userId;

    @ExcelField(value = "用户姓名")
    private String username;


    private String password;

    @ExcelField(value = "创建时",dateFormat = "yyyy年MM月dd日")
    private Date createTime;

    private String salt;

    private Date loginTime;

    private Date logoutTime;

    private String lastIp;

    private Boolean status;

    private String des;

    private String  avatar;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Date getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(Date logoutTime) {
        this.logoutTime = logoutTime;
    }

    public String getLastIp() {
        return lastIp;
    }

    public void setLastIp(String lastIp) {
        this.lastIp = lastIp;
    }

    @Override
    public String toString() {
        return "TSysUser{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", createTime=" + createTime +
                ", salt='" + salt + '\'' +
                ", loginTime=" + loginTime +
                ", logoutTime=" + logoutTime +
                ", lastIp='" + lastIp + '\'' +
                '}';
    }
}