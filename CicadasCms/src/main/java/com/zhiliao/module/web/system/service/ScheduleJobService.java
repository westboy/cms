package com.zhiliao.module.web.system.service;

import com.zhiliao.common.base.BaseService;
import com.zhiliao.mybatis.model.master.TSysScheduleJob;

public interface ScheduleJobService extends BaseService<TSysScheduleJob,Integer>{

    /* 初始化任务*/
   void initSchedule();

    /* 更改状态 */
    String changeStatus(int id, String status);


}
