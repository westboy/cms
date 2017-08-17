package com.zhiliao.module.web.cms.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhiliao.common.utils.JsonUtil;
import com.zhiliao.module.web.cms.service.FriendlinkService;
import com.zhiliao.mybatis.mapper.TCmsFriendlinkGroupMapper;
import com.zhiliao.mybatis.mapper.TCmsFriendlinkMapper;
import com.zhiliao.mybatis.model.TCmsFriendlink;
import com.zhiliao.mybatis.model.TCmsFriendlinkGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:友情链接service实现
 *
 * @author Jin
 * @create 2017-06-12
 **/
@Service
public class FriendlinkServiceImpl implements FriendlinkService{

    @Autowired
    private TCmsFriendlinkMapper friendlinkMapper;

    @Autowired
    private TCmsFriendlinkGroupMapper groupMapper;

    @Override
    public String save(TCmsFriendlink pojo) {
        if (friendlinkMapper.insertSelective(pojo)>0)
            return JsonUtil.toSUCCESS("操作成功","friendlink-tab",true);
        return  JsonUtil.toERROR("操作失败");
    }

    @Override
    public String update(TCmsFriendlink pojo) {
        if (friendlinkMapper.updateByPrimaryKeySelective(pojo)>0)
            return JsonUtil.toSUCCESS("操作成功","friendlink-tab",true);
        return  JsonUtil.toERROR("操作失败");
    }

    @Override
    public String delete(Integer[] ids) {
        if(ids!=null&&ids.length>0)
            for(Integer id :ids)
                friendlinkMapper.deleteByPrimaryKey(id);
        return JsonUtil.toSUCCESS("删除成功","friendlink-tab",false);
    }

    @Override
    public TCmsFriendlink findById(Integer id) {
        return friendlinkMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<TCmsFriendlink> findList(TCmsFriendlink pojo) {
        return friendlinkMapper.select(pojo);
    }

    @Override
    public List<TCmsFriendlink> findAll() {
        return friendlinkMapper.selectAll();
    }

    @Override
    public PageInfo<TCmsFriendlink> page(Integer pageNumber, Integer pageSize, TCmsFriendlink pojo) {
        PageHelper.startPage(pageNumber,pageSize);
        if(pojo.getGroupId()!=null){
            return  new PageInfo<>(friendlinkMapper.selectFriendLinkByGroupId(pojo.getGroupId()));
        }
        return new PageInfo<>(findList(pojo));
    }

    @Override
    public PageInfo<TCmsFriendlink> page(Integer pageNumber, Integer pageSize) {
        PageHelper.startPage(pageNumber,pageSize);
        return new PageInfo<>(findAll());
    }

    @Override
    public PageInfo<TCmsFriendlinkGroup> page(Integer pageNumber, Integer pageSize, TCmsFriendlinkGroup group) {
        PageHelper.startPage(pageNumber,pageSize);
        return new PageInfo<>(groupMapper.select(group));
    }

    @Override
    public TCmsFriendlinkGroup findGroupById(Integer id) {
        TCmsFriendlinkGroup cmsFriendlinkGroup = groupMapper.selectByPrimaryKey(id);
        return cmsFriendlinkGroup;
    }

    @Override
    public String save(TCmsFriendlinkGroup group) {
        if(groupMapper.insertSelective(group)>0)
             return JsonUtil.toSUCCESS("操作成功","friendlink-tab","friend-group-tab",true);
        return  JsonUtil.toERROR("操作失败");
    }

    @Override
    public String update(TCmsFriendlinkGroup group) {
        if(groupMapper.updateByPrimaryKeySelective(group)>0)
            return JsonUtil.toSUCCESS("操作成功","friendlink-tab","friend-group-tab",true);
        return  JsonUtil.toERROR("操作失败");
    }

    @Override
    public String deleteGroupById(Integer id) {
        if(groupMapper.deleteByPrimaryKey(id)>0)
            return JsonUtil.toSUCCESS("操作成功","friendlink-tab","friend-group-tab",true);
        return  JsonUtil.toERROR("操作失败");
    }

    @Override
    public List<TCmsFriendlinkGroup> findGroupAll() {
        return groupMapper.selectAll();
    }
}
