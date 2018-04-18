/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.license;

import com.hyt.wy.dao.safeDog;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年10月11日
 * @version 1.0
 **/
public class DogLicense implements License {

	private int res = -1;
	
	@Override
	public void onAccess(LicenseInfo info, LicenseToken token) throws LicenseException {
		res = safeDog.checkDog();
		//int tokenRes = (Integer)token.getCredentials();
		if(res < 0 ) {
			info.setAccessabled(false);
			info.setMessage("无效的加密狗");
		}else {
			info.setAccessabled(true);
		}
	}

	@Override
	public Object getLicense() {
		return res;
	}
}