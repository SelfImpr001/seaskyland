package com.cntest.fxpt.personalReport;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.Subject;
import com.cntest.fxpt.util.SystemConfig;


/**
 * 三、 各科小题得分统计及分项表现
 * 
 * @author Administrator
 * 
 */
public class SubjectTitleScore {
	PersonService personService;
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	Map<String, Double> mapContentScore;

	Map<String, Double> mapAbilityScore;

	Map<String, Double> mapTitleTypeScore;

	Map<String, Double> mapContentRatio;

	Map<String, Double> mapAbilityRatio;

	Map<String, Double> mapTitleTypeRatio;
	
	public String exec() throws NumberFormatException, Exception {
		StringBuffer buffer = new StringBuffer();
		// 科目列表
		Object[] student = personService.student;
		int wl = Integer.parseInt(student[4].toString());
		String studentid = student[3].toString();
		Exam exam = personService.exam;
		String mainKey = exam.getId()+"_"+wl;
		String topLevel = getTopLevel(exam.getLevelCode());
		buffer.append("<table width='100%' border='0' cellspacing='0' cellpadding='0' class='ontTable'>");
		buffer.append("<tr>");
		buffer.append("<td colspan='2'><div class='tableTilte'>三、各科小题得分统计及分项表现</div></td>");
		buffer.append("</tr>");
		List<AnalysisTestpaper> subjectList = personService.atpList;
		for (int i = 0; i < subjectList.size(); i++) {
			AnalysisTestpaper atp = subjectList.get(i);
			Subject subject = atp.getSubject();
			if(subject.isZF()==false){
				if (atp.isComposite() || (atp.getTestPaper().getPaperType()>0 && atp.getTestPaper().getPaperType()!=wl)) {
					continue;
				}
				HashMap<String, List<Object[]>> detailListMap = personService.allStudentImtesScoreMap.get(mainKey);
				if(detailListMap!=null && detailListMap.get(studentid+"_"+subject.getId())==null){
					continue;
				}	
			List<Object[]> listTopRatio = new ArrayList<Object[]>();
			HashMap<String, List<Object[]>> res = personService.allSubjectImtesRatioMap.get(mainKey);
			if(res!=null && res.size()>0)
			listTopRatio = res.get(subject.getId()+"");
			if(listTopRatio==null){
				continue;
			}
			Map<String, Double> topMap = new HashMap<String, Double>();
			for (int j = 0; j < listTopRatio.size(); j++) {
				Object[] ratio = (Object[]) listTopRatio.get(j);
				topMap.put(String.valueOf(ratio[0]), Double.parseDouble(ratio[1].toString()));
			}
			buffer.append("<tr><td colspan='2' align='center' valign='top'><div class='inTitleA'>"+subject.getName()+"</div></td></tr>");
			mapContentScore = new HashMap<String, Double>();
			mapAbilityScore = new HashMap<String, Double>();
			mapTitleTypeScore = new HashMap<String, Double>();
			mapContentRatio = new HashMap<String, Double>();
			mapAbilityRatio = new HashMap<String, Double>();
			mapTitleTypeRatio = new HashMap<String, Double>();
					buffer.append("<tr>");
					buffer.append("<td align='center' valign='top'><div class='insideDiv'>");
					buffer.append("<table width='99%' border='0' cellpadding='0' cellspacing='0' class='uu'>");
					buffer.append("<tr>");
					buffer.append("<th width='8%'><strong>题号</strong></th>");
					buffer.append("<th width='8%'><strong>满分</strong></th>");
					buffer.append("<th width='8%'><strong>得分</strong></th>");
					buffer.append("<th width='11%'><strong>" + topLevel + "平均得分率</strong></th>");
					buffer.append("<th width='10%'><strong>知识</strong></th>");
					buffer.append("<th width='10%'><strong>能力</strong></th>");
					buffer.append("<th width='15%'><strong>题型</strong></th>");
					buffer.append("</tr>");
					detailListMap = personService.allStudentImtesScoreMap.get(mainKey);
					if(detailListMap!=null){
						String key = studentid+"_"+subject.getId();
						List<Object[]> list = detailListMap.get(key);
						if(list!=null){
						for (int k = 0; k < list.size(); k++) {
							Object[] detail = (Object[]) list.get(k);
							if ((k & 1) != 0) {
								buffer.append("<tr class='odd'>");
							} else {
								buffer.append("<tr>");
							}
							buffer.append("<td width='6%'>" + detail[0] + "</td>");
							buffer.append("<td width='6%'>" + detail[1] + "</td>"); 
							double fullScore = Double.parseDouble(detail[1].toString());
							double score = Double.parseDouble(detail[2].toString());
							if(fullScore==score){
								buffer.append("<td width='6%' color='red'>" + detail[2] + "</th>");
							}else{
								buffer.append("<td width='6%' color='red'><h4>" + detail[2] + "</h4></th>");
							}
							buffer.append("<td width='7%'>" + FormatUtil.backspace0(topMap.get(detail[0].toString())*100) + "%</th>");
							buffer.append("<td width='16%'>" + detail[3] + "</th>");
							buffer.append("<td width='10%'>" + detail[4] + "</td>");
							buffer.append("<td width='10%'>" + detail[5] + "</td>");
							buffer.append("</tr>");
						}
						}
					}
					
					buffer.append("</table></div></td>");
					
					buffer.append("<td width='30%' align='center' valign='top'><div class='insideDiv'>");
					buffer.append(" <table width='99%' border='0' cellpadding='0' cellspacing='0' class='uu'>");
					buffer.append("<tr><th width='8%'><strong>分项表现</strong></th></tr>");
					buffer.append("<tr><td><div class='t1'>");
					buffer.append("<h3>(1)知识掌握情况：</h3>");
					buffer.append("<table width='90%' border='0' cellspacing='0' cellpadding='0' class='uu'>");
					buffer.append("<tr>");
					buffer.append("<th width='20%'>知识</th>");
					buffer.append("<th width='20%'>得分率</th>");
					buffer.append("<th colspan='2'>百分等级</th>");
					buffer.append("</tr>");
					
//					List<Object> listMyContentRatio = personService.getMyContentRatio(exam.getId(), wl, subject.getId(),String.valueOf(student[3]));
					String ckey = studentid+"_"+subject.getId()+"_"+wl;
					HashMap<String, List<Object[]>> resultMap = personService.allSubjectStduentKnowledgeContentScoreMap.get(mainKey);
					List<Object[]> listMyContentRatio = resultMap.get(ckey);
					if(listMyContentRatio!=null){
						for (int j = 0; j < listMyContentRatio.size(); j++) {
							Object[] myContentRatio = listMyContentRatio.get(j);
							String name = myContentRatio[2].toString();
							Double score = Double.parseDouble(myContentRatio[4].toString());
							String preLevel = "0";
							String key = subject.getId()+"_"+wl+"_"+name+"_"+score;
							HashMap<String, Object[]> resMap = personService.allSubjectKnowledgeContentMap.get(mainKey);
							Object[] result = resMap.get(key);
							if(result==null){
								continue;
							}
							double rank = Double.parseDouble(result[7].toString());
							double skrs = Double.parseDouble(personService.allSubjecetSkrsMap.get(mainKey).get(subject.getId()).toString());
							int sameLevel = Integer.parseInt(result[6].toString());
							double fullscore = Double.parseDouble(myContentRatio[3].toString());
							double pre = personService.getPerLevel(rank, skrs, sameLevel, score, fullscore);
							double ratio = Double.parseDouble(FormatUtil.format(Double.parseDouble(myContentRatio[4].toString())/Double.parseDouble(myContentRatio[3].toString()), "#.##"));
							buffer.append("<tr>");
							buffer.append("<td>" + name + "</td>");
							buffer.append("<td>" + ratio + "</td>");
							if(ratio==1.0){
								mapContentRatio.put(name, 100.0);
								preLevel = "100";
							}
							else if(ratio==0.0){
								mapContentRatio.put(name, 0.0);
								preLevel="0";
							}else{
								mapContentRatio.put(name, pre);
								preLevel = FormatUtil.format(pre,"#");
							}
							buffer.append(" <td style='text-align:left;' width='50%' title='" + FormatUtil.format(preLevel,"#") + "%'><div style='background:#7fbbf8; height:10px; border:1px solid #468fda; width:" + FormatUtil.format(preLevel,"#") + "%'></div></td>");
							buffer.append(" <td width='10%'>" + FormatUtil.format(preLevel,"#") + "%</td>");
							buffer.append("</tr>");
						}
					}
					buffer.append("</table>");
					buffer.append("<ul>");
					buffer.append("<li>");
					if(getContent(mapContentRatio, 3)!="" && getContent(mapContentRatio, 3).length()>0){
						buffer.append("<p>你的各个知识点成绩非常均衡，没有明显的优势知识点和劣势知识点。</p>");
					}else{
						if(mapContentRatio.size()>=2){
								buffer.append("<p align='left'>&ldquo;" + getContent(mapContentRatio, 1) + "&rdquo;<span class='c001'>掌握较好</span> <br />");
								buffer.append("&ldquo;" + getContent(mapContentRatio, 2) + "&rdquo;<span class='c002'>有待加强</span> </p>");
						}
					}
					buffer.append("</li>");
					buffer.append("</ul>");
					buffer.append("</div>");
					buffer.append("</td>");
					buffer.append("</tr>");
					
					
					buffer.append("<tr><td><div class='t1'>");
					buffer.append("<h3>(2)能力达成情况:</h3>");
					buffer.append("<table width='90%' border='0' cellspacing='0' cellpadding='0' class='uu'>");
					buffer.append("<tr>");
					buffer.append("<th width='20%'>能力</th>");
					buffer.append("<th width='20%'>得分率</th>");
					buffer.append("<th colspan='2'>百分等级</th>");
					buffer.append("</tr>");
//					List<Object> listMyAbilityRatio = personService.getMyAbilityRatio(exam.getId(), wl, subject.getId(),String.valueOf(student[3]));
					resultMap = personService.allSubjectStduentAbilityScoreMap.get(mainKey);
					List<Object[]> listMyAbilityRatio = resultMap.get(ckey);
					if(listMyAbilityRatio!=null){
						for (int j= 0; j < listMyAbilityRatio.size(); j++) {
							Object[] myAbilityRatio =listMyAbilityRatio.get(j);;
							String name = myAbilityRatio[2].toString();
							double score = Double.parseDouble(myAbilityRatio[4].toString());
							String key = subject.getId()+"_"+wl+"_"+name+"_"+score;
							HashMap<String, Object[]> resMap = personService.allSubjectAbilityMap.get(mainKey);
							Object[] result = resMap.get(key);
							if(result==null){
								continue;
							}
							double rank = Double.parseDouble(result[7].toString());
							double skrs = Double.parseDouble(personService.allSubjecetSkrsMap.get(mainKey).get(subject.getId()).toString());
							int sameLevel = Integer.parseInt(result[6].toString());
							double fullscore = Double.parseDouble(myAbilityRatio[3].toString());
							double pre = personService.getPerLevel(rank, skrs, sameLevel, score, fullscore);
							double ratio = Double.parseDouble(FormatUtil.format(Double.parseDouble(myAbilityRatio[4].toString())/Double.parseDouble(myAbilityRatio[3].toString()), "#.##"));
							buffer.append("<tr>");
							buffer.append("<td>" + name + "</td>");
							buffer.append("<td>" + ratio + "</td>");
							String preLevel = "0";
							if(ratio==1.0){
								mapAbilityRatio.put(name, 100.0);
								preLevel = "100";
							}
							else if(ratio==0.0){
								mapAbilityRatio.put(name, 0.0);
								preLevel="0";
							}else{
								mapAbilityRatio.put(name, pre);
								preLevel = FormatUtil.format(pre,"#");
							}
							buffer.append(" <td style='text-align:left;' width='50%' title='" + FormatUtil.format(preLevel,"#") + "%'><div style='background:#7fbbf8; height:10px; border:1px solid #468fda; width:" + FormatUtil.format(preLevel,"#") + "%'></div></td>");
							buffer.append(" <td width='10%'>" + FormatUtil.format(preLevel,"#") + "%</td>");
							buffer.append("</tr>");
						}
					}	
					buffer.append("</table>");
					buffer.append("<ul>");
					buffer.append("<li>");
					if(getContent(mapAbilityRatio, 3)!="" && getContent(mapAbilityRatio, 3).length()>0){
						buffer.append("<p>你的各个能力层次成绩非常均衡，没有明显的优势能力层次和劣势能力层次。</p>");
					}else{
						if(mapContentRatio.size()>=2){
								buffer.append("<p align='left'>&ldquo;" + getContent(mapAbilityRatio, 1) + "&rdquo;<span class='c001'>掌握较好</span> <br />");
								buffer.append("&ldquo;" + getContent(mapAbilityRatio, 2) + "&rdquo;<span class='c002'>有待加强</span> </p>");
						}
					}
					buffer.append("</li>");
					buffer.append("</ul>");
					buffer.append("</div>");
					buffer.append("</td>");
					buffer.append("</tr>");

					
					buffer.append("<tr><td><div class='t1'>");
					buffer.append("<h3>(3)各题型得分情况: </h3>");
					buffer.append("<table width='90%' border='0' cellspacing='0' cellpadding='0' class='uu'>");
					buffer.append("<tr>");
					buffer.append("<th width='20%'>题型</th>");
					buffer.append("<th width='20%'>得分率</th>");
					buffer.append("<th colspan='2'>百分等级</th>");
					buffer.append("</tr>");
//					List<Object> listMyTitleTypeRatio = personService.getMyTitleTypeRatio(exam.getId(), wl, subject.getId(),String.valueOf(student[3]));
					resultMap = personService.allSubjectStduentTitleTypeScoreMap.get(mainKey);
					List<Object[]> listMyTitleTypeRatio = resultMap.get(ckey);
					if(listMyTitleTypeRatio!=null){
						for (int j = 0; j < listMyTitleTypeRatio.size(); j++) {
							Object[] myTitleTypeRatio =listMyTitleTypeRatio.get(j);
							String name = myTitleTypeRatio[2].toString();
							double score = Double.parseDouble(myTitleTypeRatio[4].toString());
							String key = subject.getId()+"_"+wl+"_"+name+"_"+score;
							HashMap<String, Object[]> resMap = personService.allSubjectTitleTypeMap.get(mainKey);
							Object[] result = resMap.get(key);
							if(result==null){
								continue;
							}
							double rank = Double.parseDouble(result[7].toString());
							double skrs = Double.parseDouble(personService.allSubjecetSkrsMap.get(mainKey).get(subject.getId()).toString());
							int sameLevel = Integer.parseInt(result[6].toString());
							double fullscore = Double.parseDouble(myTitleTypeRatio[3].toString());
							double pre = personService.getPerLevel(rank, skrs, sameLevel, score, fullscore);
							double ratio = Double.parseDouble(FormatUtil.format(Double.parseDouble(myTitleTypeRatio[4].toString())/Double.parseDouble(myTitleTypeRatio[3].toString()), "#.##"));
							buffer.append("<tr>");
							buffer.append("<td>" + name + "</td>");
							buffer.append("<td>" + ratio + "</td>");
							String preLevel = "0";
							if(ratio==1.0){
								mapTitleTypeRatio.put(name, 100.0);
								preLevel = "100";
							}
							else if(ratio==0.0){
								mapTitleTypeRatio.put(name, 0.0);
								preLevel="0";
							}else{
								mapTitleTypeRatio.put(name, pre);
								preLevel = FormatUtil.format(pre,"#");
							}
							buffer.append(" <td style='text-align:left;' width='50%' title='" + FormatUtil.format(preLevel,"#") + "%'><div style='background:#7fbbf8; height:10px; border:1px solid #468fda; width:" + FormatUtil.format(preLevel,"#") + "%'></div></td>");
							buffer.append(" <td width='10%'>" + FormatUtil.format(preLevel,"#") + "%</td>");
							buffer.append("</tr>");
						}
					}
					buffer.append("</table>");
					buffer.append("<ul>");
					buffer.append("<li>");
					if(getContent(mapTitleTypeRatio, 3)!="" && getContent(mapTitleTypeRatio, 3).length()>0){
						buffer.append("<p>你的各个题型成绩非常均衡，没有明显的优势题型和劣势题型。</p>");
					}else{
						if(mapContentRatio.size()>=2){
								buffer.append("<p align='left'>&ldquo;" + getContent(mapTitleTypeRatio, 1) + "&rdquo;<span class='c001'>掌握较好</span> <br />");
								buffer.append("&ldquo;" + getContent(mapTitleTypeRatio, 2) + "&rdquo;<span class='c002'>有待加强</span> </p>");
						}
					}
					buffer.append("</li>");
					buffer.append("</ul>");
					buffer.append("</div>");
					buffer.append("</td>");
					buffer.append("</tr>");
					
					buffer.append("<tr><td><div style='font-size:12px;background:#F6FAFB; border:1px dashed #d6f1ff; padding:5px; margin:10px;text-align:left;'>");
					buffer.append("<strong>说明：</strong><br>");
					buffer.append("<p>知识、能力或题型得分的百分等级表示全区得分小于等于你的考生比例。上图中蓝色条块越长，对应的知识、能力或题型的得分越靠前。</p></div></td></tr>");
					buffer.append("</table>");
		}
	}
		return buffer.toString();
	}

