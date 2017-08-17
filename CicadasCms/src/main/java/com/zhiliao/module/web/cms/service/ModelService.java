package com.zhiliao.module.web.cms.service;

import com.zhiliao.common.base.BaseService;
import com.zhiliao.mybatis.model.TCmsModel;

import java.util.List;

/**
 * Created by binary on 2017/5/12.
 */
public interface ModelService extends BaseService<TCmsModel,Integer>{

    TCmsModel findModelByModelName(String modelName);

    TCmsModel findModelByTableName(String tableName);

    List<TCmsModel> findModelListByStatusAndSiteId(boolean status,Integer siteId);

    List<TCmsModel> findModelListByStatus(boolean status);

}
