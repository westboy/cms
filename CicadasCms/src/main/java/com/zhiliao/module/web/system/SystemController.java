package com.zhiliao.module.web.system;

import com.zhiliao.common.annotation.FormToken;
import com.zhiliao.common.constant.CmsConst;
import com.zhiliao.common.utils.CmsUtil;
import com.zhiliao.common.utils.ControllerUtil;
import com.zhiliao.common.utils.JsonUtil;
import com.zhiliao.common.utils.StrUtil;
import com.zhiliao.module.web.cms.service.CategoryService;
import com.zhiliao.module.web.cms.service.ContentService;
import com.zhiliao.module.web.cms.service.TopicService;
import com.zhiliao.module.web.system.service.SysUserService;
import com.zhiliao.module.web.system.vo.UserVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;


@Controller
public class SystemController {


    @Autowired
    private SysUserService userService;

    @Autowired
    private ContentService contentService;

    @Autowired
    private CategoryService categoryService;


    @Autowired
    private TopicService topicService;

    /**
     * 后台首页地址
     * @return
     */
    @RequestMapping("/system")
    public String index(){
        return "system/index";
    }


    /**
     * 后台登陆地址
     * @return
     */
    @FormToken
    @RequestMapping("/${system.login.path}/login")
    public String login(){
        Subject currentUser = SecurityUtils.getSubject();
        if(!CmsUtil.isNullOrEmpty(currentUser)&&currentUser.isAuthenticated())
            return "redirect:/system";
        return "system/login";
    }

    @RequestMapping("/${system.login.path}/logout")
    public String logout(@Value("${system.login.path}") String adminLoginPath){
        Subject currentUser = SecurityUtils.getSubject();
        if(!CmsUtil.isNullOrEmpty(currentUser)&&currentUser.isAuthenticated())currentUser.logout();
        return "redirect:/"+adminLoginPath+"/login";
    }


    /**
     *后台登陆提提交地址
     * @param username
     * @param password
     * @param remberMe
     * @return
     */

    @FormToken
    @RequestMapping("/${system.login.path}/doLogin")
    @ResponseBody
    public Map<String, Object> doLogin(
            HttpServletRequest request,
            @RequestParam(value = "verifyCode",required = false) String verifyCode,
            @RequestParam(value = "username",required = false) String username,
            @RequestParam(value = "password",required = false) String password,
            @RequestParam(value = "remberMe",required = false,defaultValue = "") String remberMe){

        /* 临时验证码验证 */
        if(StrUtil.isBlank(verifyCode)|| !ControllerUtil.validate(verifyCode,request))
             return JsonUtil.toMAP(false,"验证码输入错误");
        return userService.login(request,username,password,remberMe);

    }

    @FormToken
    @RequestMapping("/ajax/doLogin")
    @ResponseBody
    public String doLogin(
            HttpServletRequest request,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam(value = "remberMe",required = false,defaultValue = "") String remberMe){
         Map resutl = userService.login(request,username,password,remberMe);
         if((Boolean) resutl.get("success"))
             return JsonUtil.toSUCCESS("登陆成功","",true);
        return JsonUtil.toERROR("登陆失败！");
    }

    @FormToken
    @RequestMapping("/loginTimeout")
    public String doTimeOut(){
        return "system/login_timeout";
    }


    /**
     *
     * 修改密码
     * @return
     */
    @RequestMapping("/system/changePassword")
    public String changePassword(HttpSession session, Model model){
        UserVo userVo =  (UserVo) session.getAttribute(CmsConst.SITE_USER_SESSION_KEY);
        if(CmsUtil.isNullOrEmpty(userVo))
            throw new UnauthenticatedException();
        model.addAttribute("userId",userVo.getUserId());
        return "system/change_password";
    }

    @RequestMapping("/system/doChangePassword")
    @ResponseBody
    public String doChangePassword(@RequestParam("userId") Integer userId,@RequestParam("oldPassword") String op,@RequestParam("password") String np){
        return userService.changePassword(userId,op,np);
    }

    @RequestMapping("/system/welcome")
    public String welcome(Model model){
        model.addAttribute("contentCount",contentService.AllCount());
        model.addAttribute("categoryCount",categoryService.AllCount());
        model.addAttribute("topicCount",topicService.AllCount());
        model.addAttribute("userCount",userService.countAll());
        return "system/welcome";
    }

    @RequestMapping("/system/AllMonthCharts")
    @ResponseBody
    public String charts(){
       return contentService.findAllMonthCount();
    }
}
