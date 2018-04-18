/*
 * @(#)com.cntest.fxpt.service.IGradeService.java	1.0 2014年5月17日:下午3:48:56
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.cntest.common.page.Page;
import com.cntest.exception.BusinessException;
import com.cntest.fxpt.anlaysis.bean.Param;
import com.cntest.fxpt.anlaysis.bean.StudentCjContainer;
import com.cntest.fxpt.bean.WebRetrieveResult;
import com.cntest.fxpt.domain.ExamStudent;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月17日 下午3:48:56
 * @version 1.0
 */
public interface IExamStudentService {
	// public List<ExamStudent> list(Page<ExamStudent> page, Long examId);

	public long getStudentMaxId();

	public void importFail(Long examId);

	public void statSchoolImportPersonNum(Long examId,
			Page<Map<String, Object>> page);

	public void importSuccess(WebRetrieveResult wrr)  throws BusinessException;

	public void deleteAndUpdateExam(Long examId);

	public List<Map<String, Object>> list(Long examId);

	public void saveRollinStudent(Long targetExamId, Long srcExamId);

	public void findStudentListBySchoolCode(Long examId, String schoolCode,
			Page page);

	public int getExamStudentNum(Long examId);

	public int getExamSchoolNum(Long examId);

	public StudentCjContainer listStudentWith(Long examId, Param... params);

	public List<ExamStudent> listStudent(Long examId, Param... params);

	public StudentCjContainer listStudentWith(Long examId);

	ExamStudent findByExample(ExamStudent example);
	
	public List<Map<String, String>> getStudentList(Long examId);
	
	public boolean cleanStudent(Long examid,List<String> studentIds,List<String> schoolIds) throws Exception;
	
	public void deleteTmpExamStudent(Long examId);
	
	public List<ExamStudent> examByStudentForList(Long examid);
	/**
	 * 导入学生的重复性校验
	 * @return
	 */
	public List<String> studentValidate(WebRetrieveResult wrr);

}
