/*
 * @(#)com.cntest.fxpt.etl.business.impl.ImportStudent.java	1.0 2014年10月10日:上午9:44:09
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.bean.WebRetrieveResult;
import com.cntest.fxpt.domain.School;
import com.cntest.fxpt.etl.EtlExecutor;
import com.cntest.fxpt.etl.business.IEtlExecuteor;
import com.cntest.fxpt.etl.domain.EtlProcessResult;
import com.cntest.fxpt.etl.util.IRetrieveValue;
import com.cntest.fxpt.etl.util.KeyContainer;
import com.cntest.fxpt.service.IExamService;
import com.cntest.fxpt.service.IExamStudentService;
import com.cntest.fxpt.service.ISchoolService;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月10日 上午9:44:09
 * @version 1.0
 */
@Service("Student.IEtlExecuteor")
public class ImportStudent extends BaseEtlExecutor implements IEtlExecuteor {
	@Autowired(required = false)
	@Qualifier("ISchoolService")
	private ISchoolService schoolService;

	@Autowired(required = false)
	@Qualifier("IExamStudentService")
	private IExamStudentService examStudentService;

	@Autowired(required = false)
	@Qualifier("IExamService")
	private IExamService examService;

	@Override
	public EtlProcessResult execute(WebRetrieveResult wrr) throws Exception {
		EtlExecutor executor = null;
		Exception exception = null;
		List<String> logs=new ArrayList<String>();
		try {
			DefaultEtlProcessBuild build = initBuilder(wrr);
			//导入之前删除零时表所有信息
			examStudentService.deleteTmpExamStudent(null);
			setValidateParames(build, wrr);
			setFinalData(build, wrr);
			executor = exec(build);
		} catch (Exception e) {
			exception = e;
		} 

		/** 获取错误的信息 */
 		EtlProcessResult result = createMessage(executor, exception);
 		result.setValidateError(false);
		if (result.isHasError()) {
			deleteFile(wrr);
			examStudentService.importFail(wrr.getExamId());
		} else {
			//重复性校验
			if(wrr.isValidate()){
				List<String> list=new ArrayList<>();
				list.add("以下是重复的学生准考证号");
				list.addAll(1,examStudentService.studentValidate(wrr));
				if(list.size()>1){
					//重复数据做提示
					result.setHasError(true);
					result.setHasLog(true);
					String mes = "";
					if(list.size()>1){
						mes="总共有"+(list.size()-1)+"条数据重复,查看详细结果<a href='#' class='logFileDown'>下载</a>日志文件。";
					}
					result.setMessage(mes);
					logs.add(0, mes);
					result.setLogs(list);
					result.setValidateError(true);
				}
			}else{
				examStudentService.importSuccess(wrr);
			}
			
		}
		/** 保存- 错误日记信息*/
		createAndSaveEtlLog(wrr.getUserName(), wrr.getExamId(), "导入学生信息",
				wrr.getSchemeType(), result);
		return result;
	}

	private void setFinalData(DefaultEtlProcessBuild build,
			final WebRetrieveResult webRetrieveResult) {
		HashMap<String, IRetrieveValue> finalDatas = new HashMap<String, IRetrieveValue>();
		finalDatas.put("examId", new IRetrieveValue<Long>() {
			@Override
			public Long value() {
				return webRetrieveResult.getExamId();
			}
		});
		// finalDatas.put("id", new IRetrieveValue<Long>() {
		// private long maxId = examStudentService.getStudentMaxId();
		//
		// @Override
		// public Long value() {
		// return ++maxId;
		// }
		//
		// });
		build.setFinalDatas(finalDatas);
	}

	private void setValidateParames(DefaultEtlProcessBuild build,
			WebRetrieveResult wrr) {
		build.setValidateParame("repeatUtil", new KeyContainer());
		build.setValidateParame("schools", schoolList(wrr.getExamId()));
		build.setValidateParame("map", new HashMap<String,String>());
	}

	private Map<String, School> schoolList(Long examId) {
		List<School> schools = schoolService.list(examId);
		HashMap<String, School> result = new HashMap<String, School>();
		for (School school : schools) {
			result.put(school.getCode(), school);
		}
		return result;
	}

	@Override
	public boolean executeClean(WebRetrieveResult webRetrieveResult) {
		examStudentService.deleteAndUpdateExam(webRetrieveResult.getExamId());
		return true;
	}

}
