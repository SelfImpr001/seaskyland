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
 * @author 李贵庆2014年10月9日
 * @version 1.0
 **/
public class LicenseRegister {
	private static Logger logger = LoggerFactory.getLogger(LicenseRegister.class);
	
	private License license;
	
	private LicenseRegister() {}
	
	private final static class SingletonHolder{
		private final static LicenseRegister register = new LicenseRegister();
	}
	
	public final static LicenseRegister instanceOf(License license) {
		if(SingletonHolder.register.license != null)
			throw new LicenseException("License Exist!");
		
		if(license == null)
			throw new LicenseException("License is not allowed!");
		
		SingletonHolder.register.license = license;
		logger.debug("License registe success for {}",license.getClass().getSimpleName());
		return SingletonHolder.register;
	}
	
	public License getLicense() {
		return this.license;
	}
}

