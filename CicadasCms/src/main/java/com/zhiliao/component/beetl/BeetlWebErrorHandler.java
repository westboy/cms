package com.zhiliao.component.beetl;

import com.zhiliao.common.utils.CmsUtil;
import org.beetl.core.ConsoleErrorHandler;
import org.beetl.core.exception.BeetlException;
import org.beetl.core.exception.ErrorInfo;

import java.io.IOException;
import java.io.Writer;

public class BeetlWebErrorHandler extends ConsoleErrorHandler {


	public void processExcption(BeetlException ex, Writer writer) {
		ErrorInfo err = new ErrorInfo(ex);
		StringBuffer content = new StringBuffer();
		content.append("<!DOCTYPE html>");
		content.append("<html>");
		content.append("<head>");
		content.append("<meta charset=\"UTF-8\">");
		content.append("<title>模板解析错误</title>");
		content.append("</head>");
		content.append("<body>");
		content.append("<p style=\"font-size:22px;padding-left:10px;\"><b>"+err.getType()+"</b></p>");
		content.append("<p style=\"font-size:18px;padding-left:10px;\">描述 : " + (!CmsUtil.isNullOrEmpty(err.getMsg())?err.getMsg():"上面信息已经很详细 :)") + (CmsUtil.isNullOrEmpty(err.getCause())?"":"( "+err.getCause() +" )")+" </p>");
        content.append("<p style=\"font-size:18px;padding-left:10px;\">位置 : " +err.getResourceCallStack()+" ~ 第"+ err.getErrorTokenLine() + "行</p>");
		content.append("</body>");
		content.append("</html>");
		try {
			writer.write(content.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
