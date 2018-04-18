/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.security;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2014年11月7日
 * @version 1.0
 **/
public class DefaultUserOrg implements UserOrg {
	private String code;
	private String name;
	private String alias;
	private int type;
	private UserDetails user;

	public DefaultUserOrg(String code, String name, String alias, int type) {
		this.code = code;
		this.name = name;
		this.alias = alias;
		this.type = type;
	}

	public DefaultUserOrg(String code, String name, String alias, int type, UserDetails user) {
		this.code = code;
		this.name = name;
		this.alias = alias;
		this.type = type;
		this.user = user;
	}

	@Override
	public String getCode() {
		return this.code;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getAlias() {
		return this.alias;
	}

	@Override
	public int getType() {
		return this.type;
	}

	public UserDetails getUser() {
		return user;
	}

	public void setUser(UserDetails user) {
		this.user = user;
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.code).toHashCode();
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof DefaultUserOrg))
			return false;
		DefaultUserOrg other = (DefaultUserOrg) o;

		return new EqualsBuilder().append(this.code, other.code).isEquals();
	}

	public String toString() {
		return new ToStringBuilder(this).append("User name", this.user == null ? "" : user.getUserName()).append(this.code).append(this.name)
				.append(this.type).build();
	}

}
