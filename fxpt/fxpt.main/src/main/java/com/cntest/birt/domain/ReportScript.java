package com.cntest.birt.domain;

import java.util.Date;

public class ReportScript {
	private Long pk;   // id
	private String name;   //脚本名称
	private String source;   //脚本来源
	private String directory;  //存放地址
	private Date createdTime;  //创建时间
	private String remark;   //备注
	private String suffix;//后缀
	private String wordDocment;//生成word的位置
	private String wordName;//生成名称
	private OrgBirt orgBirt;
	
	



	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	
	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getWordDocment() {
		return wordDocment;
	}

	public void setWordDocment(String wordDocment) {
		this.wordDocment = wordDocment;
	}
	
	public String getWordName() {
		return wordName;
	}

	public void setWordName(String wordName) {
		this.wordName = wordName;
	}

	public OrgBirt getOrgBirt() {
		return orgBirt;
	}

	public void setOrgBirt(OrgBirt orgBirt) {
		this.orgBirt = orgBirt;
	}

	


}
