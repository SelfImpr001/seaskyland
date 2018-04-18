/*
 * @(#)com.cntest.fxpt.etl.impl.DefaultScriptContext.java	1.0 2014年5月10日:下午12:43:01
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.impl;

import java.util.HashMap;
import java.util.Map;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;

import org.apache.commons.collections.map.HashedMap;

import com.cntest.fxpt.etl.IScriptContext;
import com.cntest.fxpt.etl.util.DateUtils;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月10日 下午12:43:01
 * @version 1.0
 */
public class GroovyScriptContext implements IScriptContext {
	private HashMap<Long, CompiledScript> scripts = new HashMap<Long, CompiledScript>();
	private ScriptEngineManager engineManager = new ScriptEngineManager();
	private ScriptEngine engine = engineManager.getEngineByName("groovy");

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.etl.IScriptContext#compile(java.lang.String)
	 */
	@Override
	public long compile(String script) throws Exception {
		long scriptId = DateUtils.getMillisecodeToLong();
		CompiledScript compiledScript = ((Compilable) engine).compile(script);
		scripts.put(scriptId, compiledScript);
		return scriptId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.etl.IScriptContext#execute(long, java.util.Map)
	 */
	@Override
	public <T> T execute(long scriptId, Map<String, Object> parameters)
			throws Exception {
		CompiledScript script = scripts.get(scriptId);
		SimpleBindings bindings = new SimpleBindings();
		if (parameters != null) {
			bindings.putAll(parameters);
		}
		return (T) script.eval(bindings);
	}

}
