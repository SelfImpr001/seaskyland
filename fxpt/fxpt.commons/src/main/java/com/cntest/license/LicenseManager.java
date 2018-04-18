/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.license;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年10月10日
 * @version 1.0
 **/
public class LicenseManager {
	private static Logger logger = LoggerFactory.getLogger(LicenseManager.class);
	
	private LicenseToken licenseToken;
	
	private License license;
	
	private LicenseInfo result;
	
	private LicenseManager() {}
	
	private final static class SingletonHolder{
		private final static LicenseManager register = new LicenseManager();
	}
	
	public final static LicenseManager instance() {
//		if(SingletonHolder.register.license != null)
//			throw new LicenseException("License Exist!");
//		
//		if(license == null)
//			throw new LicenseException("License is not allowed!");
//		
//		SingletonHolder.register.license = license;
//		logger.debug("License registe success for {}",license.getClass().getSimpleName());
		return SingletonHolder.register;
	}
	
	public void doLicense() {
		result = new MyLicenseInfo();
		this.license.onAccess(result, licenseToken);
		//return info;
	}
	
	public LicenseManager registerLicense(License license) {
		this.license = license;
		return this;
	}
	
	public LicenseManager registerToken(LicenseToken licenseToken) {
		this.licenseToken = licenseToken;
		return this;
	}
	
	public LicenseInfo getLicenseResult() {
		return this.result;
	}
}

