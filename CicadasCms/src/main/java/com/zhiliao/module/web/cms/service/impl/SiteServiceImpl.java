package com.zhiliao.module.web.cms.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhiliao.common.constant.CmsConst;
import com.zhiliao.common.exception.SystemException;
import com.zhiliao.common.utils.CmsUtil;
import com.zhiliao.common.utils.ControllerUtil;
import com.zhiliao.common.utils.JsonUtil;
import com.zhiliao.common.utils.StrUtil;
import com.zhiliao.module.web.cms.service.SiteService;
import com.zhiliao.module.web.cms.vo.TCmsSiteVo;
import com.zhiliao.module.web.system.vo.UserVo;
import com.zhiliao.mybatis.mapper.*;
import com.zhiliao.mybatis.model.TCmsCategory;
import com.zhiliao.mybatis.model.TCmsContent;
import com.zhiliao.mybatis.model.TCmsSite;
import com.zhiliao.mybatis.model.TCmsUserSite;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Description:站点
 *
 * @author Jin
 * @create 2017-05-15
 **/
@Service
@CacheConfig(cacheNames = "cms-site-cache")
public class SiteServiceImpl implements SiteService{

    @Value("${system.site.name}")
    private String siteName;

    @Autowired
    private TCmsSiteMapper siteMapper;

    @Autowired
    private TCmsUserSiteMapper userSiteMapper;

    @Autowired
    private TCmsCategoryMapper categoryMapper;

    @Autowired
    private TSysUserMapper sysUserMapper;

    @Autowired
    private TCmsContentMapper contentMapper;

    @Override
    public String save(TCmsSite pojo) {
        return null;
    }

    @Override
    public String update(TCmsSite pojo) {
       return null;
    }

    @Override
    public String delete(Integer[] ids) {
        if(ids!=null){
           for (Integer id :ids) {
               if(id==1)return  JsonUtil.toERROR("主站点不能删除");
               /*删除一切与当前站点相关联的内容*/
               siteMapper.deleteByPrimaryKey(id);
               userSiteMapper.deleteBySiteId(id);
               /*删除站点相关的栏目*/
               TCmsCategory category = new TCmsCategory();
               category.setSiteId(id);
               categoryMapper.delete(category);
                /*删除站点相关的栏内容*/
               TCmsContent content = new TCmsContent();
               content.setSiteId(id);
               contentMapper.delete(content);
           }
           return JsonUtil.toSUCCESS("操作成功","site-tab",false);
        }
        return  JsonUtil.toERROR("操作失败");
    }

    @Cacheable(key = "'find-Id-'+#p0")
    @Override
    public TCmsSite findById(Integer id) {
        return  siteMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<TCmsSite> findList(TCmsSite pojo) {
        return siteMapper.select(pojo);
    }

    @Override
    public List<TCmsSite> findAll() {
        return siteMapper.selectAll();
    }

    @Override
    public PageInfo<TCmsSite> page(Integer pageNumber, Integer pageSize, TCmsSite pojo) {
        PageHelper.startPage(pageNumber,pageSize);
        return new PageInfo<>(findList(pojo));
    }

    @Override
    public PageInfo<TCmsSite> page(Integer pageNumber, Integer pageSize) {
        PageHelper.startPage(pageNumber,pageSize);
        return new PageInfo<>(findAll());
    }

    @CacheEvict(value = "cms-site-cache",allEntries = true,beforeInvocation = true)
    @Transactional(transactionManager = "masterTransactionManager",rollbackFor = Exception.class)
    @Override
    public String save(TCmsSiteVo pojo) {
        String[] userIds = StrUtil.excludeRepeatStr(pojo.getUserIds());
        if (siteMapper.insertSelective(pojo)>0) {
            this.UserSiteUpdate(userIds,pojo);
            return JsonUtil.toSUCCESS("操作成功", "site-tab", true);
        }
        return  JsonUtil.toERROR("操作失败");
    }


    @CacheEvict(value = "cms-site-cache",allEntries = true)
    @Transactional(transactionManager = "masterTransactionManager",rollbackFor = Exception.class)
    @Override
    public String update(TCmsSiteVo pojo) {
        String[] userIds = StrUtil.excludeRepeatStr(pojo.getUserIds());
        if (siteMapper.updateByPrimaryKeySelective(pojo)>0){
            /*如果是更新就先清空当前站点与用户的关联*/
            userSiteMapper.deleteBySiteId(pojo.getSiteId());
            /*重新遍历添加*/
            this.UserSiteUpdate(userIds,pojo);
            return JsonUtil.toSUCCESS("操作成功","site-tab",false);
        }
        return  JsonUtil.toERROR("操作失败");
    }

    private void UserSiteUpdate(String[] userIds,TCmsSiteVo pojo){
        if (userIds!=null&&userIds.length>0) {
            for (String userId : userIds) {
                if(CmsUtil.isNullOrEmpty(sysUserMapper.selectByPrimaryKey(Integer.parseInt(userId))))
                    throw new SystemException("管理员["+userId+"]不存在！");
                TCmsUserSite userSite = new TCmsUserSite();
                userSite.setSiteId(pojo.getSiteId());
                userSite.setUserId(Integer.parseInt(userId));
                userSiteMapper.insert(userSite);
            }
        }
    }

    @Override
    public TCmsSiteVo findVoById(Integer id) {
        TCmsSiteVo siteVo = new TCmsSiteVo();
        TCmsSite site = findById(id);
        BeanUtils.copyProperties(site,siteVo);
        List<TCmsUserSite> userSites = userSiteMapper.selectBySiteId(site.getSiteId());
        String userIds = "";
        if(userSites!=null&&userSites.size()>0)
            for(TCmsUserSite userSite : userSites) {
                userIds+=String.valueOf(userSite.getUserId())+",";
            }
        siteVo.setUserIds(userIds.length()>0?userIds.substring(0,userIds.length()-1):userIds);
        return siteVo;
    }

    @Override
    public String change(UserVo userVo,Integer siteId) {
        HttpSession session = ControllerUtil.getHttpSession();
        TCmsSite site = siteMapper.selectByPrimaryKey(siteId);
        if(CmsUtil.isNullOrEmpty(site))
            return JsonUtil.toERROR("当前站点不存在！");
        userVo.setSiteId(site.getSiteId());
        userVo.setSiteName(site.getSiteId()==0?this.siteName:site.getSiteName());
        /*更新session*/
        session.setAttribute(CmsConst.SITE_USER_SESSION_KEY,userVo);
        return JsonUtil.toSUCCESS("站点切换成功，请刷新当前页面！","site-tab",false);
    }

    @Cacheable(key = "'find-domain-'+#domain")
    @Override
    public TCmsSite findByDomain(String domain) {
        TCmsSite site = new TCmsSite();
        site.setDomain(domain);
        return siteMapper.selectOne(site);
    }
}
