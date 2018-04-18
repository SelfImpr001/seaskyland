/*
 * @(#)com.cntest.fxpt.etl.business.impl.ImportCj.java	1.0 2014年10月10日:上午9:56:34
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

import com.cntest.foura.service.OrganizationService;
import com.cntest.fxpt.bean.WebRetrieveResult;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.Subject;
import com.cntest.fxpt.domain.TestPaper;
import com.cntest.fxpt.etl.EtlExecutor;
import com.cntest.fxpt.etl.business.IEtlExecuteor;
import com.cntest.fxpt.etl.business.util.ImportUtil;
import com.cntest.fxpt.etl.domain.EtlProcessResult;
import com.cntest.fxpt.etl.util.IRetrieveValue;
import com.cntest.fxpt.etl.util.KeyContainer;
import com.cntest.fxpt.service.IAnalysisTestPaperService;
import com.cntest.fxpt.service.IFileManageService;
import com.cntest.fxpt.service.IItemService;
import com.cntest.fxpt.service.ISubjectService;
import com.cntest.fxpt.service.ITestPaperService;
import com.cntest.util.ExceptionHelper;
import com.cntest.util.IReadXls;
import com.cntest.util.XlsCreator;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月10日 上午9:56:34
 * @version 1.0
 */
@Service("Organization.IEtlExecuteor")
public class ImportOrg extends BaseEtlExecutor implements IEtlExecuteor {

	@Autowired(required = false)
	@Qualifier("ITestPaperService")
	private ITestPaperService testPaperService;
	
	@Qualifier("OrganizationService")
	private OrganizationService organizationService;
	

	@Autowired(required = false)
	@Qualifier("ISubjectService")
	private ISubjectService subjectService;

	@Autowired(required = false)
	@Qualifier("IItemService")
	private IItemService itemService;

	@Autowired(required = false)
	@Qualifier("IAnalysisTestPaperService")
	private IAnalysisTestPaperService analysisTestPaperService;

