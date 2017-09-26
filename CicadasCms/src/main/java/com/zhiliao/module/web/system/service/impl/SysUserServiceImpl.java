package com.zhiliao.module.web.system.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.zhiliao.common.annotation.SysLog;
import com.zhiliao.common.constant.CmsConst;
import com.zhiliao.common.exception.SystemException;
import com.zhiliao.common.utils.*;
import com.zhiliao.component.shiro.PasswordKit;
import com.zhiliao.module.web.system.service.SysUserService;
import com.zhiliao.module.web.system.vo.UserVo;
import com.zhiliao.mybatis.mapper.*;
import com.zhiliao.mybatis.model.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Description:后台用户控制器
 *
 * @author Jin
 * @create 2017-04-13
 **/
@Service
public class SysUserServiceImpl implements SysUserService{

    @Value("${system.site.name}")
    private String siteName;

    @Autowired
    private TSysUserMapper sysUserMapper;


    @Autowired
    private TSysRoleMapper sysRoleMapper;

    @Autowired
    private TSysPermissionMapper sysPermissionMapper;

    @Autowired
    private TSysUserRoleMapper userRoleMapper;

    @Autowired
    private TSysOrgUserMapper userOrgMapper;

    @Autowired
    private TCmsSiteMapper siteMapper;

    @Override
    @SysLog("后台用户登陆")
    public Map<String, Object> login(HttpServletRequest request, String username, String password, String remberMe) {
        Map<String, Object> result = Maps.newHashMap();
        result.put("success", false);
        HttpSession session = request.getSession();
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
        /*是否需要记住我*/
        if ("true".equals(remberMe)) {
            usernamePasswordToken.setRememberMe(true);
        }
        try {
            currentUser.login(usernamePasswordToken);
            TSysUser user = findSysUserByUsername(username);
            user.setLoginTime(new Date());
            user.setLastIp(ControllerUtil.getRemoteAddress(request));
            /*更新用户的登陆信息*/
            sysUserMapper.updateByPrimaryKey(user);
            /*userVo和TSysUser没什么区别，只是增加了siteId*/
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(userVo,user);
            if(userVo.getUserId()==1){
                userVo.setSiteId(1);
                userVo.setSiteName(siteName);
                /*设置session*/
                session.setAttribute(CmsConst.SITE_USER_SESSION_KEY,userVo);
                result.put("success", true);
                result.put("message", "登录成功！");
            }else {
                List<TCmsSite> sites = siteMapper.selectSitesByUserId(userVo.getUserId());
                if (!CmsUtil.isNullOrEmpty(sites)) {
                /*取出列表中第一个站点的Id,当作此登陆站点的标识*/
                    for (TCmsSite site : sites) {
                        userVo.setSiteId(site.getSiteId());
                        userVo.setSiteName(site.getSiteName());
                        break;
                    }
                /*设置session*/
                    session.setAttribute(CmsConst.SITE_USER_SESSION_KEY, userVo);
                    result.put("success", true);
                    result.put("message", "登录成功！");
                } else {
                /*当前用户没有可以管理的站点*/
                    result.put("message", "当前用户没有可以管理的站点！");
                }
            }
        } catch (UnknownAccountException e) {
            result.put("message", "账号输入错误！");
        } catch (IncorrectCredentialsException e){
            result.put("message", "密码输入错误！");
        } catch (LockedAccountException e){
            result.put("message", "当前账号已被停用！");
        } catch (AuthenticationException ae) {
            result.put("message", "账号或者密码输入错误！");
        }catch (Exception e){
            result.put("message", "发生了一个错误！");
        }
        return result;
    }


    @Transactional(transactionManager = "masterTransactionManager",rollbackFor = Exception.class)
    @Override
    public String save(TSysUser user, Integer[] roleIds,String orgIds) {
        if (CmsUtil.isNullOrEmpty(roleIds)||CmsUtil.isNullOrEmpty(orgIds)) throw new SystemException("用户角色和部门不能为空！");
        user.setUsername(PinyinUtil.convertLower(HtmlKit.getText(user.getUsername())));
        user.setCreateTime(new Date());
        /* 加工password */
        if(!StrUtil.isBlank(user.getPassword().trim())) {
            String salt = CheckSumUtil.getMD5(user.getUsername().trim());
            user.setPassword(PasswordKit.encodePassword(user.getPassword().trim(), salt));
            user.setSalt(salt);
        }
        if(sysUserMapper.insert(user)>0) {
            if (CmsUtil.isNullOrEmpty(roleIds)) throw new SystemException("请选择用户角色！");
            for (int roleId : roleIds) {
                this.saveUserRole(user.getUserId(),roleId);
            }
            for (String orgId : orgIds.split(",")) {
                this.saveUserOrg(user.getUserId(),Integer.parseInt(orgId));
            }
            return JsonUtil.toSUCCESS("保存成功", "sysUser", true);
        }
        return JsonUtil.toERROR("更新失败！");
    }

