
package com.cntest.fxpt.domain;

import java.util.Date;
public class ExamCjDataSum {
	/*
	 * 考试ID
	 */
	private Long examId;
	/*
	 * 考试时间
	 */
	private Date examDate;
	/*
	 * 考试名称
	 */
	private String examName;
	/*
	 * 参加考试人数
	 */
	private Long allNum;
	/*
	 * 考试科目
	 */
	private String subjectName;
	/*
	 * 科目考试人数
	 */
	private Long subjectNum;
	
	
	public Long getExamId() {
		return examId;
	}
	public void setExamId(Long examId) {
		this.examId = examId;
	}
	public Date getExamDate() {
		return examDate;
	}
	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}
	public String getExamName() {
		return examName;
	}
	public void setExamName(String examName) {
		this.examName = examName;
	}
	public Long getAllNum() {
		return allNum;
	}
	public void setAllNum(Long allNum) {
		this.allNum = allNum;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public Long getSubjectNum() {
		return subjectNum;
	}
	public void setSubjectNum(Long subjectNum) {
		this.subjectNum = subjectNum;
	}
}
