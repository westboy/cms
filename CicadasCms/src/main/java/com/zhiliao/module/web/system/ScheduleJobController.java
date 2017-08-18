package com.zhiliao.module.web.system;

import com.zhiliao.common.base.BaseController;
import com.zhiliao.module.web.system.service.ScheduleJobService;
import com.zhiliao.mybatis.model.TSysScheduleJob;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Description:
 *
 * @author Jin
 * @create 2017-07-07
 **/
@Controller
@RequestMapping("/system/schedule")
public class ScheduleJobController extends BaseController<TSysScheduleJob>{

    @Autowired private ScheduleJobService scheduleJobService;


    @RequiresPermissions("job:admin")
    @RequestMapping
    @Override
    public String index(@RequestParam(value = "pageCurrent",defaultValue = "1") Integer pageNumber,
                        @RequestParam(value = "pageSize",defaultValue = "50")Integer pageSize,
                        TSysScheduleJob pojo, Model model) {
        model.addAttribute("model",scheduleJobService.page(pageNumber,pageSize,pojo));
        return "system/schedule_list";
    }

    @RequiresPermissions("job:input")
    @RequestMapping("/input")
    @Override
    public String input(@RequestParam(value = "id",required = false) Integer Id,
                        Model model) {
        if(Id!=null)
            model.addAttribute("pojo",scheduleJobService.findById(Id));
        return "system/schedule_input";
    }

    @RequiresPermissions("job:save")
    @RequestMapping("/save")
    @ResponseBody
    @Override
    public String save(TSysScheduleJob pojo){
        if(pojo.getId()!=null)
            return scheduleJobService.update(pojo);
        return scheduleJobService.save(pojo);
    }

    @RequiresPermissions("job:delete")
    @RequestMapping("/delete")
    @ResponseBody
    @Override
    public String delete(@RequestParam(value = "ids",required = false) Integer[] ids) {
        return scheduleJobService.delete(ids);
    }

    @RequestMapping("/change")
    @ResponseBody
    public String change(@RequestParam(value = "id",required = false) Integer Id,
                         @RequestParam(value = "status",required = false) String status){
       return scheduleJobService.changeStatus(Id,status);
    }
}
