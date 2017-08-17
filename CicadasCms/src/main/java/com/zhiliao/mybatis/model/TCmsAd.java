package com.zhiliao.mybatis.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "t_cms_ad")
public class TCmsAd implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private Integer id;

    /**
     * 广告名称
     */
    @Column(name = "ad_name")
    private String adName;

    /**
     * 开始时间
     */
    @Column(name = "start_date")
    private Date startDate;

    /**
     * 结束时间
     */
    @Column(name = "end_date")
    private Date endDate;

    /**
     * 排序编号
     */
    @Column(name = "sort_id")
    private Integer sortId;

    /**
     * 广告分组
     */
    @Column(name = "group_id")
    private Integer groupId;

    /**
     * 广告内容（如代码）
     */
    @Column(name = "ad_body")
    private String adBody;

    private static final long serialVersionUID = 1L;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取广告名称
     *
     * @return ad_name - 广告名称
     */
    public String getAdName() {
        return adName;
    }

    /**
     * 设置广告名称
     *
     * @param adName 广告名称
     */
    public void setAdName(String adName) {
        this.adName = adName;
    }

    /**
     * 获取开始时间
     *
     * @return start_date - 开始时间
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * 设置开始时间
     *
     * @param startDate 开始时间
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * 获取结束时间
     *
     * @return end_date - 结束时间
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * 设置结束时间
     *
     * @param endDate 结束时间
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * 获取排序编号
     *
     * @return sort_id - 排序编号
     */
    public Integer getSortId() {
        return sortId;
    }

    /**
     * 设置排序编号
     *
     * @param sortId 排序编号
     */
    public void setSortId(Integer sortId) {
        this.sortId = sortId;
    }

    /**
     * 获取广告分组
     *
     * @return group_id - 广告分组
     */
    public Integer getGroupId() {
        return groupId;
    }

    /**
     * 设置广告分组
     *
     * @param groupId 广告分组
     */
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    /**
     * 获取广告内容（如代码）
     *
     * @return ad_body - 广告内容（如代码）
     */
    public String getAdBody() {
        return adBody;
    }

    /**
     * 设置广告内容（如代码）
     *
     * @param adBody 广告内容（如代码）
     */
    public void setAdBody(String adBody) {
        this.adBody = adBody;
    }
}