	@Autowired(required = false)
	@Qualifier("IFileManageService")
	private IFileManageService fileManageService;

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.etl.business.IEtlExecuteor#execute(com.cntest.fxpt.
	 * systemsetting.etl.domain.WebRetrieveResult)
	 */
	@Override
	public EtlProcessResult execute(WebRetrieveResult wrr) throws Exception {
		

		EtlExecutor executor = null;
		Exception exception = null;
		try {
			//导入之前先清空临时表数据
			fileManageService.deleteLSBorg();
			DefaultEtlProcessBuild build = initBuilder(wrr);
			executor = exec(build);
			exception=initData();
		} catch (Exception e) {
			exception = e;
		} finally {
			deleteFile(wrr);
		}
		EtlProcessResult result = createMessage(executor, exception);
		
		if(exception!=null) {
			result.setHasError(true);
		}else{
			result.setHasError(false);
			
		}
		if(result.isHasError()) {
			fileManageService.importFail(wrr);
		}else{
			fileManageService.importSuccess(wrr);
		}
		return result;
	}
	//验证导入组织的数据（上级代码的合理性）
	private Exception initData() {
		//复制数据到4a_org 并且删除零时表数据之前应该检查数据的合理性）
		Exception exception=null;
		try {
			//验证组织代码的合理性（上级组织代码必须是存在）
			List orgList=fileManageService.findAllListFromTmp();//需要导入验证的数据列表
			if(orgList.size()>0){
				for(int i=0;i<orgList.size();i++) {
					Object[] obj =(Object[]) orgList.get(i);
					int ortType=Integer.parseInt(obj[2].toString());//组织类型
					String pId="";
					if(obj[1]!=null && obj[1]!="") {
						pId =obj[1].toString();	//上级组织Id
						List orgTmpList=fileManageService.findOrgListByCodeFromTmp(pId);//查看是否上级存在（零时表）
						if(orgTmpList.size()>0) {//上级在零时表存在
							Object[] objTmp =(Object[]) orgTmpList.get(0);
							int ortTmType=Integer.parseInt(objTmp[2].toString());//上级组织类型
							if(ortType-ortTmType!=1) {
								//组织类型和所写上级组织代码不对应
								throw new Exception("ErrorMessage"+"上级组织代码:"+pId+"与所写组织类型:"+ortTmType+"不对应！--例如：上级组织代码为省，所写组织类型必须为：2-市！"+"ErrorMessage");
							}
						}else {
							List org4aList=fileManageService.findOrgListByCodeFrom4a(pId);//查看上级是否已经存在数据库	
							if(org4aList.size()>0) {
								Object[] obj4a =(Object[]) org4aList.get(0);
								int ort4aType=Integer.parseInt(obj4a[2].toString());//上级组织类型
								if(ortType-ort4aType!=1) {
									//组织类型和所写上级组织代码不对应
									throw new Exception("ErrorMessage"+"上级组织代码:"+pId+"与所写组织类型:"+ort4aType+"不对应！--例如：上级组织代码为省，所写组织类型必须为：2-市！"+"ErrorMessage");
								}
							}else {
								//上级代码不合法
								throw new Exception("ErrorMessage"+"上级组织代码："+pId + "不存在！ErrorMessage");
							}
						}
					}else {
						//为了兼容以前最高省一级的数据//这里支持 0-国家 和1-省
						if(ortType>1) {
							//组织类型和所写上级组织代码不对应
							throw new Exception("ErrorMessage"+"上级组织代码:"+pId+"与所写组织类型:"+ortType+"不对应！--例如：上级组织代码为省，所写组织类型必须为：2-市！"+"ErrorMessage");
						}
					}
				}
			}
	}catch(Exception ex) {
		exception=ex;
	}
		return exception;
	}
	private void setFinalData(DefaultEtlProcessBuild build,
			final TestPaper testPaper) {
		HashMap<String, IRetrieveValue> finalDatas = new HashMap<String, IRetrieveValue>();
		finalDatas.put("testPaperId", new IRetrieveValue<Long>() {
			@Override
			public Long value() {
				return testPaper.getId();
			}

		});
		finalDatas.put("examId", new IRetrieveValue<Long>() {
			@Override
			public Long value() {
				return testPaper.getExam().getId();
			}

		});
		finalDatas.put("sortNum", new IRetrieveValue<Integer>() {
			private int sortNum = 1;

			@Override
			public Integer value() {
				return sortNum++;
			}

		});
		build.setFinalDatas(finalDatas);
	}

	private void setValidateParames(DefaultEtlProcessBuild build) {
		build.setValidateParame("subjects", subjectList());
		build.setValidateParame("repeatUtil", new KeyContainer());
	}

	private Map<String, Subject> subjectList() {
		List<Subject> subjects = subjectService.list();
		HashMap<String, Subject> result = new HashMap<String, Subject>();
		for (Subject subject : subjects) {
			result.put(subject.getName(), subject);
		}
		return result;
	}

	private TestPaper createTestPaper(WebRetrieveResult webRetrieveResult) {
		TestPaper testPaper = parseTestPaper(
				webRetrieveResult.getSuffix(),
				webRetrieveResult.getFileDir()
						+ webRetrieveResult.getFileName(),
				webRetrieveResult.getSheetName());

		if (testPaper != null) {
			Exam exam = new Exam();
			exam.setId(webRetrieveResult.getExamId());
			testPaper.setExam(exam);
			testPaperService.add(testPaper);
		}

		return testPaper;
	}

	@Override
	public boolean executeClean(WebRetrieveResult webRetrieveResult) {
		TestPaper testPaper = parseTestPaper(
				webRetrieveResult.getSuffix(),
				webRetrieveResult.getFileDir()
						+ webRetrieveResult.getFileName(),
				webRetrieveResult.getSheetName());

		testPaper = testPaperService.get(webRetrieveResult.getExamId(),
				testPaper.getName());
		if (testPaper != null && webRetrieveResult.isOverlayImport()) {
			testPaperService.delete(testPaper);
			return true;
		} else if (testPaper == null) {
			return true;
		} else {
			return false;
		}

	}

	private TestPaper parseTestPaper(String suffix, String path,
			String sheetName) {
		IReadXls xls = XlsCreator.create(suffix, path);
		xls.open();
		xls.setSheet(sheetName);
		List<String> testPaperInfo = xls.getRow(0);
		xls.close();
		return ImportUtil.createTestPaper(testPaperInfo);
	}

}
