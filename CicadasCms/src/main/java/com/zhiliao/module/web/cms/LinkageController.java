package com.zhiliao.module.web.cms;

import com.zhiliao.common.base.BaseController;
import com.zhiliao.module.web.cms.service.LinkageService;
import com.zhiliao.mybatis.model.TCmsLinkage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Description:联动菜单
 *
 * @author Jin
 * @create 2017-05-22
 **/
@Controller
@RequestMapping("/system/cms/linkage")
public class LinkageController extends BaseController<TCmsLinkage>{

    @Autowired
    private LinkageService linkageService;

    @RequestMapping
    @Override
    public String index(@RequestParam(value = "pageCurrent",defaultValue = "1") Integer pageNumber,
                        @RequestParam(value = "pageSize",defaultValue = "100") Integer pageSize,
                        TCmsLinkage pojo, Model model) {
        model.addAttribute("model",linkageService.page(pageNumber,pageSize,pojo));
        return "cms/linkage_list";
    }

    @RequestMapping("/input")
    @Override
    public String input(@RequestParam(value = "id",required = false) Integer Id, Model model) {
        model.addAttribute("pojo",linkageService.findById(Id));
        return "cms/linkage_input";
    }

    @RequestMapping("/save")
    @ResponseBody
    @Override
    public String save(TCmsLinkage pojo)  {
        if(pojo.getId()!=null)
            return linkageService.update(pojo);
        return linkageService.save(pojo);
    }

    @RequestMapping("/delete")
    @ResponseBody
    @Override
    public String delete(@RequestParam(value = "ids",required = false) Integer[] ids) {
        return linkageService.delete(ids);
    }
}
