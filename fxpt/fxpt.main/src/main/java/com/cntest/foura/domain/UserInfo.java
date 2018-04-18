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

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * <pre>
 * 用户详细信息
 * </pre>
 * 
 * @author 李贵庆2014年6月4日
 * @version 1.0
 **/
public class UserInfo  {

	private String nickName;

	private String realName;
	
	private String email;
	
	private String cellphone;
	
	private String telphone;

	private Sex sex;

	@JsonIgnore
	private User user;

	private Set<Identity> identities;
	
	private String comment;
	
	public UserInfo() {   
	}
	public UserInfo(User user) {
		this.user = user;
	}
	
	public void addIdentity(Identity identity) {
		if(this.identities == null)
			this.identities = new HashSet<Identity>();
		this.identities.add(identity);
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.nickName).append(realName).append(sex).append(user).toHashCode();
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof UserInfo))
			return false;
		UserInfo other = (UserInfo) o;
		return new EqualsBuilder().append(this.nickName, other.nickName).append(this.realName, other.realName).append(this.sex, other.sex)
				.append(this.user, other.user).isEquals();
	}

	public String toString() {
		return new ToStringBuilder(this).append("UserInfo name", this.realName).append(this.user).build();
	}
	
	public static class Builder{
		private UserInfo userInfo;
		
		public Builder(User user) {
			this.userInfo = new UserInfo(user);
		}
		
		public Builder nickName(String nickName) {
			this.userInfo.nickName = nickName;
			return this;
		}
		
		public Builder realName(String realName) {
			this.userInfo.realName = realName;
			return this;
		}
		
		public Builder sex(Sex sex) {
			this.userInfo.sex = sex;
			return this;
		}
		
		public UserInfo create() {
			return this.userInfo;
		}
	}
  
	// for orm
	private Long pk;


	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}

	public Set<Identity> getIdentities() {
		return identities;
	}

	public void setIdentities(Set<Identity> identities) {
		this.identities = identities;
	}
	public String getNickName() {
		return nickName == null?this.realName:this.nickName;
	}
	public String getRealName() {
		return realName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCellphone() {
		return cellphone;
	}
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	public String getTelphone() {
		return telphone;
	}
	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	

}
