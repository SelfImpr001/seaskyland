/*
 * @(#)com.cntest.fxpt.service.IExamService.java	1.0 2014年5月17日:上午10:55:33
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service;

import java.util.List;

import com.cntest.fxpt.bean.WebRetrieveResult;
import com.cntest.fxpt.domain.FileManage;


public interface IFileManageService {


	public List<FileManage> fileList(Long examId,String type);
	
	public void saveFileMsg(WebRetrieveResult wrr,Long type,Long testPaperId,String username);
	
	public FileManage findFileByFileId(Long fileId);
	
	public void deleteFileByExamid(Long examId,String type);
	
	public void deleteFileByTestPaperId(Long testPaperId);
	
	public List<FileManage> findFileByTestPaperId(Long testPaperId);
	
	public void importFail(WebRetrieveResult wrr);
	
	public void importSuccess(WebRetrieveResult wrr) throws Exception;
	
	public void deleteLSBorg();
	/**
	 * 查询所有的数据（零时表里的数据）
	 * @return
	 */
	public List findAllListFromTmp();
	/**
	 * 查看组织上级id是否存在（4a_org表）
	 * @param orgId
	 * @return 
	 */
	public List findOrgListByCodeFrom4a(String orgId);
	
	/**
	 * 查看组织上级id是否存在（4a_org_tnp零时表）
	 * @param orgId
	 * @return 
	 */
	public List findOrgListByCodeFromTmp(String orgId);
	/**
	 * 确认正确的上下级关系
	 */
	public void marchingOrg();
	
}
