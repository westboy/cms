package com.zhiliao.module.web.cms.vo;

import com.zhiliao.mybatis.model.TCmsAd;

/**
 * Description:
 *
 * @author Jin
 * @create 2017-06-21
 **/
public class TCmsAdVo extends TCmsAd{

    private Integer groupId;

    @Override
    public Integer getGroupId() {
        return groupId;
    }

    @Override
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    private String groupName;

}
