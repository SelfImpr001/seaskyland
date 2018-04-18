/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.application.permission;

import com.cntest.security.remote.IUserResourceService.Type;

/** 
 * <pre>
 *  用户资源权限访问工厂
 * </pre>
 *  
 * @author 李贵庆2014年6月21日
 * @version 1.0
 **/
public class UserPermissonFactory {

	public static UserPermisson getInstanceOf(Type type) {
		if(type.equals(Type.APP))
			return AppUserPermission.getInstance();
		if(type.equals(Type.MENU))
			return MenuUserPermission.getInstance();
		if(type.equals(Type.METHOD))
			return MehtodUserPermission.getInstance();
		if(type.equals(Type.MODULE))
			return ModuleUserPermission.getInstance();
		if(type.equals(Type.DATA))
			return DataUserPermission.getInstance();		
		return  AppUserPermission.getInstance();
	}

}

