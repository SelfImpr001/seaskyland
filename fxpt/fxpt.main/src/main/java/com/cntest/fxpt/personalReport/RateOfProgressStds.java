package com.cntest.fxpt.personalReport;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;


import com.cntest.fxpt.domain.Exam;

/**
 * 一、 总体表现 2、进步幅度
 * 
 * @author Administrator
 */
public class RateOfProgressStds {
	PersonService personService;

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	private String divId = null;
	
	List<Object[]> allSubjectListResult = null;
	List<Object[]> subjectResult = null;
	List<Object[]> subjectHisListResult = null;
	private List<String> inOutList;
	private Exam exam;

	public String exec() {
		subjectResult = new ArrayList<Object[]>();
		subjectHisListResult = new ArrayList<Object[]>();
		allSubjectListResult = new ArrayList<Object[]>();
		divId = UUID.randomUUID().toString();
		Object[] student = personService.student;
		exam = personService.exam;
		int wl = Integer.parseInt(student[4].toString());
		String mainKey = exam.getId()+"_"+wl;
		String studentid = student[3].toString();
		
//		allSubjectListResult =personService.getAllHissubjectSumScoreStds(exam.getId(),wl, String.valueOf(),exam.getOwnerCode());
		
		allSubjectListResult = personService.allStduentStdsMap.get(mainKey).get(studentid);
		
		//科目等级
		
//		List<Object> listSubjectLevel = personService.getStudentSubjectLevelProc(exam.getId(),wl,studentid);
		List<Object[]> listSubjectLevel = personService.allStduentStdsLevelMap.get(mainKey).get(studentid);
		StringBuffer subjectLevel = new StringBuffer();
		if(listSubjectLevel!=null && listSubjectLevel.size()>0){
			for (int i = 0; i < listSubjectLevel.size(); i++) {
				Object[] obj = (Object[]) listSubjectLevel.get(i);
				subjectLevel.append(obj[2]+"等级:"+obj[3]+";  ");
			}
		}
		if(allSubjectListResult!=null && allSubjectListResult.size()>0)
		for (int i = 0; i < allSubjectListResult.size(); i++) {
		    Object[] subject = (Object[]) allSubjectListResult.get(i);
		    if(Long.parseLong(subject[5].toString())==exam.getId()){
		    	subjectResult.add(subject);
		    }else{
		    	subjectHisListResult.add(subject);
		    }
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("");
		buffer.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"insideTable\">");
		buffer.append("<tr>");
		buffer.append(" <td colspan=\"2\"><div class=\"inTilte\">2、进步幅度(标准分)</div></td>");
		buffer.append("</tr>");
		buffer.append("<tr>");
		buffer.append("<td width=\"65%\" align=\"center\" valign=\"top\"><div id=\"" + divId + "\" class=\"insideDiv\"></div>" + getImageScript() + "</td>");
		buffer.append("<td width=\"35%\" align=\"left\" valign=\"top\">");

		buffer.append("<div class=\"inRightDiv\"> ");
		if (subjectHisListResult == null || subjectHisListResult.size()<1) {
			buffer.append("<strong>因没有以前的成绩记录，暂无法确定你的进退步幅度。</strong><br /><br /><br />");
			buffer.append(subjectLevel);
		} else {
			buffer.append("<strong>由进步幅度可得知：</strong><br />");
			for (String str : inOutList) {
				buffer.append(str);
			}
			buffer.append(subjectLevel);
		}
		buffer.append("<div style=\"font-size:12px;background:#F6FAFB; border:1px dashed #d6f1ff; padding:5px; margin:10px;\"> ");
		buffer.append("<strong>说明：</strong>");
		buffer.append("<p>橙色柱子指上次考试的标准分，蓝色柱子指本次考试的标准分。<strong>左图中蓝色柱子高于橙色柱子，表示该科或总分取得进步；反之表示退步</strong>。");
		// buffer.append(" <strong>左图中蓝色条块越长，成绩越靠前。</strong>");
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
		List<Object[]> subjectList = subjectResult;
		Collections.sort(subjectList, new Comparator<Object[]>() {
			@Override
			public int compare(Object[] o1, Object[] o2) {
				Integer value1 =Integer.parseInt(o1[0].toString());
				Integer value2 =Integer.parseInt(o2[0].toString());
				return value1.compareTo(value2);
			}
		});
		
		buffer.append("<chart  canvasBgColor=\"FEFEFE\" canvasBaseColor=\"FEFEFE\" toolTipBgColor=\"DEDEBE\" toolTipBorder=\"889E6D\" divLineColor=\"999999\" showColumnShadow=\"0\" divLineIsDashed=\"1\" divLineDashLen=\"1\" divLineDashGap=\"2\" numberPrefix=\"\" numberSuffix=\"\" chartRightMargin=\"30\">");
		StringBuffer bufferCategories = new StringBuffer();
		StringBuffer bufferDataset = new StringBuffer();
		StringBuffer bufferDatasetHis = new StringBuffer();
		bufferCategories.append("<categories>");
		bufferDataset.append("<dataset color=\"6292a3\">");
		if (subjectHisListResult != null && subjectHisListResult.size()>0) {
			bufferDatasetHis.append("<dataset color=\"FF9900\">");
		}
		inOutList = new ArrayList<String>();
			for (int i = 0; i < subjectList.size(); i++) {
				Object[] subject = (Object[]) subjectList.get(i);
				if(subject[0]!=null&&subject[1]!=null&&subject[2]!=null&&subject[3]!=null&&subject[4]!=null){
					bufferCategories.append("<category label=\"" + String.valueOf(subject[1]) + "\"/>");
					bufferDataset.append("<set  value=\"" + FormatUtil.format(Double.parseDouble(subject[4].toString()),"#.##") + "\"/>");
					if (subjectHisListResult != null && subjectHisListResult.size()>0) {
						List<Object[]> hissubjectList = subjectHisListResult;
						for (int j = 0; j < hissubjectList.size(); j++) {
							Object[] hisSubject = (Object[]) hissubjectList.get(j);
							if( (hisSubject[0].equals(subject[0]))){
								try {
									bufferDatasetHis.append("<set  value=\"" + FormatUtil.format(Double.parseDouble(hisSubject[4].toString()),"#.##") + "\"/>");
									inOutList.add("&ldquo;" + String.valueOf(subject[1]) + "&rdquo; " + calLevel(Double.parseDouble(subject[4].toString()), Double.parseDouble(hisSubject[4].toString())));
								} catch (Exception e) {
									bufferDatasetHis.append("<set  value=\"\"/>");
								}
							}
						}
					}
				}	
		}
		bufferDataset.append("</dataset>");
		bufferCategories.append("</categories>");
		buffer.append(bufferCategories.toString());
		buffer.append(bufferDataset.toString());
		if (subjectHisListResult != null && subjectHisListResult.size()>0) {
			bufferDatasetHis.append("</dataset>");
			buffer.append(bufferDatasetHis.toString());
		}
		buffer.append("</chart>");

		return buffer.toString();
	}

