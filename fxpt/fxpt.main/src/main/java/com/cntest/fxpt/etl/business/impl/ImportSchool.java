/*
 * @(#)com.cntest.fxpt.etl.business.impl.ImportSchool.java	1.0 2014年10月10日:上午10:45:23
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.bean.EtlLog;
import com.cntest.fxpt.bean.WebRetrieveResult;
import com.cntest.fxpt.domain.Education;
import com.cntest.fxpt.domain.School;
import com.cntest.fxpt.etl.EtlExecutor;
import com.cntest.fxpt.etl.business.IEtlExecuteor;
import com.cntest.fxpt.etl.domain.EtlProcessResult;
import com.cntest.fxpt.etl.util.KeyContainer;
import com.cntest.fxpt.service.IEducationService;
import com.cntest.fxpt.service.ISchoolService;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月10日 上午10:45:23
 * @version 1.0
 */
@Service("School.IEtlExecuteor")
public class ImportSchool extends BaseEtlExecutor implements IEtlExecuteor {
	@Autowired(required = false)
	@Qualifier("ISchoolService")
	private ISchoolService schoolService;

	@Autowired(required = false)
	@Qualifier("IEducationService")
	private IEducationService educationService;

	@Override
	public EtlProcessResult execute(WebRetrieveResult wrr) throws Exception {
		EtlExecutor executor = null;
		Exception exception = null;
		try {
			DefaultEtlProcessBuild build = initBuilder(wrr);
			build.setValidateParame("repeatUtil", new KeyContainer());
			build.setValidateParame("schools", schools());
			build.setValidateParame("educations", educations());
			executor = exec(build);
		} catch (Exception e) {
			exception = e;
		} finally {
			deleteFile(wrr);
		}

		EtlProcessResult result = createMessage(executor, exception);
		EtlLog etlLog = createAndSaveEtlLog(wrr.getUserName(), wrr.getExamId(),
				"", wrr.getSchemeType(), result);

		return result;
	}

	private Map<String, Boolean> schools() {
		List<School> schools = schoolService.list();
		HashMap<String, Boolean> result = new HashMap<String, Boolean>();
		for (School school : schools) {
			result.put(school.getCode(), true);
		}
		return result;
	}

	private Map<String, Boolean> educations() {
		List<Education> educations = educationService.list(3);
		HashMap<String, Boolean> result = new HashMap<String, Boolean>();
		for (Education education : educations) {
			result.put(education.getCode(), true);
		}
		return result;
	}

	@Override
	public boolean executeClean(WebRetrieveResult webRetrieveResult) {
		// TODO Auto-generated method stub
		return false;
	}

}
