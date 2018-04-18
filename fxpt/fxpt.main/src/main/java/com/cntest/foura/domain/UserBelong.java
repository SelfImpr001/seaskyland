/**
 * <p><b>© 1997-2013 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.domain;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * <pre>
 * 用户与组合关系
 * </pre>
 * 
 * @author 李贵庆2014年6月4日
 * @version 1.0
 **/
public class UserBelong {

	private User user;

	private Organization org;

	private Boolean available = Boolean.TRUE;

	private Date joinDate; // 入职时间

	private Date offDate; // 离职时间

	private Position position; // 

	
	public UserBelong() {
	}

	public UserBelong(User user, Organization org) {
		this.user = user;
		this.org = org;
		this.joinDate = Calendar.getInstance().getTime();
	}

	public UserBelong joinDate(Date joinDate) {
		this.joinDate = joinDate;
		return this;
	}

	public void off(Date offDate) {
		if(offDate != null)
		    this.offDate = offDate;
		else
			this.offDate = Calendar.getInstance().getTime();
	}

	public UserBelong positionFor(Position position) {
		this.position = position;
		return this;
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.user).append(this.org).append(this.joinDate).append(position).toHashCode();
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof UserBelong))
			return false;
		UserBelong other = (UserBelong) o;
		return new EqualsBuilder().append(this.user, other.user).append(this.org, other.org).append(this.joinDate, other.joinDate)
				.append(this.position, other.position).isEquals();
	}

	public String toString() {
		return new ToStringBuilder(this).append("User name", this.user.getName()).append(this.org.getName()).append("available", this.available)
				.append("join date", this.joinDate).build();
	}

	// for orm
	private Long pk;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Organization getOrg() {
		return org;
	}

	public void setOrg(Organization org) {
		this.org = org;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public Date getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	public Date getOffDate() {
		return offDate;
	}

	public void setOffDate(Date offDate) {
		this.offDate = offDate;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}

}
