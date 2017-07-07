package com.zhiliao.module.web.system.service;

import com.zhiliao.common.base.BaseService;
import com.zhiliao.mybatis.model.master.TSysScheduleJob;
import org.quartz.SchedulerException;

public interface ScheduleJobService extends BaseService<TSysScheduleJob,Integer>{

    /**
     *
     * @title: initSchedule
     * @description: 初始化任务
     * @throws SchedulerException
     * @return: void
     */
    public void initSchedule();

    /**
     * 更改状态
     *
     * @throws SchedulerException
     */
    public void changeStatus(String jobId, String cmd);



}
