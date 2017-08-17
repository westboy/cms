package com.zhiliao.module.web.cms.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhiliao.common.db.DbTableAssistantService;
import com.zhiliao.common.exception.CmsException;
import com.zhiliao.common.exception.SystemException;
import com.zhiliao.common.utils.CmsUtil;
import com.zhiliao.common.utils.JsonUtil;
import com.zhiliao.common.utils.PinyinUtil;
import com.zhiliao.module.web.cms.service.ModelService;
import com.zhiliao.mybatis.mapper.TCmsCategoryMapper;
import com.zhiliao.mybatis.mapper.TCmsModelFiledMapper;
import com.zhiliao.mybatis.mapper.TCmsModelMapper;
import com.zhiliao.mybatis.model.TCmsCategory;
import com.zhiliao.mybatis.model.TCmsModel;
import com.zhiliao.mybatis.model.TCmsModelFiled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;


@Service
@CacheConfig(cacheNames = "cms-model-cache")
public class ModelServiceImpl implements ModelService{

    @Autowired
    private TCmsModelMapper modelMapper;

    @Autowired
    private TCmsModelFiledMapper modelFiledMapper;

    @Autowired
    DbTableAssistantService dbTableAssistant;

    @Autowired
    private TCmsCategoryMapper categoryMapper;

   @CacheEvict(cacheNames = "cms-model-cache",allEntries = true)
   @Override
    public String save(TCmsModel pojo) {
       String tableName = PinyinUtil.convertLower(pojo.getTableName());
       pojo.setTableName(tableName);
       try {
           dbTableAssistant.createDbtable(tableName);
       } catch (SQLException e) {
           throw  new SystemException(e.getMessage());
       }
       if (modelMapper.insertSelective(pojo)>0)
        return JsonUtil.toSUCCESS("操作成功","model-tab",true);
       return  JsonUtil.toERROR("操作失败");
    }

    @CacheEvict(cacheNames = "cms-model-cache",allEntries = true)
    @Override
    public String update(TCmsModel pojo) {
        if (modelMapper.updateByPrimaryKeySelective(pojo)>0)
            return JsonUtil.toSUCCESS("操作成功","model-tab",true);
        return  JsonUtil.toERROR("操作失败");
    }

    @Override
    public String delete(Integer[] ids){
       if(ids!=null) {
           for (Integer id : ids) {

               /*检查当前模型是否管理栏目*/
               TCmsCategory category = new TCmsCategory();
               category.setModelId(id);
               List<TCmsCategory> categorys = categoryMapper.select(category);
               if(!CmsUtil.isNullOrEmpty(categorys)&&categorys.size()>0)
                   throw new CmsException("当前模型关联多个栏目[size:"+categorys.size()+"],请先删除栏目！");
               /*删除数据库中的模型表*/
               TCmsModel model =modelMapper.selectByPrimaryKey(id);
               try {
                   dbTableAssistant.deleteDbtable(model.getTableName());
               } catch (SQLException e) {
                   throw  new SystemException(e.getMessage());
               }
               modelMapper.deleteByPrimaryKey(id);
               /*清空与当前模型相关的字段*/
               TCmsModelFiled modelFiled = new TCmsModelFiled();
               modelFiled.setModelId(id);
               modelFiledMapper.delete(modelFiled);

           }
           return JsonUtil.toSUCCESS("操作成功", "model-tab", false);
       }
       return  JsonUtil.toERROR("操作失败");
    }
    @Cacheable(key = "#p0")
    @Override
    public TCmsModel findById(Integer id) {
        TCmsModel model = modelMapper.selectByPrimaryKey(id);
        if(CmsUtil.isNullOrEmpty(model))
            throw new SystemException("模型（"+id+"）不存在！");
        return model;
    }

    @Override
    public List<TCmsModel> findList(TCmsModel pojo) {
        return modelMapper.select(pojo);
    }

    @Override
    public List<TCmsModel> findAll() {
         return modelMapper.selectAll();
    }

    @Override
    public PageInfo<TCmsModel> page(Integer pageNumber, Integer pageSize, TCmsModel pojo) {
        PageHelper.startPage(pageNumber,pageSize);
        return new PageInfo<>(findList(pojo));
    }

    @Override
    public PageInfo<TCmsModel> page(Integer pageNumber, Integer pageSize) {
        PageHelper.startPage(pageNumber,pageSize);
        return new PageInfo<>(findAll());
    }

    @Override
    public TCmsModel findModelByModelName(String modelName) {
        TCmsModel model = new TCmsModel();
        model.setModelName(modelName);
        return modelMapper.selectOne(model);
    }

    @Override
    public TCmsModel findModelByTableName(String tableName) {
        TCmsModel model = new TCmsModel();
        model.setTableName(tableName);
        return modelMapper.selectOne(model);
    }

    @Cacheable(key = "'list-status-'+#p0+'-site-'+#p1")
    @Override
    public List<TCmsModel> findModelListByStatusAndSiteId(boolean status,Integer siteId) {
       TCmsModel model = new TCmsModel();
       model.setStatus(status);
       model.setSiteId(siteId);
        return modelMapper.select(model);
    }

    @Cacheable(key = "'list-status-'+#p0")
    @Override
    public List<TCmsModel> findModelListByStatus(boolean status) {
        TCmsModel model = new TCmsModel();
        model.setStatus(status);
        return modelMapper.select(model);
    }
}
