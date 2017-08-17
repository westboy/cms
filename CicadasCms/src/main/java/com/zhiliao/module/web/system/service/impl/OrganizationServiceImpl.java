package com.zhiliao.module.web.system.service.impl;

import com.zhiliao.common.utils.JsonUtil;
import com.zhiliao.module.web.system.service.OrganizationService;
import com.zhiliao.mybatis.mapper.TSysOrgMapper;
import com.zhiliao.mybatis.mapper.TSysOrgUserMapper;
import com.zhiliao.mybatis.model.TSysOrg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 *
 * @author Jin
 * @create 2017-07-26
 **/
@Service
public class OrganizationServiceImpl implements OrganizationService{

    @Autowired
    private TSysOrgMapper orgMapper;

    @Autowired
    private TSysOrgUserMapper orgUserMapper;

    @Override
    public List<TSysOrg> findByPid(Integer pid) {
        TSysOrg pojo = new TSysOrg();
        pojo.setPid(pid);
        return orgMapper.select(pojo);
    }

    @Override
    public TSysOrg findById(Integer id) {
        return orgMapper.selectByPrimaryKey(id);
    }

    @Override
    public String delete(Integer id) {
        if(orgMapper.deleteByPrimaryKey(id)>0)
            return JsonUtil.toSUCCESS("删除成功！","org-tab",false);
        return JsonUtil.toERROR("删除失败！");
    }

    @Override
    public String save(TSysOrg pojo) {
         if(orgMapper.insertSelective(pojo)>0)
            return JsonUtil.toSUCCESS("添加成功！","org-tab",false);
        return JsonUtil.toERROR("添加成功！");
    }

    @Override
    public String update(TSysOrg pojo) {
        if(orgMapper.updateByPrimaryKeySelective(pojo)>0)
            return JsonUtil.toSUCCESS("更新成功！","org-tab",false);
        return JsonUtil.toERROR("更新成功！");
    }

    @Override
    public int findCountByOrgIdAndUserId(Integer orgId, Integer userId) {
        return orgUserMapper.selectCountByOrgIdAndUserId(orgId,userId);
    }
}
