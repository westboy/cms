package com.zhiliao.component.beetl.fun;

import com.zhiliao.common.utils.StrUtil;
import com.zhiliao.module.web.cms.service.CategoryService;
import com.zhiliao.mybatis.model.TCmsCategory;
import org.beetl.core.Context;
import org.beetl.core.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentSelectCategoryFunction implements Function{

    @Autowired
    private CategoryService service;

    @Override
    public Object call(Object[] objects, Context context) {


        Integer siteId = Integer.parseInt(objects[0].toString()); /*站点Id*/
        Long currentId =Long.parseLong(objects[1].toString()); /*当前栏目Id*/
        return recursion(currentId,0l,"",siteId);
    }



    /*递归输出子节点*/
    private String recursion(Long cid,Long  pid,String tag,Integer siteId){
         /*临时拼凑看不懂就放弃*/
        tag+=(StrUtil.isBlank(tag)?"&nbsp;&nbsp;":"&nbsp;&nbsp;&nbsp;&nbsp;");
        StringBuffer sbf = new StringBuffer();
        List<TCmsCategory> ctas  = service.findCategoryListByPid(pid,siteId);
        if(ctas!=null&&ctas.size()>0){
            for(TCmsCategory cat:ctas){
                int childSize = service.findCategoryListByPid(cat.getCategoryId(),siteId).size();
                if((cat.getAlone()||!StrUtil.isBlank(cat.getUrl()))&&childSize==0)
                    continue;
                if(childSize>0) {
                    if(cat.getParentId().longValue()==0)
                        sbf.append("<optgroup label=\""+cat.getCategoryName() + "\">");
                    else
                        sbf.append("<optgroup label=\"|—"+cat.getCategoryName() + "\">");
                    sbf.append(recursion(cid,cat.getCategoryId(),tag,siteId));
                    sbf.append("</optgroup>");
                }else {
                    if (cid.longValue() == cat.getCategoryId().longValue())
                        sbf.append("<option value=\"" + cat.getCategoryId() + "\" selected='selected' >" + tag + "&nbsp;|—" + cat.getCategoryName() + "</option>");
                    else
                        sbf.append("<option value=\"" + cat.getCategoryId() + "\" >" + tag + "&nbsp;|—" + cat.getCategoryName() + "</option>");
                }
            }
            return  sbf.toString();
        }
        return "";
    }
}
