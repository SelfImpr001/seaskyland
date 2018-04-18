/*
 * @(#)com.cntest.fxpt.etl.business.impl.ImportStudentBase.java	1.0 2014年10月16日:下午2:38:31
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business.impl;

import java.util.HashMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.bean.WebRetrieveResult;
import com.cntest.fxpt.etl.EtlExecutor;
import com.cntest.fxpt.etl.business.IEtlExecuteor;
import com.cntest.fxpt.etl.domain.EtlProcessResult;
import com.cntest.fxpt.etl.util.IRetrieveValue;
import com.cntest.fxpt.service.IStudentBaseService;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2014年10月16日 下午2:38:31
 * @version 1.0
 */
@Service("StudentBase.IEtlExecuteor")
public class ImportStudentBase extends BaseEtlExecutor implements IEtlExecuteor {
	@Autowired(required = false)
	@Qualifier("IStudentBaseService")
	private IStudentBaseService studentBaseService;

	@Override
	public EtlProcessResult execute(WebRetrieveResult wrr) throws Exception {
		EtlExecutor executor = null;
		Exception exception = null;
		try {
			DefaultEtlProcessBuild build = initBuilder(wrr);
			build.setValidateParame("studentBaseService", studentBaseService);
			setFiled(build, wrr);
			executor = exec(build);
		} catch (Exception e) {
			exception = e;
		} finally {
			deleteFile(wrr);
		}

		EtlProcessResult result = createMessage(executor, exception);
		createAndSaveEtlLog(wrr.getUserName(), wrr.getExamId(), "",
				wrr.getSchemeType(), result);
		return result;
	}

	public void setFiled(DefaultEtlProcessBuild build,
			final WebRetrieveResult webRetrieveResult) {
		HashMap<String, IRetrieveValue> finalDatas = new HashMap<String, IRetrieveValue>();
		finalDatas.put("id", new IRetrieveValue<Integer>() {
			private int maxId = studentBaseService.getMaxId();

			@Override
			public Integer value() {
				return ++maxId;
			}

		});
		finalDatas.put("guid", new IRetrieveValue<String>() {
			String guid = UUID.randomUUID().toString();

			public String value() {
				return guid;
			}
		});
		build.setFinalDatas(finalDatas);
	}

	@Override
	public boolean executeClean(WebRetrieveResult webRetrieveResult) {
		// TODO Auto-generated method stub
		return true;
	}

}
