package com.zhiliao.component.beetl.fun;

import com.zhiliao.common.utils.StrUtil;
import com.zhiliao.mybatis.model.TSysPermission;
import com.zhiliao.module.web.system.service.RoleService;
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
public class SelectPermissionFunction implements Function{

    @Autowired
    private RoleService service;

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
    private String recursion(int cid,int pid,int spid,String tag){

        tag+=(StrUtil.isBlank(tag)?"&nbsp;&nbsp;":"&nbsp;&nbsp;&nbsp;&nbsp;");
        StringBuffer sbf = new StringBuffer();
        List<TSysPermission> permissions  = service.findPermissonByPid(spid);
        if(permissions!=null&&permissions.size()>0){
            int flag_ = 0;
            for(TSysPermission per:permissions){
                   /**如果是自己就不输出了**/
                if(cid!=per.getPermissionId()&&cid!=per.getPid()||cid==0) {
                    flag_ = per.getPermissionId();
                    sbf.append("<option value=\"" + per.getPermissionId() + "\" " + isSelected(per.getPermissionId(), pid) + ">" + tag + "|—" + per.getDescription() + "</option>");
                }
                if(flag_!=0)sbf.append(recursion(cid,pid,per.getPermissionId(),tag));
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
