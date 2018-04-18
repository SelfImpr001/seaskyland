/*
 * @(#)com.cntest.fxpt.repository.IGradeDao.java	1.0 2014年5月17日:上午10:28:46
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.cntest.common.page.Page;
import com.cntest.common.repository.Repository;
import com.cntest.fxpt.anlaysis.bean.Param;
import com.cntest.fxpt.bean.WebRetrieveResult;
import com.cntest.fxpt.domain.ExamStudent;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月17日 上午10:28:46
 * @version 1.0
 */
public interface IExamStudentDao extends Repository<ExamStudent, Long> {
	public List<ExamStudent> list(Page<ExamStudent> page, Long examId);

	public List<ExamStudent> listStudentWith(Long examId, Param... params);

	public void deleteTmpExamStudent(Long examId);

	public void deleteSchoolStatPersonCount(Long examId);

	public void deleteExamClass(Long examId);

	public long getStudentMaxIdInDW();

	public int importExamClass(WebRetrieveResult wrr);

	public int getImportStudentNum(Long examid);
	
	public int copyStudent(WebRetrieveResult wrr);

	public void statSchoolImportPersonNum(Long examId,
			Page<Map<String, Object>> page);

	public int deleteStudentInDW(Long examId);

	public List<Map<String, Object>> list(Long examId);

	public int getExamSchoolNum(Long examId);

	public int getStudentCount(Long examId, Long schoolId, int wl);

	public boolean isHasWL(Long examId);

	public void statSchoolPersonCount(Long examId);

	public Map<String, Integer> statStudentCount(Long examId);

	public void copyStudentTo(Long targetExamId, Long srcExamId);

	public void findStudentListBySchoolCode(Long examId, String schoolCode,
			Page page);

	public int getExamStudentNum(Long examId);

	public ExamStudent get(Long examId, String studentId);
	
	public List<Map<String, String>> getStudentList(Long examId);

	public boolean cleanStudent(Long examid,List<String> studentIds,List<String> schoolIds) throws Exception;
	
	public List<ExamStudent> examByStudentForList(Long examid);
	
	/**
	 * 导入学生的重复性校验
	 * @return String （校验结果）
	 */
	public List<String> studentValidate(WebRetrieveResult wrr);
}
