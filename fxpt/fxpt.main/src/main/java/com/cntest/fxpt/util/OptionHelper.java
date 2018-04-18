package com.cntest.fxpt.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * 处理小题选项的工具类，负责将小题选项答案进行提取
 * @author LM
 *
 */
public class OptionHelper{
	
	private static List<String> TYPE_UPPER = new ArrayList<>(); //A B C D
	private static List<String> TYPE_LETTER = new ArrayList<>(); //a b c d 
	private static List<String> TYPE_NUMBER = new ArrayList<>(); //1 2 3 4
	private static List<String> TYPE_SERIAL = new ArrayList<>(); //(1) (2) 
	private static List<String> TYPE_CHN = new ArrayList<>(); //一 二 三 四
	
	private static int START_NUM = 0;
	private static int DEFAULT_NUM = 5;
	
	private static String[] CHN_STRS = new String[] { "一", "二", "三", "四", "五", "六",
			"七", "八", "九", "十", "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八",
			"十九", "二十", "二十一", "二十二", "二十三", "二十四", "二十五", "二十六" };
	
	/**
	 * 初始化模板数组
	 */
	static{
		int length = CHN_STRS.length;
		for (int i = 0; i < length; i++) {
			TYPE_UPPER.add(String.valueOf((char)(i+'A')));
			TYPE_LETTER.add(String.valueOf((char)(i+'a')));
			TYPE_NUMBER.add(String.valueOf((char)(i+'1')));
			TYPE_SERIAL.add("("+String.valueOf((char)(i+'1'))+")");
			TYPE_CHN.add(CHN_STRS[i]);
		}
	}
	
	/**
	 * 根据答案数组找到对应的选项数组
	 * @param strs
	 * @return
	 */
	public static List<String> getOrderList(String[] strs){
		//找到数组中对应的最大选项
		if(strs == null || strs.length <1){
			return getOrderSubList(TYPE_UPPER, StringUtils.EMPTY);
		}
		//去重复
		Set<String> set = new HashSet<String>();
		set.addAll(Arrays.asList(strs));
		String[] newStrs = set.toArray(new String[0]);
		
		List<String> modelList = getModelList(newStrs[0]);
		return getOrderSubList(modelList, newStrs);
	}
	
	/**
	 * 根据答案数组找到对应的选项数组
	 * @param list
	 * @return
	 */
	public static List<String> getOrderList(List<String> list){
		//找到数组中对应的最大选项
		if(list == null || list.size() <1){
			return getOrderSubList(TYPE_UPPER, StringUtils.EMPTY);
		}
		//去重复
		Set<String> set = new HashSet<String>();
		set.addAll(list);
		String[] newStrs = set.toArray(new String[0]);
		
		List<String> modelList = getModelList(newStrs[0]);
		return getOrderSubList(modelList, newStrs);
	}
	
	/**
	 * 根据模板数组及答案数组找到所有选项数组
	 * @param list
	 * @param strs
	 * @return
	 */
	private static List<String> getOrderSubList(List<String> list, String[] strs){
		int maxNum = 0, indexOf = 0;
		for (String str : strs) {
			indexOf = list.indexOf(str);
			maxNum = (indexOf > maxNum )? indexOf : maxNum;
		}
		
		return list.subList(START_NUM, maxNum+1);
	}
	
	/**
	 * 根据最大的选项值找到选项数组
	 * @param str
	 * @return
	 */
	public static List<String> getOrderList(String str){
		List<String> modelList = getModelList(str);
		return getOrderSubList(modelList, str);
	}
	
	/**
	 * 根据字符串找到所在模板数组
	 * @param str
	 * @return
	 */
	private static List<String> getModelList(String str){
		if(StringUtils.isBlank(str)){
			return TYPE_UPPER;
		}
		if(TYPE_UPPER.contains(str)){
			return TYPE_UPPER;
		}
		if(TYPE_LETTER.contains(str)){
			return TYPE_LETTER;
		}
		if(TYPE_NUMBER.contains(str)){
			return TYPE_NUMBER;
		}
		if(TYPE_SERIAL.contains(str)){
			return TYPE_SERIAL;
		}
		if(TYPE_CHN.contains(str)){
			return TYPE_CHN;
		}
		return TYPE_UPPER;
	}
	
	/**
	 * 根据模板数组及最大值找到对应的选项
	 * @param list
	 * @param str
	 * @return
	 */
	private static List<String> getOrderSubList(List<String> list, String str){
		if(StringUtils.isEmpty(str) || list.indexOf(str) < 1){
			return list.subList(START_NUM, DEFAULT_NUM);
		}
		int indexOf = list.indexOf(str);
		return list.subList(START_NUM, indexOf+1);
	}
	
	public static void main(String[] args) {
		/*List<String> list = OptionHelper.getList("8");
		System.err.println(list.toString());
		JSONArray array = JSONArray.fromObject(list);
		System.out.println(array.toString());*/
		/*[1, 2, 3, 4, 5, 6, 7, 8]
			["1","2","3","4","5","6","7","8"]*/
		
		/*String[] strs = new String[]{"D", "A"};
		List<String> list = OptionHelper.getList(strs);*/
	}
	
}