/*
 * @(#)com.cntest.fxpt.util.Json.java	1.0 2014年10月11日:下午2:24:52
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.util;

import java.io.ByteArrayOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.util.ExceptionHelper;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月11日 下午2:24:52
 * @version 1.0
 */
public class Json {
	private static final Logger log = LoggerFactory.getLogger(Json.class);

	public static String toJson(Object obj) {
		String result = "";
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			JsonGenerator json = new ObjectMapper().getFactory()
					.createGenerator(out, JsonEncoding.UTF8);
			json.writeObject(obj);
			json.flush();
			result = out.toString("UTF-8");
			out.close();
		} catch (Exception e) {
			log.error(ExceptionHelper.trace2String(e));
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}
}
