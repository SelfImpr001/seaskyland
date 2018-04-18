/**
 * <p><b>© 1997-2013 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.remote.domain;

import com.cntest.foura.domain.Organization;

/** 
 * <pre>
 * 南山数据接口
 * </pre>
 *  
 * @author cheny2016年12月12日
 * @version 1.0
 **/
public class NanShanOrgData {
	
	private Long id;
	private int status = 1; //0--已删除组织；
	
	private String orgCode;     //组织代码
	private String parent;      //直属上级组织代码
	private String parents;     //所有上级组织代码
	private String displayName; //机构全称
	private String name;        //机构简称
	private String priority;     //优先级     
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getParents() {
		return parents;
	}
	public void setParents(String parents) {
		this.parents = parents;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
}

