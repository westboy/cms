package com.zhiliao.mybatis.model;

import com.zhiliao.common.annotation.ExcelField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "t_cms_content")
public class TCmsContent implements Serializable {


    @ExcelField("内容编号")
    @Id
    @Column(name = "content_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private Long contentId;

    /**
     * 站点编号
     */
    @ExcelField("站点编号")
    @Column(name = "site_id")
    private Integer siteId;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 栏目
     */
    @ExcelField("栏目编号")
    @Column(name = "category_id")
    private Long categoryId;

    /**
     * 模型id
     */
    @Column(name = "model_id")
    private Integer modelId;

    /**
     * 标题
     */
    @ExcelField("内容标题")
    private String title;

    /**
     * 关键字
     */
    @ExcelField("关键字")
    private String keywords;

    /**
     * 顶置
     */
    private Boolean top;

    /**
     * 推荐
     */
    private Boolean recommend;

    /**
     * 封面
     */
    @ExcelField("照片")
    private String thumb;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 发布时间
     */
    private Date inputdate;

    /**
     * 更新时间
     */
    private Date updatedate;

    /**
     * 文章地址
     */
    private String url;

    /**
     * 用户名
     */
    private String author;


    /**
     * 查看次数
     */
    @Column(name = "view_num")
    private Integer viewNum;

    /**
     * 描述
     */
    private String description;

    private static final long serialVersionUID = 1L;

    /**
     * @return content_id
     */
    public Long getContentId() {
        return contentId;
    }

    /**
     * @param contentId
     */
    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    /**
     * 获取站点编号
     *
     * @return site_id - 站点编号
     */
    public Integer getSiteId() {
        return siteId;
    }

    /**
     * 设置站点编号
     *
     * @param siteId 站点编号
     */
    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    /**
     * 获取用户id
     *
     * @return user_id - 用户id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户id
     *
     * @param userId 用户id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取栏目
     *
     * @return category_id - 栏目
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * 设置栏目
     *
     * @param categoryId 栏目
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * 获取模型id
     *
     * @return model_id - 模型id
     */
    public Integer getModelId() {
        return modelId;
    }

    /**
     * 设置模型id
     *
     * @param modelId 模型id
     */
    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    /**
     * 获取标题
     *
     * @return title - 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title;
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
     * 获取顶置
     *
     * @return top - 顶置
     */
    public Boolean getTop() {
        return top;
    }

    /**
     * 设置顶置
     *
     * @param top 顶置
     */
    public void setTop(Boolean top) {
        this.top = top;
    }

    /**
     * 获取推荐
     *
     * @return recommend - 推荐
     */
    public Boolean getRecommend() {
        return recommend;
    }

    /**
     * 设置推荐
     *
     * @param recommend 推荐
     */
    public void setRecommend(Boolean recommend) {
        this.recommend = recommend;
    }

    /**
     * 获取封面
     *
     * @return thumb - 封面
     */
    public String getThumb() {
        return thumb;
    }

    /**
     * 设置封面
     *
     * @param thumb 封面
     */
    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    /**
     * 获取状态
     *
     * @return status - 状态
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态
     *
     * @param status 状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取发布时间
     *
     * @return inputdate - 发布时间
     */
    public Date getInputdate() {
        return inputdate;
    }

    /**
     * 设置发布时间
     *
     * @param inputdate 发布时间
     */
    public void setInputdate(Date inputdate) {
        this.inputdate = inputdate;
    }

    /**
     * 获取更新时间
     *
     * @return updatedate - 更新时间
     */
    public Date getUpdatedate() {
        return updatedate;
    }

    /**
     * 设置更新时间
     *
     * @param updatedate 更新时间
     */
    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }

    /**
     * 获取文章地址
     *
     * @return url - 文章地址
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置文章地址
     *
     * @param url 文章地址
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取用户名
     *
     * @return author - 用户名
     */
    public String getAuthor() {
        return author;
    }

    /**
     * 设置用户名
     *
     * @param author 用户名
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * 获取查看次数
     *
     * @return view_num - 查看次数
     */
    public Integer getViewNum() {
        return viewNum;
    }

    /**
     * 设置查看次数
     *
     * @param viewNum 查看次数
     */
    public void setViewNum(Integer viewNum) {
        this.viewNum = viewNum;
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
}