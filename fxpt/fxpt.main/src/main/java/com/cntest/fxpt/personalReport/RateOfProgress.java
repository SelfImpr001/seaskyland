package com.cntest.fxpt.personalReport;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.Subject;

/**
 * 一、 总体表现 2、进步幅度
 * 
 * @author Administrator
 */
public class RateOfProgress {
	PersonService personService;

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	private String divId = null;
	
	List<Object> allSubjectListResult = null;
	List<Object[]> subjectResult = null;
	List<Object[]> subjectHisListResult = null;
	private List<String> inOutList;
	private Exam exam;
	
	//当前考试所有科目百分等级
	Map<String,Double> curMap = null;
	
	//上次考试所有科目百分等级
	Map<String,Double> hisMap = null;
	
	int wl = 0;

	public String exec() {
		subjectResult = new ArrayList<Object[]>();
		subjectHisListResult = new ArrayList<Object[]>();
		allSubjectListResult = new ArrayList<Object>();
		exam = personService.exam;
		Object[] student = personService.student;
		String studentid = student[3].toString();
		wl = Integer.parseInt(student[4].toString());
		String mainKey = exam.getId()+"_"+wl;
		HashMap<String,Integer> res = personService.allSubjecetRankMap.get(mainKey);
		Map<String, Map<String, Object>> studentResMap = personService.allStudentSubjecetRankMap.get(mainKey);
		curMap = new HashMap<String, Double>();
		for(Object s : personService.atpList){
			AnalysisTestpaper sub = (AnalysisTestpaper) s;
			if(sub.getSubject().isZF()==false){
				if (sub.isComposite() || (sub.getTestPaper().getPaperType()>0 && sub.getTestPaper().getPaperType()!=wl)) {
					continue;
				}
				Long subjectid = sub.getSubject().getId();
				String stuKey = exam.getId()+"_"+studentid+"_"+subjectid;
				Map<String, Object> myResult = studentResMap.get(stuKey);
			    if(myResult!=null){
			    	 int rank = Double.valueOf(myResult.get("rank").toString()).intValue();
					//科目名称
					String subjectName = String.valueOf(myResult.get("subjectname"));
					//当前学生名次相同人数
					String sKey = exam.getId()+"_"+subjectid+"_"+rank;
					int sameNuM =  res.get(sKey);
					//得分
					double score = Double.parseDouble(myResult.get("score").toString());
					//科目满分
					double fullScore = Double.parseDouble(myResult.get("fullscore").toString());
					//实考人数
					Long skrsKey = subjectid;
					int skrs = personService.allSubjecetSkrsMap.get(mainKey).get(skrsKey);
					//百分等级
					double pre = personService.getPerLevel(rank,skrs*1.0, sameNuM, score,fullScore);
					curMap.put(String.valueOf(subjectName),pre);
			    }
			}
		}
		
		if(personService.hasHisExam()){
			Long hisExamid = personService.getHisExamidMap.get(exam.getId());
			String hisMainKey = hisExamid+"_"+wl;
			HashMap<String, Integer> hisRes = personService.allHisSubjecetRankMap.get(hisMainKey);
			Map<String, Map<String, Object>> hisStudentResMap = personService.allHisStudentSubjecetRankMap.get(hisMainKey);
			hisMap = new HashMap<String, Double>();
			for(Object s : personService.atpList){
				AnalysisTestpaper sub = (AnalysisTestpaper) s;
				if(sub.getSubject().isZF()==false){
					if (sub.isComposite() || (sub.getTestPaper().getPaperType()>0 && sub.getTestPaper().getPaperType()!=wl)) {
						continue;
					}
					Long subjectid = sub.getSubject().getId();
					String stuKey = hisExamid+"_"+studentid+"_"+subjectid;
					Map<String, Object> myResult = hisStudentResMap.get(stuKey);
				    if(myResult!=null){
				    int rank = Integer.parseInt(FormatUtil.backspace0(myResult.get("rank").toString()));
						//科目名称
						String subjectName = String.valueOf(myResult.get("subjectname"));
						//当前学生名次相同人数
						String sKey = hisExamid+"_"+subjectid+"_"+rank;
						int sameNuM = hisRes.get(sKey);
						//得分
						double score = Double.parseDouble(myResult.get("score").toString());
						//科目满分
						double fullScore = Double.parseDouble(myResult.get("fullscore").toString());
						//实考人数
						Long skrsKey = subjectid;
						int skrs = personService.allHisSubjecetSkrsMap.get(hisMainKey).get(skrsKey);
						//百分等级
						double pre = personService.getPerLevel(rank,skrs*1.0, sameNuM, score,fullScore);
						hisMap.put(String.valueOf(subjectName),pre);
				    }
				}
			}
		}
		
		
		divId = UUID.randomUUID().toString();
		StringBuffer buffer = new StringBuffer();
		buffer.append("");
		buffer.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"insideTable\">");
		buffer.append("<tr>");
		buffer.append(" <td colspan=\"2\"><div class=\"inTilte\">2、进步幅度</div></td>");
		buffer.append("</tr>");
		buffer.append("<tr>");
		buffer.append("<td width=\"65%\" align=\"center\" valign=\"top\"><div id=\"" + divId + "\" class=\"insideDiv\"></div>" + getImageScript() + "</td>");
		buffer.append("<td width=\"35%\" align=\"left\" valign=\"top\">");
		buffer.append("<div class=\"inRightDiv\"> ");
		if (hisMap == null || hisMap.size()==0) {
			buffer.append("<strong>因没有以前的成绩记录，暂无法确定你的进退步幅度。</strong><br /><br /><br />");

		} else {
			buffer.append("<strong>由进步幅度可得知：</strong><br />");
			for (String str : inOutList) {
				buffer.append(str);
			}
		}
		buffer.append("<div style=\"font-size:12px;background:#F6FAFB; border:1px dashed #d6f1ff; padding:5px; margin:10px;\"> ");
		buffer.append("<strong>说明：</strong>");
		buffer.append("<p>橙色柱子指上次考试的百分等级，蓝色柱子指本次考试的百分等级。<strong>左图中蓝色柱子高于橙色柱子，表示该科或总分取得进步；反之表示退步</strong>。");
		buffer.append("</p>");
		buffer.append("</div>");

