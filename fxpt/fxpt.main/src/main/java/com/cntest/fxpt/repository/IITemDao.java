/*
 * @(#)com.cntest.fxpt.repository.IITemDao.java	1.0 2014年5月22日:下午4:16:52
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository;

import java.util.List;

import com.cntest.fxpt.bean.UploadFileContentInfo;
import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.Item;
import com.cntest.fxpt.domain.ItemMessage;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月22日 下午4:16:52
 * @version 1.0
 */
public interface IITemDao {
	public int deleteItem(Long testPaperId);

	public List<Item> list(Long testPaperId);
	public List<Item> listByAnlaysisTestPaperId(Long AnlaysisTestPaperId);

	public int updateItemAnaysisTestPaperId(AnalysisTestpaper analysisTestpaper);
	
	public String findPaperByItemId(Long itemId,Long examid,String studentid);
	
	public String findOptionTypeByItemId(Long itemId,Long examid,String studentid);
	
	public String findPaperTotalScoreByItemId(Long examid,String studentid,Long testpaperid,String paper);
	
	public String findRespectiveScoreByItemId(Long testpaperid,Long examid,String optiontype,String studentid,String paper);
	
	/**
	 * 查询是否存在AB卷
	 * @param examId 考试ID
	 * @param testPaperId 试卷科目ID
	 * @return
	 */
	public boolean isContainABpaper(Long examId,Long testPaperId);
	
	/**
	 * 查询是否有选做题
	 * @param examId 考试ID
	 * @param testPaperId 试卷科目ID
	 * @return
	 */
	public List<Item> findAllQk(Long examId,Long testPaperId);
	
	/**
	 * 查询细目表是否已经导入
	 * @param fileInfo
	 * @param examId
	 * @return
	 */
	public boolean validate(String subjectName,String examId);
}
