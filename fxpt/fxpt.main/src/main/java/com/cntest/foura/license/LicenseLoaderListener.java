/**
d * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.license;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.Calendar;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.cntest.license.FileLicense;
import com.cntest.license.License;
import com.cntest.license.LicenseCredentialsMatcher;
import com.cntest.license.LicenseManager;
import com.cntest.license.LicenseToken;
import com.cntest.license.NoLicense;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年10月10日
 * @version 1.0
 **/
public class LicenseLoaderListener implements ServletContextListener{

	private static final String LicenseTokenClass = "LicenseToken";
	
	private static final String LicenseFileName = "Cntest.license";
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			String licenseTokenClassName = event.getServletContext().getInitParameter(LicenseTokenClass);
			//目前只支持License安装在bin目录下
			Class clazz = Class.forName(licenseTokenClassName);
			Constructor c = clazz.getConstructor();
			
			LicenseToken licenseToken = (LicenseToken)c.newInstance();
			//LicenseToken licenseToken = BeanUtils.instantiate(clazz);
			String contextPath = new File(System.getProperty("catalina.home")).getAbsolutePath();
			System.out.println(contextPath + File.separator + "bin" + File.separator + LicenseFileName);
			FileLicense license = new FileLicense(new LicenseCredentialsMatcher() {

				@Override
				public boolean doCredentialsMatch(License license, LicenseToken token) {
					return TimeLimiteEndecrypt.lt(Calendar.getInstance(), license.getLicense()+"");
				}
				
			},contextPath + File.separator + "bin" + File.separator  + LicenseFileName);
			LicenseManager.instance().registerLicense(license).registerToken(licenseToken).doLicense();
	        //String fn = event.getServletContext().getRealPath(logbackConfigLocation);
		}catch(Exception e) {
			System.out.println("License Not Found");
			LicenseManager.instance().registerLicense(new NoLicense()).doLicense();
		}
		
	}

}

