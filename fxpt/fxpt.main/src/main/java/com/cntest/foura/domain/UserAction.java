/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
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
 * 用户活动
 * </pre>
 * 
 * @author 李贵庆2014年6月7日
 * @version 1.0
 **/
public class UserAction {

	public static final String LOGIN = "login";

	public static final String LOGOUT = "logout";

	private Date actionDate;

	private String action;

	private User user;

	public UserAction(User user) {
		this.user = user;
		actionDate = Calendar.getInstance().getTime();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.user).append(action).append(actionDate).toHashCode();
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof UserAction))
			return false;
		UserAction other = (UserAction) o;
		return new EqualsBuilder().append(this.user, other.user).append(this.action, other.action).append(this.actionDate, other.actionDate)
				.isEquals();
	}

	public String toString() {
		return new ToStringBuilder(this).append("User name", this.user.getName()).append(this.action).append(this.actionDate).build();
	}

	// for orm
	private Long pk;

	public UserAction() {
	}

	public Date getActionDate() {
		return actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
