/*
 * @(#)com.cntest.fxpt.etl.transform.GroovyScriptTransform.java	1.0 2014年5月10日:下午12:22:24
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.transform;

import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.fxpt.etl.domain.RowSet;
import com.cntest.fxpt.etl.module.AbstractTransform;
import com.cntest.fxpt.etl.module.TransformStep;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月10日 下午12:22:24
 * @version 1.0
 */
public class LookUpAdd extends AbstractTransform {
	private static final Logger log = LoggerFactory.getLogger(LookUpAdd.class);
	private ArrayList<String> dataFieldNames = new ArrayList<String>();
	private ArrayList<String> newFieldNames = new ArrayList<String>();
	private ArrayList<String> lookValueNames = new ArrayList<String>();

	public LookUpAdd(TransformStep transformStep) {
		super(transformStep);
	}

	public LookUpAdd append(String lookValueName, String dataFieldName,
			String newFieldName) {
		dataFieldNames.add(dataFieldName);
		newFieldNames.add(newFieldName);
		lookValueNames.add(lookValueName);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.etl.module.AbstractTransform#transform(com.cntest.fxpt
	 * .etl.domain.RowSet)
	 */
	@Override
	public void execute(RowSet row) throws Exception {
		int fieldCount = dataFieldNames.size();
		for (int i = 0; i < fieldCount; i++) {
			String dataValue = getDataValue(row, dataFieldNames.get(i));
			String newValue = findValue(lookValueNames.get(i), dataValue);
			if (newValue == null) {
				getLog().error(
						"没有找到数据:" + dataFieldNames.get(i) + "-->" + dataValue);
				newValue = "";
			}

			row.append(newFieldNames.get(i), newValue);
		}
	}

	private String getDataValue(RowSet row, String filedNameStr) {
		String[] filedNames = filedNameStr.split("\\|");

		StringBuffer result = new StringBuffer();
		for (String filedName : filedNames) {
			result.append(row.getData(filedName));
		}
		return result.toString();
	}

	private String findValue(String lookValueName, String dataValue) {
		Map<String, String> dataSet = (Map<String, String>) this
				.getStepMetadata().getStepParameter().get(lookValueName);

		String result = null;
		if (dataSet != null) {
			result = dataSet.get(dataValue);
		}
		return result;
	}

	@Override
	public void compile() throws Exception {

	}

}
