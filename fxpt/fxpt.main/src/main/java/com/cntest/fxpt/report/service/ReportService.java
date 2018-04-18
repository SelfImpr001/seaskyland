package com.cntest.fxpt.report.service;

import java.util.List;

import com.cntest.fxpt.report.domain.Report;
import com.cntest.fxpt.report.domain.ReportForUser;

public interface ReportService {
	public ReportForUser getReportListForUserId(String userId);
	public List<Report> getListReportData(String userId);
}
