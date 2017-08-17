package com.zhiliao.module.web.cms.service;

import com.github.pagehelper.PageInfo;
import com.zhiliao.mybatis.model.TCmsTag;

public interface TagService {

    String TagJsonList(String tagWord);

    boolean save(Long contentId,String tag);

    String delete(Integer [] ids);

    java.util.List<TCmsTag> tagList();

    PageInfo page(Integer pageNumber, Integer pageSize);

    Integer AddOrUpdateTagContent(Long contentId, Integer tagId);
}