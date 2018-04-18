/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.application.permission;

import java.util.List;

import com.cntest.foura.domain.User;
import com.cntest.foura.service.URLResourceService;
import com.cntest.fxpt.domain.Exam;
import com.cntest.security.UserResource;
import com.cntest.security.remote.IUserResourceService.Level;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年6月21日
 * @version 1.0
 **/
public interface UserPermisson {

	UserPermisson inject(URLResourceService urlResourceService);

	UserPermisson with(Level level);
	
	/**
	 * 通过uuid查询用户的可访问资源权限
	 * @param user
	 * @param uuid
	 * @return
	 */
	List<UserResource> query(User user,String uuid,Exam exam);
}

