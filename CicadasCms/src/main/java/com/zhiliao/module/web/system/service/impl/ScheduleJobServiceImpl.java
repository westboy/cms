package com.zhiliao.module.web.system.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhiliao.common.constant.Const;
import com.zhiliao.common.exception.SystemException;
import com.zhiliao.component.quartz.QuartzManager;
import com.zhiliao.component.quartz.util.PojoToScheduleJob;
import com.zhiliao.common.utils.CmsUtil;
import com.zhiliao.common.utils.JsonUtil;
import com.zhiliao.common.utils.StrUtil;
import com.zhiliao.module.web.system.service.ScheduleJobService;
import com.zhiliao.mybatis.mapper.TSysScheduleJobMapper;
import com.zhiliao.mybatis.model.TCmsSite;
import com.zhiliao.mybatis.model.TSysScheduleJob;
import org.quartz.CronExpression;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.PostConstruct;
import java.util.Date;
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
    private QuartzManager quartzManager;

    @Autowired
    private TSysScheduleJobMapper scheduleJobMapper;

    @Lazy
    @PostConstruct
    @Override
    public void initSchedule(){
        List<TSysScheduleJob> jobList = this.findAll();
        for (TSysScheduleJob pojo : jobList) {
            try {
                quartzManager.addJob(PojoToScheduleJob.convert(pojo));
            } catch (SchedulerException e) {
                throw  new SystemException(e.getMessage());
            }
        }

    }

    @Override
    public String changeStatus(int id, String status)  {

        TSysScheduleJob pojo = this.findById(id);
        if (pojo== null)  throw new SystemException("任务 ["+id+"] 不存在");
        try {
            if ("stop".equals(status)) {
                quartzManager.deleteJob(PojoToScheduleJob.convert(pojo));
                pojo.setJobStatus(Const.STATUS_NOT_RUNNING);
            } else if ("start".equals(status)) {
                pojo.setJobStatus(Const.STATUS_RUNNING);
                quartzManager.addJob(PojoToScheduleJob.convert(pojo));
            }
        } catch (SchedulerException e) {
            throw new SystemException(e.getMessage());
        }
        if(this.scheduleJobMapper.updateByPrimaryKey(pojo)>0)
            return JsonUtil.toSUCCESS("添加成功！","schedule-tab",false);
        return JsonUtil.toERROR("添加失败！");
    }

    @Override
    public String save(TSysScheduleJob pojo) {
        if (!CronExpression.isValidExpression(pojo.getCronExpression())) return JsonUtil.toERROR("cron表达式不正确！");
        pojo.setCreateDate(new Date());
        pojo.setJobStatus("0");
        if(scheduleJobMapper.insertSelective(pojo)>0) {
            try {
                quartzManager.addJob(PojoToScheduleJob.convert(pojo));
            } catch (SchedulerException e) {
                throw new SystemException(e.getMessage());
            }
            return JsonUtil.toSUCCESS("添加成功！", "schedule-tab", true);
        }
        return JsonUtil.toERROR("添加失败！");
    }

    @Override
    public String update(TSysScheduleJob pojo) {
        if (!CronExpression.isValidExpression(pojo.getCronExpression())) return JsonUtil.toERROR("cron表达式不正确！");
        if (Const.STATUS_RUNNING.equals(pojo.getJobStatus())) {
            try {
                quartzManager.updateJobCron(PojoToScheduleJob.convert(pojo));
            } catch (SchedulerException e) {
                throw  new SystemException(e.getMessage());
            }
        }
        if(scheduleJobMapper.updateByPrimaryKeySelective(pojo)>0)
            return JsonUtil.toSUCCESS("更新成功！","schedule-tab",false);
        return JsonUtil.toERROR("更新失败！");
    }

    @Override
    public String delete(Integer[] ids){
        if(CmsUtil.isNullOrEmpty(ids))
            return JsonUtil.toERROR("操作失败！");
        for(Integer id :ids) {
            TSysScheduleJob pojo = scheduleJobMapper.selectByPrimaryKey(id);
            if(CmsUtil.isNullOrEmpty(pojo)) throw new SystemException("操作失败!");
            try {
                quartzManager.deleteJob(PojoToScheduleJob.convert(pojo));
            } catch (SchedulerException e) {
                throw  new SystemException(e.getMessage());
            }
            scheduleJobMapper.deleteByPrimaryKey(id);
        }
        return JsonUtil.toSUCCESS("操作成功！","schedule-tab",false);
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
        if(StrUtil.isBlank(pojo.getJobName()))
            return new PageInfo<>(this.findAll());
        Example example = new Example(TCmsSite.class);
        example.createCriteria()
                .andCondition("job_name like '"+pojo.getJobName()+"'");
        return new PageInfo<>(scheduleJobMapper.selectByExample(example));

    }

    @Override
    public PageInfo<TSysScheduleJob> page(Integer pageNumber, Integer pageSize) {
        PageHelper.startPage(pageNumber,pageSize);
        return new PageInfo<>(this.findAll());
    }
}
