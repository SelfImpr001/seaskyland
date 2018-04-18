/*
 * @(#)com.cntest.fxpt.etl.module.DBLoad.java	1.0 2014年5月12日:上午10:36:13
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.module;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.fxpt.domain.Item;
import com.cntest.fxpt.etl.BaseStep;
import com.cntest.fxpt.etl.IEtlContext;
import com.cntest.fxpt.etl.domain.MappingFiledRelation;
import com.cntest.fxpt.etl.domain.RowSet;
import com.cntest.fxpt.etl.util.IRetrieveValue;

/**
 * <Pre>
 * 辅助处理加载数据到数据库
 * </Pre>
 * 
 * @author 刘海林 2014年5月12日 上午10:36:13
 * @version 1.0
 */
public class DBLoadItemCj extends BaseStep {
	private static final Logger log = LoggerFactory
			.getLogger(DBLoadItemCj.class);
	private String tableName;
	private Connection conn;
	private DataSource dataScource;
	private ArrayList<MappingFiledRelation> importFileds = new ArrayList<MappingFiledRelation>();
	private PreparedStatement sqlStatement;
	private LinkedHashMap<String, IRetrieveValue> finalData = new LinkedHashMap<String, IRetrieveValue>();
	private List<Item> items = new ArrayList<Item>();

	/**
	 * @param name
	 * @param context
	 */
	public DBLoadItemCj(String name, IEtlContext context) {
		super(name, context);
	}

	public DBLoadItemCj setTableName(String tableName) {
		this.tableName = tableName;
		return this;
	}

	public DBLoadItemCj setDataSource(DataSource dataScource) {
		this.dataScource = dataScource;
		return this;
	}

	public DBLoadItemCj appendFiled(String mapingFiled, String importFiled) {
		importFileds.add(MappingFiledRelation.create(mapingFiled, importFiled));
		return this;
	}

	public DBLoadItemCj appendFinalData(String name,
			IRetrieveValue retrieveValue) {
		finalData.put(name, retrieveValue);
		return this;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.etl.IStep#begin()
	 */
	@Override
	public void begin() throws Exception {
		log.debug("初始化步骤" + getName());
		StringBuffer fields = new StringBuffer();
		StringBuffer values = new StringBuffer();

		for (String filed : finalData.keySet()) {
			fields.append(filed + ",");
			values.append("?,");
		}

		for (MappingFiledRelation filed : importFileds) {
			fields.append(filed.getTo().getName() + ",");
			values.append("?,");
		}

		fields.deleteCharAt(fields.length() - 1);
		values.deleteCharAt(values.length() - 1);

		StringBuffer sql = new StringBuffer();
		sql.append("insert into ");
		sql.append(tableName);
		sql.append("(");
		sql.append(fields.toString());
		sql.append(")");
		sql.append(" values(");
		sql.append(values.toString());
		sql.append(")");

		try{
			conn = dataScource.getConnection();
			conn.setAutoCommit(false);

			sqlStatement = conn.prepareStatement(sql.toString());
		}catch(Exception e){
			end();
			throw e;
		}
		
		log.debug("sql:" + sql.toString());
		log.debug("初始化成功");

	}

	@Override
	public void excuteStep() throws Exception {
		log.debug("执行步骤" + getName());
		if (!items.isEmpty()) {
			try {
				int itemsSize = items.size();
				RowSet row = null;
				int rowNum = 1;
				while ((row = getRow()) != null) {
					dealWithItem(row);
					this.putRow(row);
					if ((rowNum * itemsSize) >= 1000) {
						try {
							sqlStatement.executeBatch();
						} catch (Exception e) {
							end();
							throw new Exception("执行步骤" + getName()
									+ "提交数据到数据库的时候出错", e);
						}
					}
					rowNum++;
				}
				sqlStatement.executeBatch();
			} catch (Exception e) {
				end();
				throw new Exception("执行步骤" + getName() + "提交数据到数据库的时候出错", e);
			}
			super.excuteStep();
		}
		log.debug("执行步骤成功");
	}

	private void dealWithItem(RowSet row) throws Exception {
		for (Item item : items) {
			int i = 1;
			for (String filed : finalData.keySet()) {
				sqlStatement.setObject(i++, finalData.get(filed).value());
			}
			for (MappingFiledRelation filed : importFileds) {
				String cellMetadata = filed.getFrom().getName();
				Object value = null;
				if ("itemScore".equals(cellMetadata)) {
					value = row.getData("score" + item.getSortNum());
				} else if ("itemSelOption".equals(cellMetadata)) {
					if (item.getOptionType() != 0) {
						value = row.getData("sel" + item.getSortNum());
					} else {
						value = "";
					}
				} else if ("itemSubjectId".equals(cellMetadata)) {
					value = item.getSubject().getId();
				} else if ("itesmId".equals(cellMetadata)) {
					value = item.getId();
				} else {
					value = row.getData(cellMetadata);
				}
				sqlStatement.setObject(i++, value);
			}
			sqlStatement.addBatch();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.etl.IStep#end()
	 */
	@Override
	public void end() throws Exception {
		if (sqlStatement != null) {
			sqlStatement.close();
			sqlStatement = null;
		}
		if (conn != null) {
			conn.commit();
			conn.setAutoCommit(true);
			conn.close();
			conn = null;
		}

	}

}
