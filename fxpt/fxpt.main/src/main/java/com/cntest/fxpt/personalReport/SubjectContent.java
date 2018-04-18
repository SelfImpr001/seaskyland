package com.cntest.fxpt.personalReport;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.Subject;
import com.cntest.fxpt.util.SystemConfig;
import com.mysql.jdbc.jdbc2.optional.SuspendableXAConnection;

/**
 * 二、各科内容表现
 * 
 * @author Administrator
 * 
 */
public class SubjectContent {
	PersonService personService;
	private static Logger logger = LoggerFactory.getLogger(SubjectContent.class);

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	public String exec() {
		
		Exam exam = personService.exam;
		Object[] student = personService.student;
		int wl = Integer.parseInt(student[4].toString());
		String mainKey = exam.getId()+"_"+wl;
		StringBuffer buffer = new StringBuffer();
		// 科目列表
		List<AnalysisTestpaper> atpList = personService.atpList;
		List<String> strs = new ArrayList<String>();
		for (int i = 0; i < atpList.size(); i++) {
			AnalysisTestpaper atp = atpList.get(i);
			if(!atp.getSubject().isZF()){
				if (atp.isComposite() || (atp.getTestPaper().getPaperType()>0 && atp.getTestPaper().getPaperType()!=wl)) {
					continue;
				}
				Subject subject = atp.getSubject();
				Map<String, Double> gaoTeam = null;
				Map<String, Double> diTeam = null;
				Map<String, Double> myTeam = null;
					//高分组低分组得分率
			try {
				String key = atp.getSubject().getId()+"_"+wl;
				HashMap<String,  List<Map<String, Object>>> allSubjectContentMap = personService.allSubjectHLRatioMap.get(mainKey);
				if(allSubjectContentMap!=null){
					List<Map<String, Object>> gdratio = allSubjectContentMap.get(key);
					gaoTeam = new HashMap<String, Double>();
					diTeam = new HashMap<String, Double>();
					if(gdratio!=null)
					for (int j = 0; j < gdratio.size(); j++) {
						Map<String, Object> ratio = gdratio.get(j);
						String contentname = ratio.get("name").toString().replace("\n", "");
						gaoTeam.put(String.valueOf(contentname), Double.parseDouble(ratio.get("hratio").toString()));
						diTeam.put(String.valueOf(contentname), Double.parseDouble(ratio.get("lratio").toString()));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			//当前学生得分率
			List<Object> myratio = personService.getMyContent(exam.getId(), subject.getId(), String.valueOf(student[3]));
			if(myratio!=null && myratio.size()<1){
				continue;
			}
			myTeam = new HashMap<String, Double>();
			for (int j = 0; j < myratio.size(); j++) {
				Object[] ratio = (Object[]) myratio.get(j);
				ratio[0] = ratio[0].toString().replace("\n", "");
				myTeam.put(String.valueOf(ratio[0]), Double.parseDouble(ratio[1].toString()));
			}
			if (gaoTeam != null && diTeam != null && myTeam != null) {
				StringBuffer gaoTeamBuffer = new StringBuffer();
				StringBuffer diTeamBuffer = new StringBuffer();
				StringBuffer myTeamBuffer = new StringBuffer();
				StringBuffer categoriesBuffer = new StringBuffer();
				Iterator<String> keys = myTeam.keySet().iterator();
				while (keys.hasNext()) {
					String key = keys.next();
					Double gaoValue = gaoTeam.get(key);
					Double diValue = diTeam.get(key);
					Double myValue = myTeam.get(key);
					categoriesBuffer.append("<category label=\"" + key + "\" />");
					gaoTeamBuffer.append("<set value=\"" + FormatUtil.format(gaoValue, "#.##") + "\" />");
					diTeamBuffer.append("<set value=\"" +  FormatUtil.format(diValue, "#.##") + "\" />");
					myTeamBuffer.append("<set value=\"" +  FormatUtil.format(myValue, "#.##") + "\" />");
				}
				strs.add(getScriptForSubject(subject, categoriesBuffer, gaoTeamBuffer, diTeamBuffer, myTeamBuffer));
			}
			}
		}
		if (CollectionUtils.isNotEmpty(strs)) {
			int i = 0;
			StringBuffer tempBuffer = new StringBuffer();
			for (String str : strs) {
				tempBuffer.append(str);
				i++;
				if (i != 0 && i % 3 == 0) {
					i = 0;
					buffer.append("<tr>");
					buffer.append(tempBuffer.toString());
					buffer.append("</tr>");
					tempBuffer = new StringBuffer();
				}
			}
			if (StringUtils.isNotEmpty(tempBuffer.toString())) {
				buffer.append("<tr>");
				buffer.append(tempBuffer.toString());
				if (3 - i == 1) {
					buffer.append("<td>&nbsp;</td>");
				}
				if (3 - i == 2) {
					buffer.append("<td colspan=\"2\">&nbsp;</td>");
				}
				buffer.append("</tr>");
			}
		}
		return buffer.toString();
		
	}

	public String getScriptForSubject(Subject subject, StringBuffer categoriesBuffer, StringBuffer gaoTeamBuffer, StringBuffer diTeamBuffer, StringBuffer myTeamBuffer) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<td width=\"33%\" align=\"center\" valign=\"top\"><div class=\"insideDiv\" id=\"" + subject.getId() + "__\"></div>");
		buffer.append("<script type=\"text/javascript\">");
		String fusionCharts = "FusionCharts/MSCombi2D.swf";
		buffer.append("var chart = new FusionCharts(\""+fusionCharts+"\", \"" + subject.getId() + "_\", \"100%\", \"384\", \"0\", \"0\");");
		buffer.append("chart.setXMLData('" + getScriptForSubjectDetail(subject, categoriesBuffer, gaoTeamBuffer, diTeamBuffer, myTeamBuffer) + "');");
		buffer.append("chart.render(\"" + subject.getId() + "__\");");
		buffer.append("</script>");
		buffer.append("</td>");
		return buffer.toString();
	}

	public String getScriptForSubjectDetail(Subject subject, StringBuffer categoriesBuffer, StringBuffer gaoTeamBuffer, StringBuffer diTeamBuffer, StringBuffer myTeamBuffer) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<chart caption=\"" + subject.getName() + "各项内容表现\" showExportDataMenuItem=\"1\" palette=\"1\"   showValues=\"0\" divLineDecimalPrecision=\"1\" limitsDecimalPrecision=\"1\" yAxisName=\"得分率\"  yAxisMaxValue=\"" + 1 + "\"  formatNumberScale=\"0\" baseFontSize=\"12\" baseFont=\"SunSim\">");
		buffer.append("<categories>");
		buffer.append(categoriesBuffer.toString());
		buffer.append("</categories>");
		buffer.append("<dataset seriesName=\"低分组(后27%)得分率\" renderAs=\"Line\">");
		buffer.append(diTeamBuffer.toString());
		buffer.append("</dataset>");
		buffer.append("<dataset seriesName=\"高分组(前27%)得分率\" renderAs=\"Line\">");
		buffer.append(gaoTeamBuffer.toString());
		buffer.append("</dataset>");
		buffer.append("<dataset seriesName=\"你的得分率\" renderAs=\"Line\">");
		buffer.append(myTeamBuffer.toString());
		buffer.append("</dataset>");
		buffer.append("</chart>");
		return buffer.toString();
	}

}
