/*
 * @(#)com.cntest.fxpt.etl.business.parse.DBInputParse.java	1.0 2014年6月5日:上午11:04:04
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business.parse;

import org.dom4j.Element;

import com.cntest.fxpt.etl.IEtlContext;
import com.cntest.fxpt.etl.IStep;
import com.cntest.fxpt.etl.business.EtlValues;
import com.cntest.fxpt.etl.module.DBInput;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月5日 上午11:04:04
 * @version 1.0
 */
public class DBInputParse extends AbstractParseStep {
	private String dsName;
	private String sql;

	public DBInputParse(Element stepElement,EtlValues value) {
		super(stepElement,value);
		init();
	}

	private void init() {
		parseDataSource();
		parseSQL();
	}

	public void parseDataSource() {
		Element dsElement = (Element) getStepElement().selectSingleNode(
				"./dataScource");
		dsName = dsElement.attributeValue("springRef");
	}

	public void parseSQL() {
		Element sqlElement = (Element) getStepElement().selectSingleNode(
				"./sql");
		sql = sqlElement.getText();
	}

	public IStep parse(IEtlContext context) {
		DBInput dBInput = new DBInput(getDescription(), context);
		dBInput.setDataScource(getDataSouce(dsName));
		dBInput.setSql(getSQL());
		return dBInput;
	}

	public String getSQL() {
		return sql;
	}

}
