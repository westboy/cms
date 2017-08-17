package com.zhiliao.module.web.cms.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhiliao.common.utils.JsonUtil;
import com.zhiliao.module.web.cms.service.LinkageService;
import com.zhiliao.mybatis.mapper.TCmsLinkageMapper;
import com.zhiliao.mybatis.model.TCmsLinkage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:联动菜单service
 *
 * @author Jin
 * @create 2017-05-22
 **/
@Service
public class LinkageServiceImpl implements LinkageService{

    @Autowired
    private TCmsLinkageMapper linkageMapper;

    @Override
    public String save(TCmsLinkage pojo) {
        if (linkageMapper.insertSelective(pojo)>0)
            return JsonUtil.toSUCCESS("操作成功","linkage-tab",true);
        return  JsonUtil.toERROR("操作失败");
    }

    @Override
    public String update(TCmsLinkage pojo) {
        if (linkageMapper.updateByPrimaryKeySelective(pojo)>0)
            return JsonUtil.toSUCCESS("操作成功","linkage-tab",false);
        return  JsonUtil.toERROR("操作失败");
    }

    @Override
    public String delete(Integer[] ids) {
        if(ids!=null&&ids.length>0)
            for(Integer id :ids)
                linkageMapper.deleteByPrimaryKey(id);
        return JsonUtil.toSUCCESS("删除成功","linkage-tab",false);
    }

    @Override
    public TCmsLinkage findById(Integer id) {
        return linkageMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<TCmsLinkage> findList(TCmsLinkage pojo) {
        return linkageMapper.select(pojo);
    }

    @Override
    public List<TCmsLinkage> findAll() {
        return linkageMapper.selectAll();
    }

    @Override
    public PageInfo<TCmsLinkage> page(Integer pageNumber, Integer pageSize, TCmsLinkage pojo) {
        PageHelper.startPage(pageNumber,pageSize);
        return new PageInfo<>(findList(pojo));
    }

    @Override
    public PageInfo<TCmsLinkage> page(Integer pageNumber, Integer pageSize) {
        PageHelper.startPage(pageNumber,pageSize);
        return new PageInfo<>(findAll());
    }
}
