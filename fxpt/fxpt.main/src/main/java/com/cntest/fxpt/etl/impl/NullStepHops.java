/**
 * 
 */
package com.cntest.fxpt.etl.impl;

import java.util.ArrayDeque;
import java.util.ArrayList;

import com.cntest.fxpt.etl.IStep;
import com.cntest.fxpt.etl.IStepHops;
import com.cntest.fxpt.etl.IStepMetadata;
import com.cntest.fxpt.etl.domain.RowSet;

/**
 * @author 刘海林 步骤连接的实现
 */
public class NullStepHops implements IStepHops {

	@Override
	public IStepMetadata getStepMetadata() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putRow(RowSet row) {
		// TODO Auto-generated method stub

	}

	@Override
	public RowSet getRow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addSetp(IStep step) {
		// TODO Auto-generated method stub

	}

	@Override
	public void execSetp() {
		// TODO Auto-generated method stub

	}

}
