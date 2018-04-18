/*
 * @(#)com.cntest.fxpt.etl.module.ExcelInput.java	1.0 2014年5月9日:下午3:44:23
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.module;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.fxpt.etl.BaseStep;
import com.cntest.fxpt.etl.IEtlContext;
import com.cntest.fxpt.etl.domain.CellMetadata;
import com.cntest.fxpt.etl.domain.RowMetadata;
import com.cntest.fxpt.etl.domain.RowSet;
import com.cntest.util.ReadDBF;
import com.cntest.util.ReadXLS;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月9日 下午3:44:23
 * @version 1.0
 */
public class DBFInput extends BaseStep {
	private static final Logger log = LoggerFactory.getLogger(DBFInput.class);
	private String filePath;

	private ReadDBF readDBF = new ReadDBF();
	private RowMetadata rowMetadata = new RowMetadata();

	public DBFInput(String name, IEtlContext context) {
		super(name, context);
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.etl.IStep#begin()
	 */
	@Override
	public void begin() throws Exception {
		readDBF.setFilePath(filePath);
		readDBF.openFile();
		List<String> header = readDBF.header();
		int columIndex = 0;
		for (String headerName : header) {
			CellMetadata cellMetadata = new CellMetadata(headerName,
					columIndex++);
			rowMetadata.add(cellMetadata);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.etl.IStep#end()
	 */
	@Override
	public void end() {
		readDBF.closeFile();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.etl.IStep#excuteStep()
	 */
	@Override
	public void excuteStep() throws Exception {
		log.debug("执行步骤"+getName());
		int rowNumber = 0;
		while (readDBF.hasNext()) {
			List<String> datas = readDBF.nextRow();
			RowSet row = new RowSet(++rowNumber);
			row.setRowMetadata(rowMetadata);
			for (String data : datas) {
				row.append(data);
			}
			this.putRow(row);
			if (rowNumber % 1000 == 0) {
				this.executeNextStep();
			}
		}
		this.setFinish(true);
		super.excuteStep();
		log.debug("执行步骤成功");
	}

}
