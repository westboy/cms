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

    @Delete("DELETE FROM t_sys_user_role where user_id = #{userId} and role_id=#{roleId}")
    int DeleteByUserIdAndRoleId(@Param("userId")Integer userId,@Param("roleId")Integer roleId);

    int deleteByUserId(@Param("userId")Integer userId);
}