package com.zhiliao.component.beetl.tag.cms;

import com.github.pagehelper.PageInfo;
import com.zhiliao.common.exception.CmsException;
import com.zhiliao.common.exception.SystemException;
import com.zhiliao.common.utils.CmsUtil;
import com.zhiliao.common.utils.Pojo2MapUtil;
import com.zhiliao.common.utils.StrUtil;
import com.zhiliao.module.web.cms.service.ContentService;
import com.zhiliao.module.web.cms.service.SiteService;
import com.zhiliao.mybatis.model.TCmsContent;
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
        String target =  (String) this.getAttributeValue("target");
        String isPic =  (String) this.getAttributeValue("isPic");
        String isRecommend =  (String) this.getAttributeValue("isRecommend");
        Integer orderBy =  Integer.parseInt((String) this.getAttributeValue("orderBy"));
        Integer size =  Integer.parseInt((String) this.getAttributeValue("size"));
        Integer isHot =  Integer.parseInt((String) this.getAttributeValue("isHot"));
        PageInfo<TCmsContent> pageInfo = contentService.findContentListBySiteIdAndCategoryId(siteId, categoryId, orderBy, size, hasChild, isHot, isPic,isRecommend);
        if(CmsUtil.isNullOrEmpty(pageInfo.getList())) return;
        try {
            wrapRender(pageInfo.getList(),titleLen,siteId);
        } catch (Exception e) {
            throw new CmsException(e.getMessage());
        }
    }

    private void wrapRender(List<TCmsContent>  contentList, int titleLen, int siteId) throws Exception {
        int i = 1;
        for (TCmsContent content : contentList) {
            String title = content.getTitle();
            int length = title.length();
            if (length > titleLen) {
                content.setTitle(title.substring(0, titleLen) + "...");
            }
            if (StrUtil.isBlank(content.getUrl())) {
                TCmsSite site = siteService.findById(siteId);
                if(CmsUtil.isNullOrEmpty(site)) throw new CmsException("站点不存在[siteId:"+siteId+"]");
                String url = httpProtocol + "://" + (StrUtil.isBlank(site.getDomain())?httpHost:site.getDomain()) + "/"+sitePrefix+"/"+site.getSiteId()+"/";
                url+=content.getCategoryId()+"/"+content.getContentId();
                content.setUrl(url+siteSubfix);
            }
            Map result = Pojo2MapUtil.toMap(content);
            result.put("index",i);
            this.binds(result);
            this.doBodyRender();
            i++;
        }

    }

}
