package com.cntest.fxpt.domain;

/**
 * 学生，明细，成绩显示字段信息
 * @author cheny
 *
 */
public class ShowMessage {
	private Long id;
	private Long examid;
	private Long testPaperId;
	private String fieldName;
	private int isShow; //1 显示，0不显示
	private int showType; //1学生，2明细，3成绩
	
	
	public Long getTestPaperId() {
		return testPaperId;
	}
	public void setTestPaperId(Long testPaperId) {
		this.testPaperId = testPaperId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getExamid() {
		return examid;
	}
	public void setExamid(Long examid) {
		this.examid = examid;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fielName) {
		this.fieldName = fielName;
	}
	public int getIsShow() {
		return isShow;
	}
	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}
	public int getShowType() {
		return showType;
	}
	public void setShowType(int showType) {
		this.showType = showType;
	}

	
}
