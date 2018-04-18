/*
 * @(#)com.cntest.fxpt.domain.Grade.java	1.0 2014年5月14日:上午11:20:43
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月14日 上午11:20:43
 * @version 1.0
 */
public class Grade {
	private Long id;
	private String name;
	/**
	 * 
	 */
	public Grade() {
		// TODO Auto-generated constructor stub
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.name).toHashCode();
	}
	
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof Grade)) 
			return false;
		Grade other  = (Grade)o;
		return new EqualsBuilder().append(this.name,other.name).isEquals();
	}
	
	public String toString() {
		return new ToStringBuilder(this).append("Organization name",this.name).build();
	}
	
}
