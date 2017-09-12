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
public class SelectCategoryFunction implements Function{

    @Autowired
    private CategoryService service;

    private final String head = "<option value=\"0\" >顶级节点</option>";

    private final String isSelected = "selected=\"selected\"";

    @Override
    public Object call(Object[] objects, Context context) {

        Long pid = Long.parseLong(objects[0].toString());
        Long currentId =Long.parseLong(objects[1].toString());
        Integer siteId =Integer.parseInt(objects[2].toString());
        if(pid!=null)
            return head+recursion(currentId,pid,0l,"",siteId);
        return head+recursion(currentId,0l,0l,"",siteId);
    }



    /*递归输出子节点*/
    private String recursion(Long cid,Long pid,Long sPid,String tag,Integer siteId){
         /*临时拼凑看不懂就放弃*/
        tag+=(StrUtil.isBlank(tag)?"&nbsp;&nbsp;":"&nbsp;&nbsp;&nbsp;&nbsp;");
        StringBuffer sbf = new StringBuffer();
        List<TCmsCategory> ctas  = service.findCategoryListByPid(sPid,siteId);
        if(ctas!=null&&ctas.size()>0){
            for(TCmsCategory cat:ctas){
                   /*如果是自己就不输出了*/
                   if(cid!=cat.getCategoryId()&&cid!=cat.getParentId()||cid.longValue()==0)
                        sbf.append("<option value=\"" + cat.getCategoryId() + "\" " + isSelected(cat.getCategoryId(), pid) + ">" + tag + "|—" + cat.getCategoryName()+ "</option>");
                    sbf.append(recursion(cid,pid,cat.getCategoryId(),tag,siteId));
            }
            return  sbf.toString();
        }
        return "";
    }

    private String isSelected(Long id,Long perId){
        if(id.longValue()==perId.longValue())
            return isSelected;
        return "";
    }

}
