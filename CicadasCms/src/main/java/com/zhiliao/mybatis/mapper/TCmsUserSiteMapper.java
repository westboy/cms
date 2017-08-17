package com.zhiliao.mybatis.mapper;

import com.zhiliao.mybatis.model.TCmsUserSite;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TCmsUserSiteMapper extends Mapper<TCmsUserSite> {

    @Delete("delete from t_cms_user_site where site_id = #{siteId}")
    int deleteBySiteId(@Param("siteId") Integer siteId);

    @Select("SELECT *  FROM  t_cms_user_site where site_id = #{siteId}")
    @ResultMap("BaseResultMap")
    List<TCmsUserSite> selectBySiteId(@Param("siteId") Integer siteId);

}