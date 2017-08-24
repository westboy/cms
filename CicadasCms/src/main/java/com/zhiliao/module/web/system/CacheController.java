package com.zhiliao.module.web.system;

import com.zhiliao.common.exception.SystemException;
import com.zhiliao.common.utils.JsonUtil;
import com.zhiliao.common.utils.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Description:缓存管理控制器
 *
 * @author Jin
 * @create 2017-06-12
 **/
@Controller
@RequestMapping("/system/cache")
public class CacheController {

    @Autowired
    private EhCacheCacheManager springEhCacheManager;

    private String[] caches={"cms-site-cache","cms-category-cache","shiro-kickout-cache","cms-content-cache","cms-model-cache","cms-topic-cache"};

    @RequestMapping
    public String index(Model model){
        model.addAttribute("caches",caches);
        return "system/cache";
    }

    @RequestMapping("/clear/{cacheNames}")
    @ResponseBody
    public  String clear(@PathVariable("cacheNames") String cacheNames){
        if(StrUtil.isBlank(cacheNames))
            throw new SystemException("缓存名称不能为空！");
        Cache cache = springEhCacheManager.getCache(cacheNames);
        cache.clear();
        return JsonUtil.toSUCCESS("清理成功！");
    }

    @RequestMapping("/clear/all")
    @ResponseBody
    public  String clear(){
        for(String cacheNames :caches) {
            Cache cache = springEhCacheManager.getCache(cacheNames);
            cache.clear();
        }
        return JsonUtil.toSUCCESS("清理成功！");
    }



}
