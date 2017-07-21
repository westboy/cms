package com.zhiliao.common.jwt.vo;

/**
 * Description:payload vo
 *
 * @author Jin
 * @create 2017-04-05
 **/
public class PayloadVo {

    private String appId;
    //签发时间
    private long iat;
    //过期时间
    private long ext;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public long getIat() {
        return iat;
    }

    public void setIat(long iat) {
        this.iat = iat;
    }

    public long getExt() {
        return ext;
    }

    public void setExt(long  ext) {
        this.ext = ext;
    }

    @Override
    public String toString() {
        return "PayloadVo{" +
                "appId='" + appId + '\'' +
                ", iat=" + iat +
                ", ext=" + ext +
                '}';
    }
}
