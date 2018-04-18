/*
 * @(#)com.cntest.fxpt.repository.IExamDao.java	1.0 2014年5月17日:上午10:39:03
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository;

import java.util.List;

import com.cntest.common.page.Page;
import com.cntest.common.query.Query;
import com.cntest.exception.BusinessException;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.School;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月17日 上午10:39:03
 * @version 1.0
 */
public interface IExamDao {
	public void add(Exam exam);

	public void update(Exam exam);

	public void updateStatus(Long examId, int status);

	public void delete(Exam exam);

	public Exam findById(Long examId);

	public List<Exam> list(Page<Exam> page);

	public int getTestPaperCount(Exam exam);

	public int getExamStudentCount(Exam exam);

	public int updateHasExamStudent(Long examId, boolean hasExamStudent,
			boolean hasWlInStudentInfo);

	public Exam tryGetAnalysisExam();

	public List<School> findExamSchools(Exam exam);

	String[] findExamNations(Exam exam);
	
	public boolean hasStudentsAndSubjcetsAndCj(Long examid);

	public List<Exam> getExamAllList();
	/**
	 * 获取某次考试的细目表条数
	 * @param examid
	 * @return
	 */
	public int getItemNumByExamid(Long examid);
	
	public boolean getHasChoice(Long examid) ;
	
	public List<Exam> getExamByorgCodes(String codes);
	/**
	 * 查询该用户被授权的考试列表
	 * @param page
	 * @return
	 */
	public void examlist(com.cntest.common.query.Query<Exam> query,Long userId);
	
	/**
	 * 添加考试信息：参加考试的群体
	 * @param examId
	 * @throws BusinessException
	 */
	public void updateExamById(Long examId) throws BusinessException;
	
	public List<Exam> listBybach(Page<Exam> page, Integer... status);
	
	/**
	 * 获取所有的学年类型
	 * @return
	 */
	public List<Exam> getExamAllSchoolYears();
}
