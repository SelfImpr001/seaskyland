/*
 * @(#)com.cntest.fxpt.util.SystemConfig.java 1.0 2014年7月15日:上午10:55:52
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.util;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.util.ExceptionHelper;

import net.sf.ehcache.CacheManager;

/**
 * <Pre>
 * 获取系统配置文件属性
 * </Pre>
 * 
 * @author 刘海林 2014年7月15日 上午10:55:52
 * @version 1.0
 */
public class SystemConfig {
  private static final Logger log = LoggerFactory.getLogger(SystemConfig.class);
  private Properties pro = new Properties();
  private static volatile SystemConfig singleton;

  private SystemConfig(String configName) {
    pro = new Properties();
    try {
      pro.load(SystemConfig.class.getResourceAsStream(configName));
    } catch (Exception e) {
      log.error("加载配置文件失败;" + ExceptionHelper.trace2String(e));
    }
  }

  public static SystemConfig newInstance() {
    return newInstance("/settings.properties");
  }

  public static SystemConfig newInstance(String configName) {
    if (singleton != null) {
      return singleton;
    }
    synchronized (CacheManager.class) {
      if (singleton == null) {
        singleton = new SystemConfig(configName);
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
