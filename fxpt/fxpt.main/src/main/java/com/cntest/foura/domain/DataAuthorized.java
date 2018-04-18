/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * <pre>
 * 数据授权
 * </pre>
 * 
 * @author 李贵庆2014年12月9日
 * @version 1.0
 **/
public class DataAuthorized {

	private Long targetPk;

	private String target;
	
	private String fromTable;
	
	private Long fromPk;

	private String permissionName;

	private String permissionValue;

	private DataPermission permission;
	
	public DataAuthorized cloneFor(String target,Long tagetPk) {
		DataAuthorized other =  new DataAuthorized();
		other.setPermission(this.permission);
		other.setTarget(target);
		other.setTargetPk(tagetPk);
		other.setFromPk(this.getFromPk());
		other.setPermissionName(this.permissionName);
		other.setPermissionValue(this.permissionValue);
		other.setFromTable(this.getFromTable());
		other.setPk(null);
		return other;
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.permission).append(this.target).append(this.targetPk).append(this.fromPk)
				.toHashCode();
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof DataAuthorized))
			return false;
		DataAuthorized other = (DataAuthorized) o;
//		if(this.pk!=null && this.pk.equals(other.pk))
//			return true;
		return new EqualsBuilder().append(this.permission, other.permission).append(this.target, other.target)
				.append(this.targetPk, other.targetPk).append(this.fromTable, other.fromTable).append(this.fromPk, other.fromPk).isEquals();
	}

	public String toString() {
		return new ToStringBuilder(this).append("DataAuthorized ", this.permission).append(this.target).append(this.targetPk).append(this.fromPk)
				.append(this.permissionValue).build();
	}

	public DataAuthorized() {
	}

	private Long pk;

	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}

	public Long getTargetPk() {
		return targetPk;
	}

	public void setTargetPk(Long targetPk) {
		this.targetPk = targetPk;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public DataPermission getPermission() {
		return permission;
	}

	public void setPermission(DataPermission permission) {
		this.permission = permission;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public String getPermissionValue() {
		return permissionValue;
	}

	public void setPermissionValue(String permissionValue) {
		this.permissionValue = permissionValue;
	}

	public Long getFromPk() {
		return fromPk;
	}

	public void setFromPk(Long fromPk) {
		this.fromPk = fromPk;
	}

	public String getFromTable() {
		return fromTable;
	}

	public void setFromTable(String fromTable) {
		this.fromTable = fromTable;
	}

	
}
