package com.zhiliao.component.shiro;

import com.zhiliao.common.utils.JsonUtil;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;

public class MyFormAuthenticationFilter extends FormAuthenticationFilter{
    private static final Logger log = LoggerFactory.getLogger(MyFormAuthenticationFilter.class);

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (isLoginRequest(request, response)) {
            if (isLoginSubmission(request, response)) {
                if (log.isTraceEnabled()) {
                    log.trace("Login submission detected.  Attempting to execute login.");
                }
                return executeLogin(request, response);
            } else {
                if (log.isTraceEnabled()) {
                    log.trace("Login page view.");
                }
                //allow them to see the login page ;)
                return true;
            }
        } else {
            if (log.isTraceEnabled()) {
                log.trace("Attempting to access a path which requires authentication.  Forwarding to the " +
                        "Authentication url [" + getLoginUrl() + "]");
            }
            if (!"XMLHttpRequest" .equalsIgnoreCase(((HttpServletRequest) request).getHeader("X-Requested-With"))) {// 不是ajax请求
                saveRequestAndRedirectToLogin(request, response);
            } else {
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.println(JsonUtil.toTIMEOUT());
                out.flush();
                out.close();
            }
            return false;
        }
    }

}
