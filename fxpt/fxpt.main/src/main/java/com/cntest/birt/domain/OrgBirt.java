package com.cntest.birt.domain;

import java.util.List;
import java.util.Set;

/** 
 * <pre>
 *  组织，每个用户都对应一个或者多个组织
 * </pre>
 *  
 * @author 李贵庆2014年6月4日
 * @version 1.0
 **/

public class OrgBirt
{

	private String name;
	
	private String code;

	private Long pk;
	
	private OrgBirt parent;
	private Set<ReportScript> repList;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}

	public OrgBirt getParent() {
		return parent;
	}

	public void setParent(OrgBirt parent) {
		this.parent = parent;
	}

	public Set<ReportScript> getRepList() {
		return repList;
	}

	public void setRepList(Set<ReportScript> repList) {
		this.repList = repList;
	}

	

}

