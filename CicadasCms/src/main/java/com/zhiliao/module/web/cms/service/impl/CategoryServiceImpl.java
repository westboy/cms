package com.zhiliao.module.web.cms.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhiliao.common.constant.CmsConst;
import com.zhiliao.common.exception.CmsException;
import com.zhiliao.common.utils.HtmlKit;
import com.zhiliao.common.utils.JsonUtil;
import com.zhiliao.common.utils.PinyinUtil;
import com.zhiliao.common.utils.StrUtil;
import com.zhiliao.module.web.cms.service.CategoryService;
import com.zhiliao.mybatis.mapper.TCmsCategoryMapper;
import com.zhiliao.mybatis.mapper.TCmsContentMapper;
import com.zhiliao.mybatis.model.TCmsCategory;
import com.zhiliao.mybatis.model.TCmsContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Description:栏目分类
 *
 * @author Jin
 * @create 2017-04-18
 **/
@Service
@CacheConfig(cacheNames = "cms-category-cache")
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private TCmsCategoryMapper categoryMapper;

    @Autowired
    private TCmsContentMapper contentMapper;

    @CacheEvict(cacheNames = "cms-category-cache",allEntries = true,beforeInvocation = true)
    @Override
    public String save(TCmsCategory pojo) {
        /* 将栏目名称转换为拼音设置别名 */
        if(StrUtil.isBlank(pojo.getAlias())) {
            pojo.setAlias(PinyinUtil.convertLower(HtmlKit.getText(pojo.getCategoryName())));
        }else {
            pojo.setAlias(PinyinUtil.convertLower(HtmlKit.getText(pojo.getAlias())));
        }
        /* 判断是否设置当前栏目为顶级节点 */
        if(pojo.getParentId()!=0L){
            TCmsCategory parentCategory =categoryMapper.selectByPrimaryKey(pojo.getParentId());
            parentCategory.setHasChild(true);
            categoryMapper.updateByPrimaryKeySelective(parentCategory);
             /*判断当前栏目是否需要继承父类模板*/
            if(StrUtil.isBlank(pojo.getContentTpl()))
                pojo.setContentTpl(StrUtil.isBlank(parentCategory .getContentTpl()) ? CmsConst.CONTENT_TPL : parentCategory .getContentTpl());
            if(StrUtil.isBlank(pojo.getIndexTpl()))
                pojo.setIndexTpl(StrUtil.isBlank(parentCategory .getIndexTpl()) ? CmsConst.CATEGORY_INDEX_TPL: parentCategory .getIndexTpl());
            if(StrUtil.isBlank(pojo.getListTpl()))
                pojo.setListTpl(StrUtil.isBlank(parentCategory .getListTpl()) ? CmsConst.CATEGORY_LIST_TPL :parentCategory .getListTpl());
        }else {
            pojo.setContentTpl(StrUtil.isBlank(pojo.getContentTpl()) ? CmsConst.CONTENT_TPL : pojo.getContentTpl());
            pojo.setIndexTpl(StrUtil.isBlank(pojo.getIndexTpl()) ? CmsConst.CATEGORY_INDEX_TPL : pojo.getIndexTpl());
            pojo.setListTpl(StrUtil.isBlank(pojo.getListTpl()) ? CmsConst.CATEGORY_LIST_TPL : pojo.getListTpl());
        }
        if(categoryMapper.insert(pojo)>0){
            return JsonUtil.toSUCCESS("分类添加成功！","category-tab",true);
        }
        return JsonUtil.toERROR("分类添加失败！");
    }


    @Transactional(transactionManager = "masterTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
    @CacheEvict(cacheNames = "cms-category-cache",allEntries = true,beforeInvocation = true)
    @Override
    public String update(TCmsCategory pojo) {
        /* 将栏目名称转换为拼音设置别名 */
        //pojo.setAlias(PinyinUtil.convertLower(HtmlKit.getText(pojo.getCategoryName())));
        /*todo 这地方需要做个判断栏目是否允许更换内容模型*/
        /*  如果当前栏目父Id是不是顶级节点 */
        if(categoryMapper.selectByPrimaryKey(pojo.getCategoryId()).getParentId()==0&&pojo.getParentId()!=0L){
            /* 父类交换 */
            TCmsCategory parentCategory = categoryMapper.selectByPrimaryKey(pojo.getParentId());
            parentCategory.setParentId(0l);
            parentCategory.setHasChild(true);
            pojo.setParentId(pojo.getParentId());
            categoryMapper.updateByPrimaryKey(parentCategory);
            /*判断当前栏目是否需要继承父类模板*/
            if(StrUtil.isBlank(pojo.getContentTpl()))
               pojo.setContentTpl(StrUtil.isBlank(parentCategory .getContentTpl()) ? CmsConst.CONTENT_TPL : parentCategory .getContentTpl());
            if(StrUtil.isBlank(pojo.getIndexTpl()))
               pojo.setIndexTpl(StrUtil.isBlank(parentCategory .getIndexTpl()) ? CmsConst.INDEX_TPL : parentCategory .getIndexTpl());
            if(StrUtil.isBlank(pojo.getListTpl()))
              pojo.setListTpl(StrUtil.isBlank(parentCategory .getListTpl()) ? CmsConst.CATEGORY_LIST_TPL :parentCategory .getListTpl());
        }else {
            pojo.setContentTpl(StrUtil.isBlank(pojo.getContentTpl()) ? CmsConst.CONTENT_TPL : pojo.getContentTpl());
            pojo.setIndexTpl(StrUtil.isBlank(pojo.getIndexTpl()) ? CmsConst.INDEX_TPL : pojo.getIndexTpl());
            pojo.setListTpl(StrUtil.isBlank(pojo.getListTpl()) ? CmsConst.CATEGORY_LIST_TPL : pojo.getListTpl());
        }
        if(categoryMapper.updateByPrimaryKeySelective(pojo)>0)
            return JsonUtil.toSUCCESS("分类更新成功！","category-tab",false);
        return JsonUtil.toERROR("分类更新失败！");
    }

    @Transactional(transactionManager = "masterTransactionManager",propagation = Propagation.REQUIRED,rollbackFor=Exception.class)
    @CacheEvict(cacheNames = "cms-category-cache",allEntries = true)
    @Override
    public String delete(Long[] ids) {
        if(ids!=null&&ids.length>0)
            for(Long id : ids){
                /*先遍历获取当前子栏目*/
                TCmsCategory category = new TCmsCategory();
                category.setParentId(id);
                List<TCmsCategory> categories=categoryMapper.select(category);
                if(categories!=null&&categories.size()>0){
                   for(TCmsCategory c :categories){
                       TCmsContent content =new TCmsContent();
                       content.setCategoryId(c.getCategoryId());
                       if(contentMapper.selectCount(content)>0)
                          throw new CmsException("当前栏目["+id+"]下有多条内容，不允许删除");
                       categoryMapper.deleteByPrimaryKey(c.getCategoryId());
                    }
                    categoryMapper.deleteByPrimaryKey(id);
                }else{
                    TCmsContent content =new TCmsContent();
                    content.setCategoryId(id);
                    if(contentMapper.selectCount(content)>0)
                        throw new CmsException("当前栏目["+id+"]下有多条内容，不允许删除");
                    categoryMapper.deleteByPrimaryKey(id);
                }
            }
        return JsonUtil.toSUCCESS("删除成功！","category-tab",true);
    }

    @Override
    public PageInfo<TCmsCategory> page(Integer pageNumber, Integer pageSize, TCmsCategory pojo) {
        PageHelper.startPage(pageNumber,pageSize);
        return new PageInfo(categoryMapper.select(pojo));
    }

    @Override
    public PageInfo<TCmsCategory> page(Integer pageNumber, Integer pageSize) {
        PageHelper.startPage(pageNumber,pageSize);
        PageInfo<TCmsCategory> pageInfo = new PageInfo();
        pageInfo.setList(categoryMapper.selectAll());
        return pageInfo;
    }

    @Cacheable(key = "'find-Id-'+#p0")
    @Override
    public TCmsCategory findById(Long id) {
        return categoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<TCmsCategory> findList(TCmsCategory pojo) {
        return categoryMapper.select(pojo);
    }

    @Override
    public List<TCmsCategory> findAll() {
        return categoryMapper.selectAll();
    }




    @Cacheable(key = "'find-list-pid-'+#p0")
    @Override
    public List<TCmsCategory> findCategoryListByPid(Long pid) {
        TCmsCategory category = new TCmsCategory();
        category.setParentId(pid);
        return categoryMapper.select(category);
    }

    @Override
    public List<TCmsCategory> findCategoryListBySiteId(Integer siteId) {
        TCmsCategory category = new TCmsCategory();
        category.setSiteId(siteId);
        return categoryMapper.select(category);
    }

    @Cacheable(key = "'find-list-pid-'+#p0+'-siteId-'+#p1")
    @Override
    public List<TCmsCategory> findCategoryListByPid(Long pid,Integer siteId) {
        TCmsCategory category = new TCmsCategory();
        category.setParentId(pid);
        category.setSiteId(siteId);
        return categoryMapper.select(category);
    }

    @Override
    public List<TCmsCategory> findCategoryListByPidAndIsNav(Long pid, Integer siteId, Boolean isNav) {
        TCmsCategory category = new TCmsCategory();
        category.setParentId(pid);
        category.setSiteId(siteId);
        if(isNav)
           category.setIsNav(isNav);
        return categoryMapper.select(category);
    }

    @Cacheable(key = "'find-alias-'+#p0")
    @Override
    public TCmsCategory findByAlias(String alias) {
        TCmsCategory category = new TCmsCategory();
        category.setAlias(alias);
        return categoryMapper.selectOne(category);
    }

    @Cacheable(key = "'find-page-size-'+#p0")
    @Override
    public Integer findPageSize(Long categoryId) {
        return this.categoryMapper.selectByPrimaryKey(categoryId).getPageSize();
    }

    @Cacheable(key = "'find-alias-'+#p0+'-siteId-'+#p1")
    @Override
    public TCmsCategory findfindByAliasAndSiteId(String alias, Integer siteId) {
        TCmsCategory category = new TCmsCategory();
        category.setAlias(alias);
        category.setSiteId(siteId);
        return categoryMapper.selectOne(category);
    }

    @Override
    public Integer AllCount() {
        return this.categoryMapper.selectCount(new TCmsCategory());
    }

}
