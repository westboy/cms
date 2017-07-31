package com.zhiliao.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class StrUtil {


	/**
	 * 判断字符串是否为空
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {

		return str == null || str.length() <= 0;
	}

	/**
	 * 替换中文逗号
	 * @param content
	 * @return
	 */
	public static String replace(String content) {
		String word = content.replace("，", ",").trim();
		return word;
	}


	/**
	 * 获取文件扩展名
	 * 
	 * @param filename
	 * @return
	 */
	public static String getExtensionName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + 1);
			}
		}
		return filename;
	}

	/**
	 *
	 * 获取uuid
	 *
	 * @return
	 */

	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replace("-", "");

	}


	/* 字符串数组排重 */
	public static String[] excludeRepeatStr(String str){
		if(StrUtil.isBlank(str))
			return null;
		String[] ss = str.replace(" ","").split(",");
		Arrays.sort(ss);
		Set<String> prodCodeSet = new HashSet<>();
		prodCodeSet.addAll(Arrays.asList(ss));
		ss = prodCodeSet.toArray(new String[]{});
	  return ss;
	}



	public static  boolean isNotNumeric(String ...strs) {
		for(String str:strs){
			if(StringUtils.isNumeric(str))
				return false;
		}
		return true;

	}

	public static  boolean isContain(String[] strs,String str) {
	    if(CmsUtil.isNullOrEmpty(strs))
	    	return false;
	    for(String s:strs){
	    	if(s.equals(str))
	    		return  true;
		}
		return  false;

	}

	/*判断一个字符是否是中文*/
	public static boolean isChinese(char c) {
		return c >= 0x4E00 &&  c <= 0x9FA5;
	}
	/*判断一个字符串是否含有中文*/
	public static boolean isChinese(String str) {
		if (str == null) return false;
		for (char c : str.toCharArray()) {
			if (isChinese(c)) return true;
		}
		return false;
	}

	public static void  main(String args[]){
//       System.out.println( excludeRepeatStr("1,2,1"));
//		 System.out.println(isNotNumeric("0","46"));
		String[] ss = {"1000","200"};
		System.out.println(isContain(ss,"200"));
	}
}
