/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.web.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.cntest.license.DogLicense;
import com.cntest.license.LicenseManager;
import com.cntest.license.NoLicense;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年10月11日
 * @version 1.0
 **/
public class DogLicenseLoaderLisentner implements ServletContextListener{
	@Override
	public void contextDestroyed(ServletContextEvent event) {

	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			LicenseManager.instance().registerLicense(new DogLicense()).registerToken(null).doLicense();
		}catch(Exception e) {
			System.out.println("License Not Found");
			LicenseManager.instance().registerLicense(new NoLicense()).doLicense();
		}
		
	}
}

