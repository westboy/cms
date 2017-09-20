package com.zhiliao.component.beetl.tag.cms;

import com.google.common.collect.Maps;
import com.zhiliao.common.exception.CmsException;
import com.zhiliao.common.utils.CmsUtil;
import com.zhiliao.common.utils.ControllerUtil;
import com.zhiliao.module.web.cms.service.CategoryService;
import com.zhiliao.mybatis.model.TCmsCategory;
import com.zhiliao.mybatis.model.TCmsSite;
import org.apache.commons.collections.map.HashedMap;
import org.beetl.core.GeneralVarTagBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    @Autowired
    private CategoryService categoryService;

    @Override
    public void render() {
        String url = httpProtocol + "://" + ControllerUtil.getDomain();
        String baseURL = "";
        String pcatURL = "";
        List<Map> locations = new ArrayList<>();
        Map reult = Maps.newHashMap();
        TCmsSite site = (TCmsSite) this.getAttributeValue("site");
        if (CmsUtil.isNullOrEmpty(site))
            throw new CmsException("面包屑导航出错[site参数必须为site对象]");
        baseURL += url + "/" + sitePrefix + "/" + site.getSiteId() + "/";
        reult.put("baseURL", "<a href=\"" + baseURL + "\">首页</a>");
        TCmsCategory category = (TCmsCategory) this.getAttributeValue("category");
        if (!CmsUtil.isNullOrEmpty(category)) {
            reult.put("locations", locations(locations, baseURL, category));
        } else {
            reult.put("pcatURL", "");
        }
        if (!CmsUtil.isNullOrEmpty(category) && category.getParentId().intValue() > 0) {
            TCmsCategory parentCategory = categoryService.findById(category.getParentId());
            pcatURL += baseURL + parentCategory.getCategoryId() + siteSubfix;
            reult.put("pcatURL", "<a href=\"" + pcatURL + "\">" + parentCategory.getCategoryName() + "</a>");
        } else {
            reult.put("pcatURL", "");
        }
        this.binds(reult);
        this.doBodyRender();
    }

    private List<Map> locations(List<Map> locations, String baseURL, TCmsCategory category) {
        Map<String, String> map = new HashedMap();
        map.put("url", baseURL + category.getCategoryId() + siteSubfix);
        map.put("categoryName", category.getCategoryName());
        locations.add(0, map);
        if (category.getParentId() != 0) {
            locations = locations(locations, baseURL, categoryService.findById(category.getParentId()));
        }
        return locations;
    }

}
