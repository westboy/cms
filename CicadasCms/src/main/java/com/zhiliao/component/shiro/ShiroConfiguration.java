package com.zhiliao.component.shiro;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zhiliao.component.shiro.realm.AdminRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.ServletContainerSessionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.Collection;
import java.util.Map;

@Configuration
public class ShiroConfiguration {


	/**
	 * RememberMeCookie
	 * @return
	 */
	public SimpleCookie getRememberMeCookie(){
		SimpleCookie sessionIdCookie = new SimpleCookie("rememberMe");
		sessionIdCookie.setMaxAge(2592000);
		sessionIdCookie.setHttpOnly(true);
		return sessionIdCookie;
	}

	/**
	 *rememberMeManager
	 * @return
	 */
	@Bean(name= "rememberMeManager")
	public CookieRememberMeManager getRememberMeManager(){
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
		cookieRememberMeManager.setCookie(getRememberMeCookie());
		return cookieRememberMeManager;
	}


	/**
	 * 使用默认session
	 *
	 * @return
	 */
	@Bean(name="sessionManager")
	public ServletContainerSessionManager servletContainerSessionManager() {
		ServletContainerSessionManager sessionManager = new ServletContainerSessionManager();
		return sessionManager;
	}

	/**
	 *  凭证匹配器
	 * @return
	 */
	@Bean(name = "hashedCredentialsMatcher")
	public HashedCredentialsMatcher getHashedCredentialsMatcher(){
		HashedCredentialsMatcher hashedCredentialsMatcher =  new HashedCredentialsMatcher("SHA-256");
		hashedCredentialsMatcher.setHashIterations(2);
		hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
		return hashedCredentialsMatcher;
	}




	@Bean(name = "adminRealm")
	@DependsOn(value="lifecycleBeanPostProcessor")
	public AdminRealm getAdminRealm(HashedCredentialsMatcher hashedCredentialsMatcher){
		AdminRealm adminRealm = new AdminRealm();
		adminRealm.setCredentialsMatcher(hashedCredentialsMatcher);
		return adminRealm;
	}


	@Bean
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	/**
	 * @see org.apache.shiro.mgt.SecurityManager
	 * @return
	 */
	@Bean(name="securityManager")
	public DefaultWebSecurityManager securityManager(CookieRememberMeManager rememberMeManager,
                                                     SessionManager sessionManager,
                                                     EhCacheManager cacheManager,
                                                     AdminRealm adminRealm) {
		DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
		Collection<Realm> realms = Lists.newArrayList();
		realms.add(adminRealm);
		manager.setRealms(realms);
		manager.setSessionManager(sessionManager);
		manager.setRememberMeManager(rememberMeManager);
		manager.setCacheManager(cacheManager);
		return manager;
	}

	@Bean
	public EhCacheManager ehCacheManager(EhCacheCacheManager cacheManager){
		EhCacheManager ehCacheManager  = new EhCacheManager();
		ehCacheManager.setCacheManager(cacheManager.getCacheManager());
		return  ehCacheManager;
	}


	/**
	 * @see org.apache.shiro.spring.web.ShiroFilterFactoryBean
	 * @return
	 */
	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean shiroFilter(@Value("${system.login.path}") String loginPath,
											  @Value("${system.login.enabled-kickout}") String enabledKickout,
											  @Value("${system.login.max-session}") String maxSession,
											  SecurityManager securityManager,
											  EhCacheManager cacheManager){
		ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
		bean.setSecurityManager(securityManager);
		bean.setLoginUrl("/login");
		bean.setUnauthorizedUrl("/unauthorized");
		Map<String, Filter>filters = Maps.newHashMap();
		filters.put("anon", new AnonymousFilter());
		filters.put("auth",new MyFormAuthenticationFilter());

		/*踢人 begin*/
		KickoutSessionControlFilter kickoutSessionControlFilter = new KickoutSessionControlFilter();
		kickoutSessionControlFilter.setCache(cacheManager);
		kickoutSessionControlFilter.setMaxSession(Integer.parseInt(maxSession));
		kickoutSessionControlFilter.setEnableKckout(Boolean.parseBoolean(enabledKickout));
		filters.put("kick",kickoutSessionControlFilter);
		/*踢人 end*/
		bean.setFilters(filters);
		Map<String, String> chains = Maps.newHashMap();

		chains.put("/login", "anon");
		chains.put("/doLogin", "anon");
		chains.put("/logout", "logout");
		chains.put("/upload/**","auth");
        //后台路径
		chains.put(loginPath+"/login", "anon");
		chains.put(loginPath+"/doLogin", "anon");
		chains.put("/system/**", "kick,auth,perms[\"system\"]");
		bean.setFilterChainDefinitionMap(chains);
		return bean;
	}


	/**
	 * 开启shiro注解
	 * @param securityManager
	 * @return
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(SecurityManager securityManager){
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

	/**
	 * 注册shiroFilter
	 * @return
	 */
	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
		DelegatingFilterProxy delegatingFilterProxy = new DelegatingFilterProxy("shiroFilter");
		delegatingFilterProxy.setTargetFilterLifecycle(true);
		filterRegistration.setFilter(delegatingFilterProxy);
		filterRegistration.setEnabled(true);
		filterRegistration.addUrlPatterns("/*");
		filterRegistration.setDispatcherTypes(DispatcherType.REQUEST);
		return filterRegistration;
	}




}