package com.zhiliao.component.shiro.realm;

import com.zhiliao.mybatis.model.TClientUser;
import com.zhiliao.module.web.client.service.ClientUserService;
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
public class UserRealm extends AuthorizingRealm {


    @Autowired
	@Lazy
	private ClientUserService userService;


	//权限资源角色
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		String username = (String) principals.getPrimaryPrincipal();
		info.setStringPermissions(userService.findClientUserPermissionsByUsername(username));
		info.setRoles(userService.findClientUserRolesPByUsername(username));
		return info;
	}

	//登录验证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String username= (String) token.getPrincipal();
		TClientUser user = userService.findClientUserByUsername(username);
		if (user == null) {
			throw new UnknownAccountException();
		}
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), ByteSource.Util.bytes(user.getSalt()), getName());
		return info;
	}
}