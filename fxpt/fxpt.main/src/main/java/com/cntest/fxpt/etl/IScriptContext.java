/*
 * @(#)com.cntest.fxpt.etl.IScriptContext.java	1.0 2014年5月10日:下午12:37:48
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl;

import java.util.Map;

/**
 * <Pre>
 * 处理脚本执行的上下文，符合jsr 223规范的脚本文件都可以处理
 * </Pre>
 * 
 * @author 刘海林 2014年5月10日 下午12:37:48
 * @version 1.0
 */
public interface IScriptContext {
	public long compile(String script)  throws Exception;
	public <T> T execute(long scriptId,Map<String,Object> parameters)  throws Exception;
}
