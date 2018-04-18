/**
 * <p><b>© 1997-2013 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.cntest.security.DefaultUserResource;
import com.cntest.security.UserResource;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2014年6月5日
 * @version 1.0
 **/
public class URLResource {

	private String name;

	private String url;

	private String uuid;

	private String entry;
	
	private String gradeids;
	
	private String examtypeids;
	
	private Boolean available = Boolean.TRUE;

	private URIType type; // 资源类型 组件:1/菜单:2/应用:9

	private String icon; // 图标
	private String remarks;
	private Integer reorder; // 排序
	
	private URLResource parent; // 当前节点的父节点id(pk)

	private Set<URLResource> children;
	
	private Set<RoleResource> roles;

	public void addChild(URLResource child) {
		if (this.children == null) {
			this.children = new HashSet<URLResource>();
		}
		this.children.add(child);
	}

	public void refTo(Role role) {
		if(this.roles == null)
			this.roles = new HashSet<RoleResource>();
		this.roles.add(new RoleResource(role,this));
	}
	
	public UserResource toUserResource(boolean copyChildren) {
		if(!this.available)
			return null;
		DefaultUserResource dur = new DefaultUserResource.Builder().uuid(this.uuid).name(this.name).url(this.url).icon(this.icon).type(this.type.toString())
				.desc(this.remarks).order(this.reorder).eventCode(this.entry).create();
		if (copyChildren && this.children != null) {
			for (URLResource child : this.children) {
				if(child.available)
				    dur.addChild(child.toUserResource(copyChildren));
			}
		}
		return dur;
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.uuid).toHashCode();
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof URLResource))
			return false;
		URLResource other = (URLResource) o;
		if(this.getPk() !=null && this.getPk().equals(other.getPk()))
			return true;
		return new EqualsBuilder().append(this.uuid, other.uuid).isEquals();
	}

	public String toString() {
		return new ToStringBuilder(this).append("PK",this.pk).append("name", this.name).append(url).build();
	}

	public static class Builder {
		private URLResource urlResource;

		public Builder() {
			this.urlResource = new URLResource();
			this.urlResource.uuid = UUID.randomUUID().toString();
		}

		public Builder parent(URLResource parent) {
			this.urlResource.parent = parent;
			return this;
		}

		public Builder name(String name) {
			this.urlResource.name = name;
			return this;
		}

		public Builder url(String url) {
			this.urlResource.url = url;
			return this;
		}

		public Builder icon(String icon) {
			this.urlResource.icon = icon;
			return this;
		}

		public Builder type(URIType type) {
			this.urlResource.type = type;
			return this;
		}

		public Builder reorder(Integer reorder) {
			this.urlResource.reorder = reorder;
			return this;
		}

		public Builder remarks(String remarks) {
			this.urlResource.remarks = remarks;
			return this;
		}
		
		public Builder uuid(String uuid) {
			this.urlResource.uuid = uuid;
			return this;
		}

		public Builder available(Boolean available) {
			this.urlResource.available = available;
			return this;
		}

		public Builder pk(Long pk) {
			this.urlResource.pk = pk;
			return this;
		}

		public URLResource create() {
			return this.urlResource;
		}
	}

	// for orm
	private Long pk;



	public URLResource getParent() {
		return parent;
	}

	public void setParent(URLResource parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}


	public URIType getType() {
		return type;
	}

	public void setType(URIType type) {
		this.type = type;
	}

	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Set<URLResource> getChildren() {
		return children;
	}

	public void setChildren(Set<URLResource> children) {
		this.children = children;
	}

	public Integer getReorder() {
		return reorder;
	}

	public void setReorder(Integer reorder) {
		this.reorder = reorder;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Set<RoleResource> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleResource> roles) {
		this.roles = roles;
	}

	public String getEntry() {
		return entry;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}

	public String getGradeids() {
		return gradeids;
	}

	public void setGradeids(String gradeids) {
		this.gradeids = gradeids;
	}

	public String getExamtypeids() {
		return examtypeids;
	}

	public void setExamtypeids(String examtypeids) {
		this.examtypeids = examtypeids;
	}
	
	
}
