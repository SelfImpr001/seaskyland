/**
 * 
 */
package com.cntest.fxpt.etl.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cntest.fxpt.etl.IEtlContext;
import com.cntest.fxpt.etl.IScriptContext;
import com.cntest.fxpt.etl.IStep;

/**
 * <pre>
 * 默认实现etl上下文
 * </pre>
 * 
 * @author 刘海林
 */
public class DefaultEtlContext implements IEtlContext {
	private HashMap<String, Object> parameters = new HashMap<String, Object>();
	private ArrayList<IStep> steps = new ArrayList<IStep>();
	private GroovyScriptContext scriptContext = new GroovyScriptContext();

	@Override
	public void registerSetp(IStep step) {
		steps.add(step);
	}

	@Override
	public IEtlContext addParameter(String paramName, Object value) {
		parameters.put(paramName, value);
		return this;
	}

	@Override
	public <T> T getParameter(String paramName) {
		T result = (T) parameters.get(paramName);
		return result;
	}

	@Override
	public IScriptContext getScriptContext() {
		return scriptContext;
	}

	@Override
	public List<IStep> getSteps() {
		return steps;
	}

}
