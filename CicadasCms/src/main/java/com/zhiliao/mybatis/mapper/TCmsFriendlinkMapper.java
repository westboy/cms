package com.zhiliao.mybatis.mapper;

import com.zhiliao.mybatis.model.TCmsFriendlink;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TCmsFriendlinkMapper extends Mapper<TCmsFriendlink> {

    @Select("select f.* from t_cms_friendlink f join t_cms_friendlink_group g on f.group_id = g.id where g.id=#{id}")
    @ResultMap("BaseResultMap")
    List<TCmsFriendlink> selectFriendLinkByGroupId(@Param("id") Integer id);

}