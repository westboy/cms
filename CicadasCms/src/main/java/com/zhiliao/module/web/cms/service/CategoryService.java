package com.zhiliao.module.web.cms.service;

import com.zhiliao.common.base.BaseService;
import com.zhiliao.mybatis.model.TCmsCategory;

import java.util.List;

/**
 * Created by binary on 2017/4/18.
 */
public interface CategoryService extends BaseService<TCmsCategory,Long>{

    List<TCmsCategory> findCategoryListByPid(Long pid);

    List<TCmsCategory> findCategoryListBySiteId(Integer siteId);

    List<TCmsCategory> findCategoryListByPid(Long pid,Integer siteId);

    List<TCmsCategory> findCategoryListByPidAndIsNav(Long pid,Integer siteId,Boolean isNav);

    TCmsCategory findByAlias(String alias);

    Integer findPageSize(Long categoryId);

    TCmsCategory findfindByAliasAndSiteId(String alias,Integer siteId);

    Integer AllCount();
}
