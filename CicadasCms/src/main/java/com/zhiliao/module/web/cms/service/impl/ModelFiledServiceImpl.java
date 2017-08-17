package com.zhiliao.module.web.cms.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhiliao.common.db.DbTableAssistantService;
import com.zhiliao.common.db.kit.DbTableKit;
import com.zhiliao.common.db.vo.FiledTypeVo;
import com.zhiliao.common.exception.SystemException;
import com.zhiliao.common.utils.HtmlKit;
import com.zhiliao.common.utils.JsonUtil;
import com.zhiliao.common.utils.PinyinUtil;
import com.zhiliao.module.web.cms.service.ModelFiledService;
import com.zhiliao.mybatis.mapper.TCmsModelFiledMapper;
import com.zhiliao.mybatis.mapper.TCmsModelMapper;
import com.zhiliao.mybatis.model.TCmsModel;
import com.zhiliao.mybatis.model.TCmsModelFiled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

/**
 * Description:模型字段Service
 *
 * @author Jin
 * @create 2017-05-12
 **/
@Service
@CacheConfig(cacheNames = "cms_modelFiled-cache")
public class ModelFiledServiceImpl implements ModelFiledService{


    @Autowired
    private TCmsModelMapper modelMapper;

    @Autowired
    private TCmsModelFiledMapper filedMapper;

    @Autowired
    private DbTableAssistantService dbTableAssistant;

    @Override
    @Transactional(transactionManager = "masterTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
    public String save(TCmsModelFiled pojo) {
        pojo.setFiledName(PinyinUtil.convertLower(HtmlKit.getText(pojo.getFiledName())));
        FiledTypeVo filedTypeVo = DbTableKit.getFiledTypeVo(pojo.getFiledClass(),pojo.getFiledType(),pojo.getFiledLength(),pojo.getFiledValue());
        pojo.setFiledLength(filedTypeVo.getLength());
        pojo.setFiledType(filedTypeVo.getM().toString());
        if (filedMapper.insertSelective(pojo)>0) {
            TCmsModel model = modelMapper.selectByPrimaryKey(pojo.getModelId());
            /* 根据模型表名添加数据库字段 */
            try {
                dbTableAssistant.addDbTableColumn(model.getTableName(),pojo.getFiledName(),filedTypeVo.getM(),filedTypeVo.getLength(),false,filedTypeVo.getDefaultValue(),pojo.getNotNull(),false);
            } catch (SQLException e) {
                throw  new SystemException(e.getMessage());
            }
            return JsonUtil.toSUCCESS("操作成功", "", "model-filed-tab",true);
        }
        return  JsonUtil.toERROR("操作失败");
    }

    @Override
    public String update(TCmsModelFiled pojo) {
        if (filedMapper.updateByPrimaryKeySelective(pojo)>0)
            return JsonUtil.toSUCCESS("操作成功","model-filed-tab",false);
        return  JsonUtil.toERROR("操作失败");
    }

    @Override
    public String delete(Integer[] ids) {
        if(ids!=null) {
            for (Integer id : ids) {
                TCmsModelFiled filed =filedMapper.selectByPrimaryKey(id);
                TCmsModel model =modelMapper.selectByPrimaryKey(filed.getModelId());
                /* 根据模型表名删除表字段 */
                if(model!=null) {
                    try {
                        dbTableAssistant.deleteDbTableColumn(model.getTableName(), filed.getFiledName(), false);
                    } catch (SQLException e) {
                        throw  new SystemException(e.getMessage());
                    }
                }
                filedMapper.deleteByPrimaryKey(id);
            }
            return JsonUtil.toSUCCESS("操作成功", "model-filed-tab", false);
        }
        return  JsonUtil.toERROR("操作失败");
    }

    @Cacheable(key = "#p0")
    @Override
    public TCmsModelFiled findById(Integer id) {
        return filedMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<TCmsModelFiled> findList(TCmsModelFiled pojo) {
        return filedMapper.select(pojo);
    }

    @Override
    public List<TCmsModelFiled> findAll() {
        return filedMapper.selectAll();
    }

    @Override
    public PageInfo<TCmsModelFiled> page(Integer pageNumber, Integer pageSize, TCmsModelFiled pojo) {
        PageHelper.startPage(pageNumber,pageSize);
        return new PageInfo<>(findList(pojo));
    }

    @Override
    public PageInfo<TCmsModelFiled> page(Integer pageNumber, Integer pageSize) {
        PageHelper.startPage(pageNumber,pageSize);
        return new PageInfo<>(findAll());
    }

    @Override
    public TCmsModelFiled findModelFiledByFiledName(String filedName) {
        TCmsModelFiled filed = new TCmsModelFiled();
        filed.setFiledName(PinyinUtil.convertLower(filedName));
        return filedMapper.selectOne(filed);
    }

    @Override
    public List<TCmsModelFiled> findModelFiledListByModelId(Integer modelId) {
        List<TCmsModelFiled> result = filedMapper.selectByModelId(modelId);
        return result;
    }
}
