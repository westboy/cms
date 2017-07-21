package com.zhiliao.common.aop;

import com.zhiliao.common.annotation.ParamNotNull;
import com.zhiliao.common.exception.ApiException;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
public class ParamNotNullAspect {

	@Autowired
	private HttpServletRequest request;


	/**参数是否为空统一校验*/
	@Around("@annotation(com.zhiliao.common.annotation.ParamNotNull) && @annotation(paramNotNull)")
	public Object advice(ProceedingJoinPoint joinPoint,ParamNotNull paramNotNull) throws Throwable{
		String[] paraName = paramNotNull.parameter().split(",");
		for (String para : paraName) {
			String parameter2 = request.getParameter(para);
			if (StringUtils.isEmpty(parameter2)) {
				throw new ApiException("参数[" + para + "]不能为空");
			}
		}
		return joinPoint.proceed();
	}
	
}
