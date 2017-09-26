package com.zhiliao.component.shiro.realm;

import com.zhiliao.mybatis.model.TSysUser;
import com.zhiliao.module.web.system.service.SysUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;


@Component
public class AdminRealm extends AuthorizingRealm {

    @Autowired
	@Lazy
	private SysUserService userService;


	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String username = (String) principals.getPrimaryPrincipal();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setStringPermissions(userService.findSysUserPermissionsByUsername(username));
		info.setRoles(userService.findSysUserRolesPByUserame(username));
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String username= (String) token.getPrincipal();
		TSysUser user = userService.findSysUserByUsername(username);
		if (user == null) {
			throw new UnknownAccountException();
		}
		System.out.println("user : "+user .toString());
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), ByteSource.Util.bytes(user.getSalt()), getName());
		return info;
	}

}