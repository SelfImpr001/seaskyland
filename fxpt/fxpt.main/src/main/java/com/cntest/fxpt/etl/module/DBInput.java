/*
 * @(#)com.cntest.fxpt.etl.module.ExcelInput.java	1.0 2014年5月9日:下午3:44:23
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.module;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.fxpt.etl.BaseStep;
import com.cntest.fxpt.etl.IEtlContext;
import com.cntest.fxpt.etl.domain.CellMetadata;
import com.cntest.fxpt.etl.domain.RowMetadata;
import com.cntest.fxpt.etl.domain.RowSet;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月9日 下午3:44:23
 * @version 1.0
 */
public class DBInput extends BaseStep {
	private static final Logger log = LoggerFactory.getLogger(DBInput.class);
	private DataSource dataScource;
	private String sql;
	private RowMetadata rowMetadata = new RowMetadata();
	private ResultSet resultSet;
	private Connection conn;
	private PreparedStatement statement;

	public DBInput(String name, IEtlContext context) {
		super(name, context);
	}

	public DataSource getDataScource() {
		return dataScource;
	}

	public void setDataScource(DataSource dataScource) {
		this.dataScource = dataScource;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.etl.IStep#begin()
	 */
	@Override
	public void begin() throws Exception {

		log.debug("初始化步骤" + getName());
		log.debug("SQL:" + sql);

		try {
			conn = dataScource.getConnection();
			statement = conn.prepareStatement(sql);
			resultSet = statement.executeQuery();
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
			int columnNumber = resultSetMetaData.getColumnCount();
			for (int i = 0; i < columnNumber; i++) {
				String columnName = resultSetMetaData.getColumnLabel(i + 1);
				CellMetadata cellMetadata = new CellMetadata(columnName, i);
				rowMetadata.add(cellMetadata);
			}
		} catch (Exception e) {
			end();
			throw e;
		}
		log.debug("初始化步骤成功");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.etl.IStep#end()
	 */
	@Override
	public void end() throws Exception {
		if (resultSet != null) {
			resultSet.close();
			resultSet = null;
		}
		if (statement != null) {
			statement.close();
			statement = null;
		}
		if (conn != null) {
			conn.close();
			conn = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.etl.IStep#excuteStep()
	 */
	@Override
	public void excuteStep() throws Exception {
		log.debug("执行步骤" + getName());
		int columnSize = rowMetadata.keySet().size();
		int rowNumber = 0;
		try {
			while (resultSet.next()) {
				RowSet row = new RowSet(++rowNumber);
				row.setRowMetadata(rowMetadata);
				for (int i = 0; i < columnSize; i++) {
					String value = convertValue(i + 1,
							resultSet.getObject(i + 1));
					row.append(value);
				}
				this.putRow(row);
				if (rowNumber % 1000 == 0) {
					this.executeNextStep();
				}
			}
			this.setFinish(true);
			super.excuteStep();
		} catch (Exception e) {
			end();
			throw e;
		}
		log.debug("执行步骤成功");
	}

	private String convertValue(int columnIndex, Object value) {
		String result = "";
		if (value instanceof Boolean) {
			boolean bool = (Boolean) value;
			result = bool ? "1" : "0";
		} else {
			result = String.valueOf(value);
		}
		return result;
	}

}
