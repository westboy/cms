package com.zhiliao.common.jwt.vo;

/**
 * Description:token验证结果集
*
 **/
public class ResultVo {

    private String status;
    private PayloadVo payloadVo;

    public PayloadVo getPayloadVo() {
        return payloadVo;
    }

    public void setPayloadVo(PayloadVo payloadVo) {
        this.payloadVo = payloadVo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
