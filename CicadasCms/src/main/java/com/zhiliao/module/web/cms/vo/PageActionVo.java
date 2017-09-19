package com.zhiliao.module.web.cms.vo;

public class PageActionVo {

	private Integer pageNumber;
	private String url;
	private boolean isLink;
	private boolean isMore = false;
	private String value;
	public boolean isMore() { return isMore; }
	public void setMore(boolean more) { isMore = more; }
	public Integer getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isLink() {
		return isLink;
	}
	public void setLink(boolean isLink) {
		this.isLink = isLink;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

}
