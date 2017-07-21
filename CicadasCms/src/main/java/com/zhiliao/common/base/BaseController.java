package com.zhiliao.common.base;

import org.springframework.ui.Model;

import java.sql.SQLException;

public abstract class BaseController<T> {

     /*首页方法*/
   public abstract String index(Integer pageNumber,
                  Integer pageSize,
                  T pojo,
                  Model model);

     /*输入页面*/
     public abstract String input(Integer Id, Model model);

     /*保存方法*/
     public  abstract  String save(T pojo) throws SQLException;

     /*删除方法*/
     public abstract String delete(Integer[] ids) throws SQLException;
}
