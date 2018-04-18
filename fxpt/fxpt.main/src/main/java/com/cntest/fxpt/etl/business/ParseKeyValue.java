/*
 * @(#)com.cntest.fxpt.etl.business.ParseValue.java	1.0 2014年6月5日:下午3:57:53
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.dom4j.Element;

import com.cntest.util.SpringContext;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月5日 下午3:57:53
 * @version 1.0
 */
public class ParseKeyValue implements IParseValue {
	private Element keyValueElement;
	private String name;
	private Object value;
	private String dsName;
	private Map<String, Object> contextData;
	private String valueFiledName;
	private ArrayList<String> keyFiledNames = new ArrayList<String>();

	public ParseKeyValue(Map<String, Object> contextData,
			Element keyValueElement) {
		this.contextData = contextData;
		this.keyValueElement = keyValueElement;
		init();
	}

	private void init() {
		parseName();
		parseDsName();
		parseKeyFileds();
		parseValueFiled();
	}

	private void parseName() {
		name = keyValueElement.attributeValue("name");
	}

	private void parseDsName() {
		dsName = keyValueElement.attributeValue("dsName");
	}

	private void parseKeyFileds() {
		List<Element> keyFiledElements = keyValueElement
				.selectNodes("./keyFileds/filed");

		for (Element e : keyFiledElements) {
			String value = e.attributeValue("value");
			keyFiledNames.add(value);
		}
	}

	private void parseValueFiled() {
		Element valueFieldElements = (Element) keyValueElement
				.selectSingleNode("./valueFiled");
		valueFiledName = valueFieldElements.attributeValue("value");
	}

	public Object parse() throws Exception {

		Element sqlElement = (Element) keyValueElement
				.selectSingleNode("./sql");
		String sql = sqlElement.getTextTrim();
		sql = StringTemplateUtil.transformTemplate(contextData, sql);
		value = getKeyValueData(sql);
		return value;
	}

	protected Map<String, String> getKeyValueData(String sql) throws Exception {
		CacheMgr mgr = CacheMgr.newInstance();
		HashMap<String, String> dataset = mgr.get(sql);
		if (dataset == null) {
			Connection con = null;
			PreparedStatement updateStatement = null;
			ResultSet resultSet = null;
			try {
				con = getDS().getConnection();
				updateStatement = con.prepareStatement(sql);
				resultSet = updateStatement.executeQuery();
				dataset = new HashMap<String, String>();
				while (resultSet.next()) {
					String schoolCodeValue = getKeyValue(resultSet);
					String schoolIdValue = getValueValue(resultSet);
					dataset.put(schoolCodeValue, schoolIdValue);
				}
				mgr.put(sql, dataset);
			} catch (Exception e) {
				throw e;
			} finally {
				if (resultSet != null) {
					resultSet.close();
				}
				if (updateStatement != null) {
					updateStatement.close();
				}
				if (con != null) {
					con.close();
				}
			}
		}
		return dataset;
	}

	private String getKeyValue(ResultSet resultSet) throws Exception {

		StringBuffer result = new StringBuffer();
		for (String fieldName : keyFiledNames) {
			result.append(resultSet.getObject(fieldName));
		}
		return result.toString();
	}

	private String getValueValue(ResultSet resultSet) throws Exception {

		StringBuffer result = new StringBuffer();
		result.append(resultSet.getObject(valueFiledName));
		return result.toString();
	}

	private DataSource getDS() {
		return SpringContext.getBean(dsName);
	}

	public String getName() {
		return name;
	}
}
