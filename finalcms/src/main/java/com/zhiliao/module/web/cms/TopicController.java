package com.zhiliao.module.web.cms;

import com.zhiliao.common.base.BaseController;
import com.zhiliao.module.web.cms.service.TopicService;
import com.zhiliao.mybatis.model.master.TCmsTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;

/**
 * Description:专题控制器
 *
 * @author Jin
 * @create 2017-07-19
 **/
@Controller
@RequestMapping("/system/cms/topic")
public class TopicController extends BaseController<TCmsTopic>{

    @Autowired
    private TopicService topicService;

    @Override
    @RequestMapping
    public String index(@RequestParam(value = "pageCurrent",defaultValue = "1") Integer pageNumber,
                        @RequestParam(value = "pageSize",defaultValue = "50")Integer pageSize,
                        TCmsTopic pojo, Model model) {
        model.addAttribute("pojo",pojo);
        model.addAttribute("model",topicService.page(pageNumber,pageSize,pojo));
        return "cms/topic_list";
    }

    @Override
    @RequestMapping("/input")
    public String input(@RequestParam(value = "id",required = false) Integer Id,
                        Model model) {
        if(Id!=null)
            model.addAttribute("topic",topicService.findById(Id));
        return "cms/topic_input";
    }

    @Override
    @RequestMapping("/save")
    @ResponseBody
    public String save(TCmsTopic pojo) throws SQLException {
        if(pojo.getTopicId()!=null)
            return topicService.update(pojo);
        return topicService.save(pojo);
    }

    @RequestMapping("/delete")
    @ResponseBody
    @Override
    public String delete(@RequestParam(value = "ids",required = false) Integer[] ids) throws SQLException {
        return topicService.delete(ids);
    }
}
