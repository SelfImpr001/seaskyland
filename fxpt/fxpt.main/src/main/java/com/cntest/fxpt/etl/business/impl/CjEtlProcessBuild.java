/*
 * @(#)com.cntest.fxpt.etl.business.impl.CjEtlProcessBuild.java	1.0 2014年10月16日:下午4:04:47
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business.impl;

import java.util.HashMap;
import java.util.Map;

import com.cntest.fxpt.bean.CjWebRetrieveResult;
import com.cntest.fxpt.etl.IStep;
import com.cntest.fxpt.etl.impl.DefaultStepHops;
import com.cntest.fxpt.etl.module.DBLoadCj;
import com.cntest.fxpt.etl.module.DBLoadCjWuHou;
import com.cntest.fxpt.etl.module.DBLoadItemCj2;
import com.cntest.fxpt.etl.module.DBLoadItemCj2WuHou;
import com.cntest.fxpt.etl.util.IRetrieveValue;
import com.cntest.fxpt.util.SystemConfig;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月16日 下午4:04:47
 * @version 1.0
 */
public class CjEtlProcessBuild extends DefaultEtlProcessBuild {
	protected Map<String, IRetrieveValue> itemFinalDatas = new HashMap<String, IRetrieveValue>();

	public Map<String, IRetrieveValue> getItemFinalDatas() {
		return itemFinalDatas;
	}

	public void setItemFinalDatas(Map<String, IRetrieveValue> itemFinalDatas) {
		this.itemFinalDatas = itemFinalDatas;
	}

	public IStep build() {
		IStep input = createInput();

		DefaultStepHops hops = new DefaultStepHops(input.getStepMetadata());
		IStep transform = createTransform();
		if (transform != null) {
			hops.addSetp(transform);
			hops = new DefaultStepHops(transform.getStepMetadata());
		}

		IStep mappingField = createMappingField();
		if (mappingField != null) {
			hops.addSetp(mappingField);
			hops = new DefaultStepHops(mappingField.getStepMetadata());
		}

		IStep validate = createValidate();
		if (validate != null) {
			hops.addSetp(validate);
			hops = new DefaultStepHops(validate.getStepMetadata());
		}

		IStep itemCjLoad = createLoadItemCj();
		if (itemCjLoad != null) {
			hops.addSetp(itemCjLoad);
		}
		
		IStep load = createLoad();
		if (validate != null) {
			hops.addSetp(load);
			hops = new DefaultStepHops(load.getStepMetadata());
		}


		return input;
	}

	@Override
	public IStep createLoad() {
		if("wuhou".equals(SystemConfig.newInstance().getValue(
				"area.org.code"))){
			DBLoadCjWuHou dbLoad = new DBLoadCjWuHou("导入成绩", context);
			CjWebRetrieveResult cwrr = (CjWebRetrieveResult) webRetrieveResult;
			dbLoad.setDataSource(webRetrieveResult.getDataScource());
			dbLoad.setTestPaper(cwrr.getTestPaper());
			dbLoad.setItems(cwrr.getItems());
			dbLoad.setAnalysisTestpapers(cwrr.getAnalysisTestpapers());
			dbLoad.setPk("L");
			return dbLoad;
		}else{
			DBLoadCj dbLoad = new DBLoadCj("导入成绩", context);
			CjWebRetrieveResult cwrr = (CjWebRetrieveResult) webRetrieveResult;
			dbLoad.setDataSource(webRetrieveResult.getDataScource());
			dbLoad.setTestPaper(cwrr.getTestPaper());
			dbLoad.setItems(cwrr.getItems());
			dbLoad.setAnalysisTestpapers(cwrr.getAnalysisTestpapers());
			dbLoad.setPk("L");
			return dbLoad;
		}
		
	}

	private IStep createLoadItemCj() {
		CjWebRetrieveResult cwrr = (CjWebRetrieveResult) webRetrieveResult;
		if("wuhou".equals(SystemConfig.newInstance().getValue(
				"area.org.code"))){
			DBLoadItemCj2WuHou dBLoadItemCj = new DBLoadItemCj2WuHou("导入小题成绩", context);
			dBLoadItemCj.setDataSource(cwrr.getDataScource());
			dBLoadItemCj.setAnalysisTestpapers(cwrr.getAnalysisTestpapers());
			dBLoadItemCj.setItems(cwrr.getItems());
			return dBLoadItemCj;
			
		}else{
			DBLoadItemCj2 dBLoadItemCj = new DBLoadItemCj2("导入小题成绩", context);
			dBLoadItemCj.setDataSource(cwrr.getDataScource());
			dBLoadItemCj.setAnalysisTestpapers(cwrr.getAnalysisTestpapers());
			dBLoadItemCj.setItems(cwrr.getItems());
			return dBLoadItemCj;
		}
	}
}
