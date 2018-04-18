/*
 * @(#)com.cntest.fxpt.etl.impl.DefaultEtlProcessBuild.java	1.0 2014年5月19日:下午5:30:50
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cntest.fxpt.bean.DataTransform;
import com.cntest.fxpt.bean.WebFieldRelation;
import com.cntest.fxpt.bean.WebRetrieveResult;
import com.cntest.fxpt.etl.BaseStep;
import com.cntest.fxpt.etl.EtlFactory;
import com.cntest.fxpt.etl.EtlProcessBuild;
import com.cntest.fxpt.etl.IEtlContext;
import com.cntest.fxpt.etl.IStep;
import com.cntest.fxpt.etl.module.DBFInput;
import com.cntest.fxpt.etl.module.DBLoad;
import com.cntest.fxpt.etl.module.DBLoadOrg;
import com.cntest.fxpt.etl.module.ExcelInput;
import com.cntest.fxpt.etl.module.ExcelXLSXInput;
import com.cntest.fxpt.etl.module.MapinngFiled;
import com.cntest.fxpt.etl.module.TransformStep;
import com.cntest.fxpt.etl.transform.GroovyScriptTransform;
import com.cntest.fxpt.etl.util.IRetrieveValue;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月19日 下午5:30:50
 * @version 1.0
 */
public class DefaultEtlProcessBuild extends EtlProcessBuild {
	protected WebRetrieveResult webRetrieveResult;
	protected List<DataTransform> transforms;
	protected List<DataTransform> validates;
	protected Map<String, IRetrieveValue> finalDatas = new HashMap<String, IRetrieveValue>();
	protected HashMap<String, Object> validateParame = new HashMap<String, Object>();

	protected IEtlContext context = EtlFactory.createContext();

	public void setFinalDatas(Map<String, IRetrieveValue> finalDatas) {
		this.finalDatas = finalDatas;
	}

	public void setValidateParame(String key, Object value) {
		validateParame.put(key, value);
	}

	public void setWebRetrieveResult(WebRetrieveResult webRetrieveResult) {
		this.webRetrieveResult = webRetrieveResult;
	}

	public void setTransforms(List<DataTransform> transforms) {
		this.transforms = transforms;
	}

	public void setValidates(List<DataTransform> validates) {
		this.validates = validates;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.etl.EtlProcessBuild#createInput()
	 */
	@Override
	public IStep createInput() {
		BaseStep step;
		if (".xls".equalsIgnoreCase(webRetrieveResult.getSuffix())) {
			step = createXLS();
		} else if (".xlsx".equalsIgnoreCase(webRetrieveResult.getSuffix())) {
			step = createXLSX();
		} else {
			step = createDBF();
		}
		step.setPk("E");
		return step;
	}

	private BaseStep createXLSX() {
		ExcelXLSXInput step = new ExcelXLSXInput("抽取数据", context);
		step.setFilePath(webRetrieveResult.getFileDir()
				+ webRetrieveResult.getFileName());
		step.setSheetName(webRetrieveResult.getSheetName());
		step.setHeadRowIdx(webRetrieveResult.getHeadRowIndex()+1);
		return step;
	}

	private BaseStep createXLS() {
		ExcelInput step = new ExcelInput("抽取数据", context);
		step.setFilePath(webRetrieveResult.getFileDir()
				+ webRetrieveResult.getFileName());
		step.setSheetName(webRetrieveResult.getSheetName());
		step.setHeadRowIdx(webRetrieveResult.getHeadRowIndex()+1);
		return step;
	}

	private BaseStep createDBF() {
		DBFInput step = new DBFInput("抽取数据", context);
		step.setFilePath(webRetrieveResult.getFileDir()
				+ webRetrieveResult.getFileName());
		return step;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.etl.EtlProcessBuild#createTransform()
	 */
	@Override
	public IStep createTransform() {
		TransformStep transformStep = new TransformStep("转换数据", context);
		if (transforms != null && !transforms.isEmpty()) {
			for (DataTransform transform : transforms) {
				GroovyScriptTransform script = new GroovyScriptTransform(
						transform.getContent(), transformStep);
			}
		}

		transformStep.setSetpParameter("splitOmrStrField",
				webRetrieveResult.getOmrStr());
		transformStep.setSetpParameter("splitScoreStrField",
				webRetrieveResult.getScoreStr());
		return transformStep;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.etl.EtlProcessBuild#createMappingField()
	 */
	@Override
	public IStep createMappingField() {
		MapinngFiled mapinngFiled = new MapinngFiled("映射字段", context);

		for (WebFieldRelation webFieldRelation : webRetrieveResult
				.getWebFieldRelation()) {
			mapinngFiled.appendMapingFieldRalation(webFieldRelation.getFrom(),
					webFieldRelation.getTo());
		}
		return mapinngFiled;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.etl.EtlProcessBuild#createValidate()
	 */
	@Override
	public IStep createValidate() {
		TransformStep transformStep = new TransformStep("验证数据", context);
		if (validates != null && !validates.isEmpty()) {
			for (DataTransform transform : validates) {
				GroovyScriptTransform script = new GroovyScriptTransform(
						transform.getContent(), transformStep);
			}
		}
		for (String key : validateParame.keySet()) {
			transformStep.setSetpParameter(key, validateParame.get(key));
		}

		HashMap<String, Boolean> importFields = new HashMap<String, Boolean>();
		for (WebFieldRelation webFieldRelation : webRetrieveResult
				.getWebFieldRelation()) {
			importFields.put(webFieldRelation.getTo(), true);
		}
		transformStep.setSetpParameter("importFields", importFields);

		return transformStep;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.etl.EtlProcessBuild#createLoad()
	 */
	@Override
	public IStep createLoad() {
		if(webRetrieveResult.getSchemeType()==11) {
			DBLoadOrg dbLoad = new DBLoadOrg("加载数据到数据库中", context);
			dbLoad.setTableName(webRetrieveResult.getTableName());
			dbLoad.setDataSource(webRetrieveResult.getDataScource());
	
			for (String fieldName : finalDatas.keySet()) {
				dbLoad.appendFinalData(fieldName, finalDatas.get(fieldName));
			}
	
			for (WebFieldRelation webFieldRelation : webRetrieveResult
					.getWebFieldRelation()) {
				dbLoad.appendFiled(webFieldRelation.getTo(),
						webFieldRelation.getTo());
			}
			dbLoad.setPk("L");
			return dbLoad;
		}else {
			DBLoad dbLoad = new DBLoad("加载数据到数据库中", context);
			int checkLanguageType = 0;
			dbLoad.setTableName(webRetrieveResult.getTableName());
			dbLoad.setDataSource(webRetrieveResult.getDataScource());

			for (String fieldName : finalDatas.keySet()) {
				dbLoad.appendFinalData(fieldName, finalDatas.get(fieldName));
			}
			for (WebFieldRelation webFieldRelation : webRetrieveResult
					.getWebFieldRelation()) {
				dbLoad.appendFiled(webFieldRelation.getTo(),
						webFieldRelation.getTo());
				if(webRetrieveResult.getSchemeType()==1){
					if(webFieldRelation.getTo().equals("languageType")){
						checkLanguageType = 1;
					}
				}
			}
			if(webRetrieveResult.getSchemeType()==1){
				if(checkLanguageType == 0){
					dbLoad.appendFiled("languageType","languageType");
				}
			}
			dbLoad.setPk("L");
			return dbLoad;
		}
	}

	@Override
	public IEtlContext getContext() {
		return context;
	}

}
