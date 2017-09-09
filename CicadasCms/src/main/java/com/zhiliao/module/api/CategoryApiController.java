package com.zhiliao.module.api;

import com.zhiliao.common.annotation.ParamNotNull;
import com.zhiliao.common.constant.CmsConst;
import com.zhiliao.common.exception.ApiException;
import com.zhiliao.common.utils.CmsUtil;
import com.zhiliao.common.utils.JsonUtil;
import com.zhiliao.module.web.cms.service.CategoryService;
import com.zhiliao.module.web.cms.service.SiteService;
import com.zhiliao.mybatis.model.TCmsCategory;
import com.zhiliao.mybatis.model.TCmsSite;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description:内容栏目接口
 *
 * @author Jin
 * @create 2017-09-09
 **/
@RestController
@RequestMapping("/api/category")
public class CategoryApiController {

    @Autowired
    SiteService siteService;

    @Autowired
    CategoryService categoryService;

    @ApiOperation("栏目列表接口")
    @GetMapping(value = "/list")
    @ParamNotNull(parameter="siteId,categoryId")
    public String list(@RequestParam("siteId") Integer siteId,
                       @RequestParam("categoryId") Long categoryId,
                       @RequestParam(value = "isNav",defaultValue = "0") Boolean isNav
                       ){
        TCmsSite site = siteService.findById(siteId);
        if(CmsUtil.isNullOrEmpty(site))
            throw new ApiException("["+siteId+"]"+ CmsConst.SITE_NOT_FOUND);
        List<TCmsCategory> cats = categoryService.findCategoryListByPidAndIsNav(categoryId,siteId,isNav);
        if(CmsUtil.isNullOrEmpty(cats)) {
            TCmsCategory category = categoryService.findById(categoryId);
            if(CmsUtil.isNullOrEmpty(category))
                throw new ApiException("没有查询到数据！[siteId:" + siteId + ",categoryId:" + categoryId + "]");
            cats = categoryService.findCategoryListByPidAndIsNav(category.getParentId(),siteId,isNav);
        }

        return JsonUtil.toSuccessResultJSON("请求成功",cats);
    }

    @ApiOperation("栏目详情接口")
    @GetMapping("/{categoryId}")
    public String content(@PathVariable("categoryId") Long categoryId,
                          @RequestParam(value = "isParent",defaultValue = "0") Integer isParent){
        TCmsCategory category = categoryService.findById(categoryId);
        if(CmsUtil.isNullOrEmpty(category))
            throw new ApiException("栏目["+categoryId+"]不存在！");
        if(isParent==1&&category.getParentId()!=0)
            category = categoryService.findById(category.getParentId());
        return JsonUtil.toSuccessResultJSON("请求成功",category);
    }

}
