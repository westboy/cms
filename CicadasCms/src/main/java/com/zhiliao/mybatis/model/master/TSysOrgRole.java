package com.zhiliao.mybatis.model.master;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "t_sys_org_role")
public class TSysOrgRole implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private Integer id;

    /**
     * 机构编号
     */
    @Column(name = "org_id")
    private Integer orgId;

    /**
     * 角色编号
     */
    @Column(name = "role_id")
    private Integer roleId;

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
     * 获取机构编号
     *
     * @return org_id - 机构编号
     */
    public Integer getOrgId() {
        return orgId;
    }

    /**
     * 设置机构编号
     *
     * @param orgId 机构编号
     */
    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    /**
     * 获取角色编号
     *
     * @return role_id - 角色编号
     */
    public Integer getRoleId() {
        return roleId;
    }

    /**
     * 设置角色编号
     *
     * @param roleId 角色编号
     */
    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}