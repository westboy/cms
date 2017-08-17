package com.zhiliao.mybatis.model;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "t_cms_model_filed")
public class TCmsModelFiled implements Serializable {
    /**
     * 字段编号
     */
    @Id
    @Column(name = "filed_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private Integer filedId;

    /**
     * 模型编号
     */
    @Column(name = "model_id")
    private Integer modelId;

    /**
     * 字段名称
     */
    @Column(name = "filed_name")
    private String filedName;

    /**
     * 字段类型（如文本）
     */
    @Column(name = "filed_class")
    private String filedClass;

    /**
     * 字段类别（数据库类别）
     */
    @Column(name = "filed_type")
    private String filedType;

    /**
     * 别名
     */
    private String alias;

    /**
     * 是否为空
     */
    @Column(name = "not_null")
    private Boolean notNull;

    /**
     * 字段长度
     */
    @Column(name = "filed_length")
    private Integer filedLength;

    /**
     * 是否为主键
     */
    @Column(name = "is_primary")
    private Boolean isPrimary;

    /**
     * 字段设置
     */
    private String setting;

    /**
     * 字段值
     */
    @Column(name = "filed_value")
    private String filedValue;

    private static final long serialVersionUID = 1L;

    /**
     * 获取字段编号
     *
     * @return filed_id - 字段编号
     */
    public Integer getFiledId() {
        return filedId;
    }

    /**
     * 设置字段编号
     *
     * @param filedId 字段编号
     */
    public void setFiledId(Integer filedId) {
        this.filedId = filedId;
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
     * 获取字段名称
     *
     * @return filed_name - 字段名称
     */
    public String getFiledName() {
        return filedName;
    }

    /**
     * 设置字段名称
     *
     * @param filedName 字段名称
     */
    public void setFiledName(String filedName) {
        this.filedName = filedName;
    }

    /**
     * 获取字段类型（如文本）
     *
     * @return filed_class - 字段类型（如文本）
     */
    public String getFiledClass() {
        return filedClass;
    }

    /**
     * 设置字段类型（如文本）
     *
     * @param filedClass 字段类型（如文本）
     */
    public void setFiledClass(String filedClass) {
        this.filedClass = filedClass;
    }

    /**
     * 获取字段类别（数据库类别）
     *
     * @return filed_type - 字段类别（数据库类别）
     */
    public String getFiledType() {
        return filedType;
    }

    /**
     * 设置字段类别（数据库类别）
     *
     * @param filedType 字段类别（数据库类别）
     */
    public void setFiledType(String filedType) {
        this.filedType = filedType;
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
     * 获取是否为空
     *
     * @return not_null - 是否为空
     */
    public Boolean getNotNull() {
        return notNull;
    }

    /**
     * 设置是否为空
     *
     * @param notNull 是否为空
     */
    public void setNotNull(Boolean notNull) {
        this.notNull = notNull;
    }

    /**
     * 获取字段长度
     *
     * @return filed_length - 字段长度
     */
    public Integer getFiledLength() {
        return filedLength;
    }

    /**
     * 设置字段长度
     *
     * @param filedLength 字段长度
     */
    public void setFiledLength(Integer filedLength) {
        this.filedLength = filedLength;
    }

    /**
     * 获取是否为主键
     *
     * @return is_primary - 是否为主键
     */
    public Boolean getIsPrimary() {
        return isPrimary;
    }

    /**
     * 设置是否为主键
     *
     * @param isPrimary 是否为主键
     */
    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    /**
     * 获取字段设置
     *
     * @return setting - 字段设置
     */
    public String getSetting() {
        return setting;
    }

    /**
     * 设置字段设置
     *
     * @param setting 字段设置
     */
    public void setSetting(String setting) {
        this.setting = setting;
    }

    /**
     * 获取字段值
     *
     * @return filed_value - 字段值
     */
    public String getFiledValue() {
        return filedValue;
    }

    /**
     * 设置字段值
     *
     * @param filedValue 字段值
     */
    public void setFiledValue(String filedValue) {
        this.filedValue = filedValue;
    }
}