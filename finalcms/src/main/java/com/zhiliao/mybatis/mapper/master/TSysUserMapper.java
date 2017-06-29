package com.zhiliao.mybatis.mapper.master;

import com.zhiliao.mybatis.model.master.TSysUser;
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

    List<TSysUser> selectAll();

    int updateByPrimaryKey(TSysUser record);

    List<TSysUser> selectByCondition(TSysUser user);


}