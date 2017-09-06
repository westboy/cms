package com.zhiliao.component.beetl.tag.cms;

import com.zhiliao.common.exception.CmsException;
import com.zhiliao.common.utils.CmsUtil;
import com.zhiliao.common.utils.Pojo2MapUtil;
import com.zhiliao.common.utils.StrUtil;
import com.zhiliao.module.web.cms.service.CategoryService;
import com.zhiliao.module.web.cms.service.SiteService;
import com.zhiliao.mybatis.model.TCmsCategory;
import com.zhiliao.mybatis.model.TCmsSite;
import org.beetl.core.GeneralVarTagBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Description:内容列表标签
 *
 * @author Jin
 * @create 2017-05-26
 **/
@Service
@Scope("prototype")
public class CategoryListTag extends GeneralVarTagBinding {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private SiteService siteService;

	@Value("${system.http.protocol}")
	private String httpProtocol;

	@Value("${system.http.host}")
	private String httpHost;

	@Value("${system.site.prefix}")
	private String sitePrefix;

	@Value("${system.site.subfix}")
	private String siteSubfix;


	@Override
	public void render() {
		Integer siteId=  (this.getAttributeValue("siteId") instanceof String)?Integer.parseInt((String) this.getAttributeValue("siteId")):(Integer)this.getAttributeValue("siteId");
		Long categoryId=  (this.getAttributeValue("categoryId") instanceof String)?Long.parseLong((String) this.getAttributeValue("categoryId")):(Long) this.getAttributeValue("categoryId");
		Integer isNav = Integer.parseInt((String) this.getAttributeValue("isNav"));
		try {
			this.wrapRender(siteId,categoryId,(isNav.intValue()==1?true:false));
		} catch (Exception e) {
			throw new CmsException(e.getMessage());
		}

	}
	private void wrapRender(Integer siteId,Long categoryId,boolean isNav) throws Exception {
		TCmsSite site = siteService.findById(siteId);
		if(CmsUtil.isNullOrEmpty(site)) throw new CmsException("站点不存在[siteId:"+siteId+"]");
		List<TCmsCategory> cats = categoryService.findCategoryListByPidAndIsNav(categoryId,siteId,isNav);
        int i = 1;
		if(CmsUtil.isNullOrEmpty(cats)) {
        	TCmsCategory category = categoryService.findById(categoryId);
        	if(CmsUtil.isNullOrEmpty(category))
        		throw new CmsException("没有查询到数据！[siteId:" + siteId + ",categoryId:" + categoryId + "]");
			cats = categoryService.findCategoryListByPidAndIsNav(category.getParentId(),siteId,isNav);
		}
        for (TCmsCategory category : cats){
        	if(StrUtil.isBlank(category.getUrl()))
        		category.setUrl(httpProtocol + "://" + (StrUtil.isBlank(site.getDomain())?httpHost:site.getDomain()) + "/"+sitePrefix+"/" + category.getSiteId() + "/" + category.getCategoryId()+siteSubfix);
			Map result = Pojo2MapUtil.toMap(category);
			result.put("index",i);
        	this.binds(result);
			this.doBodyRender();
			i++;
		}
	}

}
