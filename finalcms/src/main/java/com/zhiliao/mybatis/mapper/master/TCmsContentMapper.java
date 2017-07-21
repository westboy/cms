package com.zhiliao.mybatis.mapper.master;

import com.zhiliao.mybatis.model.master.TCmsContent;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface TCmsContentMapper extends Mapper<TCmsContent> {

    /*根据站点编号删除内容*/
    @Delete("delete from t_cms_content where site_id = #{siteId}")
    int deleteBySiteId(@Param("siteId") Integer siteId);

    List<TCmsContent> selectByCondition(TCmsContent content);

    List<TCmsContent> selectByTableNameAndMap(@Param("tableName") String tableName,@Param("categoryId") Long categoryId,@Param("param") Map param);

    Map selectByContentIdAndTableName(@Param("contentId") Long contentId,
                                      @Param("tableName") String tableName);

    List<TCmsContent> selectByContentListBySiteIdAndCategoryId(@Param("siteId") Integer siteId,
                                                               @Param("categoryId") Long categoryId,
                                                               @Param("inExpress") String inExpress,
                                                               @Param("orderBy") Integer orderBy,
                                                               @Param("isHot") Integer isHot,
                                                               @Param("isPic") String isPic
                                                               );

    @Select("SELECT * FROM t_cms_content WHERE content_id = (SELECT max(content_id) FROM t_cms_content WHERE content_id < #{contentId} AND category_id =#{categoryId}  and status =1)")
    @ResultMap("BaseResultMap")
    TCmsContent selectPrevContentByContentIdAndCategoryId(@Param("contentId") Long contentId,@Param("categoryId")Long categoryId);

    @Select("SELECT * FROM t_cms_content WHERE content_id = (SELECT min(content_id) FROM t_cms_content WHERE content_id > #{contentId} AND category_id =#{categoryId}  and status =1)")
    @ResultMap("BaseResultMap")
    TCmsContent selectNextContentByContentIdAndCategoryId(@Param("contentId")Long contentId,@Param("categoryId")Long categoryId);

    @Select("SELECT c.*  FROM  t_cms_content c  inner JOIN  t_cms_category  cat  ON cat.category_id = c.category_id  WHERE cat.category_id=#{categoryId} and c.site_id=#{siteId} and c.status=1")
    @ResultMap("BaseResultMap")
    List<TCmsContent> selectByCategoyId(@Param("categoryId") Long categoryId,@Param("siteId") Integer siteId);

    @Select("SELECT c.*  FROM  t_cms_content c  inner JOIN  t_cms_category  cat  ON cat.category_id = c.category_id  WHERE cat.parent_id=#{parentId} and c.site_id=#{siteId} and c.status=1")
    @ResultMap("BaseResultMap")
    List<TCmsContent> selectByCategoyParentId(@Param("parentId") Long parentId,@Param("siteId") Integer siteId);

}