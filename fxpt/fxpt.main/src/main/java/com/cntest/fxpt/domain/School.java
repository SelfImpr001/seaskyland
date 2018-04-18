/*
 * @(#)com.cntest.fxpt.domain.School.java	1.0 2014年5月31日:上午9:46:10
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
 * @author 刘海林 2014年5月31日 上午9:46:10
 * @version 1.0
 */
public class School {
	private Long id;
	private String code;
	private String name;
	private Education education;
	private int type = 4;
	private String schoolType;
	
	public School() {}
	
	public School(String code) {
		this.code = code;
	}
	
	public School(String code,String name) {
		this(code);
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

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

	public Education getEducation() {
		return education;
	}

	public void setEducation(Education education) {
		this.education = education;
	}

	public String getSchoolType() {
		return schoolType;
	}

	public void setSchoolType(String schoolType) {
		this.schoolType = schoolType;
	}

	
	public int hashCode() {
		return new HashCodeBuilder().append(this.code).toHashCode();
	}
	
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof School)) 
			return false;
		School other  = (School)o;
		return new EqualsBuilder().append(this.code,other.code).isEquals();
	}
	
	public String toString() {
		return new ToStringBuilder(this).append("School name",this.name).append(this.code).build();
	}

	public static class Builder{
		private School school;
		
		public Builder(String code) {
			this.school = new School(code);
		}
		
		public Builder name(String name) {
			this.school.name = name;
			return this;
		}
		public School create() {
			return this.school;
		}
	}
}
