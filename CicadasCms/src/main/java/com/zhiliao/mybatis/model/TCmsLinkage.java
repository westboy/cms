package com.zhiliao.mybatis.model;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "t_cms_linkage")
public class TCmsLinkage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private Integer id;

    /**
     * 站点id
     */
    @Column(name = "site_id")
    private Integer siteId;

    private String name;

    @Column(name = "parent_id")
    private Integer parentId;

    private Integer grade;

    private String childs;

    @Column(name = "sort_id")
    private Short sortId;

    /**
     * 是否为通用
     */
    @Column(name = "is_common")
    private Boolean isCommon;

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
     * 获取站点id
     *
     * @return site_id - 站点id
     */
    public Integer getSiteId() {
        return siteId;
    }

    /**
     * 设置站点id
     *
     * @param siteId 站点id
     */
    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return parent_id
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * @param parentId
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * @return grade
     */
    public Integer getGrade() {
        return grade;
    }

    /**
     * @param grade
     */
    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    /**
     * @return childs
     */
    public String getChilds() {
        return childs;
    }

    /**
     * @param childs
     */
    public void setChilds(String childs) {
        this.childs = childs;
    }

    /**
     * @return sort_id
     */
    public Short getSortId() {
        return sortId;
    }

    /**
     * @param sortId
     */
    public void setSortId(Short sortId) {
        this.sortId = sortId;
    }

    /**
     * 获取是否为通用
     *
     * @return is_common - 是否为通用
     */
    public Boolean getIsCommon() {
        return isCommon;
    }

    /**
     * 设置是否为通用
     *
     * @param isCommon 是否为通用
     */
    public void setIsCommon(Boolean isCommon) {
        this.isCommon = isCommon;
    }
}