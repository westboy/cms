package com.zhiliao.module.web.system;


import com.zhiliao.common.utils.ExcelUtil;
import com.zhiliao.common.utils.JsonUtil;
import com.zhiliao.module.web.client.service.ClientUserService;
import com.zhiliao.module.web.system.service.OrganizationService;
import com.zhiliao.module.web.system.service.RoleService;
import com.zhiliao.module.web.system.service.SysUserService;
import com.zhiliao.module.web.system.vo.UserVo;
import com.zhiliao.mybatis.model.TClientUser;
import com.zhiliao.mybatis.model.TSysUser;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;


/**
 * Description:成员管理控制器
 *
 * @author Jin
 * @create 2017-04-06
 **/
@Controller
@RequestMapping("/system/member")
public class MemberController {

    @Autowired
    private ClientUserService clientUserService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private OrganizationService orgService;

    /* 前台用户列表 */
    @RequestMapping("/clientUser")
    public ModelAndView ClientUser(
            @RequestParam(value = "pageCurrent",defaultValue = "1") Integer pageNumber,
            @RequestParam(value = "pageSize",defaultValue ="50") Integer pageSize, TClientUser user){
        ModelAndView view = new ModelAndView("system/client_list");
        view.addObject("model",clientUserService.findClientUserPageInfo(pageNumber,pageSize,user));
        view.addObject("user", user);
        return view;

    }

    @RequestMapping("/excel")
    public ModelAndView ClientUser(HttpServletResponse response){
       ExcelUtil.exports("123",sysUserService.findSysUserPageInfo(1,10,null).getList());
        return null;
    }

    @RequiresRoles("superadmin")
    @RequestMapping("/clientUserInput")
    public String ClientUserInput(@RequestParam(value = "clientId",required = false) Integer clientId, Model model){
//        if(clientId!=null)
//            model.addAttribute("user",clientUserService.findClientUserByUserId(clientId));
//        model.addAttribute("role",roleService.findByIdAndTypeId(clientId,1));
        return "system/client_input";
    }


    @RequestMapping("/clientUserUpdate")
    @ResponseBody
    public String ClientUserUpdate(TClientUser user,@RequestParam("pid") Integer roleId ){

        if(user.getClientId()!=null)
            if(clientUserService.UpdateClientUser(user,roleId)) {
                return JsonUtil.toSUCCESS("更新成功", "client", true);
            }
        if(clientUserService.SaveClientUser(user,roleId)) {
            return JsonUtil.toSUCCESS("增加成功", "client", true);
        }
        return JsonUtil.toERROR("更新失败！");
    }

    /* 前台用户删除 */
    @RequestMapping("/clientUserDel")
    @ResponseBody
    public String ClientUserDelete(@RequestParam("clientId") Integer[] id){
        return clientUserService.Delete(id);
    }


    @RequestMapping("/roleCheck/{typeId}")
    public String roleCheck(@PathVariable("typeId") Integer typeId,Model model){
       model.addAttribute("list",roleService.findByTypeId(typeId));
       return "system/role_check";
    }



    @RequestMapping("/sysUserIndex")
    public ModelAndView SysUserIndex(){
        return new ModelAndView("system/admin");

    }

    /* 后台用户列表 */
    @RequestMapping("/sysUser")
    public ModelAndView SysUser(
            @RequestParam(value = "pageNumber",defaultValue = "1") Integer pageNumber,
            @RequestParam(value = "pageSize",defaultValue ="50") Integer pageSize, UserVo user){
        ModelAndView view = new ModelAndView("system/admin_list");
        view.addObject("model",sysUserService.findSysUserPageInfo(pageNumber,pageSize,user));
        view.addObject("user", user);
        return view;

    }

    /* 后台用户修改 */
    @RequestMapping("/sysUserInput")
    public String SysUserInput(@RequestParam(value = "userId",required = false) Integer userId, Model model){
        if(userId!=null) {
            model.addAttribute("user", sysUserService.findSysUserByUserId(userId));
            model.addAttribute("userRole", roleService.findByUserIdAndTypeId(userId,0));
        }
        model.addAttribute("orgList",orgService.findByPid(0));
        model.addAttribute("roleList",roleService.findByTypeId(0));
        return "system/admin_input";
    }

    /* 后台用户更新 */
    @RequestMapping("/sysUserUpdate")
    @ResponseBody
    public String SysUserUpdate(TSysUser user,@RequestParam(value = "roleId",required = false) Integer[] roleIds,@RequestParam(value = "orgId",required = false) String orgIds ){
        if(user.getUserId()!=null)
            return sysUserService.update(user,roleIds,orgIds);
        return sysUserService.save(user,roleIds,orgIds);
    }


    /* 后台用户删除 */
    @RequestMapping("/sysUserDel")
    @ResponseBody
    public String SysUserDelete(@RequestParam("userId") Integer[] id){
        return sysUserService.Delete(id);
    }


    @RequestMapping("/sysUsername")
    @ResponseBody
    public String validSysUserName(@RequestParam("username") String name){
          if(sysUserService.findSysUserByUsername(name)!=null)
                  return "{\"error\": \"名字已经被占用啦\"}";
              return "{\"ok\": \"名字很棒\"}";
    }


}
