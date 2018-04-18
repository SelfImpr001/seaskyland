/*
 * @(#)com.cntest.fxpt.util.ProjectInfo.java	1.0 2014年12月10日:上午10:42:11
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.uitl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年12月10日 上午10:42:11
 * @version 1.0
 */
public class Util {
	private static final Logger log = LoggerFactory.getLogger(Util.class);
	private static final String FILE_PREFIX = "file:/";

	public static String getThridProcessDir() {
		return getClassDir() + "thirdProcess/";
	}

	public static String getClassDir() {
		return Util.class.getResource("/").toString().replace(FILE_PREFIX, "");
	}

	public static String getDataDir() {
		return getClassDir() + "dataDir/";
	}

	public static File getFile() {
		String name = getMillisecodeToString() + ".txt";
		File file = new File(getDataDir() + name);
		return file;
	}

	public static File getFile(String prefix) {
		String name = prefix + "_" + getMillisecodeToString() + ".txt";
		File file = new File(getDataDir() + name);
		return file;
	}

	public synchronized static String getMillisecodeToString() {
		return dateToString(new Date(), "yyyyMMddHHmmss")
				+ (10000 + RandomUtils.nextInt(10000));
	}

	public static Long getMillisecodeToLong() {
		return Long.parseLong(dateToString(new Date(), "yyyyMMddHHmmss")
				+ (10000 + RandomUtils.nextInt(10000)));
	}

	public static String dateToString(Date date, String format) {
		String strDate = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			strDate = sdf.format(date);
		} catch (Exception w) {
			strDate = null;
			log.error("日期转换成字符串的时候出错!" + w.getLocalizedMessage());
		}
		return strDate;
	}
}
