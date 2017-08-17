package com.zhiliao.mybatis.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "t_cms_model")
public class TCmsModel implements Serializable {
    @Id
    @Column(name = "model_id")
    private Integer modelId;

    /**
     * 模型名称
     */
    @Column(name = "model_name")
    private String modelName;

    /**
     * 模型表名称
     */
    @Column(name = "table_name")
    private String tableName;

    /**
     * 站点id
     */
    @Column(name = "site_id")
    private Integer siteId;

    /**
     * 字段描述
     */
    private String des;

    /**
     * 状态
     */
    private Boolean status;

    private static final long serialVersionUID = 1L;

    /**
     * @return model_id
     */
    public Integer getModelId() {
        return modelId;
    }

    /**
     * @param modelId
     */
    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    /**
     * 获取模型名称
     *
     * @return model_name - 模型名称
     */
    public String getModelName() {
        return modelName;
    }

    /**
     * 设置模型名称
     *
     * @param modelName 模型名称
     */
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    /**
     * 获取模型表名称
     *
     * @return table_name - 模型表名称
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * 设置模型表名称
     *
     * @param tableName 模型表名称
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
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
     * 获取字段描述
     *
     * @return desc - 字段描述
     */
    public String getDes() {
        return des;
    }

    /**
     * 设置字段描述
     *
     * @param desc 字段描述
     */
    public void setDes(String desc) {
        this.des = desc;
    }

    /**
     * 获取状态
     *
     * @return status - 状态
     */
    public Boolean getStatus() {
        return status;
    }

    /**
     * 设置状态
     *
     * @param status 状态
     */
    public void setStatus(Boolean status) {
        this.status = status;
    }
}