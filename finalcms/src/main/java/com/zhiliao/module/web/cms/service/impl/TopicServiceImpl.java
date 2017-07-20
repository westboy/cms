package com.zhiliao.module.web.cms.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhiliao.common.utils.CmsUtil;
import com.zhiliao.common.utils.JsonUtil;
import com.zhiliao.module.web.cms.service.TopicService;
import com.zhiliao.mybatis.mapper.master.TCmsTopicMapper;
import com.zhiliao.mybatis.model.master.TCmsTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 *
 * @author Jin
 * @create 2017-07-19
 **/
@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TCmsTopicMapper topicMapper;

    @Override
    public String save(TCmsTopic pojo) {
        if(topicMapper.insertSelective(pojo)>0)
            return JsonUtil.toSUCCESS("操作成功！","topic-tab",true);
        return JsonUtil.toERROR("操作失败！");
    }

    @Override
    public String update(TCmsTopic pojo) {
        if(topicMapper.updateByPrimaryKeySelective(pojo)>0)
            return JsonUtil.toSUCCESS("操作成功！","topic-tab",false);
        return JsonUtil.toERROR("操作失败！");
    }

    @Override
    public String delete(Integer[] ids) {
        if(CmsUtil.isNullOrEmpty(ids))
            return JsonUtil.toERROR("操作失败！");
        for(Integer id : ids)
            topicMapper.deleteByPrimaryKey(id);
        return JsonUtil.toSUCCESS("操作成功！","topic-tab",false);
    }

    @Override
    public TCmsTopic findById(Integer id) {
        return topicMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<TCmsTopic> findList(TCmsTopic pojo) {
        return topicMapper.select(pojo);
    }

    @Override
    public List<TCmsTopic> findAll() {
        return topicMapper.selectAll();
    }

    @Override
    public PageInfo<TCmsTopic> page(Integer pageNumber, Integer pageSize, TCmsTopic pojo) {
        PageHelper.startPage(pageNumber,pageSize);
        return new PageInfo<>(this.findList(pojo));
    }

    @Override
    public PageInfo<TCmsTopic> page(Integer pageNumber, Integer pageSize) {
        PageHelper.startPage(pageNumber,pageSize);
        return new PageInfo<>(this.findAll());
    }
}
