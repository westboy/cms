package com.zhiliao.component.beetl.fun;

import com.zhiliao.common.utils.ControllerUtil;
import com.zhiliao.common.utils.StrUtil;
import com.zhiliao.module.web.cms.service.CategoryService;
import com.zhiliao.mybatis.model.TCmsCategory;
import org.beetl.core.Context;
import org.beetl.core.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:content页面输出分类栏目
 *
 * @author Jin
 * @create 2017-04-15
 **/
@Service
public class ContentTreeCategoryFunction implements Function{

    @Autowired
    private CategoryService service;

    @Value("${system.http.protocol}")
    private String httpProtocol;

    private String url = "/system/cms/category/input?id";

    @Override
    public Object call(Object[] objects, Context context) {

        Long pid = Long.parseLong(objects[0].toString());
        Integer siteId = Integer.parseInt(objects[1].toString());
        if(objects[2]!=null&&!StrUtil.isBlank(objects[2].toString()))
            this.url=objects[2].toString();
        return recursion(pid,siteId);
    }

    /* 递归函数 */
    private String recursion(Long pid,Integer siteId){
       StringBuffer sbf = new StringBuffer();
       List<TCmsCategory> cats  = service.findCategoryListByPid(pid,siteId);
       if(cats!=null&&cats.size()>0){
           for(TCmsCategory cat:cats){
               int childSize = service.findCategoryListByPid(cat.getCategoryId(),siteId).size();
               if((cat.getAlone()||!StrUtil.isBlank(cat.getUrl()))&&childSize==0)
                   continue;
               if(childSize>0) {
                   sbf.append("  <li data-id=\"" + cat.getCategoryId() + "\" data-pid=\"" + pid + "\" >" + cat.getCategoryName() + " [" + cat.getCategoryId() + "] </li>");
               }else {
                   sbf.append("  <li data-id=\"" + cat.getCategoryId() + "\" data-pid=\"" + pid + "\" data-url=\"" + httpProtocol+"://"+ ControllerUtil.getDomain()+url + "=" + cat.getCategoryId() + "\" data-divid=\"#layout-content\">" + cat.getCategoryName() + " [" + cat.getCategoryId() + "] </li>");
               }
               sbf.append(recursion(cat.getCategoryId(),siteId));
           }
           return  sbf.toString();
       }
       return "";
    }
}