	/**
	 * 百分等级得分模块与需要加强的模块
	 */
	public String getContent(Map<String, Double> map, int num) {
		List<String> str = new ArrayList<String>();
		Map<Double, Double> douMap = new HashMap<Double, Double>();
		List<Double> listMargin = new ArrayList<Double>();
		Set<String> keyset = map.keySet();
		Iterator<String> keys = keyset.iterator();
		String ret = "";
		while (keys.hasNext()) {
			String key = keys.next();
			str.add(key);
			Double score = map.get(key);
			listMargin.add(score);
			douMap.put(score, score);
		}
		if(listMargin !=null && listMargin.size()>0){
			Double[] temsp = listMargin.toArray(new Double[listMargin.size()]);
			if(temsp.length<2){
				ret="3333";
				return ret;
			}
			Collections.sort(listMargin);
			Double last = listMargin.get(listMargin.size() - 1);
			Double first = listMargin.get(0);
			Integer lastIndex = null;
			Integer firstIndex = null;
			List<Integer> lastIndexs = new ArrayList<Integer>();
			List<Integer> firstIndexs = new ArrayList<Integer>();
			for (int i = 0, j = temsp.length; i < j; i++) {
				if (last.doubleValue() == temsp[i].doubleValue()) {
					lastIndex = i;
					if (temsp.length >= 2) {
						lastIndexs.add(lastIndex);
					}
				}
				if (first.doubleValue() == temsp[i].doubleValue()) {
					firstIndex = i;
					if (temsp.length >= 2) {
						firstIndexs.add(firstIndex);
					}
				}
			}
			if (num == 1) {
				
				if (lastIndexs != null && lastIndexs.size() > 1) {
					for (int i = 0; i < lastIndexs.size(); i++) {
						ret += str.get(Integer.parseInt(lastIndexs.get(i).toString()));
						if (i != lastIndexs.size() - 1)
							ret += "、";
					}
				} else {
					if (lastIndex != null) {
						ret = str.get(lastIndex);
					}
				}
				return ret;
			} else if (num == 2) {
				
				if (firstIndexs != null && firstIndexs.size() > 1) {
					for (int i = 0; i < firstIndexs.size(); i++) {
						ret += str.get(Integer.parseInt(firstIndexs.get(i).toString()));
						if (i != firstIndexs.size() - 1)
							ret += "、";
					}
				} else {
					if (firstIndex != null) {
						ret = str.get(firstIndex);
					}
				}
				return ret;
			} else if(num == 3){
				if(firstIndexs.size()==keyset.size() ||lastIndexs.size()==keyset.size()){
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
			levelName="省";
		}else if(levelCode==2){
			levelName="市";
		}else if(levelCode==3){
			levelName="区";
		}else if(levelCode==4){
			levelName="校";
		}
		return levelName;
	}
}
