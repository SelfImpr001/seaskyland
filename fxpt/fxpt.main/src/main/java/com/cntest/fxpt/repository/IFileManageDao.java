/*
 * @(#)com.cntest.fxpt.repository.IITemDao.java	1.0 2014年5月22日:下午4:16:52
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository;

import java.util.List;

import com.cntest.fxpt.bean.WebRetrieveResult;
import com.cntest.fxpt.domain.FileManage;


public interface IFileManageDao {
	/**
	 * 文件管理 列表查询
	 * @param examId 考试Id
	 * @param type type 导入类型（0 组织,1 学生,2 细目表,3 成绩）
	 * @return
	 */
	public List<FileManage> fileList(Long examId,String type);

	/**
	 * 文件管理，保存文件的相关信息
	 * @param wrr
	 * @param type 导入类型（0 组织,1 学生,2 细目表,3 成绩）
	 * @param testPaperId 导入成绩时的试卷的ID（其他时为0L）
	 * @param username 操作者真实姓名
	 */
	public void saveFileMsg(WebRetrieveResult wrr ,Long type,Long testPaperId,String username);
	
	/**
	 * 根据id查询对应文件信息
	 * @param fileId 
	 * @return
	 */
	public FileManage findFileByFileId(Long fileId);
	/**
	 * 根据考试ID和 文件类型删除对应文件信息
	 * @param examId 考试id
	 * @param type 导入类型（0 组织,1 学生,2 细目表,3 成绩）
	 */
	public void deleteFileByExamid(Long examId,String type);
	
	/**
	 * 批量删除文件信息
	 * @param fileList
	 */
	public void deleteFiels(List<FileManage> fileList);
	
	public void deleteFileByTestPaperId(Long testPaperId);
	/**
	 * 保存文件到服务器
	 * @param fileName
	 */
	public void copyFile(WebRetrieveResult wrr);
	
	public List<FileManage> findFileByTestPaperId(Long testPaperId);
	
	public void deleteLSBorg();
	/**
	 * 将4a临时表的数据copy到4a表
	 */
	public void copyOrgTo4a();
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
