/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * <pre>
 * 数据访问权限
 * </pre>
 * 
 * @author 李贵庆2014年12月3日
 * @version 1.0
 **/
public class DataPermission {
	private String name;//权限名称
 
	private String paramName; //权限的参数名称
	
	private String table; //权限数据来源表字段
	
	private String paramKeyField; //权限数据来源表主键

	private String source; //权限数据表达式（是一个sql查询语句）

	private String paramNamefield; //权限名字的取值字段

	private String paramValueField;//权限值的取值字段

	private String parentRefKey; //权限如果在同一个表有递归关联关系时的关联字段

	private int status = 1; //权限是否启用

	private DataPermission parent;//权限在进行递归关联时的上级权限

	private Set<DataPermission> children;

	private List<DataAuthorized> targetPermissions;
	
	public boolean hasRefValues() {
		return parentRefKey != null && parentRefKey.length() > 0;
	}

	public static class Builder {
		private DataPermission dataPermission;

		public Builder(String name) {
			this.dataPermission = new DataPermission();
			this.dataPermission.name = name;
		}

		public Builder name(String name) {
			this.dataPermission.name = name;
			return this;
		}

		public Builder paramName(String paramName) {
			this.dataPermission.paramName = paramName;
			return this;
		}
		
		public Builder table(String table) {
			this.dataPermission.table = table;
			return this;
		}
		
		public Builder paramKeyField(String paramKeyField) {
			this.dataPermission.paramKeyField = paramKeyField;
			return this;
		}

		public Builder source(String source) {
			this.dataPermission.source = source;
			return this;
		}

		public Builder paramNamefield(String paramNamefield) {
			this.dataPermission.paramNamefield = paramNamefield;
			return this;
		}

		public Builder paramValueField(String paramValueField) {
			this.dataPermission.paramValueField = paramValueField;
			return this;
		}

		public Builder parentRefKey(String where) {
			this.dataPermission.parentRefKey = where;
			return this;
		}

		public DataPermission create() {
			return this.dataPermission;
		}
	}

	public void addAuthorized(DataAuthorized authorized) {
		if(this.targetPermissions == null)
			this.targetPermissions = new ArrayList<DataAuthorized>();
		this.targetPermissions.add(authorized);
	}
	
	public int hashCode() {
		return new HashCodeBuilder().append(this.name).toHashCode();
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof DataPermission))
			return false;
		DataPermission other = (DataPermission) o;
		if(this.pk!=null && this.pk.equals(other.pk))
			return true;
		return new EqualsBuilder().append(this.name, other.name).isEquals();
	}

	public String toString() {
		return new ToStringBuilder(this).append("Permission name", this.name).build();
	}

	private Long pk;

	public DataPermission() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getParamNamefield() {
		return paramNamefield;
	}

	public void setParamNamefield(String paramNamefield) {
		this.paramNamefield = paramNamefield;
	}

	public String getParamValueField() {
		return paramValueField;
	}

	public void setParamValueField(String paramValueField) {
		this.paramValueField = paramValueField;
	}

	public String getParentRefKey() {
		return parentRefKey;
	}

	public void setParentRefKey(String parentRefKey) {
		this.parentRefKey = parentRefKey;
	}

	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}

	public DataPermission getParent() {
		return parent;
	}

	public void setParent(DataPermission parent) {
		this.parent = parent;
	}

	public Set<DataPermission> getChildren() {
		return children;
	}

	public void setChildren(Set<DataPermission> children) {
		this.children = children;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<DataAuthorized> getTargetPermissions() {
		return targetPermissions;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getParamKeyField() {
		return paramKeyField;
	}

	public void setParamKeyField(String paramKeyField) {
		this.paramKeyField = paramKeyField;
	}
	
	
}
