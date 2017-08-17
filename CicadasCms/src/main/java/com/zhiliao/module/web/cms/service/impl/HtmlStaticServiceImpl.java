package com.zhiliao.module.web.cms.service.impl;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.zhiliao.common.constant.CmsConst;
import com.zhiliao.common.utils.StrUtil;
import com.zhiliao.component.beetl.html.BeetlHtmlUtil;
import com.zhiliao.module.web.cms.service.*;
import com.zhiliao.mybatis.model.TCmsCategory;
import com.zhiliao.mybatis.model.TCmsModel;
import com.zhiliao.mybatis.model.TCmsSite;
import com.zhiliao.mybatis.model.TCmsTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.Map;


@Service
public class HtmlStaticServiceImpl implements HtmlStaticService{

    @Autowired
    private SiteService siteService;

    @Autowired
    private BeetlHtmlUtil beetlHtmlUtil;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ContentService contentService;

    @Autowired
    private ModelService modelService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private TopicService topicService;

    /**
     * 生成首页
     * @param siteId
     */
    @Override
    public void index(Integer siteId) {
        TCmsSite site = siteService.findById(siteId);
        Map attr = Maps.newHashMap();
        attr.put("title",site.getTitle());
        attr.put("keyword",site.getKeyword());
        attr.put("description",site.getDescription());
        attr.put("site",site);
        beetlHtmlUtil.create(request,siteId,"index",attr,(StrUtil.isBlank(site.getTemplate())?"default":site.getTemplate()),CmsConst.INDEX_TPL);

    }

    /**
     * 生成栏目页
     * @param siteId
     * @param categoryId
     * @param pageNumber
     * @param isPageList
     */

    @Override
    public void category(Integer siteId,Long categoryId,Integer pageNumber,boolean isPageList) {
        TCmsSite site = siteService.findById(siteId);
        TCmsCategory category = categoryService.findById(categoryId);
        PageInfo page = contentService.page(pageNumber==null?1:pageNumber, siteId, categoryId);
        Map attr = Maps.newHashMap();
        attr.put("title", category.getCategoryName());
        attr.put("keyword", site.getKeyword());
        attr.put("description", site.getDescription());
        attr.put("site", site);
        attr.put("category", category);
        attr.put("page", page);
        if (!isPageList) {
            beetlHtmlUtil.create(request, siteId, categoryId.toString(), attr, (StrUtil.isBlank(site.getTemplate()) ? "default" : site.getTemplate()), StrUtil.isBlank(category.getIndexTpl()) ? CmsConst.CATEGORY_INDEX_TPL : category.getIndexTpl());
        }else {
            beetlHtmlUtil.create(request, siteId, categoryId.toString() + File.separator+"index_" + pageNumber.toString(), attr, (StrUtil.isBlank(site.getTemplate()) ? "default" : site.getTemplate()), StrUtil.isBlank(category.getListTpl()) ? CmsConst.CATEGORY_LIST_TPL : category.getListTpl());
        }
    }

    /**
     * 生成内容页
     * @param siteId
     * @param categoryId
     * @param contentId
     */

    @Override
    public void content(Integer siteId,Long categoryId,Long contentId) {
        TCmsSite site = siteService.findById(siteId);
        TCmsCategory category = categoryService.findById(categoryId);
        TCmsModel contentModel = modelService.findById(category.getModelId());
        Map content = contentService.findContentByContentIdAndTableName(contentId,contentModel.getTableName());
        contentService.viewUpdate(contentId);
        Map attr = Maps.newHashMap();
        attr.put("title",content.get("title"));
        attr.put("keyword",content.get("keywords"));
        attr.put("description",content.get("description"));
        attr.put("site",site);
        attr.put("category",category);
        attr.put("content",content);
        beetlHtmlUtil.create(request,siteId, categoryId.toString()+File.separator+contentId.toString(), attr, (StrUtil.isBlank(site.getTemplate()) ? "default" : site.getTemplate()), StrUtil.isBlank(category.getContentTpl()) ? CmsConst.CATEGORY_LIST_TPL: category.getContentTpl());
    }

    @Override
    public void topic(Integer siteId) {
        TCmsSite site = siteService.findById(siteId);
        List<TCmsTopic> topicList = topicService.findAll();
        for(TCmsTopic topic :topicList) {
            Map attr = Maps.newHashMap();
            attr.put("title", topic.getTopicName());
            attr.put("keyword", topic.getKeywords());
            attr.put("description", topic.getDescription());
            attr.put("site", site);
            attr.put("topic", topic);
            beetlHtmlUtil.create(request,siteId,"topic"+File.separator+topic.getTopicId(),attr,(StrUtil.isBlank(site.getTemplate()) ? "default" : site.getTemplate()), !StrUtil.isBlank(topic.getTopicTpl()) ? topic.getTopicTpl() : CmsConst.TOPIC_TPL);
        }
    }


}
