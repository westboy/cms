package com.zhiliao.mybatis.mapper;

import com.zhiliao.mybatis.model.TCmsSite;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TCmsSiteMapper extends Mapper<TCmsSite> {

    @Select("SELECT  `s`.* FROM  `t_sys_user` u,  `t_cms_site` s,  `t_cms_user_site` us WHERE  `u`.`user_id` = `us`.`user_id`   AND `s`.`site_id` = `us`.`site_id` AND `u`.`user_id` = #{userId}  order by site_id asc")
    @ResultMap("BaseResultMap")
    List<TCmsSite> selectSitesByUserId(@Param("userId") Integer userId);

}