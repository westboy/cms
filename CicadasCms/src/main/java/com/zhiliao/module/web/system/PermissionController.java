package com.zhiliao.module.web.system;

import com.zhiliao.module.web.system.service.RoleService;
import com.zhiliao.mybatis.model.TSysPermission;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Description:权限Controller
 *
 * @author Jin
 * @create 2017-04-15
 **/
@Controller
@RequestMapping("/system/permission")
public class PermissionController {

    @Autowired
    private RoleService roleService;



    @RequiresPermissions("permission:admin")
    @RequestMapping
    public String index(){
        return "system/permission";
    }

    @RequiresPermissions("permission:input")
    @RequestMapping("/input/{id}")
    public String input(@PathVariable(value = "id",required = false) Integer perId,Model model){
        if(perId!=null){
            model.addAttribute("per",roleService.findPermissonByid(perId));
        }
        return "system/permission_input";
    }

    @RequiresPermissions("permission:save")
    @RequestMapping("/save")
    @ResponseBody
    public String save(TSysPermission permission){

        if(permission.getPermissionId()!=null)
            return roleService.update(permission);
        return roleService.save(permission);

    }

    @RequiresPermissions("permission:delete")
    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam("permissionId") Integer perId) {

        return roleService.delectPermissionById(perId);
    }
}
