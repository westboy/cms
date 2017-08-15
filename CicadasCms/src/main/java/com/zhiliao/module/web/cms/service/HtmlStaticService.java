package com.zhiliao.module.web.cms.service;

public interface HtmlStaticService {

    void index(Integer siteId);

    void category(Integer siteId,Long categoryId,Integer pageNumber,boolean isPageList);

    void content(Integer siteId,Long categoryId,Long contentId);

    void topic(Integer siteId);

}
