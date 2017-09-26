package com.zhiliao.module.web.system;


import com.zhiliao.module.web.system.service.OrganizationService;
import com.zhiliao.module.web.system.service.RoleService;
import com.zhiliao.module.web.system.service.SysUserService;
import com.zhiliao.module.web.system.vo.UserVo;
import com.zhiliao.mybatis.model.TSysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


/**
 * Description:用户管理
 *
 * @author Jin
 * @create 2017-04-06
 **/
@Controller
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private OrganizationService orgService;

    @RequestMapping
    public ModelAndView index(){
        return new ModelAndView("system/admin");
    }

    /* 后台用户列表 */
    @RequestMapping("/list")
    public ModelAndView list(
            @RequestParam(value = "pageNumber",defaultValue = "1") Integer pageNumber,
            @RequestParam(value = "pageSize",defaultValue ="50") Integer pageSize, UserVo user){
        ModelAndView view = new ModelAndView("system/admin_list");
        view.addObject("model",sysUserService.findSysUserPageInfo(pageNumber,pageSize,user));
        view.addObject("user", user);
        return view;

    }

    /* 后台用户修改 */
    @RequestMapping("/input")
    public String input(@RequestParam(value = "userId",required = false) Integer userId, Model model){
        if(userId!=null) {
            model.addAttribute("user", sysUserService.findSysUserByUserId(userId));
            model.addAttribute("userRole", roleService.findByUserId(userId));
        }
        model.addAttribute("orgList",orgService.findByPid(0));
        model.addAttribute("roleList",roleService.findAll());
        return "system/admin_input";
    }

    /* 后台用户更新 */
    @RequestMapping("/update")
    @ResponseBody
    public String update(TSysUser user,@RequestParam(value = "roleId",required = false) Integer[] roleIds,@RequestParam(value = "orgId",required = false) String orgIds ){
        if(user.getUserId()!=null)
            return sysUserService.update(user,roleIds,orgIds);
        return sysUserService.save(user,roleIds,orgIds);
    }


    /* 后台用户删除 */
    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam("userId") Integer[] id){
        return sysUserService.Delete(id);
    }


    @RequestMapping("/valid/username")
    @ResponseBody
    public String validSysUserName(@RequestParam("username") String name){
          if(sysUserService.findSysUserByUsername(name)!=null)
                  return "{\"error\": \"名字已经被占用啦\"}";
              return "{\"ok\": \"名字很棒\"}";
    }


}
