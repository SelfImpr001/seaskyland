package com.cntest.web.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.support.ClassPathXmlApplicationContext;
/**
 * <pre>
 *  
 * </pre>
 * 
 * @author 李贵庆 2013-6-12
 * @version 1.0 
 **/
public class TaskContextListener implements ServletContextListener {
    
	@SuppressWarnings("resource")
	public void contextInitialized(ServletContextEvent event) {
		new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-task.xml");
	}

	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}

}

