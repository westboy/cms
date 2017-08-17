package com.zhiliao.component.beetl.tag.cms;

import com.github.pagehelper.PageInfo;
import com.zhiliao.common.exception.CmsException;
import com.zhiliao.common.utils.CmsUtil;
import com.zhiliao.common.utils.Pojo2MapUtil;
import com.zhiliao.common.utils.StrUtil;
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
public class ContentPageTag extends GeneralVarTagBinding {


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
    /**
     *
     * 内容分页标签
     *
     */
    @Override
    public void render() {
        if (CmsUtil.isNullOrEmpty(this.args[1]))
            throw  new CmsException("[内容分页标签]标签参数不能为空！");
        Integer siteId=  (this.getAttributeValue("siteId") instanceof String)?Integer.parseInt((String) this.getAttributeValue("siteId")):(Integer)this.getAttributeValue("siteId");
        if (CmsUtil.isNullOrEmpty(siteId))
            throw  new CmsException("[内容分页标签]站点id不能为空");
        PageInfo<TCmsContent> pageInfo = (PageInfo) this.getAttributeValue("page");
        if (CmsUtil.isNullOrEmpty(pageInfo))
            throw  new CmsException("[内容分页标签]此标签只能栏目页和栏目列表页使用");
        String titleLen = (String) this.getAttributeValue("titleLen");
        if (CmsUtil.isNullOrEmpty(pageInfo))
            throw  new CmsException("[内容分页标签]站点id不能为空");
        try {
            wrapRender(pageInfo.getList(),Integer.parseInt(titleLen),siteId);
        } catch (Exception e) {
            throw new CmsException(e.getMessage());
        }
    }

    private void wrapRender(List<TCmsContent>  contents, int titleLen, int siteId) throws Exception {
        int i = 1;
        for (TCmsContent content : contents) {
            String title = content.getTitle();
            int length = title.length();
            if (length > titleLen) content.setTitle(title.substring(0, titleLen) + "...");
            if (StrUtil.isBlank(content.getUrl())) {
                TCmsSite site = siteService.findById(siteId);
                if(CmsUtil.isNullOrEmpty(site)) throw new CmsException("站点不存在[siteId:"+siteId+"]");
                String url = httpProtocol + "://" + (StrUtil.isBlank(site.getDomain())?httpHost:site.getDomain()) + "/"+sitePrefix+"/"+siteId+"/";
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
