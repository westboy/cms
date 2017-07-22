package com.zhiliao.module.web.cms;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.zhiliao.common.dict.CmsConst;
import com.zhiliao.common.exception.CmsException;
import com.zhiliao.common.exception.SystemException;
import com.zhiliao.common.utils.CmsUtil;
import com.zhiliao.common.utils.ControllerUtil;
import com.zhiliao.common.utils.HtmlKit;
import com.zhiliao.common.utils.StrUtil;
import com.zhiliao.module.web.cms.service.*;
import com.zhiliao.module.web.cms.service.impl.HtmlStaticServiceImpl;
import com.zhiliao.mybatis.model.master.TCmsCategory;
import com.zhiliao.mybatis.model.master.TCmsModel;
import com.zhiliao.mybatis.model.master.TCmsModelFiled;
import com.zhiliao.mybatis.model.master.TCmsSite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Description:前台首页
 *
 * @author Jin
 * @create 2017-04-13
 **/
@Controller
@RequestMapping("/")
public class IndexController {

    private final static Logger log = LoggerFactory.getLogger(IndexController.class);
    @Value("${system.site.page.size}")
    private String pageSize;
    @Autowired
    private SiteService siteService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ContentService contentService;

    @Autowired
    private ModelService modelService;

    @Autowired
    private ModelFiledService modelFiledService;

    @Autowired
    private LuceneService luceneService;

    @Autowired
    private HtmlStaticServiceImpl htmlStaticService;

    @Value("${system.http.protocol}")
    private String httpProtocol;

    @Value("${system.site.subfix}")
    private String siteSubfix;

    /*网站首页*/
    @GetMapping
    public ModelAndView index(@RequestParam(value = "keyword",required = false) String keyword){
        String domain = ControllerUtil.getDomain();
        log.debug("网站首页[{}]",domain);
        TCmsSite site = siteService.findByDomain(domain);
        if(CmsUtil.isNullOrEmpty(site))
            throw new CmsException(CmsConst.SITE_NOT_FOUND);
        if(!StrUtil.isBlank(keyword))
            return new ModelAndView( "forward:/front/search");
        return new ModelAndView( "forward:/front/"+site.getSiteId());
    }


    /**
     * 站点首页
     * @param siteId
     * @param model
     * @return
     */
    @GetMapping("/front/{siteId}")
    public String index(@PathVariable("siteId") Integer siteId,
                        Model model){
        log.debug("首页");
        TCmsSite site = siteService.findById(siteId);
        if(CmsUtil.isNullOrEmpty(site))
            throw new CmsException(CmsConst.SITE_NOT_FOUND);
        if(!site.getStatus())
            throw new SystemException(CmsConst.SITE_LOCKED);
        model.addAttribute("title",site.getTitle());
        model.addAttribute("keyword",site.getKeyword());
        model.addAttribute("description",site.getDescription());
        model.addAttribute("site",site);
        if(StrUtil.isBlank(site.getTemplate()))
            return view(CmsConst.INDEX_TPL);
        return view(site.getTemplate(),CmsConst.INDEX_TPL);
    }


    @GetMapping("/front/{siteId}/{categoryId}")
    public String category(@PathVariable("siteId") Integer siteId,
                           @PathVariable("categoryId") Long categoryId,
                           Model model){
        log.debug("分类");
        TCmsSite site = siteService.findById(siteId);
        if(CmsUtil.isNullOrEmpty(site))
            throw new CmsException(CmsConst.SITE_NOT_FOUND);
        TCmsCategory category = categoryService.findById(categoryId);
        if(CmsUtil.isNullOrEmpty(category))
            throw new CmsException(CmsConst.CATEGORY_NOT_FOUND);
        PageInfo page = contentService.page(1,siteId,category.getCategoryId());
        model.addAttribute("title",category.getCategoryName());
        model.addAttribute("keyword",site.getKeyword());
        model.addAttribute("description",site.getDescription());
        model.addAttribute("site",site);
        model.addAttribute("category",category);
        model.addAttribute("page",page);
        if(StrUtil.isBlank(site.getTemplate()))
            return view(category.getIndexTpl());
        return view(site.getTemplate(),category.getIndexTpl());
    }

    /*网站栏目列表页*/
    @GetMapping("/front/{siteId}/{categoryId}/index_{pageNumber}")
    public String page(@PathVariable("siteId") Integer siteId,
                             @PathVariable("categoryId") Long categoryId,
                             @PathVariable(value = "pageNumber") Integer pageNumber,
                             Model model){
        log.debug("列表");
        TCmsSite site = siteService.findById(siteId);
        if(CmsUtil.isNullOrEmpty(site))
            throw new CmsException(CmsConst.SITE_NOT_FOUND);
        TCmsCategory category = categoryService.findById(categoryId);
        if(CmsUtil.isNullOrEmpty(category))
            throw new CmsException(CmsConst.CATEGORY_NOT_FOUND);
        if(CmsUtil.isNullOrEmpty(pageNumber))
            throw new CmsException(CmsConst.PAGE_NOT_FOUND);
        PageInfo page = contentService.page(pageNumber,siteId,category.getCategoryId());
        model.addAttribute("title",category.getCategoryName());
        model.addAttribute("keyword",site.getKeyword());
        model.addAttribute("description",site.getDescription());
        model.addAttribute("site",site);
        model.addAttribute("category",category);
        model.addAttribute("page",page);
        if(StrUtil.isBlank(site.getTemplate()))
            return view(category.getListTpl());
        return view(site.getTemplate(),category.getListTpl());
    }

