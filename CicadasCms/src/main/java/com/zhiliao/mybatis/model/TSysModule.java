package com.zhiliao.mybatis.model;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "t_sys_module")
public class TSysModule implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private Integer id;

    @Column(name = "module_name")
    private String moduleName;

    /**
     * 是否启用
     */
    @Column(name = "is_enable")
    private Boolean isEnable;

    /**
     * 是否自动展开
     */
    @Column(name = "is_auto_expand")
    private Boolean isAutoExpand;

    /**
     * 菜单url
     */
    private String url;

    /**
     * 菜单节点图标
     */
    @Column(name = "icon_name")
    private String iconName;

    /**
     * 权限key
     */
    @Column(name = "permission_key")
    private String permissionKey;

    /**
     * 父id编号
     */
    private Integer pid;

    /**
     * 有子节点
     */
    @Column(name = "has_child")
    private Boolean hasChild;

    /**
     * 排序
     */
    @OrderBy("desc")
    @Column(name = "sort_no")
    private Integer sortNo;

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
     * @return module_name
     */
    public String getModuleName() {
        return moduleName;
    }

    /**
     * @param moduleName
     */
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    /**
     * 获取是否启用
     *
     * @return is_enable - 是否启用
     */
    public Boolean getIsEnable() {
        return isEnable;
    }

    /**
     * 设置是否启用
     *
     * @param isEnable 是否启用
     */
    public void setIsEnable(Boolean isEnable) {
        this.isEnable = isEnable;
    }

    /**
     * 获取是否自动展开
     *
     * @return is_auto_expand - 是否自动展开
     */
    public Boolean getIsAutoExpand() {
        return isAutoExpand;
    }

    /**
     * 设置是否自动展开
     *
     * @param isAutoExpand 是否自动展开
     */
    public void setIsAutoExpand(Boolean isAutoExpand) {
        this.isAutoExpand = isAutoExpand;
    }

    /**
     * 获取菜单url
     *
     * @return url - 菜单url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置菜单url
     *
     * @param url 菜单url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取菜单节点图标
     *
     * @return icon_name - 菜单节点图标
     */
    public String getIconName() {
        return iconName;
    }

    /**
     * 设置菜单节点图标
     *
     * @param iconName 菜单节点图标
     */
    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    /**
     * 获取权限key
     *
     * @return permission_key - 权限key
     */
    public String getPermissionKey() {
        return permissionKey;
    }

    /**
     * 设置权限key
     *
     * @param permissionKey 权限key
     */
    public void setPermissionKey(String permissionKey) {
        this.permissionKey = permissionKey;
    }

    /**
     * 获取父id编号
     *
     * @return pid - 父id编号
     */
    public Integer getPid() {
        return pid;
    }

    /**
     * 设置父id编号
     *
     * @param pid 父id编号
     */
    public void setPid(Integer pid) {
        this.pid = pid;
    }

    /**
     * 获取有子节点
     *
     * @return has_child - 有子节点
     */
    public Boolean getHasChild() {
        return hasChild;
    }

    /**
     * 设置有子节点
     *
     * @param hasChild 有子节点
     */
    public void setHasChild(Boolean hasChild) {
        this.hasChild = hasChild;
    }

    /**
     * 获取排序
     *
     * @return sort_no - 排序
     */
    public Integer getSortNo() {
        return sortNo;
    }

    /**
     * 设置排序
     *
     * @param sortNo 排序
     */
    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }
}