package com.zhiliao.module.web.cms;

import com.github.pagehelper.PageInfo;
import com.zhiliao.common.utils.StrUtil;
import com.zhiliao.module.web.cms.service.FriendlinkService;
import com.zhiliao.mybatis.model.TCmsFriendlink;
import com.zhiliao.mybatis.model.TCmsFriendlinkGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Description:友情链接控制器
 *
 * @author Jin
 * @create 2017-06-12
 **/
@Controller
@RequestMapping("/system/cms/friendlink")
public class FrientlinkController{

    @Autowired
    private FriendlinkService friendlinkService;

    @RequestMapping
    public String index(@RequestParam(value = "pageCurrent",defaultValue = "1") Integer pageNumber,
                        @RequestParam(value = "pageSize",defaultValue = "50") Integer pageSize,
                        TCmsFriendlink pojo, Model model) {
        PageInfo page = friendlinkService.page(pageNumber,pageSize,pojo);
        model.addAttribute("model",page);
        model.addAttribute("pojo",pojo);
        model.addAttribute("groups",friendlinkService.findGroupAll());
        return "cms/friend_link";
    }


    @RequestMapping("/save")
    @ResponseBody
    public String save(TCmsFriendlink pojo) {
       if(StrUtil.isBlank(pojo.getImg()))
           pojo.setImg(null);
        if(pojo.getId()!=null)
            return friendlinkService.update(pojo);
        return friendlinkService.save(pojo);
    }


    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam(value = "ids",required = false) Integer[] ids)  {
        return friendlinkService.delete(ids);
    }

    @RequestMapping("/group/delete")
    @ResponseBody
    public String groupDelete(@RequestParam(value = "ids",required = false) Integer id) {
        return friendlinkService.deleteGroupById(id);
    }

    @RequestMapping("/group/input")
    public String groupInput(@RequestParam(value = "id",required = false) Integer id,Model model){
        if(id!=null)
            model.addAttribute("group",friendlinkService.findGroupById(id));
        return "cms/friendlink_group_input";
    }

    @RequestMapping("/group/save")
    @ResponseBody
    public String groupSave(TCmsFriendlinkGroup group){
        if(group.getId()!=null)
            return friendlinkService.update(group);
        return friendlinkService.save(group);
    }

    @RequestMapping("/group/list")
    public String groupList(@RequestParam(value = "pageCurrent",defaultValue = "1") Integer pageNumber,
                            @RequestParam(value = "pageSize",defaultValue = "50") Integer pageSize,
                            TCmsFriendlinkGroup group,
                            Model model
                            ){
        model.addAttribute("model",friendlinkService.page(pageNumber,pageSize,group));
        model.addAttribute("pojo",group);
        return "cms/friendlink_group_list";
    }
}
