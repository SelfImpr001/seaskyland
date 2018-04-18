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
 * @author 李贵庆2014年6月18日
 * @version 1.0
 **/
public class DefaultUserDetails<T> implements UserDetails<T> {
	private String userName;

	private String nickName;

	private String realName;

	private String password;
	
	private String[] roleTypeCode;
	
	private String[] orgCodes;
	
	public T origin; 

	public DefaultUserDetails() {

	}

	@Override
	public String getUserName() {
		return this.userName;
	}

	@Override
	public String getNickName() {
		return this.nickName;
	}

	@Override
	public String getRealName() {
		return this.realName;
	}

	public DefaultUserDetails<T> setUserName(String userName) {
		this.userName = userName;
		return this;
	}

	public DefaultUserDetails<T> setNickName(String nickName) {
		this.nickName = nickName;
		return this;
	}

	public DefaultUserDetails <T>setRealName(String realName) {
		this.realName = realName;
		return this;
	}

	@Override
	public T getOrigin() {
		return origin;
	}

	@Override
	public void setOrigin(T origin) {
		this.origin = origin;
	}

	public DefaultUserDetails<T> setPassword(String password) {
		this.password = password;
		return this;
	}
	public DefaultUserDetails<T> addOrgCode(String orgCode) {
		if(this.orgCodes == null)
			this.orgCodes = new String[] {orgCode};
		else {
			String[] newCodes = new String[this.orgCodes.length +1];
			
			for(int i=0;i<this.orgCodes.length;i++) {
				newCodes[i] = this.orgCodes[i];
			}
			newCodes[this.orgCodes.length] = orgCode;
			this.orgCodes = newCodes;
		}
		return this;
	}
	
	public DefaultUserDetails<T> addTypeCode(String orgCode) {
		if(this.roleTypeCode == null) {
			this.roleTypeCode = new String[] {orgCode};
		}else {
			String[] newCodes = new String[this.roleTypeCode.length +1];
			
			for(int i=0;i<this.roleTypeCode.length;i++) {
				newCodes[i] = this.roleTypeCode[i];
			}
			newCodes[this.roleTypeCode.length] = orgCode;
			this.roleTypeCode = newCodes;
		}
		return this;
	}


	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String[] getOrgCodes() {
		return this.orgCodes;
	}

	public String[] getRoleTypeCode() {
		return this.roleTypeCode;
	}

	@Override
	public boolean roleOf(RoleType roleType) {
		if(this.roleTypeCode != null) {
			for(String code:this.roleTypeCode) {
				if(code.equals(roleType.name()))
					return true;
			}
		}
		return false;
	}

	
	public int hashCode() {
		if(this.origin != null)
			return origin.hashCode();
		return new HashCodeBuilder().append(this.userName).toHashCode();
	}
	
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof DefaultUserDetails)) 
			return false;
		DefaultUserDetails other  = (DefaultUserDetails)o;
		if(this.origin != null)
			return origin.equals(other);
		return new EqualsBuilder().append(this.userName,other.userName).isEquals();
	}
	
	public String toString() {
		return new ToStringBuilder(this).append("User name",this.userName).append(this.realName).append(this.nickName).append(this.orgCodes).build();
	}

	public static class Builder<T>{
		private DefaultUserDetails<T> user;
		
		public Builder(String userName) {
			this.user = new DefaultUserDetails<T>();
			this.user.setUserName(userName);
		}
		
		public Builder<T> realName(String realName) {
			this.user.realName = realName;
			return this;
		}
		
		public Builder<T> nickName(String nickName) {
			this.user.nickName = nickName;
			return this;
		}
		
		public Builder<T> password(String password) {
			this.user.password = password;
			return this;
		}
		
		public DefaultUserDetails<T> create() {
			return this.user;
		}
	}
}
