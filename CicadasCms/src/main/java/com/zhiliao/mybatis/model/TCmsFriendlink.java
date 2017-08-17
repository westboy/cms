package com.zhiliao.mybatis.model;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "t_cms_friendlink")
public class TCmsFriendlink implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private Integer id;

    @Column(name = "link_name")
    private String linkName;

    @Column(name = "link_type")
    private Integer linkType;

    private String img;

    @Column(name = "link_url")
    private String linkUrl;

    @Column(name = "sort_id")
    private Integer sortId;

    private Boolean status;

    @Column(name = "group_id")
    private Integer groupId;

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
     * @return link_name
     */
    public String getLinkName() {
        return linkName;
    }

    /**
     * @param linkName
     */
    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    /**
     * @return link_type
     */
    public Integer getLinkType() {
        return linkType;
    }

    /**
     * @param linkType
     */
    public void setLinkType(Integer linkType) {
        this.linkType = linkType;
    }

    /**
     * @return img
     */
    public String getImg() {
        return img;
    }

    /**
     * @param img
     */
    public void setImg(String img) {
        this.img = img;
    }

    /**
     * @return link_url
     */
    public String getLinkUrl() {
        return linkUrl;
    }

    /**
     * @param linkUrl
     */
    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    /**
     * @return sort_id
     */
    public Integer getSortId() {
        return sortId;
    }

    /**
     * @param sortId
     */
    public void setSortId(Integer sortId) {
        this.sortId = sortId;
    }

    /**
     * @return status
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }

    /**
     * @return group_id
     */
    public Integer getGroupId() {
        return groupId;
    }

    /**
     * @param groupId
     */
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}