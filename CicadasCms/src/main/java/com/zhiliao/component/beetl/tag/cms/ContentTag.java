package com.zhiliao.component.beetl.tag.cms;

import com.google.common.collect.Maps;
import com.zhiliao.common.utils.CmsUtil;
import com.zhiliao.common.utils.StrUtil;
import com.zhiliao.module.web.cms.service.SiteService;
import com.zhiliao.mybatis.mapper.TCmsContentMapper;
import com.zhiliao.mybatis.model.TCmsContent;
import com.zhiliao.mybatis.model.TCmsSite;
import org.beetl.core.GeneralVarTagBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Description:CONTENT TAG
 *
 * @author Jin
 * @create 2017-06-06
 **/
@Service
@Scope("prototype")
public class ContentTag extends GeneralVarTagBinding {

    @Autowired
    private TCmsContentMapper contentMapper;

    @Autowired
    private SiteService siteService;

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
        Long categoryId=  (this.getAttributeValue("categoryId") instanceof String)?Long.parseLong((String) this.getAttributeValue("categoryId")):(Long) this.getAttributeValue("categoryId");
        Long contentId=  (this.getAttributeValue("contentId") instanceof String)? Long.parseLong((String) this.getAttributeValue("contentId")):( Long)this.getAttributeValue("contentId");
        Integer titleLen =  Integer.parseInt((String) this.getAttributeValue("titleLen"));
        wrapRender(categoryId,contentId,titleLen);

    }

    private void wrapRender(Long categoryId, Long contentId, Integer titleLen) {
        Map  result = Maps.newHashMap();
        String prevContent="没有了",nextContent="没有了";
        TCmsContent prev = contentMapper.selectPrevContentByContentIdAndCategoryId(contentId,categoryId);
        TCmsContent next = contentMapper.selectNextContentByContentIdAndCategoryId(contentId,categoryId);

        if(!CmsUtil.isNullOrEmpty(prev)) {
            TCmsSite site = siteService.findById(prev.getSiteId());
            int length = prev.getTitle().length();
            if (length > titleLen) prev.setTitle(prev.getTitle().substring(0, titleLen));

            prevContent = "<a href=\""+httpProtocol+"://"+(StrUtil.isBlank(site.getDomain())?httpHost:site.getDomain())+"/"+sitePrefix+"/"+prev.getSiteId()+"/"+prev.getCategoryId()+"/"+prev.getContentId();
            prevContent+=siteSubfix+"\">"+prev.getTitle()+ "...</a>";
        }
        if(!CmsUtil.isNullOrEmpty(next)) {
            TCmsSite site = siteService.findById(next.getSiteId());
            int length = next.getTitle().length();
            if (length > titleLen)  next.setTitle(next.getTitle().substring(0, titleLen));
            nextContent = "<a href=\""+httpProtocol+"://"+(StrUtil.isBlank(site.getDomain())?httpHost:site.getDomain())+"/"+sitePrefix+"/"+next.getSiteId()+"/"+next.getCategoryId()+"/"+next.getContentId();
            nextContent+=siteSubfix+"\">"+next.getTitle()+ "...</a>";
        }
        result.put("prev",prevContent);
        result.put("next",nextContent);
        this.binds(result);
        this.doBodyRender();


    }
}
