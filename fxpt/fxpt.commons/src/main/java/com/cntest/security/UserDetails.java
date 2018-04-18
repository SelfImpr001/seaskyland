/**
 * <p><b>© 1997-2013 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.security;

import java.io.Serializable;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年6月4日
 * @version 1.0
 **/
public interface UserDetails<T> extends Serializable {

	String getUserName();
	
	String getNickName();
	
	String getRealName();
	
	String getPassword();
	
	String[] getOrgCodes();
	
	String[] getRoleTypeCode();
	
	public T getOrigin();

	public void setOrigin(T origin);
	
	boolean roleOf(RoleType roleType);
}

