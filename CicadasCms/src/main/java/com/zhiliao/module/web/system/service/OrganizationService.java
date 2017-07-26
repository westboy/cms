package com.zhiliao.module.web.system.service;

import com.zhiliao.mybatis.model.master.TSysOrg;

import java.util.List;

/**
 * Description:部门service
 *
 * @author Jin
 * @create 2017-07-26
 **/
public interface OrganizationService {

    List<TSysOrg> findByPid(Long pid);

    TSysOrg findById(Long id);

    String delete(Long id);

    String save(TSysOrg pojo);

    String update(TSysOrg pojo);

}
