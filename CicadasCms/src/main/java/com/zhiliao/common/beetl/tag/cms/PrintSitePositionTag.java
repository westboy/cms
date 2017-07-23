package com.zhiliao.common.beetl.tag.cms;

import com.google.common.collect.Maps;
import com.zhiliao.common.exception.CmsException;
import com.zhiliao.common.utils.CmsUtil;
import com.zhiliao.common.utils.ControllerUtil;
import com.zhiliao.mybatis.model.master.TCmsCategory;
import com.zhiliao.mybatis.model.master.TCmsSite;
import org.beetl.core.GeneralVarTagBinding;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Description:多少度
 *
 * @author Jin
 * @create 2017-06-05
 **/
@Service
@Scope("prototype")
public class PrintSitePositionTag extends GeneralVarTagBinding {

    @Value("${system.http.protocol}")
    private String httpProtocol;

    @Value("${system.site.subfix}")
    private String siteSubfix;

    @Value("${system.site.prefix}")
    private String sitePrefix;

    @Override
    public void render() {
        String url = httpProtocol+"://"+ ControllerUtil.getDomain();
        String baseURL = "";
        String catURL="";
        Map reult= Maps.newHashMap();
        TCmsSite site = (TCmsSite) this.getAttributeValue("site");
        if(CmsUtil.isNullOrEmpty(site))
            throw new CmsException("面包屑导航出错[site参数必须为site对象]");
        baseURL+=url+"/"+sitePrefix+"/"+site.getSiteId()+"/";
        reult.put("baseURL","<a href=\""+baseURL+"\">首页</a>");
        TCmsCategory category = (TCmsCategory) this.getAttributeValue("category");
        if(CmsUtil.isNullOrEmpty(site))
            throw new CmsException("面包屑导航出错[category参数必须为category对象]");
        catURL+=baseURL+category.getCategoryId()+siteSubfix;
        reult.put("catURL","<a href=\""+catURL+"\">"+category.getCategoryName()+"</a>");
        this.binds(reult);
        this.doBodyRender();
    }
}
