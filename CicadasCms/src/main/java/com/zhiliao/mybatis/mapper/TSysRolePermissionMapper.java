package com.zhiliao.mybatis.mapper;

import com.zhiliao.mybatis.model.TSysRolePermission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TSysRolePermissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TSysRolePermission record);

    TSysRolePermission selectByPrimaryKey(Integer id);

    List<TSysRolePermission> selectAll();

    int updateByPrimaryKey(TSysRolePermission record);

    @Select("select count(0) from t_sys_role_permission where  role_id =#{roleId} and permisson_id =#{permissionId} ")
    int selectCountByRoleIdAndPermissionId(@Param("roleId") Integer roleId,@Param("permissionId") Integer permissionId);

    @Deprecated
    @Select("select * from t_sys_role_permission where  role_id =#{roleId} and permisson_id =#{permissionId} ")
    TSysRolePermission selectByRoleIdAndPermissionId(@Param("roleId") Integer roleId,@Param("permissionId") Integer permissionId);

    int delectByRoleId(@Param("roleId") Integer roleId);

    int delectByPermissionId(@Param("permissionId") Integer permissionId);
}