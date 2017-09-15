package com.zhiliao.module.web.cms.service.impl;

import com.github.pagehelper.PageInfo;
import com.zhiliao.component.lucene.LuceneManager;
import com.zhiliao.component.lucene.util.IndexObject;
import com.zhiliao.module.web.cms.service.LuceneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Description:luncene
 *
 * @author Jin
 * @create 2017-06-06
 **/
@Service
public class LuceneServiceImpl implements LuceneService{

    @Autowired
    private LuceneManager luceneManager;

    @Async
    @Override
    public void save(IndexObject indexObject) {
        luceneManager.create(indexObject);
    }

    @Async
    @Override
    public void update(IndexObject indexObject) {
        luceneManager.update(indexObject);
    }

    @Override
    public void delete(IndexObject indexObject) {
        luceneManager.deleteAll();
    }

    @Override
    public PageInfo page(Integer pageNumber, Integer pageSize, String keyword) {
        return luceneManager.page(pageNumber,pageSize,keyword);
    }
}
