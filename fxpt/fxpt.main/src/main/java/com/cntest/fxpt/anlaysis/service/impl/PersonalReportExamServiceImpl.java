/*
 * @(#)com.cntest.fxpt.anlaysis.service.impl.PersonalReportExamServiceImpl.java	1.0 2014年7月18日:下午5:01:37
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.anlaysis.repository.IPersonalReportExamDao;
import com.cntest.fxpt.anlaysis.service.IPesonalReportExamService;
import com.cntest.fxpt.domain.Exam;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2014年7月18日 下午5:01:37
 * @version 1.0
 */
@Service("IPesonalReportExamService")
public class PersonalReportExamServiceImpl implements IPesonalReportExamService{
	@Autowired(required = false)
	@Qualifier("IPersonalExamDao")
	private IPersonalReportExamDao iPersonalReportExamDao;
	/* (non-Javadoc)
	 * @see com.cntest.fxpt.anlaysis.service.IReportExamService#findALLStudentByExamId(java.lang.String)
	 */
	@Override
	public List<Object> findALLStudentByExamId(Long examId) {
		// TODO Auto-generated method stub
		return iPersonalReportExamDao.findALLStudentByExamId(examId);
	}

	/* (non-Javadoc)
	 * @see com.cntest.fxpt.anlaysis.service.IReportExamService#getSubjectList(int)
	 */
	@Override
	public List<Object> getSubjectList(Long examId) {
		return iPersonalReportExamDao.getSubjectList(examId);
	}

	/* (non-Javadoc)
	 * @see com.cntest.fxpt.anlaysis.service.IReportExamService#getDetailList(int)
	 */
	@Override
	public List<Object[]> getDetailList(Long examId,int wl,Long subjectId,String stuid) {
		return iPersonalReportExamDao.getDetailList(examId,wl,subjectId,stuid);
	}


	/* (non-Javadoc)
	 * @see com.cntest.fxpt.anlaysis.service.IReportExamService#getAllsubjectSumScore(int, java.lang.String)
	 */
	@Override
	public List<Object> getAllsubjectSumScoreProc(Long examId,int wl, String studentId,Long subjectid) {
		// TODO Auto-generated method stub
		return iPersonalReportExamDao.getAllsubjectSumScoreProc(examId,wl,studentId,subjectid);
	}

	/* (non-Javadoc)
	 * @see com.cntest.fxpt.anlaysis.service.IReportExamService#getAllHissubjectSumScore(int, java.lang.String)
	 */
	@Override
	public List<Object> getAllHissubjectSumScoreProc(Long examId, int wl, String studentId) {
		// TODO Auto-generated method stub
		return iPersonalReportExamDao.getAllHissubjectSumScoreProc(examId,wl,studentId);
	}
	public List<Object> getAllHissubjectSumScoreStdsProc(Long examId, int wl, String studentId,String objid) {
		// TODO Auto-generated method stub
		return iPersonalReportExamDao.getAllHissubjectSumScoreStdsProc(examId,wl,studentId,objid);
	}
	
	/* (non-Javadoc)
	 * @see com.cntest.fxpt.anlaysis.service.IReportExamService#getAllContentRatio(int, int, java.lang.String)
	 */
	public List<Object[]> getAllContentRatioProc(Long examId,int wl,String objid) {
		// TODO Auto-generated method stub
		return iPersonalReportExamDao.getAllContentRatioProc(examId,wl,objid);
	}

	public List<Object> getMyContentRatio(Long examId, Long subjectId,
			String studentId) {
		// TODO Auto-generated method stub
		return iPersonalReportExamDao.getMyContentRatio(examId,subjectId,studentId);
	}
	/* (non-Javadoc)
	 * @see com.cntest.fxpt.anlaysis.service.IReportExamService#getMyContentRatio(int, int, java.lang.String)
	 */
	@Override
	public List<Object[]> getMyContentRatioProc(Long examId,int wl, Long subjectId,
			String studentId) {
		// TODO Auto-generated method stub
		return iPersonalReportExamDao.getMyContentRatioProc(examId,wl,subjectId,studentId);
	}
	public List<Object[]> getMyAbilityRatioProc(Long examId, int wl,Long subjectId,
			String studentId) {
		// TODO Auto-generated method stub
		return iPersonalReportExamDao.getMyAbilityRatioProc(examId,wl,subjectId,studentId);
	}
	public List<Object[]> getMyTitleTypeRatioProc(Long examId,int wl, Long subjectId,
			String studentId) {
		// TODO Auto-generated method stub
		return iPersonalReportExamDao.getMyTitleTypeRatioProc(examId,wl,subjectId,studentId);
	}