		buffer.append("</div>");
		buffer.append("</td>");
		buffer.append("</tr>");
		buffer.append(" </table>");

		return buffer.toString();
	}

	/**
	 * 生成JS脚本
	 * 
	 * @return
	 */
	public String getImageScript() {

		StringBuffer buffer = new StringBuffer();
		buffer.append("<script type=\"text/javascript\">");
		buffer.append("$(function(){");
		buffer.append("var h=$(\"#sp\").height();");
		buffer.append("var w=$(\"#sp\").width();");
		String fusionCharts = "FusionCharts/MSBar2D.swf";
		buffer.append("var chart = new FusionCharts(\""+fusionCharts+"\", \"" + divId + "_\", \"100%\", h, \"0\", \"0\");");
		buffer.append("chart.setXMLData('" + getImageScriptDataString() + "');");
		buffer.append("chart.render(\"" + divId + "\");");
		buffer.append("});");
		buffer.append("</script>");
		return buffer.toString();
	}

	public String getImageScriptDataString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<chart  canvasBgColor=\"FEFEFE\" canvasBaseColor=\"FEFEFE\" toolTipBgColor=\"DEDEBE\" toolTipBorder=\"889E6D\" divLineColor=\"999999\" showColumnShadow=\"0\" divLineIsDashed=\"1\" divLineDashLen=\"1\" divLineDashGap=\"2\" numberPrefix=\"\" numberSuffix=\"%\" chartRightMargin=\"30\">");
		StringBuffer bufferCategories = new StringBuffer();
		StringBuffer bufferDataset = new StringBuffer();
		StringBuffer bufferDatasetHis = new StringBuffer();
		bufferCategories.append("<categories>");
		bufferDataset.append("<dataset color=\"6292a3\">");
		if (hisMap != null && hisMap.size()>0) {
			bufferDatasetHis.append("<dataset color=\"FF9900\">");
		}
		inOutList = new ArrayList<String>();
		for(Object s : personService.atpList){
			AnalysisTestpaper sub = (AnalysisTestpaper) s;
			if(sub.getSubject().isZF()==false){
				if (sub.isComposite() || (sub.getTestPaper().getPaperType()>0 && sub.getTestPaper().getPaperType()!=wl)) {
					continue;
				}
			if(curMap.get(sub.getName())!=null){	
				Subject subject = sub.getSubject();
				bufferCategories.append("<category label=\"" + subject.getName() + "\"/>");
				double per = curMap.get(subject.getName());			
				bufferDataset.append("<set  value=\"" + per + "\"/>");
				if (hisMap != null && hisMap.size()>0) {
					try {
						double his = hisMap.get(subject.getName());
						bufferDatasetHis.append("<set  value=\"" + his + "\"/>");
						inOutList.add("&ldquo;" + subject.getName() + "&rdquo; " + calLevel(per, his));
					} catch (Exception e) {
						bufferDatasetHis.append("<set  value=\"\"/>");
					}
				}
			}
		 }
		}
		bufferDataset.append("</dataset>");
		bufferCategories.append("</categories>");
		buffer.append(bufferCategories.toString());
		buffer.append(bufferDataset.toString());
		if (hisMap != null && hisMap.size()>0) {
			bufferDatasetHis.append("</dataset>");
			buffer.append(bufferDatasetHis.toString());
		}
		buffer.append("</chart>");
		
		return buffer.toString();
		
	}

	public String calLevel(double per, double his) {
		String ret = "";
		double dd = 0;
		if (his == 0) {
			dd = 0.25;
		} else {
			dd = (per - his)/his;
		}
		if (dd >= 0.25) {
			ret = "较大进步";
		} else if (dd >= 0.05 && dd < 0.25) {
			ret = "有所进步";
		} else if (dd >= -0.05 && dd < 0.05) {
			ret = "基本稳定";
		} else if (dd >= -0.25 && dd < -0.05) {
			ret = "有所退步";
		} else if (dd <= -0.25) {
			ret = "较大退步";
		}
		if (ret.indexOf("退步") != -1) {
			ret = "<span class=\"c002\"><strong>" + ret + "</strong></span><br />";
		} else {
			ret = "<span><strong>" + ret + "</strong></span><br />";
		}
		return ret;
	}

}
