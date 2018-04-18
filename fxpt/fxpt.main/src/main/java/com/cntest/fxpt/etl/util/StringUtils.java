package com.cntest.fxpt.etl.util;

public class StringUtils extends org.apache.commons.lang3.StringUtils{

	/**
	 * 字符串首字母小写
	 * @param filed
	 * @return
	 */
	public static String toLowerCaseFirstOne(String filed){
		return (new StringBuilder()).append(Character.toLowerCase(filed.charAt(0)))
									.append(filed.substring(1)).toString();
	}
	
	/**
	 * 字符串首字母大写
	 * @param filed
	 * @return
	 */
	public static String toUpperCaseFirstOne(String filed){
		return (new StringBuilder()).append(Character.toUpperCase(filed.charAt(0)))
									.append(filed.substring(1)).toString();
	}
	
}
