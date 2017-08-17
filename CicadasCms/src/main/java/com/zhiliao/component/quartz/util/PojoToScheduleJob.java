package com.zhiliao.component.quartz.util;

import com.zhiliao.component.quartz.job.ScheduleJob;
import com.zhiliao.mybatis.model.TSysScheduleJob;

/**
 * Description:
 *
 * @author Jin
 * @create 2017-07-10
 **/
public class PojoToScheduleJob {

    public static ScheduleJob convert(TSysScheduleJob pojo){
            ScheduleJob scheduleJob=new ScheduleJob();
            scheduleJob.setBeanClass(pojo.getBeanClass());
            scheduleJob.setCronExpression(pojo.getCronExpression());
            scheduleJob.setDescription(pojo.getDescription());
            scheduleJob.setIsConcurrent(pojo.getIsConcurrent());
            scheduleJob.setJobName(pojo.getJobName());
            scheduleJob.setJobGroup(pojo.getJobGroup());
            scheduleJob.setJobStatus(pojo.getJobStatus());
            scheduleJob.setMethodName(pojo.getMethodName());
            scheduleJob.setSpringBean(pojo.getSpringBean());
            return scheduleJob;


    }

}
