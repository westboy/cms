package com.zhiliao.module.web.cms.vo;

import com.zhiliao.mybatis.model.TCmsContent;

/**
 * Description:content
 *
 * @author Jin
 * @create 2017-06-11
 **/
public class TCmsContentVo extends TCmsContent{

    private String orderField;
    private String orderDirection;

    public String getOrderField() {
        return orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    public String getOrderDirection() {
        return orderDirection;
    }

    public void setOrderDirection(String orderDirection) {
        this.orderDirection = orderDirection;
    }

}
