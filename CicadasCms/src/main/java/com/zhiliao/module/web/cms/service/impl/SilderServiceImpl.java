package com.zhiliao.module.web.cms.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhiliao.common.utils.JsonUtil;
import com.zhiliao.module.web.cms.service.SilderService;
import com.zhiliao.mybatis.mapper.TCmsAdSilderMapper;
import com.zhiliao.mybatis.model.TCmsAdSilder;
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
public class SilderServiceImpl implements SilderService {

    @Autowired
    private TCmsAdSilderMapper silderMapper;

    @Override
    public String save(TCmsAdSilder pojo) {
        if (silderMapper.insertSelective(pojo)>0)
            return JsonUtil.toSUCCESS("操作成功","friendlink-tab",true);
        return  JsonUtil.toERROR("操作失败");
    }

    @Override
    public String update(TCmsAdSilder pojo) {
        if (silderMapper.updateByPrimaryKeySelective(pojo)>0)
            return JsonUtil.toSUCCESS("操作成功","silder-tab",true);
        return  JsonUtil.toERROR("操作失败");
    }

    @Override
    public String delete(Integer[] ids) {
        if(ids!=null&&ids.length>0)
            for(Integer id :ids)
                silderMapper.deleteByPrimaryKey(id);
        return JsonUtil.toSUCCESS("删除成功","silder-tab",false);
    }

    @Override
    public TCmsAdSilder findById(Integer id) {
        return silderMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<TCmsAdSilder> findList(TCmsAdSilder pojo) {
        return silderMapper.select(pojo);
    }

    @Override
    public List<TCmsAdSilder> findAll() {
        return silderMapper.selectAll();
    }

    @Override
    public PageInfo<TCmsAdSilder> page(Integer pageNumber, Integer pageSize, TCmsAdSilder pojo) {
        PageHelper.startPage(pageNumber,pageSize);
        return new PageInfo<>(findList(pojo));
    }

    @Override
    public PageInfo<TCmsAdSilder> page(Integer pageNumber, Integer pageSize) {
        PageHelper.startPage(pageNumber,pageSize);
        return new PageInfo<>(findAll());
    }
}
