package com.cntest.fxpt.repository;

import java.util.List;
import java.util.Map;

import com.cntest.common.page.Page;
import com.cntest.fxpt.domain.Exam;

/**
 * 下载报表接口
 */
public interface IDownLoadReportDao {

	public Exam findById(Long examId);

	public List<Exam> list(Page<Exam> page);

	String[] findExamNations(Exam exam);
	
	public Map<String, List<?>> produceHoleData(Long examId);
	
	public Map<String, List<Map<String, Integer>>> gettotalscoreSegment(Long examId);
	
	public int getSingleMaxScore(Long examId);

}
