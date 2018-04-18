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
 * @author 李贵庆2015年6月4日
 * @version 1.0
 **/
public class IllegalLicenseException extends RuntimeException {

	private static final long serialVersionUID = 7579318818550296076L;

	public IllegalLicenseException() {
		super("没有找到系统授权文件或者授权文件已过期");
	}
	
	public IllegalLicenseException(String message) {
		super(message);
	}
}

