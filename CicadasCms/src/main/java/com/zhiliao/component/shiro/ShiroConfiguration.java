package com.zhiliao.component.shiro;

import com.google.common.collect.Maps;
import com.zhiliao.component.shiro.realm.AdminRealm;
import com.zhiliao.component.shiro.realm.UserRealm;
import io.buji.pac4j.filter.CallbackFilter;
import io.buji.pac4j.filter.LogoutFilter;
import io.buji.pac4j.filter.SecurityFilter;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.ServletContainerSessionManager;
import org.assertj.core.util.Lists;
import org.pac4j.core.config.Config;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
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


	@Bean(name = "userRealm")
	@DependsOn(value="lifecycleBeanPostProcessor")
	public UserRealm userRealm(HashedCredentialsMatcher hashedCredentialsMatcher) {
		UserRealm userRealm = new UserRealm();
		userRealm.setCredentialsMatcher(hashedCredentialsMatcher);
		return userRealm;
	}


	@Bean(name = "adminRealm")
	@DependsOn(value="lifecycleBeanPostProcessor")
	public AdminRealm getAdminRealm(HashedCredentialsMatcher hashedCredentialsMatcher){
		AdminRealm adminRealm = new AdminRealm();
		adminRealm.setCredentialsMatcher(hashedCredentialsMatcher);
		return adminRealm;
	}

	@Bean(name = "defineModularRealmAuthenticator")
	public DefaultModularRealm getDefineModularRealmAuthenticator(
			AdminRealm adminRealm,
			UserRealm userRealm){
		DefaultModularRealm defineModularRealmAuthenticator = new  DefaultModularRealm();
		Map<String, Object> definedRealms = Maps.newHashMap();
		definedRealms.put("admin",adminRealm);
		definedRealms.put("user",userRealm);
		defineModularRealmAuthenticator.setDefinedRealms(definedRealms);
        return defineModularRealmAuthenticator;
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
	@DependsOn(value = "sessionManager")
	public DefaultWebSecurityManager securityManager(
			@Qualifier("rememberMeManager") CookieRememberMeManager rememberMeManager,
			@Qualifier("sessionManager") ServletContainerSessionManager sessionManager,
			@Qualifier("defineModularRealmAuthenticator") DefaultModularRealm defaultModularRealm,
			 AdminRealm adminRealm,
			 UserRealm userRealm) {
		DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
		manager.setAuthenticator(defaultModularRealm);
		Collection<Realm> realms = Lists.newArrayList();
		realms.add(adminRealm);
		realms.add(userRealm);
		manager.setRealms(realms);
		manager.setSessionManager(sessionManager);
		manager.setRememberMeManager(rememberMeManager);
		manager.setCacheManager(memoryConstrainedCacheManager());
		return manager;
	}


   public MemoryConstrainedCacheManager memoryConstrainedCacheManager(){
	      return new MemoryConstrainedCacheManager();
   }

	/**
	 * @see org.apache.shiro.spring.web.ShiroFilterFactoryBean
	 * @return
	 */
	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean shiroFilter(@Value("${system.login.path}") String loginPath,
											  @Qualifier(value ="securityManager") DefaultWebSecurityManager securityManager,
											  @Qualifier("config") Config config,
											  @Value("${sso.client.serverName}") String localUrl){
		ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
		bean.setSecurityManager(securityManager);
		bean.setLoginUrl("/login");
		bean.setUnauthorizedUrl("/unauthorized");
		Map<String, Filter>filters = Maps.newHashMap();
		filters.put("anon", new AnonymousFilter());
		filters.put("auth",new MyFormAuthenticationFilter());
		/*PAC4J */
		SecurityFilter  casSecurityFilter = new SecurityFilter();
		casSecurityFilter.setConfig(config);
		casSecurityFilter.setClients("CasClient");
		filters.put("cas",casSecurityFilter);
		/*回调*/
		CallbackFilter callbackFilter = new CallbackFilter();
		callbackFilter.setConfig(config);
		filters.put("callback",callbackFilter);
		/*退出*/
		LogoutFilter logoutFilter = new LogoutFilter();
		logoutFilter.setConfig(config);
		logoutFilter.setCentralLogout(true);
		logoutFilter.setLocalLogout(true);
		logoutFilter.setDefaultUrl(localUrl);
		Map<String, String> chains = Maps.newHashMap();
		filters.put("casLogout",logoutFilter);
		bean.setFilters(filters);
		chains.put("/login", "anon");
		chains.put("/doLogin", "anon");
		chains.put("/logout", "logout");
		chains.put("/casLogout", "casLogout");
		chains.put("/upload/**","auth");
		chains.put("/cas/user/**", "cas");
		chains.put("/callback", "callback");
        //后台路径
		chains.put(loginPath+"/login", "anon");
		chains.put(loginPath+"/doLogin", "anon");
		chains.put("/system/**", "auth,perms[\"system\"]");
		bean.setFilterChainDefinitionMap(chains);
		return bean;
	}


	/**
	 * 开启shiro注解
	 * @param securityManager
	 * @return
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(@Qualifier("securityManager") DefaultWebSecurityManager securityManager){
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