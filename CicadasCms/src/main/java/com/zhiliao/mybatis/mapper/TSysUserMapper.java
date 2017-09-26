package com.zhiliao.mybatis.mapper;

import com.zhiliao.mybatis.model.TSysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TSysUserMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(TSysUser record);

    TSysUser selectByPrimaryKey(Integer userId);

    @Select("select * from t_sys_user where username = #{username}")
    @ResultMap("BaseResultMap")
    TSysUser  selectByUsername(@Param("username") String username);

    @Select("select u.* from t_sys_user u inner join t_sys_org_user o on u.user_id = o.user_id where o.org_id=#{orgId}  group by u.user_id")
    @ResultMap("BaseResultMap")
    TSysUser  selectByOrgId(@Param("orgId") String orgId);

    List<TSysUser> selectAll();

    @Select("select count(0) from t_sys_user")
    Integer countUser();

    int updateByPrimaryKey(TSysUser record);

    List<TSysUser> selectByCondition(TSysUser user);


}