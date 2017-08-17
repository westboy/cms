package com.zhiliao.mybatis.model;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "t_cms_user_site")
public class TCmsUserSite implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private Integer id;

    /**
     * 用户id（这个用户id只代表后台管理员用户）
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 站点id
     */
    @Column(name = "site_id")
    private Integer siteId;

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
     * 获取用户id（这个用户id只代表后台管理员用户）
     *
     * @return user_id - 用户id（这个用户id只代表后台管理员用户）
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户id（这个用户id只代表后台管理员用户）
     *
     * @param userId 用户id（这个用户id只代表后台管理员用户）
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
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
}