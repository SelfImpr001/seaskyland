/*
 * @(#)com.cntest.fxpt.etl.transform.GroovyScriptTransform.java	1.0 2014年5月10日:下午12:22:24
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.transform;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.fxpt.etl.IScriptContext;
import com.cntest.fxpt.etl.domain.RowSet;
import com.cntest.fxpt.etl.module.AbstractTransform;
import com.cntest.fxpt.etl.module.TransformStep;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月10日 下午12:22:24
 * @version 1.0
 */
public class GroovyScriptTransform extends AbstractTransform {
	private static final Logger log = LoggerFactory
			.getLogger(GroovyScriptTransform.class);
	private long scriptId;

	/**
	 * <Pre>
	 * 脚本文本
	 * </Pre>
	 * 
	 * scriptExpression:String
	 * 
	 * @author:刘海林 2014年5月10日 下午12:23:06
	 */
	private String script;

	public GroovyScriptTransform(TransformStep transformStep) {
		super(transformStep);
	}

	/**
	 * 
	 */
	public GroovyScriptTransform(String script, TransformStep transformStep) {
		this(transformStep);
		this.script = script;
	}

	public void setScriptId(Long scriptId) {
		this.scriptId = scriptId;
	}

	public String getScript() {
		return script;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.etl.module.AbstractTransform#transform(com.cntest.fxpt
	 * .etl.domain.RowSet)
	 */
	@Override
	public void execute(RowSet row) throws Exception {
		log.debug("执行转换");
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("rowData", row);
		parameters.put("log", getLog());
		parameters.put("stepParameter", this.getStepMetadata()
				.getStepParameter());
		log.debug("步骤参数");
		log.debug("rowData:" + row.getClass().getName());
		log.debug("log:" + getLog().getClass().getName());
		log.debug("stepParameter:"
				+ this.getStepMetadata().getClass().getName());

		IScriptContext scriptContext = getStepMetadata().getContext()
				.getScriptContext();
		scriptContext.execute(scriptId, parameters);
		log.debug("执行转换成功");
	}

	@Override
	public void compile() throws Exception {
		log.debug("编译脚本");
		log.debug("groovy script:" + script);
		IScriptContext scriptContext = getStepMetadata().getContext()
				.getScriptContext();
		scriptId = scriptContext.compile(script);
		log.debug("编译成功");
	}

}
