package com.zhiliao.common.aop;

import com.zhiliao.common.annotation.ApiValidate;
import com.zhiliao.common.exception.ApiException;
import com.zhiliao.common.utils.CheckSumUtil;
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
import java.lang.reflect.Method;

@Aspect
@Component
public class ApiValidateAspect {

	private String appSecrt ="jdeFDS89HFassdsfFDNDS73FDJK";
	private String[] appids={"1000","2000"};

	private static final Logger log = LoggerFactory.getLogger(ApiValidateAspect.class);

	@Around("@annotation(com.zhiliao.common.annotation.ApiValidate)")
	public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		HttpServletResponse response =  attributes.getResponse();
        return this.validation(joinPoint,request,response);

	}


	public Object validation(ProceedingJoinPoint joinPoint, HttpServletRequest request,HttpServletResponse response) throws Throwable {
		/*获取请求参数*/
		String appId= request.getParameter("appid");
		String nonce= request.getParameter("nonce");
		String signature= request.getParameter("signature");
		String timestamp= request.getParameter("timestamp");
        /*获取方法名称*/
		String methodName = joinPoint.getSignature().getName();
		Method method = currentMethod(joinPoint, methodName);
		ApiValidate log = method.getAnnotation(ApiValidate.class);
		/* 验证appId */
		if(StrUtil.isBlank(appId))
			throw new ApiException("appId Can not be empty!");
		if(!StrUtil.isContain(appids,appId))
			throw new ApiException("appId validate failed!");
		/* 是否需要验证 Signature */
		if(!log.checkSignature()) return joinPoint.proceed();
		/* 验证timestamp */
		if(StrUtil.isBlank(timestamp))
			throw new ApiException("timestamp Can not be empty!");
		if((Long.parseLong(CheckSumUtil.getTimestamp())-Long.parseLong(timestamp))>220)
			throw new ApiException("the signature has be Expired!");
		/* 验证signature */
		if(StrUtil.isBlank(signature))
			throw new ApiException("signature Can not be empty!");
        if(!CheckSumUtil.getCheckSum(appSecrt,nonce,timestamp).trim().equals(signature.trim()))
			throw new ApiException("API interface parameter validation failed!");
		return joinPoint.proceed();
	}


	public  Method currentMethod(ProceedingJoinPoint joinPoint,String methodName){
		Method[] methods = joinPoint.getTarget().getClass().getMethods();
		Method resultMethod = null;
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				resultMethod = method;
				break;
			}
		}
		return resultMethod;
	}
}
