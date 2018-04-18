/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年6月20日
 * @version 1.0
 **/
public class UserRole {
	@JsonIgnore
	private User user;
	@JsonIgnore
	private Role role;
	
	
	public UserRole() {}
	
	public UserRole(User user, Role role) {
		this.user = user;
		this.role = role;
	}
	
	public boolean typeOf(String roleCode) {
		if(this.role.getCode().equalsIgnoreCase(roleCode))
			return Boolean.TRUE;
		return Boolean.FALSE;
	}
	
	public int hashCode() {
		return new HashCodeBuilder().append(this.user.hashCode()).append(this.role.hashCode()).toHashCode();
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof UserRole))
			return false;
		UserRole other = (UserRole) o;
		return new EqualsBuilder().append(this.user, other.user).append(this.role, other.role).isEquals();
	}

	
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	private Long pk;
	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}
	
}

