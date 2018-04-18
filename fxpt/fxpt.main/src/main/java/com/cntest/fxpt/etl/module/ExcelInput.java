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
import com.cntest.util.ReadXLS;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月9日 下午3:44:23
 * @version 1.0
 */
public class ExcelInput extends BaseStep {
	private static final Logger log = LoggerFactory.getLogger(ExcelInput.class);

	private String filePath;
	private String sheetName;
	private int headRowIdx = 1;
	private ReadXLS readXLS;
	private RowMetadata rowMetadata = new RowMetadata();

	public ExcelInput(String name, IEtlContext context) {
		super(name, context);
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public void setHeadRowIdx(int headRowIdx) {
		this.headRowIdx = headRowIdx;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.etl.IStep#begin()
	 */
	@Override
	public void begin() {
		log.debug("初始化步骤;工作簿：", sheetName);
		readXLS = new ReadXLS(filePath);
		readXLS.open();
		readXLS.setSheet(sheetName);
		List<String> header = readXLS.header(headRowIdx);
		int columIndex = 0;
		for (String headerName : header) {
			CellMetadata cellMetadata = new CellMetadata(headerName,
					columIndex++);
			rowMetadata.add(cellMetadata);
		}
		log.debug("初始化工作簿成功");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.etl.IStep#end()
	 */
	@Override
	public void end() {
		readXLS.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.etl.IStep#excuteStep()
	 */
	@Override
	public void excuteStep() throws Exception {
		log.debug("执行步骤" + getName());
		int rowNumber = 0;
		while (readXLS.hasNext()) {
			List<String> datas = readXLS.nextRow();
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
