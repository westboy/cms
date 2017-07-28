package com.zhiliao.component.oauth.util;

import com.alibaba.fastjson.JSONObject;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenUtil {
	private static final String STR_S = "abcdefghijklmnopqrstuvwxyz0123456789";
	/**
	 * 参考自 qq sdk
	 * @param string 从字符串中获取accessToken
	 * @return String 返回类型
	 */
	public static String getAccessToken(String string) {
		String accessToken = "";
		try {
			JSONObject json = JSONObject.parseObject(string);
			if (null != json) {
				accessToken = json.getString("access_token");
			}
		} catch (Exception e) {
			Matcher m = Pattern.compile("^access_token=(\\w+)&expires_in=(\\w+)&refresh_token=(\\w+)$").matcher(string);
			if (m.find()) {
				accessToken = m.group(1);
			} else {
				Matcher m2 = Pattern.compile("^access_token=(\\w+)&expires_in=(\\w+)$").matcher(string);
				if (m2.find()) {
					accessToken = m2.group(1);
				}
			}
		}
		return accessToken;
	}
	
	/**
	 * 匹配QQ openid
	 * @param string 字符串
	 * @return String openid
	 */
	public static String getOpenId(String string) {
		String openid = null;
		Matcher m = Pattern.compile("\"openid\"\\s*:\\s*\"(\\w+)\"").matcher(string);
		if (m.find())
		  openid = m.group(1);
		return openid;
	}
	
	private static final Random RANDOM = new Random();
	
	/**
	 * 生成一个随机数
	 * @return 随机数
	 */
	public static String randomState() {
		int count = 24;
		char[] buffer = new char[count];
		for (int i = 0; i < count; i++) {
			buffer[i] = STR_S.charAt(RANDOM.nextInt(STR_S.length()));
		}
		return new String(buffer);
	}
	
}
