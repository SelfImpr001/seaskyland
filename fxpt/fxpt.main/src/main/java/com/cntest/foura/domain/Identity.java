/**
 * <p><b>© 1997-2013 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.domain;

import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * <pre>
 * 个人身份证明，一般指证件号
 * 取值使用国标
 * type    name  
 * 01      居民身份证
 * 02      居民户口簿
 * 03      护照
 * 04      军官（士兵证）
 * 05      驾驶证
 * 90      学籍号
 * 91      准考证号
 * 99
 * </pre>
 * 
 * @author 李贵庆2014年6月4日
 * @version 1.0
 **/
public class Identity {

	private String name; // 证件名称

	private String number;// 证件号

	private String type;// 证件类型

	private Date effictiveTimeFrom; // 有效开始时间，

	private Date effictiveTimeTo;// 有效结束时间，如果无，表示永久

	private String issuer;// 发证机构

	private UserInfo owner;
	
	public Identity(UserInfo owner,String type,String name,String number) {
		this.owner = owner;
		this.type = type;
		this.number = number;
	}
	
	public Identity(UserInfo owner) {
		this.owner = owner;
	}
	public Identity() {
	}
	public int hashCode() {
		return new HashCodeBuilder().append(this.number).append(this.type).append(this.owner).toHashCode();
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Identity))
			return false;
		Identity other = (Identity) o;
		return new EqualsBuilder().append(this.name, other.name).append(this.type, other.type).append(this.owner, other.owner).isEquals();
	}

	public String toString() {
		return new ToStringBuilder(this).append("Identity name", this.name).append(" number",this.number).build();
	}
	
	public static class Builder{
		private Identity identity;
		
		public Builder(UserInfo userInfo) {
			this.identity = new Identity(userInfo);
		}
		
		public Builder name(String name) {
			this.identity.name = name;
			return this;
		}
		
		public Builder number(String number) {
			this.identity.number = number;
			return this;
		}
		
		public Builder type(String type) {
			this.identity.type = type;
			return this;
		}
		
		public Builder effictiveTimeFrom(Date effictiveTimeFrom) {
			this.identity.effictiveTimeFrom = effictiveTimeFrom;
			return this;
		}
		
		public Builder effictiveTimeTo(Date effictiveTimeTo) {
			this.identity.effictiveTimeTo = effictiveTimeTo;
			return this;
		}
		
		public Builder issuer(String issuer) {
			this.identity.issuer = issuer;
			return this;
		}
	
	    public Identity create() {
	    	return this.identity;
	    }
	}

	// for orm
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

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public UserInfo getOwner() {
		return owner;
	}

	public void setOwner(UserInfo owner) {
		this.owner = owner;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getEffictiveTimeFrom() {
		return effictiveTimeFrom;
	}

	public void setEffictiveTimeFrom(Date effictiveTimeFrom) {
		this.effictiveTimeFrom = effictiveTimeFrom;
	}

	public Date getEffictiveTimeTo() {
		return effictiveTimeTo;
	}

	public void setEffictiveTimeTo(Date effictiveTimeTo) {
		this.effictiveTimeTo = effictiveTimeTo;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

}
