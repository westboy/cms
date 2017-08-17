package com.zhiliao.module.web.system.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhiliao.common.utils.JsonUtil;
import com.zhiliao.common.utils.StrUtil;
import com.zhiliao.mybatis.mapper.TSysLogMapper;
import com.zhiliao.mybatis.model.TSysLog;
import com.zhiliao.module.web.system.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Description:log service
 *
 * @author Jin
 * @create 2017-04-10
 **/
@Service
public class LogServiceImpl implements LogService{

    @Autowired
    private TSysLogMapper logMapper;

    @Async
    @Override
    public void saveLog(String content, Date createTime,String username,String type) {
        TSysLog log = new TSysLog();
        log.setContent(content);
        log.setCreatetime(createTime);
        log.setUsername(username);
        log.setType(type);
        logMapper.insert(log);

    }

    @Override
    public PageInfo<TSysLog> page(Integer pageNumer, Integer pageSize,String startTime,String endTime) {

        PageHelper.startPage(pageNumer,pageSize);

        if(!StrUtil.isBlank(startTime)&&!StrUtil.isBlank(endTime))
            return new PageInfo<>(logMapper.selectByDate(startTime, endTime));
        if(!StrUtil.isBlank(startTime))
            return new PageInfo<>(logMapper.selectByStartDate(startTime));
        if(!StrUtil.isBlank(endTime))
            return new PageInfo<>(logMapper.selectByEndDate(endTime));

        return new PageInfo<>(logMapper.selectAll());
    }

    @Override
    public String deleteById(Integer[] logId) {
        boolean flag_ = false;
        if(logId.length>0){
            for(int id :logId){
                if(logMapper.deleteByPrimaryKey(id)>0) {
                    flag_ = true;
                }
            }
        }
        if(flag_)
           return JsonUtil.toSUCCESS("删除日志成功!");
        return JsonUtil.toERROR("删除日志失败!");
    }

}