	public String calLevel(double per, double his) {
		String ret = "";
		double dd = 0;
//		if (his == 0) {
//			dd = 0.25;
//		} else {
//			dd = (per - his)/his;
//		}
//		if (dd >= 0.25) {
//			ret = "较大进步";
//		} else if (dd >= 0.05 && dd < 0.25) {
//			ret = "有所进步";
//		} else if (dd >= -0.05 && dd < 0.05) {
//			ret = "基本稳定";
//		} else if (dd >= -0.25 && dd < -0.05) {
//			ret = "有所退步";
//		} else if (dd <= -0.25) {
//			ret = "较大退步";
//		}
		
		if(per>his){
			ret = "有所进步";
		}else if(per<his){
			ret = "有所退步";
		}else{
			ret = "稳定";
		}
		
		if (ret.indexOf("退步") != -1) {
			ret = "<span class=\"c002\"><strong>" + ret + "</strong></span><br />";
		} else {
			ret = "<span><strong>" + ret + "</strong></span><br />";
		}
		// <span class=\"c002\"><strong>尚需努力</strong></span><br />
		/*
		 * 本次考试百分等级-上次考试百分等级）/上次考试百分等级=进步率 进步率>=0.25 较大进步 0.25>进步率>=0.05 有所进步 -0.05<进步率<0.05 基本稳定
		 * -0.25<进步率<=-0.05 有所退步 进步率 <=-0.25 较大退步
		 */
		return ret;
	}

}
