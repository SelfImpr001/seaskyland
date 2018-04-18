/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.license;
/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年10月11日
 * @version 1.0
 **/
public class DogLicenseToken implements LicenseToken {

	private int res = 0;
	@Override
	public Object getCredentials() {
		return res;
	}

	@Override
	public String getAccessDeniedMessage() {
		return "无效的加密狗";
	}

}

