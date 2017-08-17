package com.zhiliao.module.web.client.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.zhiliao.common.annotation.SysLog;
import com.zhiliao.component.shiro.DefaultUsernamePasswordToken;
import com.zhiliao.component.shiro.PasswordKit;
import com.zhiliao.common.utils.CheckSumUtil;
import com.zhiliao.common.utils.ControllerUtil;
import com.zhiliao.common.utils.JsonUtil;
import com.zhiliao.common.utils.StrUtil;
import com.zhiliao.mybatis.mapper.TClientUserMapper;
import com.zhiliao.mybatis.mapper.TSysPermissionMapper;
import com.zhiliao.mybatis.mapper.TSysRoleMapper;
import com.zhiliao.mybatis.mapper.TSysUserRoleMapper;
import com.zhiliao.mybatis.model.TClientUser;
import com.zhiliao.mybatis.model.TSysPermission;
import com.zhiliao.mybatis.model.TSysRole;
import com.zhiliao.mybatis.model.TSysUserRole;
import com.zhiliao.module.web.client.service.ClientUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;


@Service
public class ClientUserServiceImpl implements ClientUserService {



    @Autowired
    private TClientUserMapper clientUserMapper;


    @Autowired
    private TSysRoleMapper sysRoleMapper;

    @Autowired
    private TSysPermissionMapper sysPermissionMapper;

    @Autowired
    private TSysUserRoleMapper userRoleMapper;

    @SysLog("前台用户登陆")
    public Map<String, Object> login(HttpServletRequest request, String username, String password, String remberMe, String loginType) {
        Map<String, Object> result = Maps.newHashMap();
        result.put("success", false);
        HttpSession session = request.getSession();
        Subject currentUser = SecurityUtils.getSubject();
        DefaultUsernamePasswordToken usernamePasswordToken = new DefaultUsernamePasswordToken(username, password);
        usernamePasswordToken.setLoginType(loginType);
        if ("true".equals(remberMe)) {
            usernamePasswordToken.setRememberMe(true);
        }
        try {
            currentUser.login(usernamePasswordToken);
            TClientUser user = new TClientUser();
            user.setIp(ControllerUtil.getRemoteAddress(request));
            user.setLastTime(new Date());
            session.setAttribute("username",username);
            result.put("success", true);
            result.put("msg", "登录成功！");
        } catch (UnknownAccountException e) {
            result.put("msg", "账号输入错误！");
        } catch (IncorrectCredentialsException e){
            result.put("msg", "密码输入错误！");
        } catch (LockedAccountException e){
            result.put("msg", "当前账号已被停用！");
        } catch (AuthenticationException ae) {
            result.put("msg", "账号或者密码输入错误！");

        }
        return result;
    }

    public String logout() {
        return null;
    }


    public boolean SaveClientUser(TClientUser clientUser){
        if(clientUser==null)
            return false;
        String salt = CheckSumUtil.getMD5(clientUser.getUsername());
        String password = PasswordKit.encodePassword(clientUser.getPassword(),salt);
        clientUser.setStatus(true);
        clientUser.setSalt(salt);
        clientUser.setPassword(password);
        return  clientUserMapper.insert(clientUser)>0;
    }

    public boolean SaveClientUser(TClientUser clientUser,Integer roleId){
        if(clientUser==null)
            return false;
        String salt = CheckSumUtil.getMD5(clientUser.getUsername());
        String password = PasswordKit.encodePassword(clientUser.getPassword(),salt);
        clientUser.setStatus(true);
        clientUser.setSalt(salt);
        clientUser.setPassword(password);
        clientUserMapper.insert(clientUser);
        TSysUserRole userRole = new TSysUserRole();
        userRole.setRoleId(roleId);
        userRole.setUserId(clientUser.getClientId());
        userRole.setRoleType(1);
        return userRoleMapper.insert(userRole)>0;
    }


    @SysLog("用户更新")
    @Transactional(transactionManager = "masterTransactionManager")
    public boolean UpdateClientUser(TClientUser clientUser,Integer roleId) {
        if(!StrUtil.isBlank(clientUser.getPassword())) {
            String salt = CheckSumUtil.getMD5(clientUser.getUsername());
            clientUser.setPassword(PasswordKit.encodePassword(clientUser.getPassword(), salt));
            clientUser.setSalt(salt);
        }
        if(clientUserMapper.updateByPrimaryKey(clientUser)>0) {
            TSysRole role =  sysRoleMapper.selectRoleByUidAndTypeId(clientUser.getClientId(), 1);
            if(role!=null)
               userRoleMapper.DeleteByUserIdAndRoleId(clientUser.getClientId(), role.getRoleId(),1);
            TSysUserRole userRole = new TSysUserRole();
            userRole.setRoleId(roleId);
            userRole.setUserId(clientUser.getClientId());
            userRole.setRoleType(1);
            return userRoleMapper.insert(userRole)>0;
        }
        return  false;
    }

    @Override
    public String Delete(Integer[] ids) {
        boolean flag_ = false;
        if(ids.length>0){
            for(int id :ids){
                if(clientUserMapper.deleteByPrimaryKey(id)>0) {
                    userRoleMapper.deleteByUserIdAndTypeId(id,1);
                    flag_ = true;
                }
            }
        }
        if(flag_)
            return JsonUtil.toSUCCESS("删除用户成功!");
        return JsonUtil.toERROR("删除用户失败!");
    }


    public Set<String> findClientUserPermissionsByUsername(String username) {
        List<TSysPermission> permissions = sysPermissionMapper.selectClientUserPermissionsByUsername(username);
        Set<String> set = new HashSet<>();
        for(TSysPermission permission :permissions){
            set.add(permission.getName());
        }
        return set;
    }

    public Set<String> findClientUserRolesPByUsername(String username) {
        List<TSysRole> roles = sysRoleMapper.selectClientUserRolesByUsername(username);
        Set<String> set = new HashSet<>();
        for(TSysRole role :roles){
            set.add(role.getRolename());
        }
        return set;
    }


    public TClientUser findClientUserByUsername(String username) {
        return clientUserMapper.selectByUsername(username);
    }


    public TClientUser findClientUserByUserId(int userId) {
        return clientUserMapper.selectByPrimaryKey(userId);
    }

    public TClientUser findClientUserByEmail(String email) {
        return clientUserMapper.selectByEmail(email);
    }



    public PageInfo<TClientUser> findClientUserPageInfo(Integer pageNumber,Integer pageSize,TClientUser user) {
        PageHelper.startPage(pageNumber,pageSize);
        return new PageInfo(clientUserMapper.selectByCondition(user));
    }

    @Override
    public Map changePassword(Integer userId, String oldPassword, String newPassword) {
        TClientUser user = findClientUserByUserId(userId);
        Map result = Maps.newHashMap();
        result.put("success",false);
        if(PasswordKit.isPasswordValid(user.getPassword(),oldPassword,user.getSalt())){
            user.setPassword(PasswordKit.encodePassword(newPassword,user.getSalt()));
            clientUserMapper.updateByPrimaryKey(user);
            result.put("msg","密码修改成功！");
        }
        result.put("msg","原密码输入错误！");
        return result;
    }
}
