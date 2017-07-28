package com.zhiliao.component.oauth.util;


import java.util.UUID;

public class StrKit {

	public static boolean isBlank(String str) {

		return str == null || str.length() <= 0;
	}

	public static boolean notBlank(String str) {

		return str != null || str.length() > 0;
	}
	public static String replace(String content) {
		String word = content.replace("，", ",").trim();
		return word;
	}

	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replace("-", "");

	}

}
