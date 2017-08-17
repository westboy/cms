package com.zhiliao.mybatis.mapper;

import com.zhiliao.mybatis.model.TCmsContent;
import org.apache.ibatis.annotations.*;
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
                                                               @Param("categoryIds") Long[] categoryIds,
                                                               @Param("orderBy") Integer orderBy,
                                                               @Param("isHot") Integer isHot,
                                                               @Param("isPic") String isPic,
                                                               @Param("isRecommend") String isRecommend
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

    @Select(" select " +
            "sum(case month(inputdate) when '1' then 1 else 0 end) as 一月份, " +
            "sum(case month(inputdate) when '2' then 1 else 0 end) as 二月份, " +
            "sum(case month(inputdate) when '3' then 1 else 0 end) as 三月份, " +
            "sum(case month(inputdate) when '4' then 1 else 0 end) as 四月份, " +
            "sum(case month(inputdate) when '5' then 1 else 0 end) as 五月份, " +
            "sum(case month(inputdate) when '6' then 1 else 0 end) as 六月份, " +
            "sum(case month(inputdate) when '7' then 1 else 0 end) as 七月份, " +
            "sum(case month(inputdate) when '8' then 1 else 0 end) as 八月份, " +
            "sum(case month(inputdate) when '9' then 1 else 0 end) as 九月份, " +
            "sum(case month(inputdate) when '10' then 1 else 0 end) as 十月份," +
            "sum(case month(inputdate) when '11' then 1 else 0 end) as 十一月份, " +
            "sum(case month(inputdate) when '12' then 1 else 0 end) as 十二月份  " +
            "from t_cms_content " +
            "where year(inputdate)=year(now());")
    @ResultType(Map.class)
    Map selectAllMonthCount();
}