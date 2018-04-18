/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.permission;

import org.apache.shiro.authc.AuthenticationException;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2015年3月20日
 * @version 1.0
 **/
public class OAuth2AuthenticationException extends AuthenticationException {

	public OAuth2AuthenticationException(Throwable cause) {
        super(cause);
    }
}

