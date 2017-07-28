package com.zhiliao.component.oauth.oauth;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhiliao.component.oauth.util.StrKit;
import com.zhiliao.component.oauth.util.TokenUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class OauthSina extends Oauth {
	private static final String AUTH_URL = "https://api.weibo.com/oauth2/authorize";
	private static final String TOKEN_URL = "https://api.weibo.com/oauth2/access_token";
	private static final String TOKEN_INFO_URL = "https://api.weibo.com/oauth2/get_token_info";
	private static final String USER_INFO_URL = "https://api.weibo.com/2/users/show.json";

	public static final String SESSION_STATE = "_SESSION_STATE_SINA_";
	public static final String OAUTH_TYPE = "SINA";

	@Value("${oauthSina.openid}")
	private String clientId;
	@Value("${oauthSina.openkey}")
	private String clientSecret;
	@Value("${oauthSina.redirect}")
	private String redirectUri;

	/**
	 * 获取授权url
	 * DOC：http://open.weibo.com/wiki/Oauth2/authorize
	 * @param state OAuth2.0标准协议建议，利用state参数来防止CSRF攻击
	 * @return String
	 */
	public String getAuthorizeUrl(String state) {
		return super.getAuthorizeUrl(AUTH_URL, state,clientId,redirectUri);
	}

	/**
	 * 获取token
	 * @param code 根据code换取token
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
		LOGGER.debug(token);
		return token;
	}
	
	/**
	 * 获取TokenInfo
	 * @param accessToken AccessToken
	 * @return String 返回类型
	 */
	public String getTokenInfo(String accessToken) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", accessToken);
		
		String jsonStr = super.doPost(TOKEN_INFO_URL, params);
		JSONObject json = JSONObject.parseObject(jsonStr);
		
		String openid = json.getString("uid");
		LOGGER.debug(openid);
		return openid;
	}
	
	/**
	 * 获取用户信息
	 * DOC：http://open.weibo.com/wiki/2/users/show
	 * @param accessToken AccessToken
	 * @param uid 用户id
	 * @return String 返回类型
	 */
	public String getUserInfo(String accessToken, String uid) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", uid);
		params.put("access_token", accessToken);
		String userInfo = super.doGet(USER_INFO_URL, params);
		LOGGER.debug(userInfo);
		return userInfo;
	}

	/**
	 * 根据code一步获取用户信息
	 * @param code oauth code
	 * @return JSONObject	返回类型
	 */
	public JSONObject getUserInfoByCode(String code) {
		String accessToken = getTokenByCode(code);
		if (StrKit.isBlank(accessToken)) {
			throw new RuntimeException("Token is Blank!");
		}
		String uid = getTokenInfo(accessToken);
		if (StrKit.isBlank(uid)) {
			throw new RuntimeException("accessToken is Blank!");
		}
		JSONObject dataMap = JSON.parseObject(getUserInfo(accessToken, uid));
		dataMap.put("access_token", accessToken);
		return dataMap;
	}

}
