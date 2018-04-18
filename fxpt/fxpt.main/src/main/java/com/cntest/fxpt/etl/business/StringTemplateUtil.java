/*
 * @(#)com.cntest.fxpt.etl.business.TransformSQLUtil.java	1.0 2014年6月5日:下午4:05:20
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.util.ExceptionHelper;

import freemarker.template.Template;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月5日 下午4:05:20
 * @version 1.0
 */
public class StringTemplateUtil {
	private static final Logger log = LoggerFactory
			.getLogger(StringTemplateUtil.class);

	public static String transformTemplate(Map<String, Object> model, String tmplate) {
		String result = "";
		try {
			Template template = new Template(null, new StringReader(tmplate));
			StringWriter out = new StringWriter();
			template.process(model, out);
			result = out.toString();
		} catch (Exception e) {
			log.debug(ExceptionHelper.trace2String(e));
			throw new RuntimeException(e);
		}
		return result;
	}
}
