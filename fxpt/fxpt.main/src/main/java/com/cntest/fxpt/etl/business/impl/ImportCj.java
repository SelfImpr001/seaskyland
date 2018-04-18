/*
 * @(#)com.cntest.fxpt.etl.business.impl.ImportCj.java	1.0 2014年10月10日:上午9:56:34
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

import com.cntest.cache.EhCacheMgr;
import com.cntest.cache.ICache;
import com.cntest.fxpt.bean.CjWebRetrieveResult;
import com.cntest.fxpt.bean.WebRetrieveResult;
import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.Item;
import com.cntest.fxpt.domain.TestPaper;
import com.cntest.fxpt.etl.EtlExecutor;
import com.cntest.fxpt.etl.business.IEtlExecuteor;
import com.cntest.fxpt.etl.domain.EtlProcessResult;
import com.cntest.fxpt.etl.util.KeyContainer;
import com.cntest.fxpt.service.IAnalysisTestPaperService;
import com.cntest.fxpt.service.ICjService;
import com.cntest.fxpt.service.IExamService;
import com.cntest.fxpt.service.IExamStudentService;
import com.cntest.fxpt.service.IFtpSettingService;
import com.cntest.fxpt.service.IItemService;
import com.cntest.fxpt.service.ITestPaperService;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月10日 上午9:56:34
 * @version 1.0
 */
@Service("CJ.IEtlExecuteor")
public class ImportCj extends BaseEtlExecutor implements IEtlExecuteor {
	@Autowired(required = false)
	@Qualifier("IItemService")
	private IItemService itemService;

	@Autowired(required = false)
	@Qualifier("ITestPaperService")
	private ITestPaperService testPaperService;

	@Autowired(required = false)
	@Qualifier("IAnalysisTestPaperService")
	private IAnalysisTestPaperService analysisTestPaperService;

	@Autowired(required = false)
	@Qualifier("IExamStudentService")
	private IExamStudentService examStudentService;

	@Autowired(required = false)
	@Qualifier("ICjService")
	private ICjService cjService;
	
	@Autowired(required = false)
	@Qualifier("IExamService")
	private IExamService examService;
	
	@Autowired(required = false)
	@Qualifier("IFtpSettingService")
	private IFtpSettingService ftpSettingService;

	@Override
	public EtlProcessResult execute(WebRetrieveResult webRetrieveResult) throws Exception {

		CjWebRetrieveResult wrr = new CjWebRetrieveResult(webRetrieveResult);
		setCjParam(wrr);
		EtlExecutor executor = null;
		Exception exception = null;
		EtlProcessResult result=null;
		try {
			//清空零时表
			cjService.deleteCjKn();
			DefaultEtlProcessBuild build = initBuilder(wrr);
			setValidateParames(build, wrr);
			executor = exec(build);
			
			result = createMessage(executor, exception);
			
			if (result.isHasError()) {
				cjService.importFail(wrr.getTestPaperId());
			} else {
				//重复性校验
				//if(wrr.isValidate()){
					List<String> list=new ArrayList<>();
					list.add("以下是覆盖的学生的准考证号");
					list.addAll(1,cjService.cjValidate(wrr));
					if(list.size()>1){
						//重复数据做提示
						result.setHasError(true);
						result.setHasLog(true);
						String mes="准考证号为"+list.get(1)+"的学生成绩信息被覆盖，"+"总共有"+(list.size()-1)+"条数据被覆盖。";
						result.setMessage(mes);
						//logs.add(0, mes);
						result.setLogs(list);
					}
				//}else{
					cjService.importSuccess(wrr);
					//检查本次考试是否已经导入学生、全部科目、全部科目成绩  20160328 hhc
					boolean b = examService.hasStudentsAndSubjcetsAndCj(wrr.getExamId());
					if(b){
						//上传学生原始成绩到FTP
						ftpSettingService.upload(wrr.getExamId());
					//}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			exception = e;
		} finally {
			createAndSaveEtlLog(wrr.getUserName(), wrr.getExamId(), "导入"
					+ wrr.getTestPaper().getName() + "成绩", wrr.getSchemeType(), result);
			deleteFile(wrr);
		}
		return result;
	}

	private void setValidateParames(DefaultEtlProcessBuild build, CjWebRetrieveResult wrr) {
		
		build.setValidateParame("choices", getCjfiledList((List<Item>)wrr.getItems()));
		build.setValidateParame("items", wrr.getItems());
		build.setValidateParame("testPaper", wrr.getTestPaper());
		build.setValidateParame("repeatUtil", new KeyContainer());
		build.setValidateParame("students", getStudentMap(wrr.getExamId()));
	}
	
	/** 获取选做题的小题 */
	private List<String> getCjfiledList(List<Item> items){
		List<String> cjfiledlist = new ArrayList<String>();
		//cjfiledlist.add("score12");
		for (Item item : items) {
			if(item.isChoice()){ //选做题的标示
				cjfiledlist.add("score"+item.getSortNum());
			}
		}
		return cjfiledlist;
	}
	
	private Map<String, Map<String, Object>> getStudentMap(Long examId) {
		ICache cache = EhCacheMgr.newInstance("student-Cache");
		Map<String, Map<String, Object>> studentMap = cache.get(examId + ".bmk");
		if (studentMap == null) {
			List<Map<String, Object>> studentInfos = examStudentService
					.list(examId);
			studentMap = new HashMap<String, Map<String, Object>>();
			for (Map<String, Object> studentInfo : studentInfos) {
				studentMap.put(studentInfo.get("zkzh").toString(), studentInfo);
			}
			cache.put(examId + "", studentMap);
		}
		
		return studentMap;
	}

	@Override
	public boolean executeClean(WebRetrieveResult wrr) {
		TestPaper testPaper = testPaperService.get(wrr.getTestPaperId());
		if(testPaper!=null){
			if (testPaper.isHasCj() && wrr.isOverlayImport()) {
				cjService.deleteCj(testPaper);
				return true;
			} else if (!testPaper.isHasCj()) {
				return true;
			} else {
				return false;
			}
		}else {
			return true;
		}
		
	}

	private void setCjParam(CjWebRetrieveResult wrr) {
		TestPaper testPaper = testPaperService.get(wrr.getTestPaperId());
		wrr.setTestPaper(testPaper);
		List<Item> items = itemService.listByTestPaperId(wrr.getTestPaperId());
		wrr.setItems(items);
		List<AnalysisTestpaper> analysisTestpapers = analysisTestPaperService.list(testPaper.getId());
		wrr.setAnalysisTestpapers(analysisTestpapers);
	}

}
