/*
 * @(#)com.cntest.fxpt.etl.module.NullStep.java	1.0 2014年5月26日:下午4:40:53
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.module;

import com.cntest.fxpt.etl.BaseStep;
import com.cntest.fxpt.etl.IEtlContext;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月26日 下午4:40:53
 * @version 1.0
 */
public class NullStep extends BaseStep {

	public NullStep(String name, IEtlContext context){
		super(name, context);
	}
	/* (non-Javadoc)
	 * @see com.cntest.fxpt.etl.IStep#begin()
	 */
	@Override
	public void begin() throws Exception {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.cntest.fxpt.etl.IStep#end()
	 */
	@Override
	public void end() throws Exception {
		// TODO Auto-generated method stub

	}

	
}
