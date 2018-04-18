package com.cntest.fxpt.domain;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


public class EducationSurvey {

	private String name;
	
	private String code;
	
	private String searchPoint;
	
	private int type; //1、2、3菜单级
	
	private Boolean available = Boolean.TRUE; 
	
	private EducationSurvey parent;
	
	private String ex1;
	private String ex2;
	private String ex3;
	
	
	public String getEx1() {
		return ex1;
	}


	public void setEx1(String ex1) {
		this.ex1 = ex1;
	}


	public String getEx2() {
		return ex2;
	}


	public void setEx2(String ex2) {
		this.ex2 = ex2;
	}


	public String getEx3() {
		return ex3;
	}


	public void setEx3(String ex3) {
		this.ex3 = ex3;
	}


	public EducationSurvey() {
	}


	public EducationSurvey(String name,String code) {
		this.name = name;
		this.code = code;
		this.type = 1;
	}
	
	public EducationSurvey(String name,String code,int type) {
		this.name = name;
		this.code = code;
		this.type = type;
	}
	
	
	public void parentTo(EducationSurvey other) {
		if(this.equals(other))
			return;
		if(other.childOf(this))
			return;
		this.parent = other;
	}
	
	/**
	 * 判断是为某一组织的下一级组织
	 * @param other
	 * @return
	 */
	public boolean childOf(EducationSurvey other) {
		//@Todo 过程待实现
		return false;
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.name).append(this.code).toHashCode();
	}
	
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof EducationSurvey)) 
			return false;
		EducationSurvey other  = (EducationSurvey)o;
		return new EqualsBuilder().append(this.name,other.name).append(this.code, other.code).isEquals();
	}
	
	public String toString() {
		return new ToStringBuilder(this).append("Organization name",this.name).append("available",this.available).build();
	}
	
	//for orm 
	private Long pk;

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

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public EducationSurvey getParent() {
		return parent;
	}

	public void setParent(EducationSurvey parent) {
		this.parent = parent;
	}

	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


	public String getSearchPoint() {
		return searchPoint;
	}


	public void setSearchPoint(String searchPoint) {
		this.searchPoint = searchPoint;
	}
	
}

