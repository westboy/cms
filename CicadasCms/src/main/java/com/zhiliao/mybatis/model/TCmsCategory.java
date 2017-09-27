package com.zhiliao.mybatis.model;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "t_cms_category")
public class TCmsCategory implements Serializable {
    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private Long categoryId;

    /**
     * 别名
     */
    private String alias;

    /**
     * 分类明细
     */
    @Column(name = "category_name")
    private String categoryName;

    /**
     * 站点编号
     */
    @Column(name = "site_id")
    private Integer siteId;

    /**
     * 父类编号
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 模型编号
     */
    @Column(name = "model_id")
    private Integer modelId;

    /**
     * 单页（0：不是，1：是）
     */
    private Boolean alone;

    /**
     * 模板
     */
    @Column(name = "index_tpl")
    private String indexTpl;

    /**
     * 列表页面
     */
    @Column(name = "list_tpl")
    private String listTpl;

    /**
     * 内容页面
     */
    @Column(name = "content_tpl")
    private String contentTpl;

    /**
     * 导航
     */
    @Column(name = "is_nav")
    private Boolean isNav;

    /**
     * 地址
     */
    private String url;

    /**
     * 是否有子类
     */
    @Column(name = "has_child")
    private Boolean hasChild;

    /**
     * 是否为通用栏目（子站点也默认继承此栏目）
     */
    @Column(name = "is_common")
    private Boolean isCommon;

    @Column(name = "allow_front_submit")
    private Boolean allowFrontSubmit;

    /**
     * 栏目分页数量
     */
    @Column(name = "page_size")
    private Integer pageSize;

    /**
     * 当前栏目下的是否支持全文搜索
     */
    @Column(name = "allow_search")
    private Boolean allowSearch;

    /**
     * 栏目图标
     */
    @Column(name = "category_icon")
    private String categoryIcon;

    /**
     * 栏目权限标识
     */
    @Column(name = "permission_key")
    private String permissionKey;

    @OrderBy("DESC")
    @Column(name = "sort_id")
    private Integer sortId;

    /**
     * 文本
     */
    private String content;

    private static final long serialVersionUID = 1L;

    /**
     * @return category_id
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryId
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * 获取别名
     *
     * @return alias - 别名
     */
    public String getAlias() {
        return alias;
    }

    /**
     * 设置别名
     *
     * @param alias 别名
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * 获取分类明细
     *
     * @return category_name - 分类明细
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * 设置分类明细
     *
     * @param categoryName 分类明细
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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
     * 获取父类编号
     *
     * @return parent_id - 父类编号
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 设置父类编号
     *
     * @param parentId 父类编号
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取模型编号
     *
     * @return model_id - 模型编号
     */
    public Integer getModelId() {
        return modelId;
    }

    /**
     * 设置模型编号
     *
     * @param modelId 模型编号
     */
    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    /**
     * 获取单页（0：不是，1：是）
     *
     * @return alone - 单页（0：不是，1：是）
     */
    public Boolean getAlone() {
        return alone;
    }

    /**
     * 设置单页（0：不是，1：是）
     *
     * @param alone 单页（0：不是，1：是）
     */
    public void setAlone(Boolean alone) {
        this.alone = alone;
    }

    /**
     * 获取模板
     *
     * @return index_tpl - 模板
     */
    public String getIndexTpl() {
        return indexTpl;
    }

    /**
     * 设置模板
     *
     * @param indexTpl 模板
     */
    public void setIndexTpl(String indexTpl) {
        this.indexTpl = indexTpl;
    }

    /**
     * 获取列表页面
     *
     * @return list_tpl - 列表页面
     */
    public String getListTpl() {
        return listTpl;
    }

    /**
     * 设置列表页面
     *
     * @param listTpl 列表页面
     */
    public void setListTpl(String listTpl) {
        this.listTpl = listTpl;
    }

    /**
     * 获取内容页面
     *
     * @return content_tpl - 内容页面
     */
    public String getContentTpl() {
        return contentTpl;
    }

    /**
     * 设置内容页面
     *
     * @param contentTpl 内容页面
     */
    public void setContentTpl(String contentTpl) {
        this.contentTpl = contentTpl;
    }

    /**
     * 获取导航
     *
     * @return is_nav - 导航
     */
    public Boolean getIsNav() {
        return isNav;
    }

    /**
     * 设置导航
     *
     * @param isNav 导航
     */
    public void setIsNav(Boolean isNav) {
        this.isNav = isNav;
    }

    /**
     * 获取地址
     *
     * @return url - 地址
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置地址
     *
     * @param url 地址
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取是否有子类
     *
     * @return has_child - 是否有子类
     */
    public Boolean getHasChild() {
        return hasChild;
    }

    /**
     * 设置是否有子类
     *
     * @param hasChild 是否有子类
     */
    public void setHasChild(Boolean hasChild) {
        this.hasChild = hasChild;
    }

    /**
     * 获取是否为通用栏目（子站点也默认继承此栏目）
     *
     * @return is_common - 是否为通用栏目（子站点也默认继承此栏目）
     */
    public Boolean getIsCommon() {
        return isCommon;
    }

    /**
     * 设置是否为通用栏目（子站点也默认继承此栏目）
     *
     * @param isCommon 是否为通用栏目（子站点也默认继承此栏目）
     */
    public void setIsCommon(Boolean isCommon) {
        this.isCommon = isCommon;
    }

    /**
     * @return allow_front_submit
     */
    public Boolean getAllowFrontSubmit() {
        return allowFrontSubmit;
    }

    /**
     * @param allowFrontSubmit
     */
    public void setAllowFrontSubmit(Boolean allowFrontSubmit) {
        this.allowFrontSubmit = allowFrontSubmit;
    }

    /**
     * 获取栏目分页数量
     *
     * @return page_size - 栏目分页数量
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * 设置栏目分页数量
     *
     * @param pageSize 栏目分页数量
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 获取当前栏目下的是否支持全文搜索
     *
     * @return allow_search - 当前栏目下的是否支持全文搜索
     */
    public Boolean getAllowSearch() {
        return allowSearch;
    }

    /**
     * 设置当前栏目下的是否支持全文搜索
     *
     * @param allowSearch 当前栏目下的是否支持全文搜索
     */
    public void setAllowSearch(Boolean allowSearch) {
        this.allowSearch = allowSearch;
    }

    /**
     * 获取栏目图标
     *
     * @return category_icon - 栏目图标
     */
    public String getCategoryIcon() {
        return categoryIcon;
    }

    /**
     * 设置栏目图标
     *
     * @param categoryIcon 栏目图标
     */
    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    /**
     * 获取栏目权限标识
     *
     * @return permission_key - 栏目权限标识
     */
    public String getPermissionKey() {
        return permissionKey;
    }

    /**
     * 设置栏目权限标识
     *
     * @param permissionKey 栏目权限标识
     */
    public void setPermissionKey(String permissionKey) {
        this.permissionKey = permissionKey;
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
     * 获取文本
     *
     * @return content - 文本
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置文本
     *
     * @param content 文本
     */
    public void setContent(String content) {
        this.content = content;
    }
}