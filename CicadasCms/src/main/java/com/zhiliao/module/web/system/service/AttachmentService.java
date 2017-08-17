package com.zhiliao.module.web.system.service;

import com.github.pagehelper.PageInfo;
import com.zhiliao.mybatis.model.TSysAttachment;

import java.util.List;

/**
 * Description:
 *
 * @author Jin
 * @create 2017-08-08
 **/
public interface AttachmentService{

    void save(TSysAttachment pojo);

    String delete(Long[] ids);

    List<TSysAttachment> findList(TSysAttachment pojo);

    List<TSysAttachment> findAll();

    PageInfo<TSysAttachment> page(Integer pageNumber, Integer pageSize, TSysAttachment pojo);

    PageInfo<TSysAttachment> page(Integer pageNumber, Integer pageSize);

}
