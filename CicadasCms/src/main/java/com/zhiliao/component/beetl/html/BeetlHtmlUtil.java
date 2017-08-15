package com.zhiliao.component.beetl.html;


import com.zhiliao.component.beetl.thread.HtmlThread;
import com.zhiliao.common.utils.PathUtil;
import com.zhiliao.common.utils.StrUtil;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.StringWriter;
import java.util.Map;


@Component
public class BeetlHtmlUtil {


	@Autowired
	private GroupTemplate groupTemplate;

	@Value("${system.http.protocol}")
	private String httpProtocol;

	@Value("${system.http.host}")
	private String httpHost;

	@Value("${system.site.subfix}")
	private String siteSubfix;

	@Value("${system.site.prefix}")
	private String sitePrefix;


	private final String STATIC_SUFFIX = ".html";


	public void create(HttpServletRequest request,Integer siteId, String action, Map<String, Object> attr, String theme, String tpl) {
		String view = "www"+File.separator + theme + File.separator + tpl+STATIC_SUFFIX;
		Template template = groupTemplate.getTemplate(view);
		StringWriter writer = new StringWriter();
		template.binding("request", request);
		template.binding("ctxPath", request.getContextPath());
		template.binding("baseURL", httpProtocol + "://" + httpHost);
		template.binding(attr);
		template.renderTo(writer);
		HtmlObject obj = new HtmlObject();
		obj.setContent(format(writer.toString()));
		String fileUrl = PathUtil.getWebRootPath() +File.separator+ "html" + File.separator+ siteId + File.separator + (StrUtil.isBlank(action)?"index":action) + ".html";
		new File(fileUrl).delete();
		obj.setFileUrl(fileUrl);
		HtmlThread.addHtml(obj);
	}
     
	private String format(String page){
		return page.replace("/"+sitePrefix+"/","/html/").replace(siteSubfix, STATIC_SUFFIX);
	}

}
