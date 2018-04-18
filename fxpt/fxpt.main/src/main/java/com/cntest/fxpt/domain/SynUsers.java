/*
 * @(#)com.cntest.fxpt.domain.SynUsers.java	1.0 2016年4月8日:下午3:33:31
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.domain;

import java.util.ArrayList;
import java.util.List;


/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2016年4月8日 下午3:33:31
 * @version 1.0
 */
public class SynUsers {
	private int id;
	private int roleid; // 角色ID
	private String namingrules; // 用户命名规则
	private String passwordrules; // 密码命名规则
	private String defaultpassword; // 默认密码
	private int isSyn; // 数据发布时自动同步用户 1 同步 0 不同步
	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRoleid() {
		return roleid;
	}

	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}

	public String getNamingrules() {
		return namingrules;
	}

	public void setNamingrules(String namingrules) {
		this.namingrules = namingrules;
	}

	public String getPasswordrules() {
		return passwordrules;
	}

	public void setPasswordrules(String passwordrules) {
		this.passwordrules = passwordrules;
	}

	public String getDefaultpassword() {
		return defaultpassword;
	}

	public void setDefaultpassword(String defaultpassword) {
		this.defaultpassword = defaultpassword;
	}

	public int getIsSyn() {
		return isSyn;
	}

	public void setIsSyn(int isSyn) {
		this.isSyn = isSyn;
	}
	private List<String> nr;

	public List<String> getNr() {
		List<String> list;
		if(this.getNamingrules()!=null && this.getNamingrules().length()>0){
			list = new ArrayList<String>();
			String[] arr = this.getNamingrules().split("、");
			for (int i = 0; i < arr.length; i++) {
				list.add(arr[i]);
			}
			nr = list;
		}
		return nr;
	}

	public void setNr(List<String> nr) {
		this.nr = nr;
	}

}
