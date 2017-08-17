package com.zhiliao.module.web.cms.service;

import com.zhiliao.common.base.BaseService;
import com.zhiliao.mybatis.model.TCmsTopic;

import java.util.List;

public interface TopicService extends BaseService<TCmsTopic,Integer> {
    Integer AllCount();

    List<TCmsTopic> findByRecommendList(Integer siteId,boolean isRecommend, Integer pageSize);
}
