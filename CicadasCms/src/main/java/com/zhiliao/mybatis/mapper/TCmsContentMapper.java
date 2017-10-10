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

    List<Map> selectByContentListBySiteIdAndCategoryIds(@Param("siteId") Integer siteId,
                                                        @Param("categoryIds") Long[] categoryIds,
                                                        @Param("orderBy") Integer orderBy,
                                                        @Param("isHot") Integer isHot,
                                                        @Param("isPic") Integer isPic,
                                                        @Param("isRecommend") Integer isRecommend,
                                                        @Param("tableName") String tableName
    );

    List<Map> selectByTopicContentListBySiteIdAndCategoryIds(@Param("siteId") Integer siteId,
                                                        @Param("categoryIds") Long[] categoryIds,
                                                        @Param("orderBy") Integer orderBy,
                                                        @Param("isHot") Integer isHot,
                                                        @Param("isPic") Integer isPic,
                                                        @Param("isRecommend") Integer isRecommend
    );

    @Select("SELECT * FROM t_cms_content WHERE content_id = (SELECT max(content_id) FROM t_cms_content WHERE content_id < #{contentId} AND category_id =#{categoryId}  and status =1)")
    @ResultMap("BaseResultMap")
    TCmsContent selectPrevContentByContentIdAndCategoryId(@Param("contentId") Long contentId,@Param("categoryId")Long categoryId);

    @Select("SELECT * FROM t_cms_content WHERE content_id = (SELECT min(content_id) FROM t_cms_content WHERE content_id > #{contentId} AND category_id =#{categoryId}  and status =1)")
    @ResultMap("BaseResultMap")
    TCmsContent selectNextContentByContentIdAndCategoryId(@Param("contentId")Long contentId,@Param("categoryId")Long categoryId);

    @Select("SELECT c.content_id contentId, " +
            "        c.site_id siteId, " +
            "        c.user_id userId, " +
            "        c.category_id categoryId, " +
            "        c.model_id modelId, " +
            "        c.title, " +
            "        c.keywords, " +
            "        c.description, " +
            "        c.top, " +
            "        c.recommend, " +
            "        c.thumb, " +
            "        c.updatedate, " +
            "        c.inputdate, " +
            "        c.status, " +
            "        c.url, " +
            "        c.author, " +
            "        c.view_num viewNum, " +
            "        m.*  " +
            "FROM  " +
            "t_cms_content c  inner JOIN  t_cms_category  cat  " +
            "ON cat.category_id = c.category_id  " +
            "left join t_cms_content_${tableName} m " +
            "on c.content_id = m.content_id " +
            "WHERE " +
            "cat.category_id=#{categoryId} " +
            "and " +
            "c.site_id=#{siteId} and c.status=1 " +
            "order by c.content_id desc")
    @ResultType(Map.class)
    List<Map> selectByCategoyId(@Param("categoryId") Long categoryId,@Param("siteId") Integer siteId ,@Param("tableName") String tableName);

    @Select("SELECT c.content_id contentId, " +
            "        c.site_id siteId, " +
            "        c.user_id userId, " +
            "        c.category_id categoryId, " +
            "        c.model_id modelId, " +
            "        c.title, " +
            "        c.keywords, " +
            "        c.description, " +
            "        c.top, " +
            "        c.recommend, " +
            "        c.thumb, " +
            "        c.updatedate, " +
            "        c.inputdate, " +
            "        c.status, " +
            "        c.url, " +
            "        c.author, " +
            "        c.view_num viewNum, " +
            "        m.*  " +
            "FROM " +
            " t_cms_content c " +
            " inner JOIN  t_cms_category  cat " +
            " ON cat.category_id = c.category_id  " +
            "left join t_cms_content_${tableName} m" +
            " on c.content_id = m.content_id " +
            " WHERE c.category_id in (${categoryIds}) " +
            "and c.site_id=#{siteId} and c.status=1" +
            " order by c.content_id desc")
    @ResultType(Map.class)
    List<Map> selectByCategoyParentId(@Param("categoryIds") String categoryIds,@Param("siteId") Integer siteId,@Param("tableName") String tableName);

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