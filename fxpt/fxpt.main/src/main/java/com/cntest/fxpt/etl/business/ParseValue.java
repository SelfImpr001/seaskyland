/*
 * @(#)com.cntest.fxpt.etl.business.ParseValue.java	1.0 2014年6月5日:下午3:57:53
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.dom4j.Element;

import com.cntest.util.SpringContext;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月5日 下午3:57:53
 * @version 1.0
 */
public class ParseValue implements IParseValue {
	private Element valueElement;
	private String name;
	private Object value;
	private String dsName;
	private Map<String, Object> contextData;

	public ParseValue(Map<String, Object> contextData, Element valueElement) {
		this.contextData = contextData;
		this.valueElement = valueElement;
		init();
	}

	private void init() {
		parseName();
		parseDsName();
	}

	private void parseName() {
		name = valueElement.attributeValue("name");
	}

	private void parseDsName() {
		dsName = valueElement.attributeValue("dsName");
	}

	public Object parse() throws Exception {
		Element sqlElement = (Element) valueElement.selectSingleNode("./sql");
		if (sqlElement != null) {

			String sql = sqlElement.getTextTrim();
			sql = StringTemplateUtil.transformTemplate(contextData, sql);
			value = getValue(sql);
		} else {
			String valueName = valueElement.attributeValue("value");
			value = PropertyUtils.getProperty(contextData, valueName);
		}

		return value;
	}

	private Object getValue(String sql) throws Exception {
		CacheMgr mgr = CacheMgr.newInstance();
		Integer maxId = mgr.get(sql);
		if (maxId == null) {
			Connection con = null;
			PreparedStatement updateStatement = null;
			ResultSet resultSet = null;
			try {
				con = getDS().getConnection();
				updateStatement = con.prepareStatement(sql);
				resultSet = updateStatement.executeQuery();
				if (resultSet.next()) {
					maxId = resultSet.getInt(1);
				}
				mgr.put(sql, maxId);
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
		return maxId;
	}

	private DataSource getDS() {
		return SpringContext.getBean(dsName);
	}

	public String getName() {
		return name;
	}

}
