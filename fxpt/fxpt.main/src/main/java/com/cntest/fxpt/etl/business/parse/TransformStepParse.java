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
import com.cntest.fxpt.etl.module.TransformStep;
import com.cntest.fxpt.etl.transform.LookUpAdd;
import com.cntest.fxpt.etl.util.IRetrieveValue;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月5日 上午11:04:04
 * @version 1.0
 */
public class TransformStepParse extends AbstractParseStep {

	public TransformStepParse(Element stepElement, EtlValues etlValues) {
		super(stepElement, etlValues);
	}

	public IStep parse(IEtlContext context) {
		TransformStep transformStep = new TransformStep(getDescription(),
				context);
		setStepParameter(transformStep);
		setLookUpAdd(transformStep);
		return transformStep;
	}

	private void setStepParameter(TransformStep transformStep) {
		List<Element> parametersElements = getStepElement().selectNodes(
				"./parameters/parameter");
		if (parametersElements != null) {
			for (Element e : parametersElements) {
				String name = e.attributeValue("name");
				String refValue = e.attributeValue("refValue");
				transformStep.setSetpParameter(name,
						getEtlValues().get(refValue));
			}
		}
	}

	private void setLookUpAdd(TransformStep transformStep) {
		List<Element> lookUpAddElements = getStepElement().selectNodes(
				"./LookUpAdd/append");

		if (lookUpAddElements != null) {
			LookUpAdd lookUpAdd = new LookUpAdd(transformStep);
			for (Element e : lookUpAddElements) {
				String lookUpDataset = e.attributeValue("lookUpDataset");
				String dataFieldName = e.attributeValue("dataFieldName");
				String newFieldName = e.attributeValue("newFieldName");
				lookUpAdd.append(lookUpDataset, dataFieldName, newFieldName);
			}
		}
	}

}
