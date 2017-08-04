package com.zhiliao.component.spring;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


@Component
public class SpringContextHolder  implements ApplicationContextAware {

	private static ApplicationContext applicationContext = null;

	private static Logger log = LoggerFactory.getLogger(SpringContextHolder.class);


	/*从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型*/
	public static <T> T getBean(String name) {
		assertContextInjected();
		return (T) applicationContext.getBean(name);
	}

	/*从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型*/
	public static <T> T getBean(Class<T> requiredType) {
		assertContextInjected();
		return applicationContext.getBean(requiredType);
	}

	/* 清除SpringContextHolder中的ApplicationContext为Null*/
	public static void clearHolder() {
		if (log.isDebugEnabled()) {
			log.debug("清除SpringContextHolder中的ApplicationContext:" + applicationContext);
		}
		applicationContext = null;
	}

	/* 注入Context到静态变量中*/
	public void setApplicationContext(ApplicationContext applicationContext) {
		log.debug("*************************** [SpringContextHolder Init] ******************************");
		SpringContextHolder.applicationContext = applicationContext;
	}


	/* 检查ApplicationContext不为空*/
	private static void assertContextInjected() {
		Validate.validState(applicationContext != null,"applicaitonContext属性未注入！");
	}
}
