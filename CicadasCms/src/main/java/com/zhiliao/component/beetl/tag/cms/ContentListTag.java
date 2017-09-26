package com.zhiliao.component.beetl.tag.cms;

import com.github.pagehelper.PageInfo;
import com.zhiliao.common.exception.CmsException;
import com.zhiliao.common.exception.SystemException;
import com.zhiliao.common.utils.CmsUtil;
import com.zhiliao.common.utils.StrUtil;
import com.zhiliao.module.web.cms.service.ContentService;
import com.zhiliao.module.web.cms.service.SiteService;
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
public class ContentListTag extends GeneralVarTagBinding {

    @Autowired
    private ContentService contentService;

    @Autowired
    private SiteService siteService;

    @Value("${system.http.protocol}")
    private String httpProtocol;

    @Value("${system.site.subfix}")
    private String siteSubfix;

    @Value("${system.http.host}")
    private String httpHost;

    @Value("${system.site.prefix}")
    private String sitePrefix;


    /**
     * 文章列表标签
     *
     * siteId:站点id
     * categoryId:分类编号
     * hasChild：是否包含子栏目内容
     * isHot：热门
     * titleLen:标题长度
     * target:是否新窗口打开
     * orderBy:排序
     * size:调用条数
     * recommend:是否推荐 ：0为不推荐，1为推荐
     *
     */
    @Override
    public void render() {
        if (CmsUtil.isNullOrEmpty(this.args[1]))
            throw  new SystemException("标签参数不能为空！");
        Integer titleLen =  Integer.parseInt((String) this.getAttributeValue("titleLen"));
        Integer siteId=  (this.getAttributeValue("siteId") instanceof String)?Integer.parseInt((String) this.getAttributeValue("siteId")):(Integer)this.getAttributeValue("siteId");
        Long categoryId=  (this.getAttributeValue("categoryId") instanceof String)?Long.parseLong((String) this.getAttributeValue("categoryId")):(Long) this.getAttributeValue("categoryId");
        Integer hasChild=  Integer.parseInt((String) this.getAttributeValue("hasChild"));
        Integer isPic =  Integer.parseInt(CmsUtil.isNullOrEmpty(this.getAttributeValue("isPic"))?"3":(String)this.getAttributeValue("isPic"));
        Integer isRecommend =  Integer.parseInt(CmsUtil.isNullOrEmpty(this.getAttributeValue("isRecommend"))?"0":(String) this.getAttributeValue("isRecommend"));
        Integer orderBy =  Integer.parseInt((String) this.getAttributeValue("orderBy"));
        Integer pageNumber =  Integer.parseInt((CmsUtil.isNullOrEmpty(this.getAttributeValue("pageNumber"))?"1":(String) this.getAttributeValue("pageNumber")));
        Integer pageSize =  Integer.parseInt((String) this.getAttributeValue("size"));
        Integer isHot =  Integer.parseInt((String) this.getAttributeValue("isHot"));
        PageInfo<Map> pageInfo = contentService.findContentListBySiteIdAndCategoryId(siteId, categoryId, orderBy, pageNumber,pageSize, hasChild, isHot, isPic,isRecommend);
        if(CmsUtil.isNullOrEmpty(pageInfo.getList())) return;
        this.wrapRender(pageInfo.getList(),titleLen,siteId);

    }

    private void wrapRender(List<Map>  contentList, int titleLen, int siteId) {
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
