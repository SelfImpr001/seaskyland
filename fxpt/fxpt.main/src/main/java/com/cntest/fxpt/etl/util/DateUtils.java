package com.cntest.fxpt.etl.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtils {
	private static final Logger logger = LoggerFactory
			.getLogger(DateUtils.class);

	/**
	 * <pre>
	 * 字符串转换成日期格式
	 * </pre>
	 * 
	 * @param strDate
	 * @param format
	 * @return
	 * @return Date
	 * @date:2011-11-11
	 * @author:刘海林
	 */
	public static Date stringToDate(String strDate, String format) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			date = sdf.parse(strDate);
		} catch (Exception w) {
			date = null;
			logger.error("转换成日期的时候出错!" + w.getLocalizedMessage());
		}
		return date;
	}

	/**
	 * <pre>
	 * 字符串转换成日期格式
	 * </pre>
	 * 
	 * @param strDate
	 * @return
	 * @return Date
	 * @date:2011-11-11
	 * @author:刘海林
	 */
	public static Date stringToDate(String strDate) {
		return stringToDate(strDate, "yyyy-MM-dd");
	}

	/**
	 * <pre>
	 * 日期转换成字符串格式
	 * </pre>
	 * 
	 * @param date
	 * @return
	 * @return String
	 * @date:2011-11-11
	 * @author:刘海林
	 */
	public static String dateToString(Date date) {
		return dateToString(date, "yyyy-MM-dd");
	}

	/**
	 * <pre>
	 * 日期转换成字符串格式
	 * </pre>
	 * 
	 * @param date
	 * @param format
	 * @return
	 * @return String
	 * @date:2011-11-11
	 * @author:刘海林
	 */
	public static String dateToString(Date date, String format) {
		String strDate = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			strDate = sdf.format(date);
		} catch (Exception w) {
			strDate = null;
			logger.error("日期转换成字符串的时候出错!" + w.getLocalizedMessage());
		}
		return strDate;
	}

	/**
	 * <pre>
	 * 获取时间的毫秒
	 * </pre>
	 * 
	 * @return
	 * @return String
	 * @date:2011-11-15
	 * @author:刘海林
	 */
	public static String getMillisecodeToString() {
		return dateToString(new Date(), "yyyyMMddHHmmss")
				+ (10000 + RandomUtils.nextInt(10000));
	}

	public static Long getMillisecodeToLong() {
		return Long.parseLong(dateToString(new Date(), "yyyyMMddHHmmss")
				+ (10000 + RandomUtils.nextInt(10000)));
	}
}
