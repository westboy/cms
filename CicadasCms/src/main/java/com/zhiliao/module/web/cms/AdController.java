package com.zhiliao.module.web.cms;

import com.zhiliao.module.web.cms.service.AdService;
import com.zhiliao.mybatis.model.TCmsAd;
import com.zhiliao.mybatis.model.TCmsAdGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description:广告控制器
 *
 * @author Jin
 * @create 2017-06-16
 **/
@Controller
@RequestMapping("/system/cms/ad")
public class AdController{

    @Autowired
    private AdService adService;



    @RequestMapping
    public String index(@RequestParam(value = "pageCurrent",defaultValue = "1") Integer pageNumber,
                        @RequestParam(value = "pageSize",defaultValue = "100") Integer pageSize,
                        TCmsAd pojo, Model model) {
        model.addAttribute("model",adService.page(pageNumber,pageSize,1));
        model.addAttribute("pojo",pojo);
        return "cms/ad_list";
    }


    @RequestMapping("/input")
    public String input(@RequestParam(value = "id",required = false) Integer Id, Model model) {
        if (Id!=null)
            model.addAttribute("ad",adService.findById(Id));
        return "cms/ad_input";
    }


    @RequestMapping("/save")
    @ResponseBody
    public String save(TCmsAd pojo) {
        if(pojo.getId()!=null)
            return adService.update(pojo);
        return adService.save(pojo);
    }

    @RequestMapping("/group/input")
    public String groupInput(@RequestParam(value = "id",required = false) Integer Id, Model model){
        if(Id!=null)
            model.addAttribute("group",adService.findById(Id));
        return "cms/ad_group_input";
    }

    @RequestMapping("/group/save")
    @ResponseBody
    public String save(TCmsAdGroup pojo) {
        if(pojo.getId()!=null)
            return adService.update(pojo);
        return adService.save(pojo);
    }

    @RequestMapping("/groupCheck")
    public String group(@RequestParam(value = "pageCurrent",defaultValue = "1") Integer pageNumber,
                        @RequestParam(value = "pageSize",defaultValue = "100") Integer pageSize,
                        TCmsAdGroup pojo, Model model) {
        model.addAttribute("model",adService.page(pageNumber,pageSize,pojo));
        model.addAttribute("group",pojo);
        return "cms/ad_group_check";
    }

    @RequestMapping("/group/delete")
    @ResponseBody
    public String deleteGroup(@RequestParam(value = "ids",required = false)Integer[] ids) {
        return adService.deleteGroup(ids);
    }


    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam(value = "ids",required = false)Integer[] ids) {
        return adService.delete(ids);
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

}
