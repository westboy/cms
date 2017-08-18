package com.zhiliao.module.web.cms;

import com.zhiliao.common.exception.SystemException;
import com.zhiliao.common.template.TemplateFile;
import com.zhiliao.common.template.TemplateFileService;
import com.zhiliao.common.utils.JsonUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Description:
 *
 * @author Jin
 * @create 2017-08-15
 **/
@Controller
@RequestMapping("/system/cms/template")
public class TemplateController {

    @Autowired
    private TemplateFileService templateFileService;


    @RequiresPermissions("template:admin")
    @RequestMapping
    public String index(Model model){
        List<TemplateFile> list = templateFileService.findAll();
        model.addAttribute("templateFiles",list);
        return "cms/template";
    }


    @RequiresPermissions("template:edit")
    @RequestMapping("/input")
    public String input(TemplateFile templateFile,Model model){
        if(templateFile.getFilePath()==null)throw new SystemException("模板路径不能为空！");
        model.addAttribute("templateFile",templateFileService.findByPath(templateFile.getFilePath()));
        return "cms/template_input";

    }


    @RequiresPermissions("template:save")
    @RequestMapping("/save")
    @ResponseBody
    public String save(TemplateFile templateFile){
        templateFileService.writeTemplateFileContent(templateFile);
        return JsonUtil.toSUCCESS("模板修改成功","template-tab",false);
    }

}
