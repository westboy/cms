package com.zhiliao.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

public class HtmlKit {



    // 只有纯文本可以通过
	public static String getText(String html) {
		if (html == null)
			return null;
		return Jsoup.clean(html, Whitelist.none());
	}
	
	// 以下标签可以通过
	// b, em, i, strong, u. 纯文本 
	public static String getSimpleHtml(String html) {
		if (html == null)
			return null;
		return Jsoup.clean(html, Whitelist.simpleText());
	}
	
	// 以下标签可以通过
	//a, b, blockquote, br, cite, code, dd, dl, dt, em, i, li, ol, p, pre, q, small, strike, strong, sub, sup, u, ul
	public static String getBasicHtml(String html) {
		if (html == null)
			return null;
		return Jsoup.clean(html, Whitelist.basic());
	}
	
	//在basic基础上  增加图片通过
	public static String getBasicHtmlAndImage(String html) {
		if (html == null)
			return null;
		return Jsoup.clean(html, Whitelist.basicWithImages());
	}
	// 只允许图片  src="" width="" height="" title="" align="" alt=""
	public static String onlyImg(String html){
		Whitelist wl = Whitelist.none();
		wl.addTags("img").addAttributes("img", "src", "width", "height", "title", "align", "alt");
		return Jsoup.clean(html, wl);
	}
	// 以下标签可以通过	
	//a, b, blockquote, br, caption, cite, code, col, colgroup, dd, dl, dt, em, h1, h2, h3, h4, h5, h6, i, img, li, ol, p, pre, q, small, strike, strong, sub, sup, table, tbody, td, tfoot, th, thead, tr, u, ul
	public static String getFullHtml(String html) {
		if (html == null)
			return null;
		return Jsoup.clean(html, Whitelist.relaxed());
	}
	
	//只允许指定的html标签
	public static String clearTags(String html, String ...tags) {
		Whitelist wl = new Whitelist();
		return Jsoup.clean(html, wl.addTags(tags));
	}
	
	// 对关键字加上颜色
	public static String markKeywods (String keywords, String target) {
		if (!StringUtils.isEmpty(keywords)) {
			String[] arr = keywords.split(" ");
			for (String s : arr) {
				if (!StringUtils.isEmpty(s)) {
					String temp = "<span class=\"highlight\">" + s + "</span>";
					if(temp!=null)
						target = target.replaceAll(s, temp);
				}
			}
		}
		return target;
	}

	// 获取文章中的img url
	public static String getImgSrc(String html) {
		if (html == null)
			return null;
	    Document doc = Jsoup.parseBodyFragment(html);
	    Element image = doc.select("img").first();
	    return image == null ? null : image.attr("src");
	}

	public static String getSafeStringXSS(String s){
		if (s == null || "".equals(s)) {
			return s;
		}
		StringBuilder sb = new StringBuilder(s.length() + 16);
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
				case '<':
					sb.append("&lt;");
					break;
				case '>':
					sb.append("&gt;");
					break;
				case '\'':
					sb.append("&prime;");// &acute;");
					break;
				case '′':
					sb.append("&prime;");// &acute;");
					break;
				case '\"':
					sb.append("&quot;");
					break;
				case '＂':
					sb.append("&quot;");
					break;
				case '&':
					sb.append("＆");
					break;
				case '#':
					sb.append("＃");
					break;
				case '\\':
					sb.append('￥');
					break;
				case '=':
					sb.append("&#61;");
					break;
				default:
					sb.append(c);
					break;
			}
		}
		return sb.toString();
	}

}
