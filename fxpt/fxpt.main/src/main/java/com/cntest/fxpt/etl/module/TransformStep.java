/*
 * @(#)com.cntest.fxpt.etl.module.TransformStep.java	1.0 2014年5月10日:上午11:12:36
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.module;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.fxpt.etl.BaseStep;
import com.cntest.fxpt.etl.IEtlContext;
import com.cntest.fxpt.etl.domain.RowLog;
import com.cntest.fxpt.etl.domain.RowMetadata;
import com.cntest.fxpt.etl.domain.RowSet;

/**
 * <Pre>
 * 负责对数据的转换，和验证；验证结果成功，错误，和警告3中类型
 * </Pre>
 * 
 * @author 刘海林 2014年5月10日 上午11:12:36
 * @version 1.0
 */
public class TransformStep extends BaseStep {
	private static final Logger log = LoggerFactory
			.getLogger(TransformStep.class);
	private ArrayList<AbstractTransform> transforms = new ArrayList<AbstractTransform>();

	/**
	 * @param name
	 * @param context
	 */
	public TransformStep(String name, IEtlContext context) {
		super(name, context);
	}

	public TransformStep appendTransform(AbstractTransform transform) {
		transforms.add(transform);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.etl.IStep#begin()
	 */
	@Override
	public void begin() throws Exception {
		for (AbstractTransform transform : transforms) {
			transform.compile();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.etl.IStep#end()
	 */
	@Override
	public void end() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.etl.IStep#excuteStep()
	 */
	@Override
	public void excuteStep() throws Exception {
		log.debug("执行步骤:" + getName());
		RowSet row = null;
		while ((row = getRow()) != null) {
			RowSet newRow = copyRowSet(row);
			RowLog rowLog = new RowLog();
			for (AbstractTransform transform : transforms) {
				transform.clearnLog();
				transform.execute(newRow);
				RowLog log = transform.getLog();
				rowLog.addLog(log);
			}
			addLog(rowLog);
			if (!rowLog.isHasError() && !rowLog.isHasIgnore()) {
				putRow(newRow);
			}
		}

		super.excuteStep();
//		log.debug("执行完毕步骤:" + getName());
	}

	private RowSet copyRowSet(RowSet row) {
		RowSet newRow = new RowSet(row.getRowNumber());
		RowMetadata rowMetadata = new RowMetadata();
		rowMetadata.add(row.getRowMetadata());
		newRow.setRowMetadata(rowMetadata);
		newRow.setDataSet(row.getDataSet());
		return newRow;
	}

}