    /*网站内容页*/
    @GetMapping("/front/{siteId}/{categoryId}/{contentId}")
    public String content(@PathVariable("siteId") Integer siteId,
                                @PathVariable("categoryId") Long categoryId,
                                @PathVariable("contentId") Long contentId,
                                 Model model){
        TCmsSite site = siteService.findById(siteId);
        if(CmsUtil.isNullOrEmpty(site))
            throw new CmsException(CmsConst.SITE_NOT_FOUND);
        TCmsCategory category = categoryService.findById(categoryId);
        if(CmsUtil.isNullOrEmpty(category))
            throw new CmsException(CmsConst.CATEGORY_NOT_FOUND);
        TCmsModel contentModel = modelService.findById(category.getModelId());
        if(CmsUtil.isNullOrEmpty(category))
            throw new CmsException(CmsConst.PAGE_NOT_FOUND);
        Map content = contentService.findContentByContentIdAndTableName(contentId,contentModel.getTableName());
        if(CmsUtil.isNullOrEmpty(content))
            throw new CmsException(CmsConst.CONTENT_NOT_FOUND);
        contentService.viewUpdate(contentId);
        model.addAttribute("title",content.get("title"));
        model.addAttribute("keyword",content.get("keywords"));
        model.addAttribute("description",content.get("description"));
        model.addAttribute("site",site);
        model.addAttribute("category",category);
        model.addAttribute("content",content);
        if(StrUtil.isBlank(site.getTemplate()))
            return this.view(category.getContentTpl());
        return this.view(site.getTemplate(),category.getContentTpl());
    }

    /*网站首页*/
    @RequestMapping("/front/search")
    public String search(@RequestParam(value = "keyword",required = false) String keyword,
                         @RequestParam(value = "m",defaultValue = "0") Integer modelId,
                         @RequestParam(value = "s",defaultValue = "0") Integer siteId,
                         @RequestParam(value = "c",defaultValue = "0") Long catId,
                         @RequestParam(value = "p",defaultValue = "1") Integer pageNumber,
                         HttpServletRequest request
                         ){
        log.debug("搜索");
        TCmsSite site = siteService.findById(siteId);
        if(CmsUtil.isNullOrEmpty(site))
            throw new CmsException(CmsConst.SITE_NOT_FOUND);
        if (modelId > 0 && catId > 0) {
            String action = httpProtocol + "://" + ControllerUtil.getDomain();
            action += "/front/search?m=" + modelId + "&c=" + catId;
            TCmsCategory category = categoryService.findById(catId);
            if(CmsUtil.isNullOrEmpty(category))
                throw new CmsException(CmsConst.CATEGORY_NOT_FOUND);
            TCmsModel model = modelService.findById(modelId);
            Map<String, Object> param = Maps.newHashMap();
            List<TCmsModelFiled> modelFileds = modelFiledService.findModelFiledListByModelId(modelId);
            /* 循环检索获取filedName和fildValue*/
            for (TCmsModelFiled filed : modelFileds) {
                String filedValue = request.getParameter(filed.getFiledName());
                if (!StrUtil.isBlank(filedValue)) {
                    param.put(HtmlKit.getText(filed.getFiledName()).trim(), HtmlKit.getText(filedValue).trim());
                    action += "&" + filed.getFiledName() + "=" + filedValue;
                }
            }
            PageInfo page = contentService.findContentListByModelFiledValue(pageNumber,catId,model.getTableName(), param);
            request.setAttribute("title",category.getCategoryName());
            request.setAttribute("keyword",site.getKeyword());
            request.setAttribute("description",site.getDescription());
            request.setAttribute("site", site);
            request.setAttribute("category", category);
            request.setAttribute("page", page);
            request.setAttribute("param", param);
            request.setAttribute("action", action);
            return view(site.getTemplate(), category.getListTpl());
        }else{
            String action = httpProtocol + "://" + ControllerUtil.getDomain();
            if(StrUtil.isBlank(keyword))
                throw new CmsException(CmsConst.SEARCH_KEYWORD_NOT_FOUND);
            action +="/front/search?keyword="+keyword;
            PageInfo page =luceneService.page(pageNumber,Integer.parseInt(this.pageSize),keyword);
            request.setAttribute("page",page);
            request.setAttribute("site",site);
            request.setAttribute("action", action);
            request.setAttribute("keyword", keyword);
            return view(site.getTemplate(), CmsConst.SEARCH_TPL);
        }
    }




    @RequestMapping("/h")
    @ResponseBody
    public String getPageSize() {
        htmlStaticService.index(1);
        return "123";
    }

    private String view(String viewName){
        return  "front/default/"+viewName.trim();
    }


    private String view(String theme,String viewName){
        return "front/"+theme.trim()+"/"+viewName.trim();
    }
    
}
