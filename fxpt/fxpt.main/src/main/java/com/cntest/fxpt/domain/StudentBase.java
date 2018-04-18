/*
 * @(#)com.cntest.fxpt.domain.StudentBase.java	1.0 2014年10月9日:下午2:50:22
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
 * @author 黄洪成 2014年10月9日 下午2:50:22
 * @version 1.0
 */
public class StudentBase {
	private Integer id;
	private String guid;
	private String name;
	private int sex;
	private String grade;
	private School school;
	private String xh;
	
	public StudentBase() {}
	
	public StudentBase(String name) {
		this.name = name;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public boolean sameAs(StudentBase student) {
		return this.getName().equals(student.getName()) && this.getXh().equals(student.getXh()) && this.getSchool().equals(student.getSchool());
	}

	public boolean sameAs(ExamStudent student) {
		return this.getName().equals(student.getName()) && this.getXh().equals(student.getZkzh()) && this.getSchool().equals(student.getSchool());
	}
	
	public int hashCode() {
		if(this.guid == null)
			return  new HashCodeBuilder().append(this.name).append(this.school).append(this.xh).toHashCode();
		return new HashCodeBuilder().append(this.guid).toHashCode();
	}
	
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof StudentBase)) 
			return false;
		StudentBase other  = (StudentBase)o;
		if(this.guid == null)
			return this.sameAs(other);
		return new EqualsBuilder().append(this.guid,other.guid).isEquals();
	}
	
	public String toString() {
		return new ToStringBuilder(this).append("Student name",this.name).append(this.xh).append(this.school == null?"":this.school.getName()).build();
	}

	public static class Builder{
		private StudentBase student;
		public Builder(String name) {
			this.student = new StudentBase(name);
		}
		
		public Builder name(String name) {
			this.student.name = name;
			return this;
		}
		
		public Builder guid(String guid) {
			this.student.guid = guid;
			return this;
		}
		
		public Builder sex(int sex) {
			this.student.sex = sex;
			return this;
		}
		
		public Builder school(School school) {
			this.student.school = school;
			return this;
		}
		
		public Builder grade(String grade) {
			this.student.grade = grade;
			return this;
		}
		
		public Builder xh(String xh) {
			this.student.xh = xh;
			return this;
		}
		
		public StudentBase create() {
			return this.student;
		}
	}

}
