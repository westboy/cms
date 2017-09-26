package com.zhiliao.module.web.system;

import com.zhiliao.mybatis.model.TSysRole;
import com.zhiliao.module.web.system.service.RoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Description:角色控制器
 *
 * @author Jin
 * @create 2017-04-14
 **/
@Controller
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    @RequiresPermissions({"role:admin"})
    @RequestMapping
    public String index(Model model){
        model.addAttribute("roles",roleService.findAll());
        return "system/role";
    }


    @RequiresPermissions({"role:input"})
    @RequestMapping("/input")
    public String input(@RequestParam(value = "roleId",required = false) Integer roleId,
                        @RequestParam(value = "roleType",required = false) Integer roleType, Model model){
          if(roleId!=null){
              model.addAttribute("role",roleService.findByid(roleId));
          }
          model.addAttribute("roleType",roleType);
          model.addAttribute("permissions",roleService.findPermissonByPid(0));
          return "system/role_input";
    }

    @RequiresPermissions({"role:save"})
    @RequestMapping("/save")
    @ResponseBody
    public String input(TSysRole role,@RequestParam(value = "permissionId",required = false) Integer[] permissionId) {
        if(role.getRoleId()!=null)
            return roleService.update(role,permissionId);
        return roleService.save(role,permissionId);
    }

    @RequiresPermissions({"role:delete"})
    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam("roleId") Integer roleId){
       return roleService.delete(roleId);
    }

}
