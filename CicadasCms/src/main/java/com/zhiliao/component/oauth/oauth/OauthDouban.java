package com.zhiliao.component.oauth.oauth;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhiliao.component.oauth.util.StrKit;
import com.zhiliao.component.oauth.util.TokenUtil;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

public class OauthDouban extends Oauth {
	private static final String AUTH_URL = "https://www.douban.com/service/auth2/auth";
	private static final String TOKEN_URL = "https://www.douban.com/service/auth2/token";
	private static final String USER_INFO_URL = "https://api.douban.com/v2/user/~me";

	public static final String SESSION_STATE = "_SESSION_STATE_DOUBAN_";
	public static final String OAUTH_TYPE = "DOUBAN";

	@Value("${oauthDouban.openid}")
	private String clientId;
	@Value("${oauthDouban.openkey}")
	private String clientSecret;
	@Value("${oauthDouban.redirect}")
	private String redirectUri;


	/**
	 * 获取授权url
	 * @param state OAuth2.0标准协议建议，利用state参数来防止CSRF攻击
	 * @return String 返回类型
	 */
	public String getAuthorizeUrl(String state) {
		return super.getAuthorizeUrl(AUTH_URL, state,clientId,redirectUri);
	}

	/**
	 * 获取token
	 * @param code 采用code获取token
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
		return token;
	}

	/**
	 *  获取用户信息
	 * @param accessToken accessToken
	 * @return JSONObject
	 */
	public JSONObject getUserInfo(String accessToken) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("Authorization", "Bearer " + accessToken);
		String userInfo = super.doGetWithHeaders(USER_INFO_URL, params);
		JSONObject dataMap = JSON.parseObject(userInfo);
		return dataMap;
	}

	/**
	 * 根据code一步获取用户信息
	 * @param code oauth code
	 * @return JSONObject 返回类型
	 */
	public JSONObject getUserInfoByCode(String code) {
		String accessToken = getTokenByCode(code);
		if (StrKit.isBlank(accessToken)) {
			throw new RuntimeException("accessToken is Blank!");
		}
		JSONObject dataMap = getUserInfo(accessToken);
		dataMap.put("access_token", accessToken);
		return dataMap;
	}

}
