/**
 * <p><b>© 2001-2013 CC</b></p>
 * 
 **/

package com.cntest.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆 2013-8-10
 * @version 1.0
 **/
public class ExceptionHelper {
	public static String trace2String(Throwable t) {
		if (t == null)
			return "Throwable is null ";
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		return sw.toString();
	}

	public static String trace2String(Exception e) {
		return trace2String(e.getCause() == null ? e : e.getCause());
	}
}
