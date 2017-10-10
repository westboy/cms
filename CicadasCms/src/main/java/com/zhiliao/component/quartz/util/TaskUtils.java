package com.zhiliao.component.quartz.util;

import com.zhiliao.common.exception.SystemException;
import com.zhiliao.component.quartz.job.ScheduleJob;
import com.zhiliao.component.spring.SpringContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;


public class TaskUtils {
    public final static Logger log = LoggerFactory.getLogger(TaskUtils.class);

	/**
	 * 通过反射调用scheduleJob中定义的方法
	 * 
	 * @param scheduleJob
	 */
	public static void invokeMethod(ScheduleJob scheduleJob) {
		Object object = null;
		Class<?> clazz;
		if (StringUtils.isNotBlank(scheduleJob.getSpringBean())) {
			object = SpringContextHolder.getBean(scheduleJob.getSpringBean());
		} else if (StringUtils.isNotBlank(scheduleJob.getBeanClass())) {
			try {
				clazz = Class.forName(scheduleJob.getBeanClass());
				object = clazz.newInstance();
			} catch (Exception e) {
				throw new SystemException(e.getMessage());
			}
		}
		if (object == null) {
			log.error("任务名称 = [" + scheduleJob.getJobName() + "]---------------未启动成功，请检查是否配置正确！！！");
			return;
		}
		clazz = object.getClass();
		Method method = null;
		try {
			method = clazz.getDeclaredMethod(scheduleJob.getMethodName());
		} catch (NoSuchMethodException e) {
			log.error("任务名称 = [" + scheduleJob.getJobName() + "]---------------未启动成功，方法名设置错误！！！");
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		if (method != null) {
			try {
				method.invoke(object);
			} catch (Exception e) {
				throw new SystemException(e.getMessage());
			}
		}
	}
}
