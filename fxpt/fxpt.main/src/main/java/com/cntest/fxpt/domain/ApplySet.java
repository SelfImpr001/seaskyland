package com.cntest.fxpt.domain;

public class ApplySet {
	private Long id;
	private String systemName;   //系统名称
	private String loginIcon;    //登录页面图标
	private String handleIcon;   //操作页面图标
	private Boolean status;      //启用状态
	private Integer order;       //排序
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public String getLoginIcon() {
		return loginIcon;
	}
	public void setLoginIcon(String loginIcon) {
		this.loginIcon = loginIcon;
	}
	public String getHandleIcon() {
		return handleIcon;
	}
	public void setHandleIcon(String handleIcon) {
		this.handleIcon = handleIcon;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}

	
}
