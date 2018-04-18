package com.cntest.fxpt.report.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cntest.fxpt.report.domain.Report;
import com.cntest.fxpt.report.domain.ReportForUser;
import com.cntest.fxpt.report.service.ReportService;
@Service("ReportService")
public class ReportServiceImpl implements ReportService{

	@Override
	public ReportForUser getReportListForUserId(String userId) {
		ReportForUser reportForUser = new ReportForUser();
		reportForUser.setUserId(userId);
		reportForUser.setListReports(getListReportData(userId));
		return reportForUser;
	}

	@Override
	public List<Report> getListReportData(String userId) {
		List<Report> listReports = new ArrayList<Report>();
		Report report = new Report();
		if(userId.equals("1")){
			report.setReportYear("2014");
			List<String> listReportName = new ArrayList<String>();
			listReportName.add("2014成绩单");
			listReportName.add("2014进步幅度");
			listReportName.add("2014模拟上线");
			report.setReportListName(listReportName);
			listReports.add(report);
			
			report = new Report();
			report.setReportYear("2013");
			listReportName = new ArrayList<String>();
			listReportName.add("2013成绩单");
			report.setReportListName(listReportName);
			listReports.add(report);
			
		}else if(userId.equals("2")){
			report.setReportYear("2013");
			List<String> listReportName = new ArrayList<String>();
			listReportName.add("2013进步幅度");
			listReportName.add("2013模拟上线");
			report.setReportListName(listReportName);
			listReports.add(report);
			
		}else{
			report.setReportYear("2012");
			List<String> listReportName = new ArrayList<String>();
			listReportName.add("2012模拟上线");
			report.setReportListName(listReportName);
			listReports.add(report);
		}
		
		return listReports;
	}

}
