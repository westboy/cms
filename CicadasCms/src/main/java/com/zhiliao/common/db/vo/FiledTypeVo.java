package com.zhiliao.common.db.vo;

import com.zhiliao.common.db.impl.M;

/**
 * Description:数据库字段类型VO
 *
 * @author Jin
 * @create 2017-05-13
 **/
public class FiledTypeVo {

    private M m;

    public M getM() {
        return m;
    }

    public void setM(M m) {
        this.m = m;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    private Integer length;

    private String defaultValue;

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