	/* (non-Javadoc)
	 * @see com.cntest.fxpt.anlaysis.service.IReportExamService#getTopRatio(int, int)
	 */
	@Override
	public List<Object[]> getTopRatioProc(Long examId,int wl,String orgcode) {
		// TODO Auto-generated method stub
		return iPersonalReportExamDao.getTopRatioProc(examId,wl,orgcode);
	}
	public Exam getExam(Long examId) {
		return iPersonalReportExamDao.getExam(examId);
	}
	public Map<String, List<Object[]>> getAllStudentContentPreProc(Long examId, Long subjectId) {
		return iPersonalReportExamDao.getAllStudentContentPreProc(examId,subjectId);
	}
	public Map<String, List<Object[]>> getAllStudentAbilityPreProc(Long examId, Long subjectId) {
		return iPersonalReportExamDao.getAllStudentAbilityPreProc(examId,subjectId);
	}
	public Map<String, List<Object[]>> getAllStudentTitleTypePreProc(Long examId, Long subjectId) {
		return iPersonalReportExamDao.getAllStudentTitleTypePreProc(examId,subjectId);
	}
	public int getAllExamStudentNumProc(Long examid,Long subjectId) {
		return iPersonalReportExamDao.getAllExamStudentNumProc(examid,subjectId);
	}
	public Object[] findStuentByExamIdAndStudentId(Long examid,String studentId){
		return iPersonalReportExamDao.findStuentByExamIdAndStudentId(examid, studentId);
	}
	public int getExamstudentNum(Long examid, Long subjectid, int wl,String objid) {
		return iPersonalReportExamDao.getExamstudentNum( examid, subjectid, wl,objid);
	}
	public int getTopOrgCode(String orgcode) {
		return iPersonalReportExamDao.getTopOrgCode(orgcode);
	}
	public List<Object[]> getAllStudentList(Long examid) {
		return iPersonalReportExamDao.getAllStudentList(examid);
	}
	public List<Object> getStudentSubjectLevelProc(Long examId, int wl,
			String studentId) {
		return iPersonalReportExamDao.getStudentSubjectLevelProc(examId,wl,studentId);
	}
	public Object getzfSumScore(Long examId, int wl, String studentId) {
		return iPersonalReportExamDao.getzfSumScore(examId,wl,studentId);
	}
	public int getSameRank(Long examid, int wl, int rank) {
		return iPersonalReportExamDao.getSameRank(examid,wl,rank);
	}
	public List<Object[]> getAllSubjectSkrs(Long examid, int wl,String objid){
		return iPersonalReportExamDao.getAllSubjectSkrs(examid, wl, objid);
	}
	public Map<Long, Double> getStudentAllSubjectScore(Long examid, String studentid,
			int wl) {
		return iPersonalReportExamDao.getStudentAllSubjectScore(examid,studentid,wl);
	}
	public Map<Long, Double> getSocreByObjID(Long examid, int objid, int wl) {
		return iPersonalReportExamDao.getSocreByObjID(examid,objid,wl);
	}
	public void cacluteAllSubjectRank(Long examId,int wl,Long subjectid){
		iPersonalReportExamDao.cacluteAllSubjectRank(examId, wl,subjectid);
	}
	public List<Object> getAllSubjectRank(Long examid,int wl){
		return iPersonalReportExamDao.getAllSubjectRank(examid, wl);
	}
	public List<Object> getStudentsSubjectRank(Long examid,int wl){
		return iPersonalReportExamDao.getStudentsSubjectRank(examid, wl);
	}
	public void cacluteZFScore(Long examId, int wl){
		iPersonalReportExamDao.cacluteZFScore(examId, wl);
	}
	public List<Object[]> getallZFScore(Long examId, int wl){
		return iPersonalReportExamDao.getallZFScore(examId, wl);
	}
	public List<Object[]> cacluteStudentAllSubjectsStd(Long examId,String objid){
		return iPersonalReportExamDao.cacluteStudentAllSubjectsStd(examId, objid);
	}
	public List<Object[]> getStduentSLevel(Long examid){
		return iPersonalReportExamDao.getStduentSLevel(examid);
	}
	public List<Object[]> getAllSubjectKnowledgeContentMap(Long examid,int wl){
		return iPersonalReportExamDao.getAllSubjectKnowledgeContentMap(examid, wl);
	}
	public List<Object[]> getAllSubjectAbilityMapMap(Long examid,int wl){
		return iPersonalReportExamDao.getAllSubjectAbilityMapMap(examid, wl);
	}
	public List<Object[]> getAllSubjectTitleTypeMap(Long examid,int wl){
		return iPersonalReportExamDao.getAllSubjectTitleTypeMap(examid, wl);
	}
	public Long getHisExamidByCurrExamid(Long examid){
		return iPersonalReportExamDao.getHisExamidByCurrExamid(examid);
	}
	public void clearTempTable(){
		iPersonalReportExamDao.clearTempTable();
	}
	public List<Map<String, Object>> cacluteAllSubjectRankS(Long examId,int wl,Long subjectid){
		return iPersonalReportExamDao.cacluteAllSubjectRankS(examId, wl, subjectid);
	}
	
	public List<Map<String, Object>> cacluteZFScoreS(Long examId, int wl){
		return iPersonalReportExamDao.cacluteZFScoreS(examId, wl);
	}
	
	public List<Map<String, Object>> getAllContentRatioProcS(Long examId,int wl,String objid) {
		return iPersonalReportExamDao.getAllContentRatioProcS(examId, wl, objid);
	}
}
