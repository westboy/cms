package com.zhiliao.module.web.client.service;

import com.github.pagehelper.PageInfo;
import com.zhiliao.mybatis.model.TClientUser;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

/**
 * Description:UserServer Interface
 *
 * @author Jin
 * @create 2017-04-06
 **/
public interface  ClientUserService{



    /**
     * 用户登陆
     * @param username
     * @param password
     * @param remberMe
     * @return
     */

    Map<String, Object> login(HttpServletRequest request, String username, String password, String remberMe, String loginType);

    /**
     * 用户退出
     * @return
     */
    String logout();

    /**
     * 保存前台用户
     * @param clientUser
     * @return
     */
    boolean SaveClientUser(TClientUser clientUser);



    /**
     * 更新前台用户
     * @param clientUser
     * @return
     */
    boolean UpdateClientUser(TClientUser clientUser,Integer roleId);


    /**
     * 更新前台用户
     * @return
     */
    String Delete(Integer[] ids);



    /**
     * 根据用户名查询前台用户权限
     * @param username
     * @return
     */
    Set<String> findClientUserPermissionsByUsername(String username);

    /**
     * 根据用户名查询前台用户角色
     * @param username
     * @return
     */
    Set<String> findClientUserRolesPByUsername(String username);

    /**
     * 根据用户名查询前台用户
     * @param username
     * @return
     */

    TClientUser findClientUserByUsername(String username);


    /**
     * 根据Id查询前台用户
     * @param userId
     * @return
     */
    TClientUser findClientUserByUserId(int userId);


    /**
     * 根据email查询前台用户
     * @param email
     * @return
     */
    TClientUser findClientUserByEmail(String email);



    /**
     * 根据条件查询前台用户分页
     * @return
     */
    PageInfo<TClientUser> findClientUserPageInfo(Integer pageNumber,Integer pageSize,TClientUser user);

    Map changePassword(Integer userId, String op, String np);

    boolean SaveClientUser(TClientUser clientUser,Integer roleId);

}
