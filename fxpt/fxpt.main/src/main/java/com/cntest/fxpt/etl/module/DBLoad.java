/*
 * @(#)com.cntest.fxpt.etl.module.DBLoad.java	1.0 2014年5月12日:上午10:36:13
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.module;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.fxpt.anlaysis.service.IExamContext;
import com.cntest.fxpt.anlaysis.service.impl.ExamMgr;
import com.cntest.fxpt.etl.BaseStep;
import com.cntest.fxpt.etl.IEtlContext;
import com.cntest.fxpt.etl.domain.EtlProcessResult;
import com.cntest.fxpt.etl.domain.MappingFiledRelation;
import com.cntest.fxpt.etl.domain.RowSet;
import com.cntest.fxpt.etl.util.IRetrieveValue;
import com.cntest.util.ExceptionHelper;
import com.cntest.web.view.ModelAndViewBuilder;
import com.cntest.web.view.Progress;
import com.cntest.web.view.ProgressListener;

/**
 * <Pre>
 * 辅助处理加载数据到数据库
 * </Pre>
 * 
 * @author 刘海林 2014年5月12日 上午10:36:13
 * @version 1.0
 */
public class DBLoad extends BaseStep implements ProgressListener{
	private static final Logger log = LoggerFactory.getLogger(DBLoad.class);
	private String tableName;
	private Connection conn;
	private DataSource dataScource;
	private ArrayList<MappingFiledRelation> importFileds = new ArrayList<MappingFiledRelation>();
	private PreparedStatement sqlStatement;
	private LinkedHashMap<String, IRetrieveValue> finalData = new LinkedHashMap<String, IRetrieveValue>();
	int dealRowNum = 1;
	int p = 1;
	HttpSession session = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();

	/**
	 * @param name
	 * @param context
	 */
	public DBLoad(String name, IEtlContext context) {
		super(name, context);
	}

	public DBLoad setTableName(String tableName) {
		this.tableName = tableName;
		return this;
	}

	public DBLoad setDataSource(DataSource dataScource) {
		this.dataScource = dataScource;
		return this;
	}

	public DBLoad appendFiled(String mapingFiled, String importFiled) {
		importFileds.add(MappingFiledRelation.create(mapingFiled, importFiled));
		return this;
	}

	public DBLoad appendFinalData(String name, IRetrieveValue retrieveValue) {
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
			while ((row = getRow()) != null) {
				int i = 1;
				for (String filed : finalData.keySet()) {
					sqlStatement.setObject(i++, finalData.get(filed).value());
				}
				for (MappingFiledRelation filed : importFileds) {
					String cellMetadata = filed.getFrom().getName();
					cellMetaValue = row.getData(cellMetadata);
					if(cellMetadata.equals("languageType")){
						sqlStatement.setObject(i++, cellMetaValue==null?"汉语言":cellMetaValue);
					}else{
						sqlStatement.setObject(i++, cellMetaValue);
					}
				}
				dealRowNum = row.getRowNumber();
				session.setAttribute("step","正在初始化第"+dealRowNum+"条数据");
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
		
		p = dealRowNum/1000;
		if(p>15){
			p = 15;
		}
		session.setAttribute("pc",p);
		super.excuteStep();
		
//		log.debug("执行步骤成功");
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

	@Override
	public Progress on(Map<String, String> params) {
		Long examId = Long.parseLong(params.get("examId"));
		IExamContext c = ExamMgr.getInstance().get(examId);
		Progress p = null;
		if (c != null) {
			int totalNum = 4992;
			int cur = dealRowNum;

			if (totalNum == cur && !c.isAllComplate()) {
				cur = cur - 1;
			}

			if (totalNum == cur && c.isAllComplate()) {
				ExamMgr.getInstance().remove(examId);
			}

			totalNum = totalNum <= 0 ? 100 : totalNum;
			cur = cur <= 0 ? 1 : cur;
			p = new Progress(totalNum, cur);
		} else {
			p = new Progress(1, 1);
		}
		return p;
	}
	
}
