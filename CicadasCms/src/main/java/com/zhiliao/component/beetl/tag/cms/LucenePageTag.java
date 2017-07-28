package com.zhiliao.component.beetl.tag.cms;

import com.github.pagehelper.PageInfo;
import com.zhiliao.common.exception.CmsException;
import com.zhiliao.component.lucene.util.IndexObject;
import com.zhiliao.common.utils.CmsUtil;
import org.beetl.core.GeneralVarTagBinding;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * lucene 全文检索标签
 *
 */
@Service
@Scope("prototype")
public class LucenePageTag extends GeneralVarTagBinding {

    @Value("${system.http.protocol}")
    private String httpProtocol;

    @Value("${system.site.subfix}")
    private String siteSubfix;
    /**
     *
     * 内容分页标签
     *
     */
    @Override
    public void render() {
        PageInfo<IndexObject> pageInfo = (PageInfo) this.getAttributeValue("page");
        if (CmsUtil.isNullOrEmpty(pageInfo))throw  new CmsException("[查询分页标签]标签调用出错！");
        if (CmsUtil.isNullOrEmpty(pageInfo)) throw  new CmsException("[查询分页标签]分页信息不存在！");
        wrapRender(pageInfo);
    }

    private void wrapRender(PageInfo<IndexObject> page) {
        for (IndexObject content : page.getList()) {
            this.binds(content);
            this.doBodyRender();
        }
    }

}
