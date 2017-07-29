package com.zhiliao.mybatis.model.master;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "t_sys_org")
public class TSysOrg implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private Long id;

    /**
     * 组织名称
     */
    private String name;

    /**
     * 地址
     */
    private String address;

    /**
     * 编号
     */
    private String code;

    /**
     * 父ID
     */
    private Long pid;

    /**
     * 联系电话
     */
    @Column(name = "tel_phone")
    private String telPhone;

    private static final long serialVersionUID = 1L;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取组织名称
     *
     * @return name - 组织名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置组织名称
     *
     * @param name 组织名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取地址
     *
     * @return address - 地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置地址
     *
     * @param address 地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取编号
     *
     * @return code - 编号
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置编号
     *
     * @param code 编号
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取父ID
     *
     * @return pid - 父ID
     */
    public Long getPid() {
        return pid;
    }

    /**
     * 设置父ID
     *
     * @param pid 父ID
     */
    public void setPid(Long pid) {
        this.pid = pid;
    }

    /**
     * 获取联系电话
     *
     * @return tel_phone - 联系电话
     */
    public String getTelPhone() {
        return telPhone;
    }

    /**
     * 设置联系电话
     *
     * @param telPhone 联系电话
     */
    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }
}