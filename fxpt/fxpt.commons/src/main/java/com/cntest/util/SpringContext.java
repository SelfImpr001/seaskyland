/*
 * @(#)com.cntest.util.SpringContext.java	1.0 2014年5月19日:上午11:43:42
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * <div> </div>
 * 
 * @author 刘海林 2013年12月17日 下午2:49:48
 * 
 */
public class SpringContext implements ApplicationContextAware {
	private static final Logger log = LoggerFactory
			.getLogger(SpringContext.class);
	private static ApplicationContext appContext;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.context.ApplicationContextAware#setApplicationContext
	 * (org.springframework.context.ApplicationContext)
	 */
	public void setApplicationContext(ApplicationContext appContext)
			throws BeansException {
		SpringContext.appContext = appContext;
	}

	public static <T> T getBean(String beanName) {
		try {
			@SuppressWarnings("unchecked")
			T bean = (T) getAppContext().getBean(beanName);
			return bean;
		} catch (Exception e) {
			log.error("获取对象失败{}", e);
		}
		return null;
	}
	
	public static <T> T getBean(Class<T> clazz) {
		try {
			T bean = (T) getAppContext().getBean(clazz);
			return bean;
		} catch (Exception e) {
			log.error("获取对象失败{}", e);
		}
		return null;
	}

	public static ApplicationContext getAppContext() {
		return appContext;
	}

	public static void registerBean(String beanName, Class clazz) {
		DefaultListableBeanFactory acb = (DefaultListableBeanFactory) appContext
				.getAutowireCapableBeanFactory();
		BeanDefinitionBuilder bdb = BeanDefinitionBuilder
				.genericBeanDefinition(clazz);
		acb.registerBeanDefinition(beanName, bdb.getBeanDefinition());
	}
	
	public static void registerBean(String beanName, Object target) {
		DefaultListableBeanFactory acb = (DefaultListableBeanFactory) appContext
				.getAutowireCapableBeanFactory();
		acb.registerSingleton(beanName, target);
	}
}