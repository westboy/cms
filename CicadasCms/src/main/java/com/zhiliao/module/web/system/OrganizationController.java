package com.zhiliao.module.web.system;

import com.zhiliao.module.web.system.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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

}
