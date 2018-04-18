/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.common.config;
/** 
 * <pre>
 * 系统运行时配置
 * </pre>
 *  
 * @author 李贵庆2014年6月20日
 * @version 1.0
 **/
public class SystemRuntimeConfiguration {
    
	public static String appKey = "";
	public static String appRole = "";
	
	public SystemRuntimeConfiguration() {
	    //init();	
	}
	
	public static void setAppKey(String appKey) {
		SystemRuntimeConfiguration.appKey = appKey;
	}
	
	public static String getAppKey() {
		return SystemRuntimeConfiguration.appKey;
	}

	public static String getAppRole() {
		return SystemRuntimeConfiguration.appRole;
	}

	public static void setAppRole(String appRole) {
		SystemRuntimeConfiguration.appRole = appRole;
	}
	
}

