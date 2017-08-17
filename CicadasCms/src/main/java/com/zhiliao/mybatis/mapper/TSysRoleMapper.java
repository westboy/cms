package com.zhiliao.mybatis.mapper;

import com.zhiliao.mybatis.model.TSysRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TSysRoleMapper {
    int deleteByPrimaryKey(Integer roleId);

    int insert(TSysRole record);

    TSysRole selectByPrimaryKey(Integer roleId);

    List<TSysRole> selectAll();

    int updateByPrimaryKey(TSysRole record);

    @Select("SELECT  `r`.* FROM  `t_sys_user` a,  `t_sys_role` r,  `t_sys_user_role` ar WHERE  `a`.`user_id` = `ar`.`user_id`   AND `r`.`role_id` = `ar`.`role_id`    AND `r`.`role_type` = 0 AND `a`.`username` = #{username}")
    @ResultMap("BaseResultMap")
    List<TSysRole> selectSysUserRolesByUsername(@Param("username") String username);

    @Select("SELECT  `r`.* FROM  `t_client_user` a,  `t_sys_role` r,  `t_sys_user_role` ar WHERE  `a`.`client_id` = `ar`.`user_id`   AND `r`.`role_id` = `ar`.`role_id`    AND `r`.`role_type` = 1 AND `a`.`username` = #{username}")
    @ResultMap("BaseResultMap")
    List<TSysRole> selectClientUserRolesByUsername(@Param("username") String username);

    @Select("select * from `t_sys_role` where role_type = #{typeId}")
    @ResultMap("BaseResultMap")
    List<TSysRole> selectRolesByTypeId(@Param("typeId") Integer typeId);

    @Select("select r.* from `t_sys_role` r join t_sys_user_role ur on  r.role_id = ur.role_id   where r.role_type = #{typeId} and ur.user_id = #{userId}")
    @ResultMap("BaseResultMap")
    TSysRole selectRoleByUidAndTypeId(@Param("userId") Integer userId,@Param("typeId") Integer typeId);

    @Select("select r.* from `t_sys_role` r join t_sys_user_role ur on  r.role_id = ur.role_id   where r.role_type = #{typeId} and ur.user_id = #{userId}")
    @ResultMap("BaseResultMap")
    List<TSysRole> selectRoleByUseridAndTypeId(@Param("userId") Integer userId,@Param("typeId") Integer typeId);
}