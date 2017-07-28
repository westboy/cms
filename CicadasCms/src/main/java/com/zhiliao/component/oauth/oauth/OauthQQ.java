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
public class OauthQQ extends Oauth{

	private static final String AUTH_URL = "https://graph.qq.com/oauth2.0/authorize";
	private static final String TOKEN_URL = "https://graph.qq.com/oauth2.0/token";
	private static final String TOKEN_INFO_URL = "https://graph.qq.com/oauth2.0/me";
	private static final String USER_INFO_URL = "https://graph.qq.com/user/get_user_info";

	public static final String SESSION_STATE = "_SESSION_STATE_QQ_";
	public static final String OAUTH_TYPE = "QQ";

	@Value("${oauthQQ.openid}")
	private String clientId;
	@Value("${oauthQQ.openkey}")
	private String clientSecret;
	@Value("${oauthQQ.redirect}")
	private String redirectUri;

	/**
	 * 获取授权url
	 * DOC：http://wiki.connect.qq.com/%E4%BD%BF%E7%94%A8authorization_code%E8%8E%B7%E5%8F%96access_token
	 * @param state OAuth2.0标准协议建议，利用state参数来防止CSRF攻击
	 * @return String 返回类型
	 */ 
	public String getAuthorizeUrl(String state) {
		return super.getAuthorizeUrl(AUTH_URL, state,clientId,redirectUri);
	}

	/**
	 * 获取token
	 * @param code 根据code获取token
	 * @return String 返回类型
	 */
	public String getTokenByCode(String code) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("code", code);
		params.put("client_id", clientId);
		params.put("client_secret", clientSecret);
		params.put("grant_type", "authorization_code");
		params.put("redirect_uri", redirectUri);
		// access_token=FE04************************CCE2&expires_in=7776000
		String token = TokenUtil.getAccessToken(super.doGet(TOKEN_URL, params));
		LOGGER.debug("OauthQQ Token ："+token);
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
		// callback( {"client_id":"YOUR_APPID","openid":"YOUR_OPENID"} );
		String openid = TokenUtil.getOpenId(super.doGet(TOKEN_INFO_URL, params));
		LOGGER.debug("OauthQQ OpenId ："+openid );
		return openid;
	}
	
	/**
	 * 获取用户信息
	 * DOC：http://wiki.connect.qq.com/get_user_info
	 * @param accessToken AccessToken
	 * @param uid 用户id
	 * @return String 返回类型
	 */
	public String getUserInfo(String accessToken, String uid) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", accessToken);
		params.put("oauth_consumer_key", clientId);
		params.put("openid", uid);
		params.put("format", "json");
		// // {"ret":0,"msg":"","nickname":"YOUR_NICK_NAME",...}
		String userinfo = super.doGet(USER_INFO_URL, params);
		LOGGER.debug("QQOauth UserInfo ："+userinfo);
		return userinfo;
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
		String openId = getTokenInfo(accessToken);
		if (StrKit.isBlank(openId)) {
			throw new RuntimeException("openId is Blank!");
		}
		JSONObject dataMap = JSON.parseObject(getUserInfo(accessToken, openId));
		dataMap.put("openid", openId);
		dataMap.put("access_token", accessToken);
		return dataMap;
	}

}
