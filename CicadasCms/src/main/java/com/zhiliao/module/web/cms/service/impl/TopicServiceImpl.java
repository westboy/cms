package com.zhiliao.module.web.cms.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhiliao.common.utils.CmsUtil;
import com.zhiliao.common.utils.JsonUtil;
import com.zhiliao.module.web.cms.service.TopicService;
import com.zhiliao.mybatis.mapper.TCmsTopicMapper;
import com.zhiliao.mybatis.model.TCmsTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 *
 * @author Jin
 * @create 2017-07-19
 **/
@Service
@CacheConfig(cacheNames = "cms-topic-cache")
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TCmsTopicMapper topicMapper;

    @CacheEvict(cacheNames ="cms-topic-cache",allEntries = true)
    @Override
    public String save(TCmsTopic pojo) {
        if(topicMapper.insertSelective(pojo)>0)
            return JsonUtil.toSUCCESS("操作成功！","topic-tab",true);
        return JsonUtil.toERROR("操作失败！");
    }

    @CacheEvict(cacheNames ="cms-topic-cache",allEntries = true)
    @Override
    public String update(TCmsTopic pojo) {
        if(topicMapper.updateByPrimaryKeySelective(pojo)>0)
            return JsonUtil.toSUCCESS("操作成功！","topic-tab",false);
        return JsonUtil.toERROR("操作失败！");
    }

    @CacheEvict(cacheNames ="cms-topic-cache",allEntries = true)
    @Override
    public String delete(Integer[] ids) {
        if(CmsUtil.isNullOrEmpty(ids))
            return JsonUtil.toERROR("操作失败！");
        for(Integer id : ids)
            topicMapper.deleteByPrimaryKey(id);
        return JsonUtil.toSUCCESS("操作成功！","topic-tab",false);
    }

    @Cacheable(key = "'topicId-'+#p0")
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

    @Cacheable(key = "'allCount'")
    @Override
    public Integer AllCount() {
        return this.topicMapper.selectCount(new TCmsTopic());
    }

    @Cacheable(key = "'recommend-list'+#p0+#p1+#p2")
    @Override
    public List<TCmsTopic> findByRecommendList(Integer siteId,boolean isRecommend, Integer pageSize) {
        PageHelper.startPage(1,pageSize);
        TCmsTopic topic = new TCmsTopic();
        topic.setSiteId(siteId);
        topic.setIsRecommend(isRecommend);
        return this.topicMapper.select(topic);
    }
}
