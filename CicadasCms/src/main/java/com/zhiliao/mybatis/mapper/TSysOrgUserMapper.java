package com.zhiliao.mybatis.mapper;

import com.zhiliao.mybatis.model.TSysOrgUser;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface TSysOrgUserMapper extends Mapper<TSysOrgUser> {

    @Select("select count(0) from t_sys_org_user where  org_id =#{orgId} and user_id =#{userId} ")
    int selectCountByOrgIdAndUserId(@Param("orgId") Integer orgId, @Param("userId") Integer userId);

    @Delete("delete from t_sys_org_user where user_id =#{userId}")
    int deleteByUserId( @Param("userId") Integer userId);
}