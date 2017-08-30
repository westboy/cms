package com.zhiliao.common.aop;

import com.zhiliao.common.exception.SystemException;
import com.zhiliao.common.utils.StrUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Aspect
@Component
public class FormTokenAspect {

	private static final String PARAM_TOKEN = "token";
	private static final String PARAM_TOKEN_FLAG = "TokenFlag_";

	private static final Logger log = LoggerFactory.getLogger(FormTokenAspect.class);

	@Around("@annotation(com.zhiliao.common.annotation.FormToken)")
	public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		HttpServletResponse response =  attributes.getResponse();
		String className = joinPoint.getTarget().getClass().getName();
		HttpSession session = request.getSession();
		if ("GET".equalsIgnoreCase(request.getMethod())) {
			log.info("生成token");
			/* GET 生成 token */
		 return   this.generate(joinPoint, request, session, PARAM_TOKEN_FLAG + className);
		} else {
			log.info("验证token");
			/* POST 验证 token */
		 return   this.validation(joinPoint, request,response, session, PARAM_TOKEN_FLAG + className);
		}

	}


	public Object generate(ProceedingJoinPoint joinPoint, HttpServletRequest request, HttpSession session,
			String tokenFlag) throws Throwable {
		String uuid = StrUtil.getUUID().toString();
		String tokenInput = "<input id=\"token\"  type=\"hidden\" name=\""+PARAM_TOKEN+"\" value=\"" + uuid + "\">";
		session.setAttribute(tokenFlag, uuid);
		request.setAttribute(PARAM_TOKEN, tokenInput);
		return joinPoint.proceed();
	}


	public Object validation(ProceedingJoinPoint joinPoint, HttpServletRequest request,HttpServletResponse response, HttpSession session,
			String tokenFlag) throws Throwable {
		Object sessionFlag = session.getAttribute(tokenFlag);
		Object requestFlag = request.getParameter(PARAM_TOKEN);
		if (requestFlag!=null&&sessionFlag != null && sessionFlag.equals(requestFlag)) {
			session.removeAttribute(tokenFlag);
		}else {
			throw new SystemException("不能重复提交表单！");
		}
		return joinPoint.proceed();
	}

}
