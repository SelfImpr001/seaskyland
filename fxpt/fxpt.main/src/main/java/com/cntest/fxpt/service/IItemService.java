/*
 * @(#)com.cntest.fxpt.service.IItemService.java	1.0 2014年5月22日:下午4:21:04
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service;

import java.util.List;

import com.cntest.fxpt.bean.UploadFileContentInfo;
import com.cntest.fxpt.domain.Item;
import com.cntest.fxpt.domain.TestPaper;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月22日 下午4:21:04
 * @version 1.0
 */
public interface IItemService {
	public void importFail(Long testPaperId);

	public List<Item> listByTestPaperId(Long testPaperId);
	public List<Item> listByAnlaysisTestPaperId(Long AnlaysisTestPaperId);

	public void importSuccess(Long examId, TestPaper testPaper);
	
	public String findPaperByItemId(Long itemId,Long examid,String studentid);
	
	public String findOptionTypeByItemId(Long itemId,Long examid,String studentid);
	
	public String findPaperTotalScoreByItemId(Long examid,String studentid,Long testpaperid,String paper);
	
	public String findRespectiveScoreByItemId(Long testpaperid,Long examid,String optiontype,String studentid,String paper);
	/**
	 * 查询细目表是否已经导入
	 * @param fileInfo
	 * @param examId
	 * @return
	 */
	public boolean validate(String  subjectName,String examId);

}
