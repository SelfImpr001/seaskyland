/*
 * @(#)com.cntest.fxpt.anlaysis.service.PesonalReportExamService.java	1.0 2014年7月18日:下午5:00:53
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.service;

import java.util.List;
import java.util.Map;

import com.cntest.fxpt.domain.Exam;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2014年7月18日 下午5:00:53
 * @version 1.0
 */
public interface IPesonalReportExamService {
	public Exam getExam(Long examId);
	public List<Object> findALLStudentByExamId(Long examId);
	public List<Object> getSubjectList(Long examId);
	public List<Object[]> getDetailList(Long examId,int wl,Long subjectId,String stuid) ;
	public List<Object> getAllsubjectSumScoreProc(Long examId,int wl,String studentId,Long subjectId);
	public Object getzfSumScore(Long examId,int wl, String studentId);
	public List<Object> getAllHissubjectSumScoreProc(Long examId, int wl,String studentId);
	public List<Object> getAllHissubjectSumScoreStdsProc(Long examId, int wl,String studentId,String objid);
	public List<Object[]> getAllContentRatioProc(Long examId,int wl,String objid);
	public int getSameRank(Long examid,int wl,int rank);
	
	public int getTopOrgCode (String orgcode);
	
	public List<Object> getMyContentRatio(Long examId,Long subjectId,String studentId);
	
	public List<Object[]> getMyContentRatioProc(Long examId,int wl,Long subjectId,String studentId);
	public List<Object[]> getMyAbilityRatioProc(Long examId,int wl,Long subjectId,String studentId);
	public List<Object[]> getMyTitleTypeRatioProc(Long examId,int wl,Long subjectId,String studentId);
	public List<Object[]> getTopRatioProc(Long examId,int wl,String orgcode);
	public Map<String, List<Object[]>> getAllStudentContentPreProc(Long examId,Long subjectId);
	public Map<String, List<Object[]>> getAllStudentAbilityPreProc(Long examId,Long subjectId);
	public Map<String, List<Object[]>> getAllStudentTitleTypePreProc(Long examId,Long subjectId);
	public int getAllExamStudentNumProc(Long examid,Long subjectId);
	public Object[] findStuentByExamIdAndStudentId(Long examid,String studentId);
	
	public List<Object> getStudentSubjectLevelProc(Long examId,int wl,String studentId);
	public int getExamstudentNum(Long examid,Long subjectid,int wl,String objid);
	
	
	public List<Object[]> getAllStudentList(Long examid);
	
	public Map<Long, Double> getStudentAllSubjectScore(Long examid,String studentid,int wl);
	public Map<Long,Double> getSocreByObjID(Long examid,int objid,int wl);
	
	
	public void cacluteAllSubjectRank(Long examId,int wl,Long subjectid);
	public List<Object> getAllSubjectRank(Long examid,int wl);
	public List<Object> getStudentsSubjectRank(Long examid,int wl);
	public void cacluteZFScore(Long examId, int wl);
	public List<Object[]> getallZFScore(Long examId, int wl);
	public List<Object[]> getAllSubjectSkrs(Long examid, int wl,String objid);
	public List<Object[]> cacluteStudentAllSubjectsStd(Long examId,String objid);
	public List<Object[]> getStduentSLevel(Long examid);
	public List<Object[]> getAllSubjectKnowledgeContentMap(Long examid,int wl);
	public List<Object[]> getAllSubjectAbilityMapMap(Long examid,int wl);
	public List<Object[]> getAllSubjectTitleTypeMap(Long examid,int wl);
	public Long getHisExamidByCurrExamid(Long examid);
	public void clearTempTable();
	public List<Map<String, Object>> cacluteAllSubjectRankS(Long examId,int wl,Long subjectid);
	public List<Map<String, Object>> cacluteZFScoreS(Long examId, int wl);
	public List<Map<String, Object>> getAllContentRatioProcS(Long examId,int wl,String objid) ;
}
