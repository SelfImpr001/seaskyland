/**
 * 
 */
package com.cntest.fxpt.bi.domain;

import java.text.MessageFormat;

/**
 * @author Administrator
 * 
 */
public class BiInfo {
	private Integer id;
	private String name;
	private String url;
	private String remark;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String toString() {
		return MessageFormat.format("name:{1};url:{2};describe:{3}",this.getName(), this.getUrl(),this.getRemark());
	}

}
