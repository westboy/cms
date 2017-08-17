package com.zhiliao.mybatis.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "t_sys_schedule_job")
public class TSysScheduleJob implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private Integer id;

    /**
     * cron表达式
     */
    @Column(name = "cron_expression")
    private String cronExpression;

    /**
     * 任务调用的方法名
     */
    @Column(name = "method_name")
    private String methodName;

    /**
     * 任务是否有状态
     */
    @Column(name = "is_concurrent")
    private String isConcurrent;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 更新者
     */
    @Column(name = "update_by")
    private String updateBy;

    /**
     * 任务执行时调用哪个类的方法 包名+类名
     */
    @Column(name = "bean_class")
    private String beanClass;

    /**
     * 创建时间
     */
    @Column(name = "create_date")
    private Date createDate;

    /**
     * 任务状态
     */
    @Column(name = "job_status")
    private String jobStatus;

    /**
     * 任务分组
     */
    @Column(name = "job_group")
    private String jobGroup;

    /**
     * 更新时间
     */
    @Column(name = "update_date")
    private Date updateDate;

    /**
     * 创建者
     */
    @Column(name = "create_by")
    private String createBy;

    /**
     * Spring bean
     */
    @Column(name = "spring_bean")
    private String springBean;

    /**
     * 任务名
     */
    @Column(name = "job_name")
    private String jobName;

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
     * 获取cron表达式
     *
     * @return cron_expression - cron表达式
     */
    public String getCronExpression() {
        return cronExpression;
    }

    /**
     * 设置cron表达式
     *
     * @param cronExpression cron表达式
     */
    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    /**
     * 获取任务调用的方法名
     *
     * @return method_name - 任务调用的方法名
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * 设置任务调用的方法名
     *
     * @param methodName 任务调用的方法名
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * 获取任务是否有状态
     *
     * @return is_concurrent - 任务是否有状态
     */
    public String getIsConcurrent() {
        return isConcurrent;
    }

    /**
     * 设置任务是否有状态
     *
     * @param isConcurrent 任务是否有状态
     */
    public void setIsConcurrent(String isConcurrent) {
        this.isConcurrent = isConcurrent;
    }

    /**
     * 获取任务描述
     *
     * @return description - 任务描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置任务描述
     *
     * @param description 任务描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取更新者
     *
     * @return update_by - 更新者
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     * 设置更新者
     *
     * @param updateBy 更新者
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    /**
     * 获取任务执行时调用哪个类的方法 包名+类名
     *
     * @return bean_class - 任务执行时调用哪个类的方法 包名+类名
     */
    public String getBeanClass() {
        return beanClass;
    }

    /**
     * 设置任务执行时调用哪个类的方法 包名+类名
     *
     * @param beanClass 任务执行时调用哪个类的方法 包名+类名
     */
    public void setBeanClass(String beanClass) {
        this.beanClass = beanClass;
    }

    /**
     * 获取创建时间
     *
     * @return create_date - 创建时间
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 设置创建时间
     *
     * @param createDate 创建时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取任务状态
     *
     * @return job_status - 任务状态
     */
    public String getJobStatus() {
        return jobStatus;
    }

    /**
     * 设置任务状态
     *
     * @param jobStatus 任务状态
     */
    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    /**
     * 获取任务分组
     *
     * @return job_group - 任务分组
     */
    public String getJobGroup() {
        return jobGroup;
    }

    /**
     * 设置任务分组
     *
     * @param jobGroup 任务分组
     */
    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    /**
     * 获取更新时间
     *
     * @return update_date - 更新时间
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 设置更新时间
     *
     * @param updateDate 更新时间
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * 获取创建者
     *
     * @return create_by - 创建者
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 设置创建者
     *
     * @param createBy 创建者
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    /**
     * 获取Spring bean
     *
     * @return spring_bean - Spring bean
     */
    public String getSpringBean() {
        return springBean;
    }

    /**
     * 设置Spring bean
     *
     * @param springBean Spring bean
     */
    public void setSpringBean(String springBean) {
        this.springBean = springBean;
    }

    /**
     * 获取任务名
     *
     * @return job_name - 任务名
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * 设置任务名
     *
     * @param jobName 任务名
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
}