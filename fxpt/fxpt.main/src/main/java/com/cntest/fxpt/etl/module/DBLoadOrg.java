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

import com.cntest.fxpt.etl.BaseStep;
import com.cntest.fxpt.etl.IEtlContext;
import com.cntest.fxpt.etl.domain.MappingFiledRelation;
import com.cntest.fxpt.etl.domain.RowSet;
import com.cntest.fxpt.etl.util.IRetrieveValue;
import com.cntest.util.ExceptionHelper;

/**
 * <Pre>
 * 辅助处理加载数据到数据库
 * </Pre>
 * 
 * @author 刘海林 2014年5月12日 上午10:36:13
 * @version 1.0
 */
public class DBLoadOrg extends BaseStep {
	private static final Logger log = LoggerFactory.getLogger(DBLoadOrg.class);
	private String tableName;
	private Connection conn;
	private DataSource dataScource;
	private ArrayList<MappingFiledRelation> importFileds = new ArrayList<MappingFiledRelation>();
	private PreparedStatement sqlStatement;
	private LinkedHashMap<String, IRetrieveValue> finalData = new LinkedHashMap<String, IRetrieveValue>();

	/**
	 * @param name
	 * @param context
	 */
	public DBLoadOrg(String name, IEtlContext context) {
		super(name, context);
	}

	public DBLoadOrg setTableName(String tableName) {
		this.tableName = tableName;
		return this;
	}

	public DBLoadOrg setDataSource(DataSource dataScource) {
		this.dataScource = dataScource;
		return this;
	}

	public DBLoadOrg appendFiled(String mapingFiled, String importFiled) {
		importFileds.add(MappingFiledRelation.create(mapingFiled, importFiled));
		return this;
	}

	public DBLoadOrg appendFinalData(String name, IRetrieveValue retrieveValue) {
		finalData.put(name, retrieveValue);
		return this;
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
		
		try {
			String cellMetaValue = null;
			RowSet row = null;
			String orgCodes=",";
			while ((row = getRow()) != null) {
				int i = 1;
				for (String filed : finalData.keySet()) {
					sqlStatement.setObject(i++, finalData.get(filed).value());
				}
				for (MappingFiledRelation filed : importFileds) {
					String cellMetadata = filed.getFrom().getName();
					cellMetaValue = row.getData(cellMetadata);
					
					if(cellMetaValue=="" || "".equals(cellMetaValue)){
						//上级组织代码，学校类型，学校学段可以为空，其他的不能为空
						if("p_id,schoolTypeId,schoolSegmentId".indexOf(cellMetadata)>-1) {
							sqlStatement.setObject(i++, null);
						}else {    
							String msg ="";
							if(cellMetadata.equalsIgnoreCase("org_id") || cellMetadata.equalsIgnoreCase("org_code")){
								msg="组织代码不能为空！";
							}else if(cellMetadata.equalsIgnoreCase("org_name")) {
								msg="组织名称不能为空！";
							}else if(cellMetadata.equalsIgnoreCase("org_type")){
								msg="所属机构不能为空！";
							}
							//此格式不能改（必须是”ErrorMessage+----你要提示的内容----+”ErrorMessage““）
							throw new Exception("ErrorMessage"+"字段"+msg + "ErrorMessage");
						}
					}else{
						//验证org_code的唯一性
						if(cellMetadata.equalsIgnoreCase("org_code")) {
							if(orgCodes.indexOf(","+cellMetaValue+",")==-1) {
								orgCodes+=cellMetaValue+",";
							}else {
								//此格式不能改（必须是”ErrorMessage+----你要提示的内容----+”ErrorMessage““）
								throw new Exception("ErrorMessage"+"组织代码："+cellMetaValue + "重复ErrorMessage");
							}
						}
						sqlStatement.setObject(i++, cellMetaValue);
					}
				}
				this.putRow(row);
				sqlStatement.addBatch();
			}
			sqlStatement.executeBatch();
		} catch (Exception e) {
			end();
			log.error(ExceptionHelper.trace2String(e));
			throw new Exception("执行步骤" + getName() + "提交数据到数据库的时候出错:"
					+ ExceptionHelper.trace2String(e));
		}
		super.excuteStep();
		log.debug("执行步骤成功");
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
