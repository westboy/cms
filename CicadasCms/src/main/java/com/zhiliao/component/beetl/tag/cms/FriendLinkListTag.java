package com.zhiliao.component.beetl.tag.cms;

import com.github.pagehelper.PageInfo;
import com.zhiliao.common.exception.CmsException;
import com.zhiliao.common.utils.CmsUtil;
import com.zhiliao.common.utils.Pojo2MapUtil;
import com.zhiliao.module.web.cms.service.FriendlinkService;
import com.zhiliao.mybatis.model.TCmsFriendlink;
import org.beetl.core.GeneralVarTagBinding;
import org.springframework.beans.factory.annotation.Autowired;
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
public class FriendLinkListTag extends GeneralVarTagBinding {

	@Autowired
	private FriendlinkService friendlinkService;

	@Override
	public void render() {

		String size = (String) this.getAttributeValue("size");
		String type = (String) this.getAttributeValue("type");
		String groupId = (String) this.getAttributeValue("groupId");
        if(CmsUtil.isNullOrEmpty(size))
        	throw  new CmsException("[友链标签]:size不能为空");
        TCmsFriendlink friendlink = new TCmsFriendlink();
		if(!CmsUtil.isNullOrEmpty(type))friendlink.setLinkType(Integer.parseInt(type));
		if(!CmsUtil.isNullOrEmpty(groupId))friendlink.setGroupId(Integer.parseInt(groupId));
		PageInfo<TCmsFriendlink> page = friendlinkService.page(1,Integer.parseInt(size),friendlink);
		try {
			this.wrapRender(page.getList());
		} catch (Exception e) {
			throw new CmsException(e.getMessage());
		}

	}

		private void wrapRender(List<TCmsFriendlink> list) throws Exception {
        int i=1;
        for (TCmsFriendlink link : list){
            Map result=Pojo2MapUtil.toMap(link);
			result.put("index",i);
        	this.binds(result);
			this.doBodyRender();
			i++;
		}
	}

}
