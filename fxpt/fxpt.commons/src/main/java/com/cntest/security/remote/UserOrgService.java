/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.security.remote;

import java.util.List;

import com.cntest.security.UserDetails;
import com.cntest.security.UserOrg;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年11月7日
 * @version 1.0
 **/
public interface UserOrgService {

	List<UserOrg> getUserOrgs(UserDetails user);
}

