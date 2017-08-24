package com.zhiliao.component.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

public class KickoutSessionControlFilter extends AccessControlFilter {
    private boolean enableKckout = true; /*踢前后判断*/

    private boolean kickoutAfter = false; /*踢前后判断*/
    private int maxSession = 1; /*同一帐号最大会话数*/

    private Cache<String,  Deque<Session>> cache;

    public void setEnableKckout(boolean enableKckout) {
        this.enableKckout = enableKckout;
    }

    public void setKickoutAfter(boolean kickoutAfter) {
        this.kickoutAfter = kickoutAfter;
    }

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }

    public void setCache(CacheManager cacheManager) {
            this.cache = cacheManager.getCache("shiro-kickout-cache");
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

        Subject subject= getSubject(request, response);
        if(!subject.isAuthenticated() && !subject.isRemembered())return true;
        Session session = subject.getSession();
        String username = (String) subject.getPrincipal();
        Serializable sessionId = session.getId();
        boolean dequePushFlag = true;
        Deque<Session> deque = cache.get(username);

        if(deque == null) {
            deque = new LinkedList<>();
            cache.put(username, deque);
        }

        if (deque.isEmpty()) {
            dequePushFlag = true;
        } else {
            for (Session sessionInqueue : deque) {
                if (sessionId.equals(sessionInqueue.getId())) {
                    dequePushFlag = false;
                    break;
                }
            }
        }

        if (dequePushFlag)deque.push(session);

        while(deque.size() > maxSession) {
            Session kickoutSession;
            if(kickoutAfter)
                kickoutSession  = deque.removeFirst();
            else
                kickoutSession  = deque.removeLast();
            if(kickoutSession != null)kickoutSession.stop();
        }
        return true;
    }
}