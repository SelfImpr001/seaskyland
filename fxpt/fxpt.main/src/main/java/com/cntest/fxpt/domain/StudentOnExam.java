/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年11月10日
 * @version 1.0
 **/
public class StudentOnExam {
	
	private ExamStudent examinee;
	
	private Exam exam;
	
	private StudentBase student;
	
	private Long examineeId;
	
	public StudentOnExam(Exam exam,ExamStudent examinee,StudentBase student) {
		this.exam = exam;
		this.examinee = examinee;
		this.examineeId = this.examinee.getId();
		this.student = student;
	}
	
	public int hashCode() {
		return new HashCodeBuilder().append(this.examinee).append(student).toHashCode();
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof StudentOnExam))
			return false;
		StudentOnExam other = (StudentOnExam) o;
		return new EqualsBuilder().append(this.examinee, other.examinee).append(this.student, other.student).isEquals();
	}

	public String toString() {
		return new ToStringBuilder(this).append("Exam :", this.exam.getName()).append(" Student")
				.append(this.student.getName()).append(this.student.getGuid()).build();
	}
	
	public StudentOnExam() {
		
	}
	
	private Long pk;

	public ExamStudent getExaminee() {
		return examinee;
	}

	public void setExaminee(ExamStudent examinee) {
		this.examinee = examinee;
	}

	public Exam getExam() {
		return exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

	public StudentBase getStudent() {
		return student;
	}

	public void setStudent(StudentBase student) {
		this.student = student;
	}

	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}

	public Long getExamineeId() {
		return examineeId;
	}

	public void setExamineeId(Long examineeId) {
		this.examineeId = examineeId;
	}
	
}

