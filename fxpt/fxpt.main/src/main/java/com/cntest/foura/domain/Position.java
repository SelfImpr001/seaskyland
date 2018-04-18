/**
 * <p><b>© 1997-2013 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/** 
 * <pre>
 * 职位--学生，班主任，任课老师，校领导，局领导，教研等
 * </pre>
 *  
 * @author 李贵庆2014年6月4日
 * @version 1.0
 **/
public class Position {

	private String name;
	
	private String desc;
	
	public int hashCode() {
		return new HashCodeBuilder().append(this.name).toHashCode();
	}
	
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof Position)) 
			return false;
		Position other  = (Position)o;
		return new EqualsBuilder().append(this.name,other.name).isEquals();
	}
	
	public String toString() {
		return new ToStringBuilder(this).append("Position name",this.name).build();
	}
	private Long pk;
	
	
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}

