package com.zhiliao.component.oauth.oauth;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhiliao.component.oauth.util.StrKit;
import com.zhiliao.component.oauth.util.TokenUtil;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

public class OauthBaidu extends Oauth {
	private static final String AUTH_URL = "https://openapi.baidu.com/oauth/2.0/authorize";
	private static final String TOKEN_URL = "https://openapi.baidu.com/oauth/2.0/token";
	private static final String USER_INFO_URL = "https://openapi.baidu.com/rest/2.0/passport/users/getInfo";

	public static final String SESSION_STATE = "_SESSION_STATE_BAIDU_";
	public static final String OAUTH_TYPE = "BAIDU";

	@Value("${oauthBaidu.openid}")
	private String clientId;
	@Value("${oauthBaidu.openkey}")
	private String clientSecret;
	@Value("${oauthBaidu.redirect}")
	private String redirectUri;

	/**
	 * 获取授权url
	 * @param state	OAuth2.0标准协议建议，利用state参数来防止CSRF攻击
	 * @return String url
	 */
	public String getAuthorizeUrl(String state) {

		return super.getAuthorizeUrl(AUTH_URL, state,clientId,redirectUri);
	}
	
	/**
	 * 获取token
	 * @param code 使用code换取token
	 * @return String 返回类型
	 */
	public String getTokenByCode(String code) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("code", code);
		params.put("client_id", clientId);
		params.put("client_secret", clientSecret);
		params.put("grant_type", "authorization_code");
		params.put("redirect_uri", redirectUri);
		String token = TokenUtil.getAccessToken(super.doPost(TOKEN_URL, params));
		LOGGER.debug("sina:"+token);
		return token;
	}
	
	/**
	 * 获取UserInfo
	 * @param accessToken AccessToken
	 * @return String 返回类型
	 */
	public String getUserInfo(String accessToken) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", accessToken);
		return super.doPost(USER_INFO_URL, params);
	}
	
	/**
	 * 根据code一步获取用户信息
	 * @param code oauth code
	 * @return JSONObject返回类型
	 */
	public JSONObject getUserInfoByCode(String code) {
		String accessToken = getTokenByCode(code);
		if (StrKit.isBlank(accessToken)) {
			throw new RuntimeException("accessToken is Blank!");
		}
		String userInfo = getUserInfo(accessToken);
		JSONObject dataMap = JSON.parseObject(userInfo);
		dataMap.put("access_token", accessToken);
		LOGGER.debug("sina:"+dataMap);
		return dataMap;
	}
}
