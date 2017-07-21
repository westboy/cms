package com.zhiliao.common.base;

import com.github.pagehelper.PageInfo;

import java.io.Serializable;
import java.util.List;


public interface BaseService<T,I extends Serializable> {

    String save(T pojo);
    String update(T pojo);
    String delete(I[] ids);
    T findById(I id);
    List<T> findList(T pojo);
    List<T> findAll();
    PageInfo<T> page(Integer pageNumber, Integer pageSize, T pojo);
    PageInfo<T> page(Integer pageNumber, Integer pageSize);



}
