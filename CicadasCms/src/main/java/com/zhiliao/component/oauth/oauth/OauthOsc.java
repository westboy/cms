package com.zhiliao.component.oauth.oauth;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhiliao.component.oauth.util.StrKit;
import com.zhiliao.component.oauth.util.TokenUtil;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;


public class OauthOsc extends Oauth {

	private static final String AUTH_URL = "http://www.oschina.net/action/oauth2/authorize"; // 授权
	private static final String TOKEN_URL = "http://www.oschina.net/action/openapi/token"; // tonken
	private static final String USER_INFO_URL = "http://www.oschina.net/action/openapi/user"; // 用户信息
	private static final String TWEET_PUB = "http://www.oschina.net/action/openapi/tweet_pub"; // 动弹

	public static final String SESSION_STATE = "_SESSION_STATE_OSC_";
	public static final String OAUTH_TYPE = "OSC";

	@Value("${oauthOsc.openid}")
	private String clientId;
	@Value("${oauthOsc.openkey}")
	private String clientSecret;
	@Value("${oauthOsc.redirect}")
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
	 * @param code 换取token
	 * @return String 返回类型
	 */
	public String getTokenByCode(String code) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("code", code);
		params.put("client_id", clientId);
		params.put("client_secret", clientSecret);
		params.put("grant_type", "authorization_code");
		params.put("redirect_uri", redirectUri);
		String token = TokenUtil.getAccessToken(super.doGet(TOKEN_URL, params));
		LOGGER.debug(token);
		return token;
	}

	/**
	 *  获取用户信息
	 * @param accessToken AccessToken
	 * @return JSONObject
	 */
	public JSONObject getUserInfo(String accessToken) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", accessToken);
		String userInfo = super.doGet(USER_INFO_URL, params);
		JSONObject dataMap = JSON.parseObject(userInfo);
		LOGGER.debug(dataMap.toJSONString());
		return dataMap;
	}
	
	/**
	 * 发送文字动弹
	 * @param accessToken AccessToken
	 * @param msg 动态内容
	 * @return JSONObject
	 */
	public JSONObject tweetPub(String accessToken, String msg) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", accessToken);
		params.put("msg", msg);
		return JSON.parseObject(super.doPost(TWEET_PUB, params));
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
		LOGGER.debug(dataMap.toJSONString());
		return dataMap;
	}
}
