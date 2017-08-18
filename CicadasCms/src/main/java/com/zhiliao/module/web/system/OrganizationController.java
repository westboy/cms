package com.zhiliao.module.web.system;

import com.zhiliao.module.web.system.service.OrganizationService;
import com.zhiliao.mybatis.model.TSysOrg;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Description:
 *
 * @author Jin
 * @create 2017-07-29
 **/
@Controller
@RequestMapping("/system/org")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @RequiresPermissions("department:admin")
    @RequestMapping
    public String index(){
      return "system/organization";
    }


    @RequestMapping("/input/{id}")
    public String input(@PathVariable("id") Integer id,Model model){
        if(id!=null&&id.intValue()!=0)
            model.addAttribute("org",organizationService.findById(id));
        return "system/organization_input";
    }

    @RequiresPermissions("department:save")
    @RequestMapping("/save")
    @ResponseBody
    public String save(TSysOrg pojo){
        if(pojo.getId()!=null)
            return organizationService.update(pojo);
        return organizationService.save(pojo);
    }

    @RequiresPermissions("department:delete")
    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam("id") Integer id){
        return organizationService.delete(id);
    }
}
