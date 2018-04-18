/**
 * <p><b>© 1997-2013 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/
package com.cntest.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.cntest.license.IllegalLicenseException;
import com.cntest.util.ExceptionHelper;
import com.cntest.web.view.ResponseStatus;
import com.google.gson.Gson;

/**
 * <pre>
 * 自定义异常处理
 * </pre>
 * 
 * @author 李贵庆 2013-9-5
 * @version 1.0
 **/
public class CntestSimpleMappingExceptionResolver extends
		SimpleMappingExceptionResolver {
	private static final Logger logger = LoggerFactory
			.getLogger(CntestSimpleMappingExceptionResolver.class);

	protected String determineViewName(Exception ex, HttpServletRequest request) {
		if(ex instanceof IllegalLicenseException)
			return "nolicense";
		return super.determineViewName(ex, request);
	}
	
	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {

		logger.error(ExceptionHelper.trace2String(ex));
		String viewName = determineViewName(ex, request);
		if (viewName != null) {// JSP格式返回
			if (!(request.getHeader("accept").indexOf("application/json") > -1 || 
				 (request.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1) ||
				  (request.getHeader("Content-Type") != null && request.getHeader("Content-Type").indexOf("multipart/form-data") > -1))
				) {
				// 如果不是异步请求
				Integer statusCode = determineStatusCode(request, viewName);
				
				if (statusCode != null) {
					applyStatusCodeIfPossible(request, response, statusCode);
				}
				if(ex instanceof BusinessException || ex instanceof IllegalLicenseException)
				    request.setAttribute("message", ex.getMessage());
				else if(ex instanceof SQLException)
				    request.setAttribute("message", "服务器异常");
				else
					request.setAttribute("message", "很抱歉，系统在处理您的请求产生未定义错误，请联系管理员");
				return getModelAndView(viewName, ex, request);
			} else {// JSON格式返回
				try {
					//response.addHeader("Content-Type", "application/json;charset=UTF-8");
					ResponseStatus rs = new ResponseStatus.Builder(
							Boolean.FALSE).code("Cntest-00000")
							.msg("很抱歉，系统在处理您的请求产生未定义错误，请联系管理员").build();
					if (ex instanceof BusinessException) {
						BusinessException be = (BusinessException) ex;
						rs = new ResponseStatus.Builder(Boolean.FALSE)
								.code(be.getCode()).msg(be.getMessage())
								.msgDetail(ExceptionHelper.trace2String(ex))
								.build();
					} else if (ex instanceof SystemException) {
						SystemException se = (SystemException) ex;
						rs = new ResponseStatus.Builder(Boolean.FALSE)
								.code(se.getCode()).msg(se.getMessage())
								.msgDetail(ExceptionHelper.trace2String(ex))
								.build();
					} else if (ex instanceof SQLException){
						rs = new ResponseStatus.Builder(Boolean.FALSE)
								.code("Cntest-00002").msg("数据操作异常，请联系管理员")
								.msgDetail(ExceptionHelper.trace2String(ex))
								.build();
					}else if(ex instanceof IllegalLicenseException) {
						rs = new ResponseStatus.Builder(Boolean.FALSE)
						.code("Cntest-00001").msg("系统使用授权已失效！")
						.msgDetail(ExceptionHelper.trace2String(ex))
						.build();
					}else  {
						
					}

					HashMap<String, ResponseStatus> jsonMap = new HashMap<String, ResponseStatus>();
					jsonMap.put(ResponseStatus.NAME, rs);
					String json = new Gson().toJson(jsonMap);
					PrintWriter writer = response.getWriter();

					writer.write(json);
					writer.flush();
				} catch (IOException e) {
					logger.error(ExceptionHelper.trace2String(e));
				} finally {

				}
				return null;

			}
		} else {
			return null;
		}
	}
}
