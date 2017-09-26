package com.zhiliao.module.web.system.service;

import com.github.pagehelper.PageInfo;
import com.zhiliao.module.web.system.vo.UserVo;
import com.zhiliao.mybatis.model.TSysUser;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

/**
 * Created by binary on 2017/4/13.
 */
public interface SysUserService {

    /**
     * 用户登陆
     * @param username
     * @param password
     * @param remberMe
     * @return
     */

    Map<String, Object> login(HttpServletRequest request, String username, String password, String remberMe);


    String save(TSysUser user,Integer[] roleIds,String orgIds);

    String update(TSysUser user,Integer[] roleIds,String orgIds);

    /**
     * 根据用户名查询用户权限
     * @param username
     * @return
     */
    Set<String> findSysUserPermissionsByUsername(String username);

    /**
     * 根据用户名查询用户角色
     * @param username
     * @return
     */
    Set<String> findSysUserRolesPByUserame(String username);

    /**
     * 根据用户名查询后台用户
     * @param username
     * @return
     */
    TSysUser findSysUserByUsername(String username);


    /**
     * 根据Id查询后台用户
     * @param userId
     * @return
     */
    TSysUser findSysUserByUserId(int userId);



    /**
     * 根据条件查询系统用户分页
     * @return
     */
    PageInfo<TSysUser> findSysUserPageInfo(Integer pageNumber,Integer pageSize, UserVo user);


    /**
     * 批量删除管理员
     * @param ids
     * @return
     */
    String Delete(Integer[] ids);


    String changePassword(Integer userId,String oldPassword,String newPassword);

    Integer countAll();
}
