/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.license;

import java.util.Calendar;

import com.cntest.license.LicenseToken;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2015年1月21日
 * @version 1.0
 **/
public class TimeLimiteLicenseToken implements LicenseToken {

	@Override
	public Object getCredentials() {

		return Calendar.getInstance();
	}

	@Override
	public String getAccessDeniedMessage() {

		return "授权文件已过期！";
	}

}

