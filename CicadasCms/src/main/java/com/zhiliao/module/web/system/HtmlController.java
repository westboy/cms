package com.zhiliao.module.web.system;

import com.github.pagehelper.PageInfo;
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
@RequestMapping("/hs")
public class HtmlController {

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
        return "no";
    }


    private void generateCategory(Integer siteId){
        List<TCmsCategory> categoryList = categoryService.findCategoryListBySiteId(siteId);
        for (TCmsCategory category: categoryList) {
            htmlStaticService.category(siteId,category.getCategoryId(),1,false);
            this.generateCategoryPage(siteId,category.getCategoryId(),category.getPageSize());
            this.generateContent(siteId,category.getCategoryId());
        }
    }

    private void generateCategoryPage(Integer siteId,Long categoryId,Integer pageSize){
        TCmsCategory bean = new TCmsCategory();
        bean.setCategoryId(categoryId);
        PageInfo<TCmsCategory> page = categoryService.page(1,pageSize,bean);
        for(TCmsCategory category :page.getList()) {
            for (int i = 1; i <= page.getPages(); i++) {
                htmlStaticService.category(siteId, category.getCategoryId(), i, true);
            }
        }

    }

    private void generateContent(Integer siteId,Long categoryId){
        List<TCmsContent> contentList = contentService.findByCategoryId(categoryId);
        for(TCmsContent content:contentList){
            htmlStaticService.content(siteId,categoryId,content.getContentId());
        }

    }

}
