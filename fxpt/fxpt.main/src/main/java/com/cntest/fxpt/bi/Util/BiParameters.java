/*
 * @(#)com.cntest.fxpt.bi.Util.BiParameters.java	1.0 2014年5月20日:下午2:06:52
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.bi.Util;

import org.springframework.stereotype.Repository;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2014年5月20日 下午2:06:52
 * @version 1.0
 */
@Repository("BiParameters")
public class BiParameters {
	
	/**
	 * 控制报表工具栏中隐藏主要参数入口
	 */
	private static String hidetoolbaritems = "hidetoolbaritems";
	
	//----------------下面是要隐藏的具体功能参数-------------------
	/**
	 * MYFAVORITE 保存到收藏夹
	 */
	private static String MYFAVORITE = "MYFAVORITE";
	/**
	 * REFRESH 刷新
	 */
	private static String REFRESH = "REFRESH";
	/**
	 * SAVE 保存
	 */
	private static String SAVE = "SAVE";
	/**
	 * SAVEAS 另存为
	 */
	private static String SAVEAS = "SAVEAS";
	/**
	 * EXPORT 导出
	 */
	private static String EXPORT = "EXPORT";
	/**
	 * EXCEL 在Excel 中分析
	 */
	private static String EXCEL = "EXCEL";
	/**
	 * VIEW 视图
	 */
	private static String VIEW = "VIEW";
	/**
	 * SELECTFIELD 增加/删除字段
	 */
	private static String SELECTFIELD = "SELECTFIELD";
	/**
	 * CHARTSETTING 图形设置
	 */
	private static String CHARTSETTING = "CHARTSETTING";
	/**
	 * PRINT 打印
	 */
	private static String PRINT = "PRINT";
	/**
	 * PARAMSETTING 参数设置
	 */
	private static String PARAMSETTING = "PARAMSETTING";
	/**
	 * SUBTOTAL 分类汇总
	 */
	private static String SUBTOTAL = "SUBTOTAL";
	/**
	 * REPORTSETTING 报表设置
	 */
	private static String REPORTSETTING = "REPORTSETTING";
	
	
	public static String getHidetoolbaritems() {
		return hidetoolbaritems;
	}
	public static String getMYFAVORITE() {
		return MYFAVORITE;
	}
	public static String getREFRESH() {
		return REFRESH;
	}
	public static String getSAVE() {
		return SAVE;
	}
	public static String getSAVEAS() {
		return SAVEAS;
	}
	public static String getEXPORT() {
		return EXPORT;
	}
	public static String getEXCEL() {
		return EXCEL;
	}
	public static String getVIEW() {
		return VIEW;
	}
	public static String getSELECTFIELD() {
		return SELECTFIELD;
	}
	public static String getCHARTSETTING() {
		return CHARTSETTING;
	}
	public static String getPRINT() {
		return PRINT;
	}
	public static String getPARAMSETTING() {
		return PARAMSETTING;
	}
	public static String getSUBTOTAL() {
		return SUBTOTAL;
	}
	public static String getREPORTSETTING() {
		return REPORTSETTING;
	}
	
	
}
