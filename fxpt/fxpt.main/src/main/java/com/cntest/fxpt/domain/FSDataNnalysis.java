/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.domain;

import java.util.Date;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2016年9月28日
 * @version 1.0
 **/
public class FSDataNnalysis {
	private Long id                   ;//自增主键
	private Date createTime          ;//创建日期
	private String name                 ;//名称      
	private String status               ;//信息状态
	private String dataCollection      ;//数据采集
	private String student              ;//学生信息
	private String specTtem            ;//细目      
	private String score                ;//成绩      
	private String monitorName         ;//报告名称
	private String monitorSchoolYear  ;//学年      
	private String monitorSemester     ;//学期      
	private String monitorType         ;//类型      
	private Date monitorDate         ;//日期      
	private String monitorHref         ;//查看报告
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDataCollection() {
		return dataCollection;
	}
	public void setDataCollection(String dataCollection) {
		this.dataCollection = dataCollection;
	}
	public String getStudent() {
		return student;
	}
	public void setStudent(String student) {
		this.student = student;
	}
	public String getSpecTtem() {
		return specTtem;
	}
	public void setSpecTtem(String specTtem) {
		this.specTtem = specTtem;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getMonitorName() {
		return monitorName;
	}
	public void setMonitorName(String monitorName) {
		this.monitorName = monitorName;
	}
	public String getMonitorSchoolYear() {
		return monitorSchoolYear;
	}
	public void setMonitorSchoolYear(String monitorSchoolYear) {
		this.monitorSchoolYear = monitorSchoolYear;
	}
	public String getMonitorSemester() {
		return monitorSemester;
	}
	public void setMonitorSemester(String monitorSemester) {
		this.monitorSemester = monitorSemester;
	}
	public String getMonitorType() {
		return monitorType;
	}
	public void setMonitorType(String monitorType) {
		this.monitorType = monitorType;
	}
	 
	public String getMonitorHref() {
		return monitorHref;
	}
	public void setMonitorHref(String monitorHref) {
		this.monitorHref = monitorHref;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getMonitorDate() {
		return monitorDate;
	}
	public void setMonitorDate(Date monitorDate) {
		this.monitorDate = monitorDate;
	}
}

