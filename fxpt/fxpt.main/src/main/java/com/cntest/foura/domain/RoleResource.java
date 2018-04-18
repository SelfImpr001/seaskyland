/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.cntest.security.DefaultUserResource;
import com.cntest.security.UserResource;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2014年6月21日
 * @version 1.0
 **/
public class RoleResource {
	private Long pk;
	private Role role;
	private URLResource resource;
	private int order;

	public RoleResource() {
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public RoleResource(Role role, URLResource resource) {
		this.role = role;
		this.resource = resource;
	}

	public UserResource toUserResource(boolean copyChildren) {
		if (this.resource == null || !this.resource.getAvailable())
			return null;
		DefaultUserResource dur = new DefaultUserResource.Builder().uuid(this.resource.getUuid()).name(this.resource.getName())
				.url(this.resource.getUrl()).icon(this.resource.getIcon()).type(this.resource.getType().toString()).desc(this.resource.getRemarks())
				.order(this.resource.getReorder()).eventCode(this.resource.getEntry()).create();
		if (copyChildren && this.resource.getChildren() != null) {
			for (URLResource child : this.resource.getChildren()) {
				if (child.getAvailable())
					dur.addChild(child.toUserResource(copyChildren));
			}
		}
		return dur;
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.role.hashCode()).append(this.resource.hashCode()).toHashCode();
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof RoleResource))
			return false;
		RoleResource other = (RoleResource) o;
		return new EqualsBuilder().append(this.role, other).append(this.resource, other.resource).isEquals();
	}

	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public URLResource getResource() {
		return resource;
	}

	public void setResource(URLResource resource) {
		this.resource = resource;
	}

}
