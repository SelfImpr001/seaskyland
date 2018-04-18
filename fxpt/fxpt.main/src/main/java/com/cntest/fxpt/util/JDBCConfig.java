/*
 * @(#)com.cntest.fxpt.util.SystemConfig.java	1.0 2014年7月15日:上午10:55:52
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.util;

import java.util.Properties;

import net.sf.ehcache.CacheManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.util.ExceptionHelper;

/**
 * <Pre>
 * 获取系统配置文件属性
 * </Pre>
 * 
 * @author 刘海林 2014年7月15日 上午10:55:52
 * @version 1.0
 */
public class JDBCConfig {
	private static final Logger log = LoggerFactory
			.getLogger(JDBCConfig.class);
	private Properties pro = new Properties();
	private static volatile JDBCConfig singleton;

	private JDBCConfig(String configName) {
		pro = new Properties();
		try {
			pro.load(JDBCConfig.class.getResourceAsStream("/properties/" + configName));
		} catch (Exception e) {
			log.error("加载配置文件失败;" + ExceptionHelper.trace2String(e));
		}
	}

	public static JDBCConfig newInstance() {
		return newInstance("jdbc.properties");
	}

	public static JDBCConfig newInstance(String configName) {
		if (singleton != null) {
			return singleton;
		}
		synchronized (CacheManager.class) {
			if (singleton == null) {
				singleton = new JDBCConfig(configName);
			}
			return singleton;
		}
	}

	public String getValue(String name) {
		return getValue(name, "");
	}

	public String getValue(String name, String defaultVlaue) {
		String value = pro.getProperty(name);
		if (value == null || value.equals("")) {
			value = defaultVlaue;
		}
		return value;
	}
}
