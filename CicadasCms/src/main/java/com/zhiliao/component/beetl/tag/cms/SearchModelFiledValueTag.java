package com.zhiliao.component.beetl.tag.cms;


import com.zhiliao.common.exception.CmsException;
import com.zhiliao.common.utils.CmsUtil;
import com.zhiliao.common.utils.ControllerUtil;
import com.zhiliao.module.web.cms.vo.ModelFiledVo;
import com.zhiliao.module.web.cms.service.ModelFiledService;
import com.zhiliao.module.web.cms.service.SiteService;
import com.zhiliao.mybatis.model.TCmsModelFiled;
import org.beetl.core.GeneralVarTagBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Map.Entry;

@Service
@Scope("prototype")
public class SearchModelFiledValueTag extends GeneralVarTagBinding {


	@Value("${system.http.protocol}")
	private String httpProtocol;

	@Autowired
	private SiteService siteService;

	@Autowired
	private ModelFiledService modelFiledService;


	@Value("${system.site.prefix}")
	private String sitePrefix;

	@SuppressWarnings("unchecked")
	@Override
	public void render() {
		Integer siteId=  (this.getAttributeValue("siteId") instanceof String)?Integer.parseInt((String) this.getAttributeValue("siteId")):(Integer)this.getAttributeValue("siteId");
		Long catId=  (this.getAttributeValue("categoryId") instanceof String)?Long.parseLong((String) this.getAttributeValue("categoryId")):(Long) this.getAttributeValue("categoryId");
		Integer modelId=  (this.getAttributeValue("modelId") instanceof String)?Integer.parseInt((String) this.getAttributeValue("modelId")):(Integer)this.getAttributeValue("modelId");
		String filedName = (String) this.getAttributeValue("filedName");
		Map<String, Object> param = (Map<String, Object>) this.getAttributeValue("param");
		TCmsModelFiled modelFiled = modelFiledService.findModelFiledByFiledName(filedName);
		if(CmsUtil.isNullOrEmpty(modelFiled)){
			throw new CmsException("模型字段不存在！");
		}
		String url = httpProtocol + "://" + ControllerUtil.getDomain() + "/search?s="+siteId+"&m=" + modelId + "&c=" + catId + "&"
				+ filedName + "={filedValue}";
		String[] values = modelFiled.getFiledValue().split("#");
		String str = "";
		String kv = "";
		boolean flag = true;
		if (modelFiled.getFiledClass().equals("radio")) {
			for (String value : values) {
				ModelFiledVo mfv = new ModelFiledVo();
				if (param != null) {
					if (!param.isEmpty()) {
						for (Entry<String, Object> entry : param.entrySet()) {
							if (!filedName.equals(entry.getKey())) {
								if (!kv.equals(entry.getKey() + entry.getValue())) {
									str += "&" + entry.getKey() + "=" + entry.getValue();
								}
								kv = entry.getKey() + entry.getValue();
							}
						}
					}
				}
				if (flag) {
					mfv.setUrl(httpProtocol + "://" + ControllerUtil.getDomain() + "/search?m=" + modelId + "&c="
							+ catId + str);
					mfv.setName("全部");
					this.binds(mfv);
					this.doBodyRender();
					flag=false;
				}
				mfv.setUrl(url.replace("{filedValue}", value) + str);
				mfv.setName(value);
				this.binds(mfv);
				this.doBodyRender();

			}
		}else{
			ModelFiledVo mfv = new ModelFiledVo();
			mfv.setUrl("#");
			mfv.setName( modelFiled.getFiledValue());
			this.binds(mfv);
			this.doBodyRender();
		}
	}

	public static void main(String[] args) {
		String l = "http://127.0.0.1:8080/search?m=48&c=5&test=&test=qwe";
		System.out.println(l.contains("test"));
	}

}