    @Transactional(transactionManager = "masterTransactionManager",rollbackFor = Exception.class)
    @Override
    public String update(TSysUser user, Integer[] roleIds,String orgIds) {
        user.setUsername(PinyinUtil.convertLower(HtmlKit.getText(user.getUsername())));
        /* 加工password */
        if(!StrUtil.isBlank(user.getPassword().trim())) {
            String salt = CheckSumUtil.getMD5(user.getUsername().trim());
            user.setPassword(PasswordKit.encodePassword(user.getPassword().trim(), salt));
            user.setSalt(salt);
        }
        if(sysUserMapper.updateByPrimaryKey(user)>0) {
            userRoleMapper.deleteByUserId(user.getUserId());
            if (CmsUtil.isNullOrEmpty(roleIds)) throw new SystemException("请选择用户角色！");
            for (Integer roleId : roleIds) {
               this.saveUserRole(user.getUserId(),roleId);
            }
            if(!StrUtil.isBlank(orgIds)) {
                this.userOrgMapper.deleteByUserId(user.getUserId());
                for (String orgId : orgIds.split(",")) {
                    this.saveUserOrg(user.getUserId(), Integer.parseInt(orgId));
                }
            }
            return JsonUtil.toSUCCESS("更新成功", "sysUser", false);
        }
        return JsonUtil.toERROR("更新失败！");
    }

    public void saveUserRole(Integer userId,Integer roleId){
        TSysUserRole userRole = new TSysUserRole();
        userRole.setRoleId(roleId);
        userRole.setUserId(userId);
        userRoleMapper.insert(userRole);
    }

    public void saveUserOrg(Integer userId,Integer orgId){
        TSysOrgUser orgUser = new TSysOrgUser();
        orgUser.setUserId(userId);
        orgUser.setOrgId(orgId);
        this.userOrgMapper.insertSelective(orgUser);
    }

    @Override
    public Set<String> findSysUserPermissionsByUsername(String username) {
        /* 根据用户名查询权限 */
        List<TSysPermission> permissions = sysPermissionMapper.selectSysUserPermissionsByUsername(username);
        Set<String> set = new HashSet<>();
        for(TSysPermission permission :permissions){
            set.add(permission.getName());
        }
        return set;

    }

    @Override
    public Set<String> findSysUserRolesPByUserame(String username) {
        List<TSysRole> roles = sysRoleMapper.selectSysUserRolesByUsername(username);
        Set<String> set = new HashSet<>();
        for(TSysRole role :roles){
            set.add(role.getRolename());
        }
        return set;
    }


    public TSysUser findSysUserByUsername(String username) {
        return sysUserMapper.selectByUsername(username);
    }


    public TSysUser findSysUserByUserId(int userId) {
        return sysUserMapper.selectByPrimaryKey(userId);
    }



    @Override
    public PageInfo<TSysUser> findSysUserPageInfo(Integer pageNumber,Integer pageSize,UserVo user) {
        PageHelper.startPage(pageNumber,pageSize);
        return new PageInfo(sysUserMapper.selectByCondition(user));
    }

    @Override
    @Transactional
    public String Delete(Integer[] ids) {
        boolean flag_ = false;
        UserVo userVo = ((UserVo) ControllerUtil.getHttpSession().getAttribute(CmsConst.SITE_USER_SESSION_KEY));
        if(CmsUtil.isNullOrEmpty(userVo))
            throw  new UnauthenticatedException();
        if(ids.length>0){
            for(int id :ids){
                if(id == 1)
                    throw new SystemException("不能删除超级管理员！");
                if(id == userVo.getUserId())
                    throw new SystemException("你不能删除自己！");
                if(sysUserMapper.deleteByPrimaryKey(id)>0) {

                    flag_ = userRoleMapper.deleteByUserId(id)>0;
                }
            }
        }
        if(flag_)
            return JsonUtil.toSUCCESS("删除用户成功!");
        return JsonUtil.toERROR("删除用户失败!");
    }

    @Override
    public String changePassword(Integer userId, String oldPassword, String newPassword) {
        TSysUser user = findSysUserByUserId(userId);
        if(PasswordKit.isPasswordValid(user.getPassword(),oldPassword,user.getSalt())){
            user.setPassword(PasswordKit.encodePassword(newPassword,user.getSalt()));
            sysUserMapper.updateByPrimaryKey(user);
            return JsonUtil.toSUCCESS("密码修改成功！","changepwd_page",true);
        }
        return JsonUtil.toERROR("原密码输入错误！");
    }

    @Override
    public Integer countAll() {
        return sysUserMapper.countUser();
    }
}
