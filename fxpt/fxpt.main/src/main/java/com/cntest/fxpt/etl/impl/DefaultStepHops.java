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
public class DefaultStepHops implements IStepHops {
	private ArrayDeque<RowSet> dataSet = new ArrayDeque<RowSet>(1000);
	private ArrayDeque<RowSet> dataSet2 = new ArrayDeque<RowSet>(1000);
	private IStepMetadata stepMetadata;
	private ArrayList<IStep> steps = new ArrayList<IStep>();

	public DefaultStepHops(IStepMetadata stepMetadata) {
		stepMetadata.registerChildStepHops(this);
		setStepMemadata(stepMetadata);
	}

	private void setStepMemadata(IStepMetadata stepMetadata) {
		this.stepMetadata = stepMetadata;
	}

	public IStepMetadata getStepMetadata() {
		return stepMetadata;
	}

	public void putRow(RowSet row) {
		dataSet.add(row);
	}

	public RowSet getRow() {
		RowSet result = dataSet.poll();
		if (result != null) {
			dataSet2.add(result);
		}
		return result;
	}

	@Override
	public void addSetp(IStep step) {
		step.getStepMetadata().registerParentStepHops(this);
		steps.add(step);
	}

	@Override
	public void execSetp() throws Exception {
		for (IStep step : steps) {
			step.excuteStep();
			dataSet = dataSet2;
			dataSet2 = new ArrayDeque<RowSet>(1100);
		}
		dataSet = new ArrayDeque<RowSet>(1100);
		dataSet2 = new ArrayDeque<RowSet>(1100);
	}

}
