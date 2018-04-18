/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.license;

import java.io.File;
import java.io.FileWriter;
import java.io.RandomAccessFile;

import org.apache.commons.codec.binary.Base64;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2014年10月10日
 * @version 1.0
 **/
public class Base64CredentialsMatcher implements LicenseCredentialsMatcher {

	private String salt = "cntest.com";

	public Base64CredentialsMatcher() {

	}

	public Base64CredentialsMatcher(String salt) {
		if (salt != null && salt.length() > 0)
			this.salt = salt;
	}

	@Override
	public boolean doCredentialsMatch(License license, LicenseToken token) {
		String realCredentials = Base64.encodeBase64String((salt + token
				.getCredentials()).getBytes());
		return realCredentials.equals(license.getLicense());
	}
	// 4a IP地址license 认证生成
	public static void main(String[] args)throws Exception{
		Base64CredentialsMatcher mather = new Base64CredentialsMatcher();
		String s = Base64.encodeBase64String((mather.salt + "10.8.5.239").getBytes()); 
		FileWriter fileWriter = new FileWriter("C:/Cntest.license");
		fileWriter.write(s);
		fileWriter.flush();
		fileWriter.close();
		System.out.println("C:/Cntest.license");
	}
}
