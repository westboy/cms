package com.zhiliao.module.web.cms.service;

import com.zhiliao.common.base.BaseService;
import com.zhiliao.mybatis.model.TCmsModelFiled;

import java.util.List;

/**
 * Created by binary on 2017/5/12.
 */
public interface ModelFiledService extends BaseService<TCmsModelFiled,Integer> {
    TCmsModelFiled findModelFiledByFiledName(String filedName);

    List<TCmsModelFiled> findModelFiledListByModelId(Integer modelId);

}
