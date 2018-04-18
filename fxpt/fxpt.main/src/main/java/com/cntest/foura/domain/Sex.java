/**
 * <p><b>© 1997-2013 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.domain;
/** 
 * <pre>
 * 性别
 * </pre>
 *  
 * @author 李贵庆2014年6月4日
 * @version 1.0
 **/
public enum Sex {
	UNKNOW("0"),FEMALE("2"),MALE("1");
	
	private String code;
	
	private String desc;
	
	private Sex(String code) {
		this.code = code;
	}
}

