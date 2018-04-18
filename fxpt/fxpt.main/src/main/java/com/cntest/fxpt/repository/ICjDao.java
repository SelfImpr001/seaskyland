/*
 * @(#)com.cntest.fxpt.repository.ICjDao.java	1.0 2014年10月17日:下午3:40:28
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository;

import java.util.List;
import java.util.Map;

import com.cntest.fxpt.anlaysis.bean.Param;
import com.cntest.fxpt.anlaysis.bean.TempItemScore;
import com.cntest.fxpt.anlaysis.bean.TempTotalScore;
import com.cntest.fxpt.bean.CjWebRetrieveResult;
import com.cntest.fxpt.domain.ExamCjDataSum;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月17日 下午3:40:28
 * @version 1.0
 */
public interface ICjDao {
	public int deleteCj(Long testPaperId);
	
	public int deleteCjKn(Long testPaperId);

	public int deleteExamAllCj(Long examId);

	public List<Integer> getExamStudentWlFor(Long testPaperId);

	public List<Map<String, Object>> list(Long examId,int wl, Long testPaperId,
			Long schoolId, int firstNum, int maxNum);

	public Map<Long, Integer> statTestPaperSKRS(Long examId);
	
	public List<TempTotalScore> loadCj(Long examId,Param... params);
	public List<TempItemScore> loadItemCj(Long examId,Param... params);
	
	public boolean appendCleanBySchoolIds(Long testPaper,List<String> hashSet);
	
	public void copyCjandItemCj(CjWebRetrieveResult wrr);
	/**
	 * 查询此处导入成绩学生的ID
	 * @param examId
	 * @param testPaperId
	 * @return
	 */
	public List getStudentIds(Long examId,Long testPaperId);
	
	/**
	 * 考试信息汇总（考试人数，考试科目，科目对应的考试人数等等）
	 * @return List<ExamCjDataSum>
	 * examData 考试时间  
	 * examName 考试名称
	 */
	public List<ExamCjDataSum> dataSumList(String examData,String examName);
	
	/**
	 * 查询学生某一组选做题分数信息
	 * @param studentid
	 * @param itemNos
	 * @param examId
	 * @param testPaperId
	 * @return List<TempItemScore>
	 */
	public List<TempItemScore> finChoiseByStudentId(String studentid,String itemNos,Long examId,Long testPaperId);
	
	/**
	 * 确定选做题的无效分数，且置换多选题目的分数为-888
	 * @param studentid
	 * @param itemNos
	 * @param examId
	 * @param testPaperId
	 * @return List<TempItemScore>
	 */
	public void updateCjTempStatusNo(String studentid,String itemNos,Long examId,Long testPaperId,int score);
	/**
	 * 确定选做题的有效分数
	 * @param studentid
	 * @param itemNos
	 * @param examId
	 * @param testPaperId
	 * @return List<TempItemScore>
	 */
	public void updateCjTempStatusIn(String studentid,String itemNos,Long examId,Long testPaperId,int score);
	/**
	 * 处理选做题
	 * @param testpaperId
	 * @param isSort
	 * @throws Exception
	 */
	public void updateChoiceData(Long testpaperId, String rules) throws Exception;
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
