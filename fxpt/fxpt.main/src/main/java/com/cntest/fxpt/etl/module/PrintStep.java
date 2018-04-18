/*
 * @(#)com.cntest.fxpt.etl.module.PrintStep.java	1.0 2014年5月9日:下午7:19:56
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.module;

import com.cntest.fxpt.etl.BaseStep;
import com.cntest.fxpt.etl.IEtlContext;
import com.cntest.fxpt.etl.domain.RowSet;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月9日 下午7:19:56
 * @version 1.0
 */
public class PrintStep extends BaseStep {

	/**
	 * @param name
	 * @param context
	 */
	public PrintStep(String name, IEtlContext context) {
		super(name, context);
		// TODO Auto-generated constructor stub
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
	public void end() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.cntest.fxpt.etl.IStep#excuteStep()
	 */
	@Override
	public void excuteStep() {
		
		RowSet row = null;
		while((row = getRow()) != null){
			System.out.println(row.toString());
		}
	}

}
