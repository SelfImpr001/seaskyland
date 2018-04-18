/*
 * @(#)com.cntest.fxpt.domain.ExamStudent.java	1.0 2014年5月14日:上午11:15:25
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月14日 上午11:15:25
 * @version 1.0
 */
public class ExamStudent {
	private Long id;
	private String studentId;
	private String name;
	private String zkzh;
	private int wl;
	private int gender;
	private String domicile;
	private String nation;
	private boolean isPast;
	private boolean isTransient;
	private String learLanguage;
	private String studentType;
	private String languagePattern;
	private String area;
	private String languageType;
	private Exam exam;
	private School school;
	private ExamClass clazz;
	//由于boolean类型的值页面不好处理，加两个int值对应上面的2各boolean
	private int pastValue;
	private int trantValue;

	

	public int getPastValue() {
		return pastValue;
	}

	public void setPastValue(int pastValue) {
		this.pastValue = pastValue;
	}

	public int getTrantValue() {
		return trantValue;
	}

	public void setTrantValue(int trantValue) {
		this.trantValue = trantValue;
	}

	public ExamStudent(String name) {
		this.name = name;
	}

	public ExamStudent(String name, String studentId) {
		this(name);
		this.studentId = studentId;
	}

	public ExamStudent(String name, String studentId, School school) {
		this(name, studentId);
		this.school = school;
	}

	public ExamStudent() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Exam getExam() {
		return exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getZkzh() {
		return zkzh;
	}

	public void setZkzh(String zkzh) {
		this.zkzh = zkzh;
	}

	public int getGender() {
		return gender;
	}

	public School getSchool() {
		return school;
	}

	public ExamClass getClazz() {
		return clazz;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public void setClazz(ExamClass clazz) {
		this.clazz = clazz;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getDomicile() {
		return domicile;
	}

	public void setDomicile(String domicile) {
		this.domicile = domicile;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getLearLanguage() {
		return learLanguage;
	}

	public void setLearLanguage(String learLanguage) {
		this.learLanguage = learLanguage;
	}

	public int getWl() {
		return wl;
	}

	public void setWl(int wl) {
		this.wl = wl;
	}

	public boolean isPast() {
		return isPast;
	}

	public void setPast(boolean isPast) {
		this.isPast = isPast;
	}

	public boolean isTransient() {
		return isTransient;
	}

	public void setTransient(boolean isTransient) {
		this.isTransient = isTransient;
	}

	public String getLanguageType() {
		return languageType;
	}

	public void setLanguageType(String languageType) {
		this.languageType = languageType;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.studentId).toHashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof ExamStudent))
			return false;
		ExamStudent other = (ExamStudent) o;
		return new EqualsBuilder().append(this.studentId, other.studentId)
				.isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("Examinee name", this.name)
				.append(this.studentId)
				.append(this.school == null ? "" : this.school.getName())
				.append(this.exam != null ? this.exam.getName() : "").build();
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getStudentType() {
		return studentType;
	}

	public void setStudentType(String studentType) {
		this.studentType = studentType;
	}

	public String getLanguagePattern() {
		return languagePattern;
	}

	public void setLanguagePattern(String languagePattern) {
		this.languagePattern = languagePattern;
	}

}
