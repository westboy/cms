package com.zhiliao.module.web.system;

import com.zhiliao.common.utils.JsonUtil;
import com.zhiliao.common.utils.StrUtil;
import com.zhiliao.module.web.cms.service.CategoryService;
import com.zhiliao.module.web.cms.service.ContentService;
import com.zhiliao.module.web.cms.service.HtmlStaticService;
import com.zhiliao.module.web.cms.service.SiteService;
import com.zhiliao.mybatis.model.TCmsCategory;
import com.zhiliao.mybatis.model.TCmsContent;
import com.zhiliao.mybatis.model.TCmsSite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String index() {
       return "system/site_static";
    }

    @RequestMapping("/all")
    @ResponseBody
    public String all() {
        List<TCmsSite> siteList = siteService.findAll();
        for(TCmsSite site : siteList) {
            this. htmlStaticService.index(site.getSiteId());
            this.generateCategory(site.getSiteId());
            this. htmlStaticService.topic(site.getSiteId());
        }
        return JsonUtil.toSUCCESS("全站静态页面生成成功！");
    }

    @RequestMapping("/siteStatic/{siteId}")
    @ResponseBody
    public String siteStatic(@PathVariable("siteId") Integer siteId) {
        this.htmlStaticService.index(siteId);
        this.generateCategory(siteId);
        this. htmlStaticService.topic(siteId);
        return JsonUtil.toSUCCESS("静态页面正在生成....");
    }

    @RequestMapping("/siteIndexStatic/{siteId}")
    @ResponseBody
    public String siteIndexStatic(@PathVariable("siteId") Integer siteId) {
        htmlStaticService.index(siteId);
        return JsonUtil.toSUCCESS("首页静态页面生成成功！");
    }

    @RequestMapping("/siteCategoryStatic")
    @ResponseBody
    public String siteCategoryStatic(@RequestParam("siteId") Integer siteId,@RequestParam("categoryIds") String categoryIds) {
        if(StrUtil.isBlank(categoryIds))  return JsonUtil.toERROR("请选择需要生成的栏目！");
        this.generateCategory(siteId);
        for(String id : categoryIds.split(",")){
            this.generateCategoryPage(siteId,Long.parseLong(id));
        }
        return JsonUtil.toSUCCESS("静态页面正在生成....");
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
