package com.zhiliao.module.web.cms.service;

import com.github.pagehelper.PageInfo;
import com.zhiliao.component.lucene.util.IndexObject;

public interface LuceneService {

    void save(IndexObject indexObject);

    void update(IndexObject indexObject);

    void delete(IndexObject indexObject);

    PageInfo page(Integer pageNumber, Integer pageSize,String keyword);
}
