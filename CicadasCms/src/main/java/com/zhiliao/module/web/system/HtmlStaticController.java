package com.zhiliao.module.web.system;

import com.zhiliao.common.utils.JsonUtil;
import com.zhiliao.module.web.cms.service.CategoryService;
import com.zhiliao.module.web.cms.service.ContentService;
import com.zhiliao.module.web.cms.service.HtmlStaticService;
import com.zhiliao.module.web.cms.service.SiteService;
import com.zhiliao.mybatis.model.master.TCmsCategory;
import com.zhiliao.mybatis.model.master.TCmsContent;
import com.zhiliao.mybatis.model.master.TCmsSite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Description:
 *
 * @author Jin
 * @create 2017-07-22
 **/
@Controller
@RequestMapping("/system/toStaticHtml")
public class HtmlStaticController {

    @Autowired
    private SiteService siteService;

    @Autowired
    private HtmlStaticService htmlStaticService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ContentService contentService;



    @RequestMapping
    @ResponseBody
    public String all() {
        List<TCmsSite> siteList = siteService.findAll();
        for(TCmsSite site : siteList) {
            htmlStaticService.index(site.getSiteId());
            this.generateCategory(site.getSiteId());
        }
        return JsonUtil.toSUCCESS("全站静态页面生成成功！");
    }


    private void generateCategory(Integer siteId){
        List<TCmsCategory> categoryList = categoryService.findCategoryListBySiteId(siteId);
        for (TCmsCategory category: categoryList) {
            htmlStaticService.category(siteId,category.getCategoryId(),1,false);
            this.generateCategoryPage(siteId,category.getCategoryId());
            this.generateContent(siteId,category.getCategoryId());
        }
    }

    private void generateCategoryPage(Integer siteId,Long categoryId){
            int pages = contentService.page(1, siteId, categoryId).getPages();
            for (int pageNumber = 1; pageNumber <= pages; pageNumber++) {
                htmlStaticService.category(siteId, categoryId, pageNumber, true);
            }


    }

    private void generateContent(Integer siteId,Long categoryId){
        List<TCmsContent> contentList = contentService.findByCategoryId(categoryId);
        for(TCmsContent content:contentList){
            htmlStaticService.content(siteId,categoryId,content.getContentId());
        }

    }

}
