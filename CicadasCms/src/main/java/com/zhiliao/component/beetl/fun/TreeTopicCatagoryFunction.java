package com.zhiliao.component.beetl.fun;

import com.zhiliao.common.utils.StrUtil;
import com.zhiliao.module.web.cms.service.CategoryService;
import com.zhiliao.mybatis.model.TCmsCategory;
import org.beetl.core.Context;
import org.beetl.core.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:权限tag
 * 这个函数需要调用时遍历使用
 * @author Jin
 * @create 2017-04-14
 **/
@Service
public class TreeTopicCatagoryFunction implements Function {

    @Autowired
    private CategoryService service;

    private final String isChecked = " data-checked='true' ";

    @Override
    public Object call(Object[] objects, Context context) {

        String siteId = objects[0].toString();
        String pid = (String) objects[1];
        String categoryIds = (String) objects[2];
        return recursion(Long.parseLong(pid),Integer.parseInt(siteId),categoryIds);

    }

    /* 递归函数 */
    private String recursion(Long pid,Integer siteId,String categoryIds){
        StringBuffer sbf = new StringBuffer();
        List<TCmsCategory> cats  = service.findCategoryListByPid(pid,siteId);
        if(cats!=null&&cats.size()>0){
            for(TCmsCategory cat:cats){
                sbf.append("<li data-id=\""+cat.getCategoryId()+"\" data-pid=\""+pid+"\" "+isChecked(cat.getCategoryId(),categoryIds)+" >"+cat.getCategoryName()+" </li>");
                sbf.append(recursion(cat.getCategoryId(),siteId,categoryIds));
            }
            return  sbf.toString();
        }
        return "";
    }


    public String isChecked(Long c1,String categoryIds){
        if(!StrUtil.isBlank(categoryIds)) {
            String[] array = categoryIds.split(",");
            for (String catId : array) {
                Long c2 = Long.parseLong(catId);
                if (c1.longValue() == c2.longValue())
                    return isChecked;
                continue;
            }
        }
        return "";
    }
}