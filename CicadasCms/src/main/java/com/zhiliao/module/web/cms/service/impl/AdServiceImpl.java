package com.zhiliao.module.web.cms.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhiliao.common.utils.CmsUtil;
import com.zhiliao.common.utils.JsonUtil;
import com.zhiliao.module.web.cms.service.AdService;
import com.zhiliao.module.web.cms.vo.TCmsAdVo;
import com.zhiliao.mybatis.mapper.TCmsAdGroupMapper;
import com.zhiliao.mybatis.mapper.TCmsAdMapper;
import com.zhiliao.mybatis.model.TCmsAd;
import com.zhiliao.mybatis.model.TCmsAdGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:广告实现类
 *
 * @author Jin
 * @create 2017-06-16
 **/
@Service
@CacheConfig(cacheNames = "cms-ad-cache")
public class AdServiceImpl implements AdService{

    @Autowired
    private TCmsAdMapper adMapper;

    @Autowired
    private TCmsAdGroupMapper adGroupMapper;

    @Override
    @CacheEvict(cacheNames = "cms-ad-cache",allEntries = true)
    public String save(TCmsAd pojo) {
        if (adMapper.insertSelective(pojo)>0)
            return JsonUtil.toSUCCESS("操作成功","ad-tab",true);
        return  JsonUtil.toERROR("操作失败");
    }

    @Override
    @CacheEvict(cacheNames = "cms-ad-cache",allEntries = true)
    public String update(TCmsAd pojo) {
        if (adMapper.updateByPrimaryKeySelective(pojo)>0)
            return JsonUtil.toSUCCESS("操作成功","ad-tab",false);
        return  JsonUtil.toERROR("操作失败");
    }

    @Override
    @CacheEvict(cacheNames = "cms-ad-cache",allEntries = true)
    public String delete(Integer[] ids) {
        if(ids!=null){
            for(int id :ids)
                adMapper.deleteByPrimaryKey(id);
        }
        return JsonUtil.toSUCCESS("操作成功","ad-tab",false);
    }

    @Override
    public String deleteGroup(Integer[] ids) {
        if(ids!=null){
            for(int id :ids)
                adGroupMapper.deleteByPrimaryKey(id);
        }
        return JsonUtil.toSUCCESS("操作成功","ad-tab",false);
    }

    @Cacheable(key = "'id-'+#p0")
    @Override
    public TCmsAd findById(Integer id) {
        return adMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<TCmsAd> findList(TCmsAd pojo) {
        return adMapper.select(pojo);
    }

    @Override
    public List<TCmsAd> findAll() {
        return adMapper.selectAll();
    }

    @Override
    public PageInfo<TCmsAd> page(Integer pageNumber, Integer pageSize, TCmsAd pojo) {
        PageHelper.startPage(pageNumber,pageSize);
        return new PageInfo<>(this.findList(pojo));
    }

    @Override
    public PageInfo<TCmsAd> page(Integer pageNumber, Integer pageSize) {
        PageHelper.startPage(pageNumber,pageSize);
        return new PageInfo<>(this.findAll());
    }

    @Cacheable(key = "'javascript-id-'+#p0")
    @Override
    public String toJavascript(Object id) {
        TCmsAd ad = adMapper.selectByIdAndEffective((Integer) id);
        if(CmsUtil.isNullOrEmpty(ad))
           return "document.write('您所访问的广告已过期，或者还没有开始');！";
        StringBuffer body = new StringBuffer();
        body.append("var dtnow=new Date();");
        body.append("dtnow=dtnow.getFullYear()+'-'+(dtnow.getMonth()+1)+'-'+dtnow.getDate();");
        body.append("var HtmlContent=\"\";");
        body.append(" HtmlContent='"+ad.getAdBody()+"';");
        body.append("HtmlContent = HtmlContent.replace(\"&#39\",\"'\");HtmlContent = HtmlContent.replace('&quot;','\"');");
        body.append("document.write(HtmlContent);");
        return body.toString();
    }


    @Override
    public String save(TCmsAdGroup pojo) {
        if (adGroupMapper.insertSelective(pojo)>0)
            return JsonUtil.toSUCCESS("操作成功","ad-tab",true);
        return  JsonUtil.toERROR("操作失败");
    }

    @Override
    public String update(TCmsAdGroup pojo) {
        if (adGroupMapper.updateByPrimaryKeySelective(pojo)>0)
            return JsonUtil.toSUCCESS("操作成功","ad-tab",true);
        return  JsonUtil.toERROR("操作失败");
    }

    @Override
    public PageInfo<TCmsAdVo> page(Integer pageNumber, Integer pageSize,Integer status) {
        PageHelper.startPage(pageNumber,pageSize);
        return new PageInfo<>(this.findVoListByStatus(status));
    }

    @Override
    public List<TCmsAdVo> findVoListByStatus(Integer status) {
        return adMapper.selectByGroupStatus(status);
    }


    @Override
    public TCmsAdGroup findById(Object id) {
        return adGroupMapper.selectByPrimaryKey(id);
    }

    @Override
    public TCmsAd findByIdAndEffective(Integer id) {
        return adMapper.selectByIdAndEffective(id);
    }

    @Override
    public List<TCmsAdGroup> findList(TCmsAdGroup pojo) {
        return adGroupMapper.select(pojo);
    }

    @Override
    public PageInfo<TCmsAdGroup> page(Integer pageNumber, Integer pageSize, TCmsAdGroup adGroup) {
        PageHelper.startPage(pageNumber,pageSize);
        return new PageInfo<>(this.findList(adGroup));
    }
}
