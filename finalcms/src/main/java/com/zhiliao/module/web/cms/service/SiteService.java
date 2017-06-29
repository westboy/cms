package com.zhiliao.module.web.cms.service;


import com.zhiliao.common.base.BaseService;
import com.zhiliao.module.web.cms.vo.TCmsSiteVo;
import com.zhiliao.module.web.system.vo.UserVo;
import com.zhiliao.mybatis.model.master.TCmsSite;

/**
 * Created by binary on 2017/5/15.
 */
public interface SiteService extends BaseService<TCmsSite,Integer> {

    @Deprecated
    String save(TCmsSite pojo);

    @Deprecated
    String update(TCmsSite pojo);

    String save(TCmsSiteVo pojo);

    String update(TCmsSiteVo pojo);

    TCmsSiteVo findVoById(Integer id);

    String change(UserVo userVo, Integer siteId);

    TCmsSite findByDomain(String domain);
}
