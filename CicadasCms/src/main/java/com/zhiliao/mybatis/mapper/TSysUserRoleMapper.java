package com.zhiliao.mybatis.mapper;

import com.zhiliao.mybatis.model.TSysUserRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TSysUserRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TSysUserRole record);

    TSysUserRole selectByPrimaryKey(Integer id);

    List<TSysUserRole> selectAll();

    int updateByPrimaryKey(TSysUserRole record);

    @Delete("DELETE FROM t_sys_user_role where user_id = #{uid} and role_id=#{roleId} and role_type = #{roleType}")
    int DeleteByUserIdAndRoleId(@Param("uid")Integer uid,@Param("roleId")Integer roleId,@Param("roleType")Integer roleType);

    int deleteByUserIdAndTypeId(@Param("userId")Integer uid, @Param("roleType") Integer roleType);
}