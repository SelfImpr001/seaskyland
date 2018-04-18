/*
 * @(#)com.cntest.fxpt.personalReport.Avgs.java	1.0 2016年12月13日:下午2:11:49
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.personalReport;

import java.util.List;
import java.util.Map;

import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.Exam;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2016年12月13日 下午2:11:49
 * @version 1.0
 */
public class Avgs {
	PersonService personService;

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
	
	public String exec() {
		Exam exam = personService.exam;
		Object[] student = personService.student;
		String studentid = student[3].toString();
		int classId = Integer.parseInt(student[6].toString());
		int schoolId = Integer.parseInt(student[7].toString());
		int countyId = Integer.parseInt(student[8].toString());
		int wl = Integer.parseInt(student[4].toString());
		Map<Long, Double> allSubjectScoreMap  = personService.getStudentAllSubjectScore(exam.getId(), studentid, wl);
		Map<Long, Double> classScoreMap  = personService.getSocreByObjID(exam.getId(), classId, wl);
		Map<Long, Double> schoolScoreMap  = personService.getSocreByObjID(exam.getId(), schoolId, wl);
		Map<Long, Double> countyScoreMap  = personService.getSocreByObjID(exam.getId(), countyId, wl);
		
		StringBuffer allSubjects = new StringBuffer();
		StringBuffer studentScores = new StringBuffer();
		StringBuffer classScores = new StringBuffer();
		StringBuffer schoolScores = new StringBuffer();
		StringBuffer countyScores = new StringBuffer();
		
		List<AnalysisTestpaper> atpList = personService.atpList;
		for (int i = 0; i < atpList.size(); i++) {
			AnalysisTestpaper atp = atpList.get(i);
			if(!atp.getSubject().isZF()){
				if (atp.isComposite() || (atp.getTestPaper().getPaperType()>0 && atp.getTestPaper().getPaperType()!=wl)) {
					continue;
				}
				Long subjectid = atp.getSubject().getId();
				if(allSubjectScoreMap!=null && allSubjectScoreMap.get(subjectid)==null){
					continue;
				}
				allSubjects.append("<category label=\"" + atp.getName() + "\" />");
				if(allSubjectScoreMap!=null)
					studentScores.append("<set value=\"" + FormatUtil.format(allSubjectScoreMap.get(subjectid), "#.##") + "\" />");
				if(classScoreMap!=null)
					classScores.append("<set value=\"" + FormatUtil.format(classScoreMap.get(subjectid), "#.##") + "\" />");	
				if(schoolScoreMap!=null)
					schoolScores.append("<set value=\"" + FormatUtil.format(schoolScoreMap.get(subjectid), "#.##") + "\" />");	
				if(countyScoreMap!=null)
					countyScores.append("<set value=\"" + FormatUtil.format(countyScoreMap.get(subjectid), "#.##") + "\" />");	
			}
		}
		return getScriptForSubject(allSubjects,studentScores,classScores,schoolScores,countyScores);
	}
	
	public String getScriptForSubject(StringBuffer subjects, StringBuffer studentScores, StringBuffer classScores, StringBuffer schoolScores, StringBuffer countyScores) {
		StringBuffer buffer = new StringBuffer();
		String key = "anvksjflkdsjfkldsjfksld";
		buffer.append("<div class=\"insideDiv\" id=\"" + key + "__\"></div>");
		buffer.append("<script type=\"text/javascript\">");
		String fusionCharts = "FusionCharts/MSCombi2D.swf";
		buffer.append("var chart = new FusionCharts(\""+fusionCharts+"\", \"" + key + "_\", \"100%\", \"384\", \"0\", \"0\");");
		buffer.append("chart.setXMLData('" + getScriptForSubjectDetail(subjects, studentScores, classScores, schoolScores,countyScores) + "');");
		buffer.append("chart.render(\"" + key + "__\");");
		buffer.append("</script>");
		return buffer.toString();
	}
	public String getScriptForSubjectDetail(StringBuffer subjects, StringBuffer studentScores, StringBuffer classScores, StringBuffer schoolScores, StringBuffer countyScores) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<chart caption=\"\" showExportDataMenuItem=\"1\" palette=\"1\"   showValues=\"0\" divLineDecimalPrecision=\"1\" limitsDecimalPrecision=\"1\" yAxisName=\"\"  yAxisMaxValue=\"" + 1 + "\"  formatNumberScale=\"0\" baseFontSize=\"12\" baseFont=\"SunSim\" valuePosition=\"auto\">");
		buffer.append("<categories>");
		buffer.append(subjects.toString());
		buffer.append("</categories>");
		buffer.append("<dataset seriesName=\"你的得分\" renderAs=\"Bar\">");
		buffer.append(studentScores.toString());
		buffer.append("</dataset>");
		buffer.append("<dataset seriesName=\"班级平均分\" renderAs=\"Line\" color=\"#5CACEE\">");
		buffer.append(classScores.toString());
		buffer.append("</dataset>");
		buffer.append("<dataset seriesName=\"学校平均分\" renderAs=\"Line\">");
		buffer.append(schoolScores.toString());
		buffer.append("</dataset>");
		buffer.append("<dataset seriesName=\"区平均分\" renderAs=\"Line\">");
		buffer.append(countyScores.toString());
		buffer.append("</dataset>");
		buffer.append("</chart>");
		return buffer.toString();
	}
	
}
