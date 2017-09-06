package com.zhiliao.component.beetl.fun;

import com.zhiliao.common.utils.StrUtil;
import com.zhiliao.module.web.system.service.OrganizationService;
import com.zhiliao.mybatis.model.TSysOrg;
import org.beetl.core.Context;
import org.beetl.core.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:select
 *
 * @author Jin
 * @create 2017-04-15
 **/
@Service
public class SelectOrganizationFunction implements Function{

    @Autowired
    private OrganizationService service;

    private final String head = "<option value=\"0\" >顶级节点</option>";

    private final String isSelected = "selected=\"selected\"";

    @Override
    public Object call(Object[] objects, Context context) {

        Integer pid = (Integer)objects[0];
        Integer currentId = (Integer)objects[1];
        if(pid!=null)
            return head+recursion(currentId,pid,0,"");
        return head+recursion(currentId,0,0,"");
    }



    /*递归输出子节点*/
    private String recursion(Integer cid,Integer pid,Integer spid,String tag){

        tag+=(StrUtil.isBlank(tag)?"&nbsp;&nbsp;":"&nbsp;&nbsp;&nbsp;&nbsp;");
        StringBuffer sbf = new StringBuffer();
        List<TSysOrg> orgList  = service.findByPid(spid);
        if(orgList!=null&&orgList.size()>0){
            int flag_ = 0;
            for(TSysOrg org:orgList){
                   /**如果是自己就不输出了**/
                if(cid.intValue()!=org.getId().intValue()&&cid.intValue()!=org.getPid().intValue()||cid.intValue()==0) {
                    flag_ = org.getId().intValue();
                    sbf.append("<option value=\"" + org.getId() + "\" " + isSelected(org.getId().intValue(), pid) + ">" + tag + "|—" + org.getName() + "</option>");
                }
                if(flag_!=0)sbf.append(recursion(cid,pid,org.getId(),tag));
            }
            return  sbf.toString();
        }
        return "";
    }

    private String isSelected(Integer id,Integer perId){
        if(id.intValue()==perId.intValue())
            return isSelected;
        return "";
    }

}
