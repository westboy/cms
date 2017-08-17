package com.zhiliao.mybatis.model;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "t_cms_topic")
public class TCmsTopic implements Serializable {
    @Id
    @Column(name = "topic_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private Integer topicId;

    @Column(name = "category_ids")
    private String categoryIds;

    /**
     * 名称
     */
    @Column(name = "topic_name")
    private String topicName;

    /**
     * 简称
     */
    @Column(name = "short_name")
    private String shortName;

    /**
     * 关键字
     */
    private String keywords;

    /**
     * 描述
     */
    private String description;

    /**
     * 标题图
     */
    @Column(name = "title_img")
    private String titleImg;

    /**
     * 内容图
     */
    @Column(name = "content_img")
    private String contentImg;

    /**
     * 专题模板
     */
    @Column(name = "topic_tpl")
    private String topicTpl;

    /**
     * 排列顺序
     */
    @Column(name = "sort_id")
    private Integer sortId;

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    @Column(name = "site_id")
    private Integer siteId;

    /**
     * 是否推??
     */
    @Column(name = "is_recommend")
    private Boolean isRecommend;

    private static final long serialVersionUID = 1L;

    /**
     * @return topic_id
     */
    public Integer getTopicId() {
        return topicId;
    }

    /**
     * @param topicId
     */
    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    /**
     * @return category_ids
     */
    public String getCategoryIds() {
        return categoryIds;
    }

    /**
     * @param categoryIds
     */
    public void setCategoryIds(String categoryIds) {
        this.categoryIds = categoryIds;
    }

    /**
     * 获取名称
     *
     * @return topic_name - 名称
     */
    public String getTopicName() {
        return topicName;
    }

    /**
     * 设置名称
     *
     * @param topicName 名称
     */
    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    /**
     * 获取简称
     *
     * @return short_name - 简称
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * 设置简称
     *
     * @param shortName 简称
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    /**
     * 获取关键字
     *
     * @return keywords - 关键字
     */
    public String getKeywords() {
        return keywords;
    }

    /**
     * 设置关键字
     *
     * @param keywords 关键字
     */
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    /**
     * 获取描述
     *
     * @return description - 描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置描述
     *
     * @param description 描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取标题图
     *
     * @return title_img - 标题图
     */
    public String getTitleImg() {
        return titleImg;
    }

    /**
     * 设置标题图
     *
     * @param titleImg 标题图
     */
    public void setTitleImg(String titleImg) {
        this.titleImg = titleImg;
    }

    /**
     * 获取内容图
     *
     * @return content_img - 内容图
     */
    public String getContentImg() {
        return contentImg;
    }

    /**
     * 设置内容图
     *
     * @param contentImg 内容图
     */
    public void setContentImg(String contentImg) {
        this.contentImg = contentImg;
    }

    /**
     * 获取专题模板
     *
     * @return topic_tpl - 专题模板
     */
    public String getTopicTpl() {
        return topicTpl;
    }

    /**
     * 设置专题模板
     *
     * @param topicTpl 专题模板
     */
    public void setTopicTpl(String topicTpl) {
        this.topicTpl = topicTpl;
    }

    /**
     * 获取排列顺序
     *
     * @return sort_id - 排列顺序
     */
    public Integer getSortId() {
        return sortId;
    }

    /**
     * 设置排列顺序
     *
     * @param sortId 排列顺序
     */
    public void setSortId(Integer sortId) {
        this.sortId = sortId;
    }

    /**
     * 获取是否推??
     *
     * @return is_recommend - 是否推??
     */
    public Boolean getIsRecommend() {
        return isRecommend;
    }

    /**
     * 设置是否推??
     *
     * @param isRecommend 是否推??
     */
    public void setIsRecommend(Boolean isRecommend) {
        this.isRecommend = isRecommend;
    }
}