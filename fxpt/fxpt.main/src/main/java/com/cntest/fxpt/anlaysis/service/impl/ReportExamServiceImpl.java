/*
 * @(#)com.cntest.fxpt.service.anlaysis.impl.AnlaysisExamServiceImpl.java	1.0 2014年6月24日:下午1:31:51
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.common.page.Page;
import com.cntest.common.query.Query;
import com.cntest.exception.BusinessException;
import com.cntest.foura.domain.Organization;
import com.cntest.foura.domain.User;
import com.cntest.foura.domain.UserBelong;
import com.cntest.foura.service.OrganizationService;
import com.cntest.foura.service.UserBelongService;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.anlaysis.repository.IReportExamDao;
import com.cntest.fxpt.anlaysis.service.IReportExamService;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.report.ReportQuery;
import com.cntest.fxpt.report.ReportQueryFactory;
import com.cntest.security.UserDetails;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月24日 下午1:31:51
 * @version 1.0
 */
@Service("IReportExamService")
public class ReportExamServiceImpl implements IReportExamService {

	@Autowired(required = false)
	@Qualifier("IReportExamDao")
	private IReportExamDao anlaysisExamDao;

	@Autowired(required = false)
	private UserService userService;
	
	@Autowired(required = false)
	private OrganizationService orgService;
	
	@Autowired(required = false)
	private UserBelongService userBelongService;
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.service.anlaysis.IAnlaysisExamService#countyRecentlyList
	 * (java.lang.String)
	 */
	@Override
	public List<Exam> countyRecentlyList(String countyCode) {
		return anlaysisExamDao.countyRecentlyList(countyCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.service.anlaysis.IAnlaysisExamService#schoolRecentlyList
	 * (java.lang.String)
	 */
	@Override
	public List<Exam> schoolRecentlyList(String schoolCode) {
		return anlaysisExamDao.schoolRecentlyList(schoolCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.service.anlaysis.IAnlaysisExamService#studentRecentlyList
	 * (java.lang.String)
	 */
	@Override
	public List<Exam> studentRecentlyList(String studentCode) {
		return anlaysisExamDao.studentRecentlyList(studentCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.service.anlaysis.IAnlaysisExamService#countyList(java
	 * .lang.String)
	 */
	@Override
	public List<Exam> countyList(String countyCode, Page<Exam> page) {
		return anlaysisExamDao.countyList(countyCode, page);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.service.anlaysis.IAnlaysisExamService#schoolList(java
	 * .lang.String)
	 */
	@Override
	public List<Exam> schoolList(String schoolCode, Page<Exam> page) {
		return anlaysisExamDao.schoolList(schoolCode, page);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.service.anlaysis.IAnlaysisExamService#studentList(java
	 * .lang.String)
	 */
	@Override
	public List<Exam> studentList(String studentCode, Page<Exam> page) {
		return anlaysisExamDao.studentList(studentCode, page);
	}

	@Override
	public Exam getExam(Long examId) {
		return anlaysisExamDao.getExam(examId);
	}

	@Override
	public List<Exam> getSameTermExams(UserDetails user,Exam exam) throws BusinessException {
		ReportQuery reportQuery = ReportQueryFactory.getReportQuery(user,"");
		return reportQuery.querySameTermExamReport(user, exam);
	}

	@Override
	public List<Exam> getSamTermReports(UserDetails user, Query<Exam> query)  throws BusinessException{
		String[] org=userBelongService.getBelongCodes(user.getUserName());
		String orgStr="";
		Organization orgmessage=null;
		if(org!=null)
		for(String o:org){
			orgmessage=orgService.getOrgByCode(o);
			orgStr+=orgmessage.getType();
		}
		ReportQuery reportQuery = ReportQueryFactory.getReportQuery(user,orgStr);
		return reportQuery.queryFor(user, query);
	}

}
