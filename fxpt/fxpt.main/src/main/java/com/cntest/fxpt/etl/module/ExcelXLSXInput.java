/*
 * @(#)com.cntest.fxpt.etl.module.ExcelInput.java	1.0 2014年5月9日:下午3:44:23
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.module;

import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.cntest.fxpt.etl.BaseStep;
import com.cntest.fxpt.etl.IEtlContext;
import com.cntest.fxpt.etl.domain.CellMetadata;
import com.cntest.fxpt.etl.domain.RowMetadata;
import com.cntest.fxpt.etl.domain.RowSet;
import com.cntest.fxpt.service.IItemService;
import com.cntest.util.ExceptionHelper;
import com.cntest.util.ReadXLSX;
import com.cntest.util.SpringContext;
import com.cntest.util.excle2007.IRowOpt;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月9日 下午3:44:23
 * @version 1.0
 */
public class ExcelXLSXInput extends BaseStep {
	private static final Logger log = LoggerFactory
			.getLogger(ExcelXLSXInput.class);

	private String filePath;
	private String sheetName;
	private int headRowIdx = 1;
	private ReadXLSX readXLSX;
	private RowMetadata rowMetadata = new RowMetadata();
	

	public ExcelXLSXInput(String name, IEtlContext context) {
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
		log.debug("初始化步骤：" + getName());
		readXLSX = new ReadXLSX(filePath);
		readXLSX.open();
		readXLSX.setSheet(sheetName);
		List<String> header = readXLSX.header(headRowIdx);
		int columIndex = 0;
		for (String headerName : header) {
			CellMetadata cellMetadata = new CellMetadata(headerName,
					columIndex++);
			rowMetadata.add(cellMetadata);
		}
		log.debug("初始化步骤完成：" + getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.etl.IStep#end()
	 */
	@Override
	public void end() {
		readXLSX.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.etl.IStep#excuteStep()
	 */
	@Override
	public void excuteStep() throws Exception {
		log.debug("执行步骤：" + getName());
//		IItemService itemService = null;
//		itemService = SpringContext.getBean("IItemService");
		readXLSX.dealWithRowData(new IRowOpt() {
			private int rowNumber = 0;

			@Override
			public void row(int rowIdx, List<String> row) {
				if (rowIdx <= headRowIdx+1) {
					return;
				}
				rowNumber = rowIdx - 1;
				RowSet newRowset = new RowSet(rowNumber);
				newRowset.setRowMetadata(rowMetadata);
				
				for (String cell : row) {
					newRowset.append(cell);
				}
				ExcelXLSXInput.this.putRow(newRowset);
				if (rowNumber % 1000 == 0) {
					try {
						ExcelXLSXInput.this.executeNextStep();
					} catch (Exception e) {
						log.debug(ExceptionHelper.trace2String(e));
						throw new RuntimeException("执行步骤出错", e);
					}
				}
			}

		});
		this.setFinish(true);
		super.excuteStep();
//		log.debug("执行步骤完毕：" + getName());
	}

}
