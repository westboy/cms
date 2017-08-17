package com.zhiliao.mybatis.mapper;

import com.zhiliao.mybatis.model.TClientUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TClientUserMapper {
    int deleteByPrimaryKey(Integer clientId);

    int insert(TClientUser record);

    TClientUser selectByPrimaryKey(Integer clientId);

    @Select("select * from t_client_user where username = #{username}")
    @ResultMap("BaseResultMap")
    TClientUser selectByUsername(@Param("username") String username);

    @Select("select * from t_client_user where email = #{email}")
    @ResultMap("BaseResultMap")
    TClientUser selectByEmail(@Param("email") String email);

    List<TClientUser> selectByCondition(TClientUser user);

    List<TClientUser> selectAll();

    int updateByPrimaryKey(TClientUser record);
}