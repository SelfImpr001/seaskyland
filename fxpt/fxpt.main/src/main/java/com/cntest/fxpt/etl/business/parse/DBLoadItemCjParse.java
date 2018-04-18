/*
 * @(#)com.cntest.fxpt.etl.business.parse.DBInputParse.java	1.0 2014年6月5日:上午11:04:04
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business.parse;

import java.util.List;

import org.dom4j.Element;

import com.cntest.fxpt.etl.IEtlContext;
import com.cntest.fxpt.etl.IStep;
import com.cntest.fxpt.etl.business.EtlValues;
import com.cntest.fxpt.etl.module.DBLoad;
import com.cntest.fxpt.etl.module.DBLoadItemCj;
import com.cntest.fxpt.etl.util.IRetrieveValue;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月5日 上午11:04:04
 * @version 1.0
 */
public class DBLoadItemCjParse extends AbstractParseStep {
	private String dsName;
	private String tableName;

	// private ArrayList<String[]> mappingFileds = new ArrayList<String[]>();

	public DBLoadItemCjParse(Element stepElement, EtlValues etlValues) {
		super(stepElement, etlValues);
		init();
	}

	private void init() {
		parseDataSource();
		parseTable();
	}

	public void parseDataSource() {
		Element dsElement = (Element) getStepElement().selectSingleNode(
				"./dataScource");
		dsName = dsElement.attributeValue("springRef");
	}

	public void parseTable() {
		Element tableElement = (Element) getStepElement().selectSingleNode(
				"./table");
		tableName = tableElement.attributeValue("value");
	}

	public IStep parse(IEtlContext context) {
		DBLoadItemCj dBLoadItemCj = new DBLoadItemCj(getDescription(), context);
		dBLoadItemCj.setDataSource(getDataSouce(dsName));
		dBLoadItemCj.setTableName(tableName);

		mappingField(dBLoadItemCj);
		finalDataField(dBLoadItemCj);

		return dBLoadItemCj;
	}

	private void mappingField(DBLoadItemCj dBLoadItemCj) {
		List<Element> mapFileds = getStepElement().selectNodes("./mapField");
		for (Element e : mapFileds) {
			String from = e.attributeValue("from");
			String to = e.attributeValue("to");
			dBLoadItemCj.appendFiled(from, to);
		}
	}

	private void finalDataField(DBLoadItemCj dBLoadItemCj) {
		List<Element> finalDataFields = getStepElement().selectNodes(
				"./finalData");
		if (finalDataFields != null) {
			for (Element e : finalDataFields) {

				String refValue = e.attributeValue("refValue");
				Integer value = getEtlValues().get(refValue);

				IRetrieveValue rv = createRetrieveValue(isAutoIncrement(e),
						value);
				String name = e.attributeValue("name");
				dBLoadItemCj.appendFinalData(name, rv);

			}
		}
	}

	private boolean isAutoIncrement(Element e) {
		String autoIncrement = e.attributeValue("autoIncrement");
		if (autoIncrement.equalsIgnoreCase("true")) {
			return true;
		} else {
			return false;
		}

	}

	private IRetrieveValue createRetrieveValue(boolean autoIncrement,
			final Integer value) {

		if (autoIncrement) {
			return new IRetrieveValue<Integer>() {
				private int id = value+1;

				@Override
				public Integer value() {
					return id++;
				}

			};
		} else {
			return new IRetrieveValue<Integer>() {
				private int id = value;

				@Override
				public Integer value() {
					return id;
				}

			};
		}

	}

}
