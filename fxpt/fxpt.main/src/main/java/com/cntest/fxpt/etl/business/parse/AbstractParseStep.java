/*
 * @(#)com.cntest.fxpt.etl.business.parse.AbstractParseStep.java	1.0 2014年6月5日:上午11:04:57
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business.parse;

import javax.sql.DataSource;

import org.dom4j.Element;

import com.cntest.fxpt.etl.IEtlContext;
import com.cntest.fxpt.etl.IStep;
import com.cntest.fxpt.etl.business.EtlValues;
import com.cntest.util.SpringContext;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月5日 上午11:04:57
 * @version 1.0
 */
public abstract class AbstractParseStep {
	private Element stepElement;
	private EtlValues etlValues;

	private String name;
	private String type;
	private String description;
	private boolean isBegin = false;

	public AbstractParseStep(Element stepElement, EtlValues etlValues) {
		this.stepElement = stepElement;
		this.etlValues = etlValues;
		init();
	}

	public EtlValues getEtlValues() {
		return etlValues;
	}

	protected Element getStepElement() {
		return stepElement;
	}

	private void init() {
		parseName();
		parseType();
		parseIsBegin();
		parseDescription();
	}

	private void parseName() {
		name = stepElement.attributeValue("name");
	}

	private void parseType() {
		type = stepElement.attributeValue("type");
	}

	private void parseIsBegin() {
		String isBeginStr = stepElement.attributeValue("isBegin");
		if (isBeginStr != null && isBeginStr.equalsIgnoreCase("true")) {
			isBegin = true;
		}
	}

	private void parseDescription() {
		description = stepElement.attributeValue("description");
	}

	public boolean isBegin() {
		return isBegin;
	}

	public DataSource getDataSouce(String dsName) {
		DataSource ds = SpringContext.getBean(dsName);
		return ds;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getDescription() {
		return description;
	}

	public abstract IStep parse(IEtlContext context);

}
