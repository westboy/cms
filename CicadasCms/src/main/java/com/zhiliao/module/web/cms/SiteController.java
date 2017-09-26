package com.zhiliao.module.web.cms;

import com.zhiliao.common.annotation.SysLog;
import com.zhiliao.common.utils.CmsUtil;
import com.zhiliao.common.utils.UserUtil;
import com.zhiliao.module.web.cms.service.SiteService;
import com.zhiliao.module.web.cms.vo.TCmsSiteVo;
import com.zhiliao.module.web.system.service.SysUserService;
import com.zhiliao.module.web.system.vo.UserVo;
import com.zhiliao.mybatis.model.TCmsSite;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Description:站点控制器
 *
 * @author Jin
 * @create 2017-05-15
 **/
@Controller
@RequestMapping("/system/cms/site")
public class SiteController{

    @Autowired
    private SysUserService userService;

    @Autowired
    private SiteService siteService;

    @RequiresPermissions("site:admin")
    @RequestMapping
    public String index(@RequestParam(value = "pageCurrent",defaultValue = "1") Integer pageNumber,
                        @RequestParam(value = "pageSize",defaultValue = "100") Integer pageSize,
                        TCmsSite pojo, Model model) {
        model.addAttribute("model",siteService.page(pageNumber,pageSize,pojo));
        model.addAttribute("pojo",pojo);
        return "cms/site_list";
    }

    @SysLog("站点添加")
    @RequiresPermissions("site:input")
    @RequestMapping("/input")
    public String input(@RequestParam(value = "id",required = false) Integer Id,Model model) {
        if (Id!=null)
            model.addAttribute("site",siteService.findVoById(Id));
        return "cms/site_input";
    }

    @RequiresPermissions("site:save")
    @RequestMapping("/save")
    @ResponseBody
    public String save(TCmsSiteVo pojo) {
        if(pojo.getSiteId()!=null)
            return siteService.update(pojo);
        return siteService.save(pojo);
    }

    @RequiresPermissions("site:delete")
    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam(value = "ids",required = false) Integer[] ids) {
        return siteService.delete(ids);
    }

    /*切换站点*/
    @RequiresPermissions("site:change")
    @RequestMapping("/change")
    @ResponseBody
    public String change(@RequestParam(value = "id",required = false) Integer id) {
        UserVo userVo = UserUtil.getSysUserVo();
        return siteService.change(userVo,id);
    }

    @RequestMapping("/userCheck")
    public ModelAndView userCheck(
            @RequestParam(value = "pageNumber",defaultValue = "1") Integer pageNumber,
            @RequestParam(value = "pageSize",defaultValue ="50") Integer pageSize, UserVo user){
        ModelAndView view = new ModelAndView("cms/user_check");
        view.addObject("model",userService.findSysUserPageInfo(pageNumber,pageSize,user));
        view.addObject("user", user);
        return view;

    }

    @RequestMapping("/checkDomain")
    @ResponseBody
    public String checkDomain(@RequestParam(value = "domain",required = false) String domain){
        if(!CmsUtil.isNullOrEmpty(siteService.findByDomain(domain)))
            return "{\"error\": \"域名已存在\"}";
        return "{\"ok\": \"验证通过\"}";
    }


    @RequestMapping("/checkAdmin")
    @ResponseBody
    public String checkDomain(@RequestParam(value = "userId",required = false) Integer userId){
        if(CmsUtil.isNullOrEmpty(userService.findSysUserByUserId(userId)))
            return "{\"error\": \"管理不存在！\"}";
        return "{\"ok\": \"验证通过\"}";
    }
}
