package com.zhiliao.component.beetl.fun;

import com.zhiliao.module.web.system.service.RoleService;
import com.zhiliao.mybatis.model.TSysPermission;
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
public class PermissionFunction implements Function {

    @Autowired
    private RoleService service;

    private final String isChecked = " data-checked='true' ";

    @Override
    public Object call(Object[] objects, Context context) {

        /*父节点Id*/
        Integer pid = (Integer) objects[0];
         /*角色Id*/
        Integer roleId = (Integer) objects[1];

        List<TSysPermission> permissions = service.findPermissonByPid(pid);
        TSysPermission tsp=service.findPermissonByid(pid);
        if(permissions!=null&&permissions.size()>0) {
            StringBuffer sbf = new StringBuffer();
            sbf.append("<li data-id='"+tsp.getPermissionId()+"' data-pid='"+tsp.getPid()+"'"+isChecked(pid,roleId)+">"+tsp.getDescription()+"</li>");
            for (TSysPermission p : permissions) {
                sbf.append("<li data-id='"+p.getPermissionId()+"' data-pid='"+p.getPid()+"' "+isChecked(p.getPermissionId(),roleId)+">"+p.getDescription()+"</li>");
                sbf.append(subPermission(p.getPermissionId(),roleId));
            }
            return sbf.toString();
        }else{
            return "<li data-id='"+tsp.getPermissionId()+"' data-pid='"+tsp.getPid()+"'"+isChecked(pid,roleId)+">"+tsp.getDescription()+"</li>";
        }

    }

    /**
     * 递归查询子节点
     * @param pid
     * @param roleId
     * @return
     */
    public String subPermission(Integer pid,Integer roleId){
        StringBuffer sbf = new StringBuffer();
        List<TSysPermission> permissions = service.findPermissonByPid(pid);
        for (TSysPermission p : permissions) {
            sbf.append("<li data-id='"+p.getPermissionId()+"' data-pid='"+p.getPid()+"' "+isChecked(p.getPermissionId(),roleId)+">"+p.getDescription()+"</li>");
            sbf.append(subPermission(p.getPermissionId(),roleId));
        }

        return sbf.toString();
    }

    /**
     * 判断是否用于权限
     * @param permissionId
     * @param roleId
     * @return
     */
    public String isChecked(Integer permissionId,Integer roleId){
        if(service.findPermissionCountByRoleId(roleId,permissionId)>0)
            return isChecked;
        return "";
    }
}