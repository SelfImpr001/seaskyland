/**
 * <p><b>© 2001-2013 CC</b></p>
 * 
 **/

package com.cntest.util;

import java.util.Locale;
import java.util.ResourceBundle;




/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆 2013-8-10
 * @version 1.0
 **/
public class SystemParameter {
	private final static String configFileName = "properties/system";
	
	public static String getParameter(String key) {
		try {
			return ResourceBundle.getBundle(configFileName,
					MyController.newInstance()).getString(key);
		} catch (Exception e) {
            e.printStackTrace();
		}
		return "";
	}
	
	public static String getParameter(String key,String fileName) {
		try {
			return ResourceBundle.getBundle(fileName,
					MyController.newInstance()).getString(key);
		} catch (Exception e) {
            e.printStackTrace();
		}
		return "";
	}
	
	private static class MyController extends ResourceBundle.Control {

		private MyController() {

		}

		private static class SingletonHolder {
			private static final MyController controller = new MyController();
		}

		public static MyController newInstance() {
			return SingletonHolder.controller;
		}

		@Override
		public long getTimeToLive(String baseName, Locale locale) {
			return 0;
		}

		@Override
		public boolean needsReload(String baseName, Locale locale,
				String format, ClassLoader loader, ResourceBundle bundle,
				long loadTime) {
			return true;
		}

	}

}
