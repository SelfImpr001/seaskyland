/*
 * @(#)com.cntest.fxpt.domain.Exam.java	1.0 2014年5月14日:上午11:13:50
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * <Pre>
 * 考试
 * </Pre>
 * 
 * @author 刘海林 2014年5月14日 上午11:13:50
 * @version 1.0
 */
public class Exam {
	private Long id;
	private String name;
	private String sortName;
	private Date examDate;
	private Date createDate;
	private boolean isWlForExamStudent;
	private int examStudentJiebie;
	private String examStudentJiebieName;
	private int schoolYear;// 使用年数表示，2013表示2012-2013学年
	private String schoolYearName;
	private int schoolTerm;// 1表示第一学期，2表示第二学期
	private Grade grade;
	private ExamType examType;
	private String ownerCode;
	private String containOrg;
	private String ownerName;
	private int levelCode;
	private String levelName;
	private int status;//
	private boolean hasExamStudent;
	private String createUserName;
	private int studentBaseStatus; // 学生库导入，1为已导入0未导入
	private int impItemCount;
	private int impCjCount;
	@JsonIgnore
	private List<TestPaper> testPapers = new ArrayList<TestPaper>();

	public int getStudentBaseStatus() {
		return studentBaseStatus;
	}

	public void setStudentBaseStatus(int studentBaseStatus) {
		this.studentBaseStatus = studentBaseStatus;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getOwnerCode() {
		return ownerCode;
	}

	public void setOwnerCode(String ownerCode) {
		this.ownerCode = ownerCode;
	}

	
	public String getContainOrg() {
		return containOrg;
	}

	public void setContainOrg(String containOrg) {
		this.containOrg = containOrg;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public int getLevelCode() {
		return levelCode;
	}

	public void setLevelCode(int levelCode) {
		this.levelCode = levelCode;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public int getImpItemCount() {
		return impItemCount;
	}

	public int getImpCjCount() {
		return impCjCount;
	}

	public void setImpItemCount(int impItemCount) {
		this.impItemCount = impItemCount;
	}

	public void setImpCjCount(int impCjCount) {
		this.impCjCount = impCjCount;
	}

	/**
	 * 
	 */
	public Exam() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public Date getExamDate() {
		return examDate;
	}

	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}

	public ExamType getExamType() {
		return examType;
	}

	public void setExamType(ExamType examType) {
		this.examType = examType;
	}

	public boolean isWlForExamStudent() {
		return isWlForExamStudent;
	}

	public void setWlForExamStudent(boolean isWlForExamStudent) {
		this.isWlForExamStudent = isWlForExamStudent;
	}

	public Grade getGrade() {
		return grade;
	}

	public int getExamStudentJiebie() {
		return examStudentJiebie;
	}

	public void setExamStudentJiebie(int examStudentJiebie) {
		this.examStudentJiebie = examStudentJiebie;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	public int getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(int schoolYear) {
		this.schoolYear = schoolYear;
	}

	public int getSchoolTerm() {
		return schoolTerm;
	}

	public void setSchoolTerm(int schoolTerm) {
		this.schoolTerm = schoolTerm;
	}

	public boolean isHasExamStudent() {
		return hasExamStudent;
	}

	public void setHasExamStudent(boolean hasExamStudent) {
		this.hasExamStudent = hasExamStudent;
	}

	public List<TestPaper> getTestPapers() {
		return testPapers;
	}

	public void setTestPapers(List<TestPaper> testPapers) {
		this.testPapers = testPapers;
	}

	public int getTestPaperSize() {
		return testPapers.size();
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getExamStudentJiebieName() {
		return examStudentJiebieName;
	}

	public void setExamStudentJiebieName(String examStudentJiebieName) {
		this.examStudentJiebieName = examStudentJiebieName;
	}

	public String getSchoolTermName(boolean isWithNum) {
		if (schoolTerm == 1) {
			return isWithNum ? "第一学期" : "上学期";
		} else {
			return isWithNum ? "第二学期" : "下学期";
		}
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getSchoolYearName() {
		return schoolYearName;
	}

	public void setSchoolYearName(String schoolYearName) {
		this.schoolYearName = schoolYearName;
	}

}
