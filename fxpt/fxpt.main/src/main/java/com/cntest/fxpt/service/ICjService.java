/*

 * @(#)com.cntest.fxpt.service.ICjService.java	1.0 2014年10月17日:下午3:37:53
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.cntest.common.page.Page;
import com.cntest.fxpt.anlaysis.bean.Param;
import com.cntest.fxpt.anlaysis.bean.StudentCj;
import com.cntest.fxpt.anlaysis.bean.TempItemScore;
import com.cntest.fxpt.anlaysis.bean.TempTotalScore;
import com.cntest.fxpt.bean.CjWebRetrieveResult;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.ExamCjDataSum;
import com.cntest.fxpt.domain.TestPaper;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月17日 下午3:37:53
 * @version 1.0
 */
public interface ICjService {
	public int deleteCj(TestPaper testPaper);

	public int deleteExamAllCj(Long examId);

	public void importSuccess(CjWebRetrieveResult wrr) throws Exception;

	public void importFail(Long testPaperId);

	public List<Map<String, Object>> listCj(Long examId, Long testPaperId,
			Long schoolId, Page<Map<String, Object>> page);

	public List<TestPaper> statTestPaperExamCount(Long examId);

	public List<TempTotalScore> loadCj(Long examId, Param... params);

	public List<TempItemScore> loadItemCj(Long examId, Param... params);

	public StudentCj loadStudentCj(Long examId, String studentId);
	
	public boolean appendCleanBySchoolIds(Long testPaper,List<String> hashSet);
	
	public void copyCjandItemCj(CjWebRetrieveResult wrr);
	
	/**
	 * 考试信息汇总（考试人数，考试科目，科目对应的考试人数等等）
	 * @return List<ExamCjDataSum>
	 * examData 考试时间  
	 * examName 考试名称
	 */
	public List<ExamCjDataSum> dataSumList(String examData,String examName);
	
	public void setPageMsg(List<ExamCjDataSum> list,Page<ExamCjDataSum> page);
	
	public void deleteCjKn();
	
	/**
	 * 处理小题成绩.25和.75
	 * @param examId
	 * @param testPaperId
	 * @throws Exception
	 */
	public void dataValidate(Long examId ,Long testPaperId) throws Exception;
	/**
	 * 导入成绩重复数据校验
	 * @param wrr
	 * @return 返回重复数据的学生Id列表
	 * @throws Exception
	 */
	public List<String> cjValidate(CjWebRetrieveResult wrr) throws Exception;
}
