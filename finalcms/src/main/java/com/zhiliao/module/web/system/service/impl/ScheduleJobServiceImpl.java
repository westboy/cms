package com.zhiliao.module.web.system.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhiliao.common.exception.SystemException;
import com.zhiliao.common.quartz.QuartzManager;
import com.zhiliao.common.quartz.job.ScheduleJob;
import com.zhiliao.common.utils.CmsUtil;
import com.zhiliao.common.utils.JsonUtil;
import com.zhiliao.module.web.system.service.ScheduleJobService;
import com.zhiliao.mybatis.mapper.master.TSysScheduleJobMapper;
import com.zhiliao.mybatis.model.master.TSysScheduleJob;
import org.quartz.CronExpression;
import org.quartz.SchedulerException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 *
 * @author Jin
 * @create 2017-07-07
 **/
@Service
public class ScheduleJobServiceImpl implements ScheduleJobService{

    @Autowired
    QuartzManager quartzManager;

    @Autowired private TSysScheduleJobMapper scheduleJobMapper;

    @Override
    public void initSchedule(){
        quartzManager = new QuartzManager();
        List<TSysScheduleJob> jobList = this.findAll();
        for (TSysScheduleJob pojo : jobList) {
            ScheduleJob scheduleJob=new ScheduleJob();
            BeanUtils.copyProperties(scheduleJob,pojo);
            try {
                quartzManager.addJob(scheduleJob);
            } catch (SchedulerException e) {
                throw  new SystemException(e.getMessage());
            }
        }

    }

    @Override
    public void changeStatus(String jobId, String cmd)  {

        TSysScheduleJob pojo = this.findById(Integer.parseInt(jobId));
        if (pojo== null) return;
        try {
        ScheduleJob scheduleJob=new ScheduleJob();
        if ("stop".equals(cmd)) {
            BeanUtils.copyProperties(scheduleJob,pojo);
            quartzManager.deleteJob(scheduleJob);
            pojo.setJobStatus(ScheduleJob.STATUS_NOT_RUNNING);
        } else if ("start".equals(cmd)) {
            pojo.setJobStatus(ScheduleJob.STATUS_RUNNING);
            BeanUtils.copyProperties(scheduleJob,pojo);
            quartzManager.addJob(scheduleJob);
        }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        this.scheduleJobMapper.updateByPrimaryKey(pojo);
    }



    @Override
    public String save(TSysScheduleJob pojo) {
        if (!CronExpression.isValidExpression(pojo.getCronExpression())) return JsonUtil.toERROR("表达式不正确！");
        if(scheduleJobMapper.insertSelective(pojo)>0)
          return JsonUtil.toSUCCESS("添加成功！","schedule-tab",true);
        return JsonUtil.toERROR("添加失败！");
    }

    @Override
    public String update(TSysScheduleJob pojo) {
        if (ScheduleJob.STATUS_RUNNING.equals(pojo.getJobStatus())) {
            ScheduleJob scheduleJob=new ScheduleJob();
            BeanUtils.copyProperties(scheduleJob,pojo);
            try {
                quartzManager.updateJobCron(scheduleJob);
            } catch (SchedulerException e) {
                throw  new SystemException(e.getMessage());
            }
        }
        return JsonUtil.toSUCCESS("添加成功！","schedule-tab",false);
    }

    @Override
    public String delete(Integer[] ids){
        if(CmsUtil.isNullOrEmpty(ids))
            return JsonUtil.toERROR("操作失败！");
        for(Integer id :ids) {
            TSysScheduleJob pojo = scheduleJobMapper.selectByPrimaryKey(id);
            if(CmsUtil.isNullOrEmpty(pojo)) throw new SystemException("操作失败!");
            try {
                ScheduleJob scheduleJob=new ScheduleJob();
                BeanUtils.copyProperties(scheduleJob,pojo);
                quartzManager.deleteJob(scheduleJob);
            } catch (SchedulerException e) {
                throw  new SystemException(e.getMessage());
            }
            scheduleJobMapper.deleteByPrimaryKey(id);
        }
        return JsonUtil.toSUCCESS("添加成功！","schedule-tab",false);
    }

    @Override
    public TSysScheduleJob findById(Integer id) {
        return this.scheduleJobMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<TSysScheduleJob> findList(TSysScheduleJob pojo) {
        return this.scheduleJobMapper.select(pojo);
    }

    @Override
    public List<TSysScheduleJob> findAll() {
        return this.scheduleJobMapper.selectAll();
    }

    @Override
    public PageInfo<TSysScheduleJob> page(Integer pageNumber, Integer pageSize, TSysScheduleJob pojo) {
        PageHelper.startPage(pageNumber,pageSize);
        return new PageInfo<>(this.findList(pojo));
    }

    @Override
    public PageInfo<TSysScheduleJob> page(Integer pageNumber, Integer pageSize) {
        PageHelper.startPage(pageNumber,pageSize);
        return new PageInfo<>(this.findAll());
    }
}
