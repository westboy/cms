package com.zhiliao.component.beetl.fun;

import com.zhiliao.common.utils.ControllerUtil;
import com.zhiliao.mybatis.model.TSysPermission;
import com.zhiliao.module.web.system.service.RoleService;
import org.beetl.core.Context;
import org.beetl.core.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:输出子权限节点函数
 *
 * @author Jin
 * @create 2017-04-15
 **/
@Service
public class TreePermissionFunction implements Function{

    @Autowired
    private RoleService service;

    @Value("${system.http.protocol}")
    private String httpProtocol;

    @Override
    public Object call(Object[] objects, Context context) {

        Integer pid = (Integer) objects[0];
        return recursion(pid);
    }

    /* 递归函数 */
    private String recursion(int pid){
       StringBuffer sbf = new StringBuffer();
       List<TSysPermission> permissions  = service.findPermissonByPid(pid);
       if(permissions!=null&&permissions.size()>0){
           for(TSysPermission per:permissions){
               sbf.append("  <li data-id=\""+per.getPermissionId()+"\" data-pid=\""+pid+"\" data-url=\""+httpProtocol+"://"+ ControllerUtil.getDomain()+"/system/permission/input/"+per.getPermissionId()+"\" data-divid=\"#layout-1\">"+per.getDescription()+"</li>");
               sbf.append(recursion(per.getPermissionId()));
           }
           return  sbf.toString();
       }
       return "";
    }
}
