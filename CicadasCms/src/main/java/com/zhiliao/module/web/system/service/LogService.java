package com.zhiliao.module.web.system.service;

import com.github.pagehelper.PageInfo;
import com.zhiliao.mybatis.model.TSysLog;

import java.util.Date;

/**
 * Created by binary on 2017/4/10.
 */
public interface LogService {

    void saveLog(String content, Date createTime, String username, String type);

    PageInfo<TSysLog>  page(Integer pageNumer,Integer pageSize,String startTime,String endTime);

    String deleteById(Integer[] logId);

}
