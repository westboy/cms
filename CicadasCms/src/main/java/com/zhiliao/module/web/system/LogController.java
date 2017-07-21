package com.zhiliao.module.web.system;

import com.zhiliao.common.annotation.SysLog;
import com.zhiliao.module.web.system.service.LogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Description:系统日志
 *
 * @author Jin
 * @create 2017-04-11
 **/
@Controller
@RequestMapping("/sys/log")
public class LogController {

    @Autowired
    private LogService logService;

    @SysLog("查看日志")
    @RequiresUser
    @RequiresPermissions("log:view")
    @RequestMapping
    public ModelAndView index(@RequestParam(value = "pageCurrent",defaultValue = "1") Integer pageNumber,
                              @RequestParam(value = "pageSize",defaultValue = "50") Integer pageSize,
                              @RequestParam(value="startTime",defaultValue = "") String startTime,
                              @RequestParam(value = "endTime",defaultValue = "") String endTime){
        ModelAndView view = new ModelAndView("system/log_list");
        view.addObject("model",logService.page(pageNumber,pageSize,startTime,endTime));
        view.addObject("startTime",startTime);
        view.addObject("endTime",endTime);
        return view;
    }


    @SysLog("删除日志")
    @RequiresUser
    @RequiresPermissions("log:delete")
    @RequestMapping("/del")
    @ResponseBody
    public String delLog(@RequestParam("id") Integer[] logId){
        return logService.deleteById(logId);
    }
}
