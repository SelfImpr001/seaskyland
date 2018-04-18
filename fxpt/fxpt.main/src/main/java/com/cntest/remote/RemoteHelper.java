package com.cntest.remote;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.fxpt.etl.util.StringUtils;

/**
 * 远程接口帮助类.
 */
public class RemoteHelper {
    
	private static Logger logger = LoggerFactory.getLogger(RemoteHelper.class);
	
	/**
	 * 装换传递过来的参数
	 * @param parameterMap
	 * @return
	 */
	public static Map<String, String> getParameterMap(Map<String, String[]> parameterMap){
		Map<String, String> paramMap = new HashMap<String, String>();
		for (Entry<String, String[]> entry : parameterMap.entrySet()) {
			String[] value = entry.getValue();
			if(value.length>0 && StringUtils.isNotBlank(value[0]) && !"null".equals(value[0])){
				if("UID".equals(entry.getKey())){
					paramMap.put((entry.getKey()).toLowerCase(), value[0]);
				}else{
					paramMap.put(StringUtils.toLowerCaseFirstOne(entry.getKey()), value[0]);
				}
			}
		}
		return paramMap;
	}
	
	/**
	 * 格式化返回结果信息
	 * @param response
	 * @param success
	 * @param info
	 */
	public static void getResult(HttpServletResponse response, Boolean success, String info){
		StringBuilder builder = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		if(success == null)
			builder.append(info);
		else
			builder = builder.append("<result>")
						.append("	<success>").append(success?"true":"false").append("</success>")
						.append("	<info>").append(info).append("</info>")
						.append("</result>");
		
		BufferedWriter out = null;
		try {
			logger.info("Message,data["+builder.toString()+"]");
			out = new BufferedWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"));
			out.write(builder.toString());
			out.flush();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(out != null){
				try {
					out.close();
					out=null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
