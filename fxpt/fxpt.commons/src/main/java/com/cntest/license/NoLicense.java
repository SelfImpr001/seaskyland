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
public class NoLicense implements License {

	@Override
	public void onAccess(LicenseInfo info, LicenseToken token) throws LicenseException {
		info.setAccessabled(false);
		info.setMessage("没有找到本系统的使用授权文件");
	}

	@Override
	public Object getLicense() {
		// TODO Auto-generated method stub
		return null;
	}

}

