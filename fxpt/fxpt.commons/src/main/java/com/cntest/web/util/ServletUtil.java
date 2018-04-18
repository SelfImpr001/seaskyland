/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.web.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.util.WebUtils;

import com.google.gson.Gson;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年9月24日
 * @version 1.0
 **/
public class ServletUtil {

	public static boolean isAjaxRequest(ServletRequest request) {
		HttpServletRequest httpRequest = WebUtils.toHttp(request);
		return (httpRequest.getHeader("X-Requested-With") != null && httpRequest.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1);
	}
	
	public static String getHeader(ServletRequest request,String header) {
		HttpServletRequest httpRequest = WebUtils.toHttp(request);
		return httpRequest.getHeader(header);
	}
	
	public static  <T>  T requestBodyToJson(ServletRequest request,Class<T> clazz) throws IOException{
		InputStream in = request.getInputStream();
    	BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    	StringBuilder json = new StringBuilder();
    	String line = null;       	
    	while((line = reader.readLine()) != null) {
    		json.append(line);
    	}
    	return new Gson().fromJson(json.toString(),clazz);
	}
	
	public static Map<String,String> getRequestMap(ServletRequest request){
		
		HashMap<String,String> requestMap = new HashMap<String,String>();
		Map<String, String[]> tmpMap = request.getParameterMap();
		for (String key : tmpMap.keySet()) {
			String[] value = tmpMap.get(key);
			if (value == null) {
				value = new String[] { "" };
			}
			requestMap.put(key, value[0]);
		}
		return requestMap;
	}
}

