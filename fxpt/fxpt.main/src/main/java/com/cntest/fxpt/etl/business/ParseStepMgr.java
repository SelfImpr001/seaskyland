/*
 * @(#)com.cntest.fxpt.etl.business.ParseStep.java	1.0 2014年6月5日:上午10:10:50
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.fxpt.etl.EtlFactory;
import com.cntest.fxpt.etl.IEtlContext;
import com.cntest.fxpt.etl.IStep;
import com.cntest.fxpt.etl.IStepHops;
import com.cntest.fxpt.etl.business.parse.AbstractParseStep;
import com.cntest.fxpt.etl.business.parse.DBInputParse;
import com.cntest.fxpt.etl.business.parse.DBLoadItemCjParse;
import com.cntest.fxpt.etl.business.parse.DBLoadParse;
import com.cntest.fxpt.etl.business.parse.TransformStepParse;
import com.cntest.fxpt.util.SystemConfig;

/**
 * <Pre>
 * 解析一个步骤
 * </Pre>
 * 
 * @author 刘海林 2014年6月5日 上午10:10:50
 * @version 1.0
 */
public class ParseStepMgr {
	private static final Logger log = LoggerFactory
			.getLogger(ParseStepMgr.class);
	private Element root;
	private HashMap<String, IStep> stepMap = new HashMap<String, IStep>();
	private IEtlContext context = EtlFactory.createContext();
	private EtlValues etlValues = new EtlValues();
	private Map<String, Object> dataModel = new HashMap<String, Object>();
	private IStep beginStep = null;

	public ParseStepMgr(String fileName) throws Exception {
		String rootPath = getRootDir() + fileName;
		SAXReader reader = new SAXReader();
		Document doc = reader.read(new URL(rootPath));
		root = doc.getRootElement();
	}

	private String getRootDir() {
		String rootPath = this.getClass().getResource("").toString() + "xml/";
		String db = SystemConfig.newInstance().getValue("db", "mysql");
		if (db.equalsIgnoreCase("sqlserver2005")) {
			rootPath += "sqlserver2005/";
		}

		return rootPath;
	}

	public IStep parse() throws Exception {
		parseValues();
		parseKeyValue();
		parseSteps();
		parseHops();

		return beginStep;
	}

	private void parseHops() {
		String xpath = "./hop";
		List<Element> hopElement = root.selectNodes(xpath);
		for (Element e : hopElement) {
			String ref = e.attributeValue("ref");
			IStep step = stepMap.get(ref);
			IStepHops hop = EtlFactory.createStepHops(step.getStepMetadata());

			List<Element> addSteps = e.selectNodes("./add");
			for (Element addStep : addSteps) {
				String addStepName = addStep.attributeValue("ref");
				step = stepMap.get(addStepName);
				hop.addSetp(step);
			}
		}
	}

	private void parseValues() throws Exception {
		String xpath = "./values/value";
		List<Element> valuesElement = root.selectNodes(xpath);
		if (valuesElement != null) {
			for (Element e : valuesElement) {
				ParseValue pv = new ParseValue(dataModel, e);
				Object value = pv.parse();
				etlValues.appendValue(pv.getName(), value);
			}
		}
	}

	private void parseKeyValue() throws Exception {
		String xpath = "./values/keyValue";
		List<Element> keyValuesElement = root.selectNodes(xpath);
		if (keyValuesElement != null) {
			for (Element e : keyValuesElement) {
				ParseKeyValue pkv = new ParseKeyValue(dataModel, e);
				Object value = pkv.parse();
				etlValues.appendValue(pkv.getName(), value);
			}
		}
	}

	private void parseSteps() {
		String xpath = "./step";
		List<Element> stepElements = root.selectNodes(xpath);
		for (Element stepElement : stepElements) {
			String stepType = stepElement.attributeValue("type");

			AbstractParseStep p = null;
			if (stepType.equalsIgnoreCase("DBInput")) {
				p = new DBInputParse(stepElement, etlValues);
			} else if (stepType.equalsIgnoreCase("DBLoad")) {
				p = new DBLoadParse(stepElement, etlValues);
			} else if (stepType.equalsIgnoreCase("TransformStep")) {
				p = new TransformStepParse(stepElement, etlValues);
			} else if (stepType.equalsIgnoreCase("DBLoadItemCj")) {
				p = new DBLoadItemCjParse(stepElement, etlValues);
			}
			IStep step = p.parse(context);
			stepMap.put(p.getName(), step);

			if (p.isBegin() && beginStep == null) {
				this.beginStep = step;
			}
		}
	}

	public void appendDataModel(String name, Object value) {
		dataModel.put(name, value);
	}

	public Map<String, Object> getDataModel() {
		return dataModel;
	}

}
