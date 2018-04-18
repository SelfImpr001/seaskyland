/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.application.permission;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2014年6月27日
 * @version 1.0
 **/
@Component
public class PasswordService {

	@Value("${shiro.password.algorithmName}")
	private String algorithmName = "md5";
	@Value("${shiro.password.hashIterations}")
	private int hashIterations = 2;
	
	public String encryptPassword(String password,String salt) {
		 String newPassword = new SimpleHash(
	                algorithmName,
	                password,
	                ByteSource.Util.bytes(salt),
	                hashIterations).toHex();
		return newPassword;
	}
}
