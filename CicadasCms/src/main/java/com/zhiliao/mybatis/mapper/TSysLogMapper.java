package com.zhiliao.mybatis.mapper;

import com.zhiliao.mybatis.model.TSysLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TSysLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TSysLog record);

    TSysLog selectByPrimaryKey(Integer id);

    List<TSysLog> selectAll();

    @Select("select * from t_sys_log where createTime>=#{starDate} order by id desc")
    List<TSysLog> selectByStartDate(String startDate);

    @Select("select * from t_sys_log where createTime<=#{endDate} order by id desc")
    List<TSysLog> selectByEndDate(String endDate);

    @Select("select * from t_sys_log where  createTime>=#{starDate} and createTime<=#{endDate} order by id desc")
    List<TSysLog> selectByDate(@Param("starDate") String starDate,@Param("endDate") String endTime);

    int updateByPrimaryKey(TSysLog record);
}