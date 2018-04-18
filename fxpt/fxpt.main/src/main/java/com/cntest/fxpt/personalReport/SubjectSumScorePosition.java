package com.cntest.fxpt.personalReport;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tools.ant.SubBuildListener;

import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.Subject;
import com.cntest.fxpt.util.SystemConfig;


/**
 * 一、	总体表现   1、各科及总分在群体中的位置
 * @author Administrator
 */
public class SubjectSumScorePosition {
	
	PersonService personService;

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	String firstSubjectName = "";
	int studentNum = 0;
	double firstSubjectRatio = 0;
	public String exec(){
		Exam exam = personService.exam;
		//当前群体最高级
		StringBuffer buffer = new StringBuffer();
		buffer.append("<table width='100%' border='0' cellspacing='0' cellpadding='0' class='insideTable'>");
		buffer.append("<tr>");
		buffer.append("<td colspan='2'><div class='inTilte'>1、各科及总分在群体中的位置</div></td>");
		buffer.append("</tr>");
		buffer.append("<tr>");
		buffer.append("<td width='65%' align='center' valign='top'>");
		buffer.append("<div class='inLeftDiv'>");
		buffer.append("<table width='99%' border='0' cellspacing='0' cellpadding='0' class='uu' id='sp'>");
		buffer.append("<tr>");
		buffer.append("<th width='84'>科目</th>");
		buffer.append("<th width='84'>分数</th>");
		buffer.append("<th colspan='2'>百分等级</th>");
		buffer.append("</tr>");
		Object[] student = personService.student;
		String studentid = student[3].toString();
		String topLevel = getTopLevel(exam.getLevelCode());
		int wl = Integer.parseInt(student[4].toString());
		//科目列表 、有文理分科
		String mainKey = exam.getId()+"_"+wl;
		HashMap<String, Integer> res = personService.allSubjecetRankMap.get(mainKey);
		Map<String, Map<String, Object>> studentResMap = personService.allStudentSubjecetRankMap.get(mainKey);
	   int i= 0 ;
	   Map<String,Double> map = new HashMap<String, Double>();
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
					//单科
					if ((i & 1) != 0) {
						buffer.append("<tr class='odd'>");
					} else {
						buffer.append("<tr>");
					}
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
					if(i==0){
						firstSubjectName = subjectName;
						studentNum=skrs;
						firstSubjectRatio= pre;
					}
					buffer.append("<td>"+subjectName+"</td><td>"+String.valueOf(score)+"</td><td style='text-align:left;' title='"+FormatUtil.backspace0(pre)+"%' width='70%'><div style=\"background:#7fbbf8; height:10px; border:1px solid #468fda; width:"+FormatUtil.backspace0(pre)+"%;\"></div></td><td width='20px'>"+FormatUtil.backspace0(pre)+"%</td>");
					map.put(String.valueOf(subjectName),pre);
					
		    }
			}
		    i+=1;
		}
			
			//总分
			String zfKey = studentid;
			HashMap<String, HashMap<String, Object>> zfRes =  personService.allStudentZFScoreMap.get(mainKey);
			HashMap<String, Object> zfResult = zfRes.get(zfKey);
			if(zfResult!=null){
			int zfRank = Double.valueOf(zfResult.get("rank").toString()).intValue();
			HashMap<Integer, Integer> zfNumMap = personService.zfRankNumMap.get(mainKey);
			int sameNum =zfNumMap.get(zfRank);
			double fullScore = Double.parseDouble(zfResult.get("fullscore").toString());
			int skrs = Double.valueOf(zfResult.get("totalNum").toString()).intValue();
			Double totalScore = Double.parseDouble(zfResult.get("score").toString());
			double pre = personService.getPerLevel(zfRank,skrs,sameNum,totalScore,fullScore);
			buffer.append("<tr>");
			buffer.append("<td>总分</td><td>"+String.valueOf(totalScore)+"</td><td style='text-align:left;' title='"+FormatUtil.backspace0(pre)+"%' width='70%'><div style=\"background:#7fbbf8; height:10px; border:1px solid #468fda; width:"+FormatUtil.backspace0(pre)+"%;\"></div></td><td width='20px'>"+FormatUtil.backspace0(pre)+"%</td>");
			
			buffer.append("</tr></table></div>");
			buffer.append("</td><td width='35%' align='left' valign='top'>");
			buffer.append("<div class='inRightDiv'>");
			if(map.size()>=2){
				buffer.append("<strong>由各科在群体中的位置可得知：</strong><br />");
			}
			if(getContent(map, 3)!="" && getContent(map, 3).length()>0){
				buffer.append("<p>你的各个科目成绩非常均衡，没有明显的优势科目和劣势科目。</p>");
			}else{
				if(map.size()>=2){
					buffer.append("<ul><li>&ldquo;"+getContent(map,1)+"&rdquo;<span class='c001'>得分较好</span></li>");
					buffer.append("<li>&ldquo;"+getContent(map,2)+"&rdquo;<span class='c002'>尚需努力</span></li></ul>");
				}
			}
			buffer.append("<div style='font-size:12px;background:#F6FAFB; border:1px dashed #d6f1ff; padding:5px; margin:10px;'>");
			buffer.append("<strong>说明：</strong>");
			buffer.append("<p>各科或总分的百分等级表示"+topLevel+"分数小于等于你的考生比例。如&ldquo;"+firstSubjectName+"&rdquo;的百分等级为"+FormatUtil.backspace0(firstSubjectRatio)+"%，表示"+topLevel+""+studentNum+"名考生中，有"+studentNum+"*"+FormatUtil.backspace0(firstSubjectRatio)+"%约"+FormatUtil.backspace0(studentNum*(Double.parseDouble(FormatUtil.backspace0(firstSubjectRatio))/100))+"名考生的得分低于你。<strong>左图中蓝色条块越长，成绩越靠前。</strong></p></div>");
			buffer.append("</div></td></tr></table>");
			firstSubjectName="";
			firstSubjectRatio=0;
			studentNum=0;
			}
		
		return buffer.toString();
	}
	
	/**
	 * 百分等级得分较好的科目与百分等级得分需要加强的科目
	 */
	public String getContent(Map<String,Double> map,int num){
		List<String> str = new ArrayList<String>();
		Map<Double, Double> douMap = new HashMap<Double, Double>();
		List<Double> listMargin = new ArrayList<Double>();
		Set<String> keyset = map.keySet();
		Iterator<String> keys = keyset.iterator();
		String ret="";
		while (keys.hasNext()) {
			String key = keys.next();
			str.add(key);
			Double score = map.get(key);
			listMargin.add(score);
			douMap.put(score, score);
		}
		if(listMargin!=null && listMargin.size()>0){
			Double[] temsp = listMargin.toArray(new Double[listMargin.size()]);
			Collections.sort(listMargin);
			Double last = listMargin.get(listMargin.size() - 1);
			Double first = listMargin.get(0);
			Double lastSecond = null;
			Double firstSecond = null;
			if(listMargin.size()>=5){
				lastSecond = listMargin.get(listMargin.size() - 2);
				firstSecond = listMargin.get(1);
				}
			Integer lastIndex = null;
			Integer lastIndexSecond = null;
			Integer firstIndex = null;
			Integer firstIndexSecond = null;
			List<Integer> lastIndexs = new ArrayList<Integer>();
			List<Integer> firstIndexs = new ArrayList<Integer>();
			for (int i = 0, j = temsp.length; i < j; i++) {
				if (last.doubleValue() == temsp[i].doubleValue()) {
					lastIndex = i;
					if (temsp.length >= 2) {
						lastIndexs.add(lastIndex);
					}
				}
				if(keyset.size()>=5 && lastSecond!=null && lastSecond.doubleValue() == temsp[i].doubleValue()){
					lastIndexSecond = i;
				}
				if(first.doubleValue() == temsp[i].doubleValue()){
					firstIndex = i;
					if (temsp.length >= 2) {
						firstIndexs.add(firstIndex);
					}
				}
				if(keyset.size()>=5 && firstSecond!=null && firstSecond.doubleValue() == temsp[i].doubleValue()){
					firstIndexSecond = i;
				}
			}
			if(num==1){
				if (lastIndexs != null && lastIndexs.size() > 1) {
					for (int i = 0; i < lastIndexs.size(); i++) {
						ret += str.get(Integer.parseInt(lastIndexs.get(i).toString()));
						if (i != lastIndexs.size() - 1)
							ret += "、";
					}
				} else {
					if (lastIndex != null){
						ret = str.get(lastIndex);
						if(keyset.size()>=5 && lastIndexSecond!=null){
							ret = str.get(lastIndex);
							ret = ret+"、"+str.get(lastIndexSecond);
						}
					}
				}
				return ret;
			}else if(num==2){
				if (firstIndexs != null && firstIndexs.size() > 1) {
					for (int i = 0; i < firstIndexs.size(); i++) {
						ret += str.get(Integer.parseInt(firstIndexs.get(i).toString()));
						if (i != firstIndexs.size() - 1)
							ret += "、";
					}
				} else {
					if (firstIndex != null){
						ret = str.get(firstIndex);
						if(keyset.size()>=5 && firstIndexSecond!=null){
							ret = str.get(firstIndex);
							ret = ret+"、"+str.get(firstIndexSecond);
						}
					}
				}
				return ret;
			}else if(num==3){
				if(lastIndexs.size() == keyset.size() || firstIndexs.size() == keyset.size())
				{
					ret="3333";
					return ret;
				}
			}
		}
		return "";
	}
	
	public String getTopLevel(int levelCode){
		String levelName="全体";
		if(levelCode==1){
			levelName="全省";
		}else if(levelCode==2){
			levelName="全市";
		}else if(levelCode==3){
			levelName="全区";
		}else if(levelCode==4){
			levelName="全校";
		}
		return levelName;
	}
}
