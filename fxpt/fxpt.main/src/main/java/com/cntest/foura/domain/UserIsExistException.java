/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.domain;

import com.cntest.exception.BusinessException;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年7月1日
 * @version 1.0
 **/
public class UserIsExistException extends BusinessException {

	private static final long serialVersionUID = -3771354319348817091L;

	public UserIsExistException() {
		this("cntext.4a.userexist","用户已存在");
	}
	
	public UserIsExistException(String code, String message) {
		super(code, message);
	}

}

