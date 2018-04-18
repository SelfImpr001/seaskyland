package com.cntest.fxpt.service;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;

import com.cntest.common.page.Page;
import com.cntest.fxpt.domain.Exam;

public interface IDownLoadReportService {

	public Exam findById(Long examId);

	public List<Exam> list(Page<Exam> page);

	String[] findExamNations(Exam exam);
	
	public Map<String, List<?>> produceHoleData(Long examId);
	
	public boolean examTemplateCreate(Map<String, List<?>> excelData,File file,InputStream in,
			InputStream in1,InputStream in_area,
			InputStream in_score,InputStream in_totalScore,
			InputStream in_singleScore,InputStream in_singleTotalScore,
			InputStream in_knowledgeAnalysis,InputStream in_subjectiveObjectiveAnalysis,Long examId);
	
	public Workbook examTemplateCreateXlsx(Map<String, List<?>> excelData,File file,
			InputStream in_knowledgeAnalysis,InputStream in_subjectiveObjectiveAnalysis);

}
