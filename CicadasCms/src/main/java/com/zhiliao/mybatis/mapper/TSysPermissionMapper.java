package com.zhiliao.mybatis.mapper;

import com.zhiliao.mybatis.model.TSysPermission;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TSysPermissionMapper {
    int deleteByPrimaryKey(Integer permissionId);

    int insert(TSysPermission record);

    TSysPermission selectByPrimaryKey(Integer permissionId);

    List<TSysPermission> selectAll();

    int updateByPrimaryKey(TSysPermission record);

    @Select("SELECT `p`.* FROM  `t_sys_user` a,  `t_sys_role` r,  `t_sys_permission` p,  `t_sys_user_role` ar,  `t_sys_role_permission` rp WHERE  `a`.`user_id` = `ar`.`user_id`    AND `r`.`role_id` = `ar`.`role_id`    AND `r`.`role_id` = `rp`.`role_id`    AND `p`.`permission_id` = `rp`.`permisson_id`   AND `a`.`username` = #{username}")
    @ResultMap("BaseResultMap")
    List<TSysPermission> selectSysUserPermissionsByUsername(@Param("username") String username);

    @Select("SELECT * FROM t_sys_permission where pid=#{pid}")
    @ResultMap("BaseResultMap")
    List<TSysPermission> selectByPid(Integer pid);

    @Delete("delete FROM t_sys_permission where pid=#{pid}")
    int deleteByPid(Integer pid);
}