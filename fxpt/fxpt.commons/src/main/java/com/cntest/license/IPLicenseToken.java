/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.license;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.util.IpReader;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2014年10月10日
 * @version 1.0
 **/
public class IPLicenseToken implements LicenseToken {
	private static Logger logger = LoggerFactory.getLogger(IPLicenseToken.class);
	
	private LicenseToken other;
	
	private String myIp;
	
	private static String message = "License认证失败：服务器IP地址不匹配"; 

	public IPLicenseToken() {
		
	}

	public IPLicenseToken(LicenseToken other) {
		this.other = other;
	}

	@Override
	public String getCredentials() {
		this.myIp= IpReader.getLocalIP();
		logger.info("Server IP is {}",this.myIp);
		return this.myIp;
	}

	@Override
	public String getAccessDeniedMessage() {
		if(other != null)
			return message + ";" + other.getAccessDeniedMessage();
		return message;
	}

}
