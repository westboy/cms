package com.zhiliao.component.beetl.fun;

import com.zhiliao.module.web.system.service.OrganizationService;
import com.zhiliao.mybatis.model.TSysOrg;
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
public class OrganizationFunction implements Function {

    @Autowired
    private OrganizationService service;

    private final String isChecked = " data-checked='true' ";

    @Override
    public Object call(Object[] objects, Context context) {

        /*父节点Id*/
        Integer pid = (Integer) objects[0];
         /*角色Id*/
        Integer userId = (Integer) objects[1];

        List<TSysOrg> orgList = service.findByPid(pid);
        TSysOrg tso=service.findById(pid);
        if(orgList!=null&&orgList.size()>0) {
            StringBuffer sbf = new StringBuffer();
            sbf.append("<li data-id='"+tso.getId()+"' data-pid='"+tso.getPid()+"'"+isChecked(pid,userId)+">"+tso.getName()+"</li>");
            for (TSysOrg o : orgList) {
                sbf.append("<li data-id='"+o.getId()+"' data-pid='"+o.getPid()+"' "+isChecked(o.getId(),userId)+">"+o.getName()+"</li>");
                sbf.append(subPermission(o.getId(),userId));
            }
            return sbf.toString();
        }else{
            return "<li data-id='"+tso.getId()+"' data-pid='"+tso.getPid()+"'"+isChecked(pid,userId)+">"+tso.getName()+"</li>";
        }

    }

    /**
     * 递归查询子节点
     * @param pid
     * @param userId
     * @return
     */
    public String subPermission(Integer pid,Integer userId){
        StringBuffer sbf = new StringBuffer();
        List<TSysOrg> orgList = service.findByPid(pid);
        for (TSysOrg o : orgList) {
            sbf.append("<li data-id='"+o.getId()+"' data-pid='"+o.getPid()+"' "+isChecked(o.getId(),userId)+">"+o.getName()+"</li>");
            sbf.append(subPermission(o.getId(),userId));
        }

        return sbf.toString();
    }


    public String isChecked(Integer orgId,Integer userId){
        if(service.findCountByOrgIdAndUserId(orgId,userId)>0)
            return isChecked;
        return "";
    }
}