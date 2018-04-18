package com.cntest.fxpt.report.domain;

import java.util.List;

public class ReportForUser {
	private String userId;
	private List<Report> listReports;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<Report> getListReports() {
		return listReports;
	}
	public void setListReports(List<Report> listReports) {
		this.listReports = listReports;
	}

}
