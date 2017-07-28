package com.zhiliao.module.web.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhiliao.common.annotation.FormToken;
import com.zhiliao.common.exception.SystemException;
import com.zhiliao.component.shiro.DefaultUsernamePasswordToken;
import com.zhiliao.common.utils.ControllerUtil;
import com.zhiliao.module.web.client.service.ClientUserService;
import com.zhiliao.mybatis.mapper.master.TSysLogMapper;
import com.zhiliao.mybatis.model.master.TClientUser;
import io.buji.pac4j.subject.Pac4jPrincipal;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.pac4j.core.profile.CommonProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Description:前台首页控制器
 *
 * @author Jin
 * @create 2017-04-06
 **/
@Controller
public class LoginController {

    @Autowired
    private ClientUserService userService;

    @Value("${system.member.allow.register}")
    private String allowRegiser;

    @Autowired
    TSysLogMapper sysLogMapper;

    @RequestMapping("/cas")
    @ResponseBody
    public String index() throws UnsupportedEncodingException {
        final PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        String object = JSON.toJSONString(principals.getPrimaryPrincipal());
        JSONObject object1 = JSON.parseObject(object);
        JSONObject object2 =object1.getJSONObject("profile");
       JSONObject object3 = object2.getJSONObject("attributes");
        final Pac4jPrincipal principal = principals.oneByType(Pac4jPrincipal.class);
        if (principal != null) {
           CommonProfile profile =  principal.getProfile();
        }
        return  object3.get("usertype").toString();
    }

    @FormToken
    @RequestMapping("/login")
    public String login(HttpServletRequest request){
        return "client/login";
    }



    @RequestMapping("/unauthorized")
    public void unauthorized(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ControllerUtil.renderErrorHtml("非法访问","你可能没有权限",request,response);
    }


    @FormToken
    @RequestMapping("/doLogin")
    @ResponseBody
    public Map<String, Object> doLogin(
                       HttpServletRequest request,
                       @RequestParam("username") String username,
                       @RequestParam("password") String password,
                       @RequestParam(value = "remberMe",required = false,defaultValue = "") String remberme){
        return userService.login(request,username,password,remberme, DefaultUsernamePasswordToken.USER);
    }


    @FormToken
    @RequestMapping("/register")
    public ModelAndView register(){
        if(Boolean.parseBoolean(allowRegiser))
            throw new SystemException("系统已关闭会员注册功能！");
       ModelAndView view = new ModelAndView("client/register");
       return view ;
    }

    @FormToken
    @RequestMapping("/doRegister")
    @ResponseBody
    public String doRegister(TClientUser user, HttpServletRequest request){
        String ip=ControllerUtil.getRemoteAddress(request);
        user.setIp(ip);
        if(userService.SaveClientUser(user))
            return "保存成功！";
        return "保存失败！";
    }

    @RequestMapping("/validate/username")
    @ResponseBody
    public String validateUsername(@RequestParam("username") String username){
       TClientUser user= userService.findClientUserByUsername(username);
       if(user!=null)
           return "{\"error\": \"名字已经被占用啦\"}";
        return "{\"ok\": \"名字很棒\"}";
    }

    @RequestMapping("/validate/email")
    @ResponseBody
    public String validateEmail(@RequestParam("email") String email){
        TClientUser user= userService.findClientUserByEmail(email);
        if(user!=null)
            return "{\"error\": \"名字已经被占用啦\"}";
        return "{\"ok\": \"名字很棒\"}";
    }

    /**
     *
     * 修改密码
     * @return
     */
    @RequestMapping("/client/changePassword")
    public String changePassword(HttpSession session, Model model){
        TClientUser user = (TClientUser) session.getAttribute("client");
        model.addAttribute("userId",user.getClientId());
        model.addAttribute("isClient",true);
        return "system/change_password";
    }


    @RequestMapping("/client/doChangePassword")
    @ResponseBody
    public String doChangePassword(@RequestParam("userId") Integer userId,@RequestParam("oldPassword") String op,@RequestParam("password") String np){
        userService.changePassword(userId,op,np);   /*TODO 有待根据前端修改*/
        return null;
    }


}
