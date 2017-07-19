package com.zhiliao.module.web.cms;

import com.zhiliao.module.web.cms.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Description:ss
 *
 * @author Jin
 * @create 2017-05-23
 **/
@Controller
@RequestMapping("/system/cms/tag")
public class TagController{

    @Autowired
    private TagService tagService;



    @RequestMapping
    public String index(@RequestParam(value = "pageCurrent",defaultValue = "1") Integer pageNumber,
                        @RequestParam(value = "pageSize",defaultValue = "100") Integer pageSize, Model model) {
        model.addAttribute("model",tagService.page(pageNumber,pageSize));
        return "cms/tag_list";
    }

    @RequestMapping("/json")
    @ResponseBody
    public String tag(@RequestParam(value = "term",defaultValue = "") String term){
        return  tagService.TagJsonList(term);
    }

    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam("ids") Integer[] ids) {
        return tagService.delete(ids);
    }
}
