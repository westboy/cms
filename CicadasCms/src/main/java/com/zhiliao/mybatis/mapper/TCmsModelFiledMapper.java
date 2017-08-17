package com.zhiliao.mybatis.mapper;

import com.zhiliao.mybatis.model.TCmsModelFiled;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TCmsModelFiledMapper extends Mapper<TCmsModelFiled> {

    @Select("select * from  t_cms_model_filed where model_id = #{id}")
    @ResultMap("BaseResultMap")
    List<TCmsModelFiled> selectByModelId(@Param("id") Integer modelId);

}