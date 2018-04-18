/**
 * <p><b>© 1997-2013 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.cntest.fxpt.domain.UserExam;
import com.cntest.security.DefaultUserDetails;
import com.cntest.security.UserDetails;

/** 
 * <pre>
 * 用户
 * </pre>
 *  
 * @author 李贵庆2014年6月3日
 * @version 1.0
 **/
public class User {
	private String name;

	private String password;

	private int status;
	
	private int type = 2; //1--系统账号；2--业务员账号
	
	private Set<UserRole> roles;
	
	private UserInfo userInfo;
	
	private List<UserExam> userExamList;
	
	private Set<UserResource> resources;
	
	private String isup;//初始密码登录
	
	


	public List<UserExam> getUserExamList() {
		return userExamList;
	}

	public void setUserExamList(List<UserExam> userExamList) {
		this.userExamList = userExamList;
	}

	public Set<com.cntest.foura.domain.UserResource> getResources() {
		return resources;
	}

	public void setResources(Set<com.cntest.foura.domain.UserResource> resources) {
		this.resources = resources;
	}

	public void addResource(URLResource resource) {
		if(this.resources == null)
			this.resources = new HashSet<UserResource>();
		this.resources.add(new com.cntest.foura.domain.UserResource(this,resource));
	}
	
	public User(String name) {
		this.name = name;
	}
	
	public void addRole(Role role) {
		if(this.roles == null)
			this.roles = new HashSet<UserRole>();
		this.roles.add(new UserRole(this,role));
	}
	
	public void updateRoles(Collection<Role> Roles) {
		if(this.roles != null && this.roles.size() > 0) {
			this.roles.removeAll(this.roles); 
		}
		for(Role role:Roles) {
			addRole(role);
		}
	}
	


	public boolean hasRole(Role role) {
		if(this.roles != null && this.roles.size() > 0) {
			//UserRole ur = new UserRole(this,role);
			boolean b = false;
			for(UserRole ur :this.roles) {
				b = ur.getRole().equals(role);
				if(b)
					return b;
			}
			//return this.roles.contains(ur);
		}
		return false;
	}
	
    public UserBelong belongTo(Organization org) {
    	UserBelong belong = new UserBelong(this,org);
    	return belong;
    }
    
    public UserAction login() {
    	UserAction action = new UserAction(this);
    	action.setAction(UserAction.LOGIN);
    	return action;
    }
    
    public UserAction logout() {
    	UserAction action = new UserAction(this);
    	action.setAction(UserAction.LOGOUT);
    	return action;
    }
	
	public boolean isLocked() {
		return false;
	}
	
	public UserDetails<User> toUserDetails() {
		//toDo
		DefaultUserDetails<User> user =  new DefaultUserDetails<User>().setUserName(this.getName()).setPassword(this.getPassword());
		if(this.getUserInfo() != null) {
			user.setRealName(this.getUserInfo().getRealName()).setNickName(this.getUserInfo().getNickName());
			
		}
		if(this.getRoles() != null) {
			for(UserRole ur:this.getRoles()) {
				user.addTypeCode(ur.getRole().getCode());
			}
		}
		user.setOrigin(this);
		return user;
	}
	
	public static User from(UserDetails<User> userDetails) {
		return new User.Builder(userDetails.getUserName()).create();
	}
	
//	public static User currentUser() {
//		Subject subject = SecurityUtils.getSubject(); 
//		
//		return User.from((UserDetails)subject.getPrincipal());
//	}
	
	public static User currentUser(UserDetails us) {
		//Subject subject = SecurityUtils.getSubject(); 
		
		return User.from(us);
	}
	
	public int hashCode() {
		return new HashCodeBuilder().append(this.name).toHashCode();
	}
	
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof User)) 
			return false;
		User other  = (User)o;
		return new EqualsBuilder().append(this.name,other.name).isEquals();
	}
	
	public String toString() {
		return new ToStringBuilder(this).append("User name",this.name).build();
	}

	public static class Builder{
		private User user;
		
		public Builder(String name) {
			this.user = new User(name);
		}
		
		public Builder name(String name) {
			this.user.name = name;
			return this;
		}
		
		public Builder password(String password) {
			this.user.password = password;
			return this;
		}
		
		public Builder status(int status) {
			this.user.status = status;
			return this;
		}
		
		public User create() {
			return this.user;
		}
	}
	
	// for orm
	private Long pk;

    public User() {}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}

	public Set<UserRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<UserRole> roles) {
		this.roles = roles;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
		if(userInfo != null)
		    userInfo.setUser(this);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getIsup() {
		return isup;
	}

	public void setIsup(String isup) {
		this.isup = isup;
	}



	
}

