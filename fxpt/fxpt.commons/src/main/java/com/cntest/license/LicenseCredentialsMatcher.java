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
 * @author 李贵庆2014年10月10日
 * @version 1.0
 **/
public interface LicenseCredentialsMatcher {

	boolean doCredentialsMatch(License license,LicenseToken token);
}

