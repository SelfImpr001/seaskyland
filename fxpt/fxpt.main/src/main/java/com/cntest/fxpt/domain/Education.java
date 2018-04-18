/*
 * @(#)com.cntest.fxpt.domain.PartOfEducation.java	1.0 2014年5月31日:上午9:46:26
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月31日 上午9:46:26
 * @version 1.0
 */
public class Education {
	private Long id;
	private String code;
	private String name;
	private int type;//1省 2地市3区县
	private Education parent;
	private List<Education> childs=new ArrayList<Education>();
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Education getParent() {
		return parent;
	}
	public void setParent(Education parent) {
		this.parent = parent;
	}
	public List<Education> getChilds() {
		return childs;
	}
	public void setChilds(List<Education> childs) {
		this.childs = childs;
	}
	
	public void addChild(Education child) {
		if(this.childs == null)
			this.childs = new ArrayList<Education>();
		if(!this.childs.contains(child)) {
		    this.childs.add(child);
		    child.setParent(this);
		}
	}
	
	public int hashCode() {
		return new HashCodeBuilder().append(this.code).toHashCode();
	}
	
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof Education)) 
			return false;
		Education other  = (Education)o;
		return new EqualsBuilder().append(this.code,other.code).isEquals();
	}
	
	public String toString() {
		return new ToStringBuilder(this).append("Education name",this.name).append("Code",this.code).build();
	}
}
