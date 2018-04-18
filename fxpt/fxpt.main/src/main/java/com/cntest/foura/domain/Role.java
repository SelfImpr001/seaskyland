/**
 * <p><b>© 1997-2013 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.domain;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * <pre>
 * 角色
 * </pre>
 * 
 * @author 李贵庆2014年6月4日
 * @version 1.0
 **/
public class Role {
	private String name;

	private String desc;
	private String code;           //角色编号
	private int type = 2;
	private Boolean available = Boolean.TRUE; // 是否可用,如果不可用将不会添加给用户
	private Set<UserRole> users;
	private Set<RoleResource> resources;
	private int roleCount;   //角色下面的用户数
	public Role(String name) {
		this.name = name;
	}
	public void addResource(URLResource resource) {
		if(this.resources == null)
			this.resources = new HashSet<RoleResource>();
		this.resources.add(new RoleResource(this, resource));
	}
	public int hashCode() {
		return new HashCodeBuilder().append(this.name).toHashCode();
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Role))
			return false;
		Role other = (Role) o;
		return new EqualsBuilder().append(this.name, other.name).isEquals();
	}

	public String toString() {
		return new ToStringBuilder(this).append("Role name", this.name).append("available",this.available).build();
	}

	// for orm or pojo
	private Long pk;

	public Role() {

	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Set<UserRole> getUsers() {
		return users;
	}

	public void setUsers(Set<UserRole> users) {
		this.users = users;
	}
	public Set<RoleResource> getResources() {
		return resources;
	}
	public void setResources(Set<RoleResource> resources) {
		this.resources = resources;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getRoleCount() {
		return roleCount;
	}
	public void setRoleCount(int roleCount) {
		this.roleCount = roleCount;
	}
	
	
}
