package com.zhiliao.component.beetl.tag.cms;

import com.github.pagehelper.PageInfo;
import com.zhiliao.common.exception.CmsException;
import com.zhiliao.common.utils.CmsUtil;
import com.zhiliao.common.utils.StrUtil;
import com.zhiliao.module.web.cms.service.ContentService;
import com.zhiliao.module.web.cms.service.SiteService;
import com.zhiliao.module.web.cms.service.TopicService;
import com.zhiliao.mybatis.model.TCmsSite;
import com.zhiliao.mybatis.model.TCmsTopic;
import org.beetl.core.GeneralVarTagBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Description:专题类容列表标签
 *
 * @author Jin
 * @create 2017-05-26
 **/
@Service
@Scope("prototype")
public class TopicContentTag extends GeneralVarTagBinding {

	@Autowired
	private ContentService contentService;

	@Autowired
	private SiteService siteService;


	@Autowired
	private TopicService topicService;

	@Value("${system.http.protocol}")
	private String httpProtocol;

	@Value("${system.http.host}")
	private String httpHost;

	@Value("${system.site.subfix}")
	private String siteSubfix;

	@Value("${system.site.prefix}")
	private String sitePrefix;

	@Override
	public void render() {

		Integer titleLen =  Integer.parseInt((String) this.getAttributeValue("titleLen"));
		Integer siteId=  (this.getAttributeValue("siteId") instanceof String)?Integer.parseInt((String) this.getAttributeValue("siteId")):(Integer)this.getAttributeValue("siteId");
		Integer isHot =  Integer.parseInt((String) this.getAttributeValue("isHot"));
		Integer isPic =  Integer.parseInt(CmsUtil.isNullOrEmpty(this.getAttributeValue("isPic"))?"0":(String)this.getAttributeValue("isPic"));
		Integer isRecommend =  Integer.parseInt(CmsUtil.isNullOrEmpty(this.getAttributeValue("isRecommend"))?"0":(String) this.getAttributeValue("isRecommend"));
		Integer orderBy =  Integer.parseInt((String) this.getAttributeValue("orderBy"));
		Integer size =  Integer.parseInt((String) this.getAttributeValue("size"));
		Integer topicId =  Integer.parseInt((String) this.getAttributeValue("topicId"));
		TCmsTopic topic = topicService.findById(topicId);
		if(CmsUtil.isNullOrEmpty(topic))return;
		/*将专题中的categoryIds 字符串转为Long数组*/
		String[] str1 = topic.getCategoryIds().split(",");
		Long[] str2 = new Long[str1.length];
		for (int i = 0; i < str1.length; i++) {
			str2[i] = Long.valueOf(str1[i]);
		}
		PageInfo<Map> pageInfo = contentService.findTopicContentListBySiteIdAndCategoryIds(siteId, str2, orderBy, size, isHot, isPic,isRecommend);
		if(CmsUtil.isNullOrEmpty(pageInfo.getList()))return;
		try {
			wrapRender(pageInfo.getList(),titleLen,siteId);
		} catch (Exception e) {
			throw new CmsException(e.getMessage());
		}

	}
	private void wrapRender(List<Map>  contentList, int titleLen, int siteId) throws Exception {
		int i = 1;
		for (Map content : contentList) {
			String title = content.get("title").toString();
			int length = title.length();
			if (length > titleLen) {
				content.put("title",title.substring(0, titleLen) + "...");
			}
			if (StrUtil.isBlank(content.get("url").toString())) {
				TCmsSite site = siteService.findById(siteId);
				if(CmsUtil.isNullOrEmpty(site)) throw new CmsException("站点不存在[siteId:"+siteId+"]");
				String url = httpProtocol + "://" + (StrUtil.isBlank(site.getDomain())?httpHost:site.getDomain()) + "/"+sitePrefix+"/"+site.getSiteId()+"/";
				url+=content.get("categoryId")+"/"+content.get("contentId");
				content.put("url",url+siteSubfix);
			}
			content.put("index",i);
			this.binds(content);
			this.doBodyRender();
			i++;
		}

	}


}
