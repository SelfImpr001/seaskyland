/*
 * @(#)com.cntest.fxpt.domain.CombinationSubject.java	1.0 2014年6月12日:下午2:47:55
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.domain;

import com.cntest.fxpt.anlaysis.bean.StudentSubjectScore;
import com.cntest.fxpt.repository.IAnalysisTestPaperDao;
import com.cntest.fxpt.util.SaveEtlProcessResultToFile;
import com.cntest.fxpt.util.SystemConfig;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月12日 下午2:47:55
 * @version 1.0
 */
public class CombinationSubject {
	private static String ALL_SUBJECT_TOTALSCORE = "全科毕业总分";
	private static String MASTER_SUBJECT_TOTALSCORE = "主科毕业总分";
	private static String EXTENSION_TOTALSCORE = "升学总分";
	
	private Long id;
	private String name;
	private int paperType;
	private Exam exam;
	private boolean isSysCreate = false;
	private String analysisTestPaperRule;

	private List<CombinationSubjectXTestPaper> childTestPaper;
	private List<AnalysisTestpaper> analysisTestpapers;
	private List<CombinationSubjectCalculateRule> combinationSubjectCalculateRules;

	private double fullScore = -1;
	private double kgScore = -1;
	private double zgScore = -1;
	private double aScore = -1;
	private double bScore = -1;
	private double kgaScore = -1;
	private double kgbScore = -1;
	private double zgaScore = -1;
	private double zgbScore = -1;

	public double getFullScore(IAnalysisTestPaperDao atpDa) {
		if (fullScore == -1) {
			if("wuhou".equals(SystemConfig.newInstance().getValue(
					"area.org.code"))){
				calculateScorewuhou(atpDa);
			}else{
				calculateScore(atpDa);
			}
		}
		return fullScore;
	}

	public double getKgScore(IAnalysisTestPaperDao atpDa) {
		if (kgScore == -1) {
			if("wuhou".equals(SystemConfig.newInstance().getValue(
					"area.org.code"))){
				calculateScorewuhou(atpDa);
			}else{
				calculateScore(atpDa);
			}
		}
		return kgScore;
	}

	public double getZgScore(IAnalysisTestPaperDao atpDa) {
		if (zgScore == -1) {
			if("wuhou".equals(SystemConfig.newInstance().getValue(
					"area.org.code"))){
				calculateScorewuhou(atpDa);
			}else{
				calculateScore(atpDa);
			}
		}
		return zgScore;
	}

	private void calculateScore(IAnalysisTestPaperDao atpDao) {
		if (childTestPaper != null) {
			fullScore = 0;
			kgScore = 0;
			zgScore = 0;
			for (CombinationSubjectXTestPaper csXtp : childTestPaper) {

				List<AnalysisTestpaper> analysisTestpapers = csXtp
						.getTestPaper().getAnalysisTestpapers();
				if (analysisTestpapers.size() == 1) {
					AnalysisTestpaper atp = analysisTestpapers.get(0);
					fullScore += atp.getFullScore();
					kgScore += atp.getKgScore();
					zgScore += atp.getZgScore();

				} else {
					for (AnalysisTestpaper atp : analysisTestpapers) {
						if (!atp.isComposite()) {
							fullScore += atp.getFullScore();
							kgScore += atp.getKgScore();
							zgScore += atp.getZgScore();
						}
					}
				}

			}
		}
	}
	
	private void calculateScorewuhou(IAnalysisTestPaperDao atpDao) {
		if (childTestPaper != null) {
//			String[] masterSubjects = loadMasterSubject();
//			List<String> masterSubjectsList = Arrays.asList(masterSubjects);
			fullScore = 0;
			kgScore = 0;
			zgScore = 0;

			for (CombinationSubjectXTestPaper csXtp : childTestPaper) {
				List<AnalysisTestpaper> analysisTestpapers = csXtp
						.getTestPaper().getAnalysisTestpapers();
				kgaScore = 0;
				kgbScore = 0;
				zgaScore = 0;
				zgbScore = 0;
				if (analysisTestpapers.size() == 1 && atpDao!=null) {
					AnalysisTestpaper atp = analysisTestpapers.get(0);
					zgaScore = atpDao.findCombinationSubjectZFFromDwdimitem(exam.getId(), atp.getTestPaper().getId(), "A","0");
					kgaScore = atpDao.findCombinationSubjectZFFromDwdimitem(exam.getId(), atp.getTestPaper().getId(), "A","1,2");
					zgbScore = atpDao.findCombinationSubjectZFFromDwdimitem(exam.getId(), atp.getTestPaper().getId(), "B","0");
					kgbScore = atpDao.findCombinationSubjectZFFromDwdimitem(exam.getId(), atp.getTestPaper().getId(), "B","1,2");
					if(aScore!=0 || bScore!=0){
						if(ALL_SUBJECT_TOTALSCORE.equals(this.getName())){
							fullScore += zgaScore+kgaScore;
							kgScore += kgaScore;
							zgScore += zgaScore;
						}else if(MASTER_SUBJECT_TOTALSCORE.equals(this.getName())){
								fullScore += zgaScore+kgaScore;
								kgScore += kgaScore;
								zgScore += zgaScore;
						}else if(EXTENSION_TOTALSCORE.equals(this.getName())){
							double sumScore = SaveEtlProcessResultToFile.convertSubjectScoreToSX(zgaScore+kgaScore,  zgbScore+kgbScore, atp.getName());
							double kgsumScore = SaveEtlProcessResultToFile.convertSubjectScoreToSX(kgaScore,kgbScore, atp.getName());
							double zgsumScore = SaveEtlProcessResultToFile.convertSubjectScoreToSX(zgaScore,  zgbScore, atp.getName());
							fullScore += sumScore;
							kgScore += kgsumScore;
							zgScore += zgsumScore;
						}else{
							fullScore += atp.getFullScore();
							kgScore += atp.getKgScore();
							zgScore += atp.getZgScore();
						}
					}else{
						fullScore += atp.getFullScore();
						kgScore += atp.getKgScore();
						zgScore += atp.getZgScore();
					}
				} else {
					for (AnalysisTestpaper atp : analysisTestpapers) {
						if (!atp.isComposite()&&atpDao!=null) {
//							zgaScore = atpDao.findCombinationSubjectZFFromDwdimitem(exam.getId(), atp.getTestPaper().getId(), "A","0");
//							kgaScore = atpDao.findCombinationSubjectZFFromDwdimitem(exam.getId(), atp.getTestPaper().getId(), "A","1,2");
//							zgbScore = atpDao.findCombinationSubjectZFFromDwdimitem(exam.getId(), atp.getTestPaper().getId(), "B","0");
//							kgbScore = atpDao.findCombinationSubjectZFFromDwdimitem(exam.getId(), atp.getTestPaper().getId(), "B","1,2");
							if(aScore!=0 || bScore!=0){
								if(ALL_SUBJECT_TOTALSCORE.equals(this.getName())){
									AnalysisTestpaper atpa  = findAnalysisTestpaperByStr(analysisTestpapers, "A");
									fullScore += atpa.getFullScore();
									kgScore += atpa.getKgScore();
									zgScore += atpa.getZgScore();
//									fullScore += zgaScore+kgaScore;
//									kgScore += kgaScore;
//									zgScore += zgaScore;
								}else if(MASTER_SUBJECT_TOTALSCORE.equals(this.getName())){
									AnalysisTestpaper atpa  = findAnalysisTestpaperByStr(analysisTestpapers, "A");
									fullScore += atpa.getFullScore();
									kgScore += atpa.getKgScore();
									zgScore += atpa.getZgScore();
//										fullScore += zgaScore+kgaScore;
//										kgScore += kgaScore;
//										zgScore += zgaScore;
								}else if(EXTENSION_TOTALSCORE.equals(this.getName())){
									AnalysisTestpaper atpa  = findAnalysisTestpaperByStr(analysisTestpapers, "SX");
									fullScore += atpa.getFullScore();
									kgScore += atpa.getKgScore();
									zgScore += atpa.getZgScore();
//									double sumScore = SaveEtlProcessResultToFile.convertSubjectScoreToSX(zgaScore+kgaScore,  zgbScore+kgbScore, atp.getName());
//									double kgsumScore = SaveEtlProcessResultToFile.convertSubjectScoreToSX(kgaScore,kgbScore, atp.getName());
//									double zgsumScore = SaveEtlProcessResultToFile.convertSubjectScoreToSX(zgaScore,  zgbScore, atp.getName());
//									fullScore += sumScore;
//									kgScore += kgsumScore;
//									zgScore += zgsumScore;
								}else{
									fullScore += atp.getFullScore();
									kgScore += atp.getKgScore();
									zgScore += atp.getZgScore();
								}
							}else{
								fullScore += atp.getFullScore();
								kgScore += atp.getKgScore();
								zgScore += atp.getZgScore();
							}
							
						}
					}
				}

			}
		}
	}
	
	private AnalysisTestpaper findAnalysisTestpaperByStr(List<AnalysisTestpaper> analysisTestpapers,String str){
		AnalysisTestpaper atp = null;
		for (AnalysisTestpaper analysisTestpaper : analysisTestpapers) {
			if(analysisTestpaper.getName().indexOf(str)!=-1){
				atp = analysisTestpaper;
				break;
			}
		}
		
		if(atp == null){
			for (AnalysisTestpaper analysisTestpaper : analysisTestpapers) {
					if(analysisTestpaper.getName().indexOf("SX")==-1){
						atp = analysisTestpaper;
						break;
					}
			}
		}
		return atp;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPaperType() {
		return paperType;
	}

	public void setPaperType(int paperType) {
		this.paperType = paperType;
	}

	public Exam getExam() {
		return exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

	public List<CombinationSubjectXTestPaper> getChildTestPaper() {
		return childTestPaper;
	}

	public void setChildTestPaper(
			List<CombinationSubjectXTestPaper> childTestPaper) {
		this.childTestPaper = childTestPaper;
	}

	public boolean isSysCreate() {
		return isSysCreate;
	}

	public void setSysCreate(boolean isSysCreate) {
		this.isSysCreate = isSysCreate;
	}

	public String getAnalysisTestPaperRule() {
		return analysisTestPaperRule;
	}

	public void setAnalysisTestPaperRule(String analysisTestPaperRule) {
		this.analysisTestPaperRule = analysisTestPaperRule;
	}

	public List<AnalysisTestpaper> getAnalysisTestpaper() {
		if (analysisTestpapers == null && childTestPaper != null) {
			analysisTestpapers = new ArrayList<>();
			for (CombinationSubjectXTestPaper csXtp : childTestPaper) {
				List<AnalysisTestpaper> analysisTestpapers = csXtp
						.getTestPaper().getAnalysisTestpapers();
//				if (analysisTestpapers.size() == 1) {
					this.analysisTestpapers.add(analysisTestpapers.get(0));
//				} else {
//					for (AnalysisTestpaper atp : analysisTestpapers) {
//						if (!atp.isComposite()) {
//							this.analysisTestpapers.add(atp);
////							break;
//						}
//					}
//				}
			}
		}
		return analysisTestpapers;
	}

	// 武侯定制化需求A卷、B卷
	public List<AnalysisTestpaper> getAnalysisTestpaperWuHou() {
		
		if ((analysisTestpapers==null?true:(analysisTestpapers.size()==0?true:false))  && childTestPaper != null) {
			analysisTestpapers = new ArrayList<>();
			String analysisTestPaperRule = getAnalysisTestPaperRule();
			if (null != analysisTestPaperRule && !"".equals(analysisTestPaperRule)) {
				String[] analysisTestPaperRules = analysisTestPaperRule.split("[|]");
				for (CombinationSubjectXTestPaper csXtp : childTestPaper) {
					for (String _analysisTestPaperRule : analysisTestPaperRules) {
						String[] bb = _analysisTestPaperRule.split("[_]");
						if (bb.length > 0) {
							Long analysisTestpaperId = Long.valueOf(bb[0]);
							List<AnalysisTestpaper> analysisTestpapers = csXtp.getTestPaper().getAnalysisTestpapers();
							for (AnalysisTestpaper atp : analysisTestpapers) {
								if (atp.getId() == analysisTestpaperId) {
									this.analysisTestpapers.add(atp);
								}
							}
						}
					}
				}
			} else {
				for (CombinationSubjectXTestPaper csXtp : childTestPaper) {
					List<AnalysisTestpaper> analysisTestpapers = csXtp.getTestPaper().getAnalysisTestpapers();
					if (analysisTestpapers.size() == 1) {
						this.analysisTestpapers.add(analysisTestpapers.get(0));
					} else {
						for (AnalysisTestpaper atp : analysisTestpapers) {
							if(ALL_SUBJECT_TOTALSCORE.equals(this.getName())){
								if(atp.getTestPaper().isContainPaper()){
									if(atp.getName().indexOf("A")!=-1){
										this.analysisTestpapers.add(atp);
										break;
									}
								}else{
									if(atp.getName().indexOf("SX")==-1){
										this.analysisTestpapers.add(atp);
										break;
									}
								}
							}else if(MASTER_SUBJECT_TOTALSCORE.equals(this.getName())){
								if(atp.getTestPaper().isContainPaper()){
									if(atp.getName().indexOf("A")!=-1){
										this.analysisTestpapers.add(atp);//如果是总分取全卷  如果是毕业成绩取A卷  升学成绩取B卷
										break;
									}
								}else{
									if(atp.getName().indexOf("SX")==-1){
										this.analysisTestpapers.add(atp);
										break;
									}
								}
							}else if(EXTENSION_TOTALSCORE.equals(this.getName())){
								if(atp.getName().indexOf("SX")!=-1){
									this.analysisTestpapers.add(atp);//如果是总分取全卷  如果是毕业成绩取A卷  升学成绩取B卷
									break;
								}
							}else{
								this.analysisTestpapers.add(atp);
								break;
							}
						}
					}
				}
			}
		}
		return analysisTestpapers;
	}

	ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
	ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("JavaScript");
	
	// 武侯定制化需求A卷、B卷
	public Double getAnalysisTestpaperWuHou(Long analysisTestpaperId, StudentSubjectScore score) throws ScriptException {

		if (childTestPaper != null) {
			analysisTestpapers = new ArrayList<>();
			for (CombinationSubjectXTestPaper csXtp : childTestPaper) { // 试卷
				String analysisTestPaperRule = getAnalysisTestPaperRule();
				// analysisTestPaperRule 存在两种格式   95|96 或 95_100-80:20,79-70:16,69-60:12,59-0:8|96_50-40:10,39-30:8,29-20:7
				if(null != analysisTestPaperRule && !"".equals(analysisTestPaperRule)) {
					String[] analysisTestPaperRules = analysisTestPaperRule.split("[|]");
					for (String _analysisTestPaperRule : analysisTestPaperRules) {
						String[] bb = _analysisTestPaperRule.split("[_]");
						Long _analysisTestpaperId = Long.valueOf(bb[0]);
						if(bb.length > 1) {
							List<AnalysisTestpaper> analysisTestpapers = csXtp.getTestPaper().getAnalysisTestpapers(); // 试卷的分析ID
							for (AnalysisTestpaper atp : analysisTestpapers) {
								if (atp.getId() == _analysisTestpaperId && analysisTestpaperId == _analysisTestpaperId)  {

									String script = "if(80 <= score && score <= 100) 20; else if(70 <= score && score < 80) 16; else if(60 <= score && score < 70) 12; else if(0 <= score && score < 60) 8;";
//									String script = "if(80 <= score && score <= 100) return 20; else if(70 <= score && score < 80) return 16; else if(60 <= score && score < 70) return 12; else if(0 <= score && score < 60) return 8;";
									script = bb[1];
									scriptEngine.put("score", score.getScore());
									Object result = scriptEngine.eval(script);
									
									System.out.println(result);
									return Double.valueOf(result.toString());
									/*
									String[] cc = bb[1].split("[,]");
									if(cc.length > 1) {
										for (String c : cc) {
											String[] dd = c.split("[:]");
											if(dd.length > 1) {
												String[] ee = dd[0].split("[-]");
												if(Double.valueOf(ee[0]) < Double.valueOf(ee[1]) && Double.valueOf(ee[0]) < score.getScore().getValue() && score.getScore().getValue() < Double.valueOf(ee[0])) {
													return Double.valueOf(dd[1]);
												}
											}
										}
									}
									*/
								}
							}
						} else {
							List<AnalysisTestpaper> analysisTestpapers = csXtp.getTestPaper().getAnalysisTestpapers();
							for (AnalysisTestpaper atp : analysisTestpapers) {
								if (atp.getId() == _analysisTestpaperId && analysisTestpaperId == _analysisTestpaperId) {
									return score.getScore().getValue();
								}
							}
						}
					}
				}
			}
		}
		return score.getScore().getValue();
	}

	public List<CombinationSubjectCalculateRule> getCombinationSubjectCalculateRules() {
		return combinationSubjectCalculateRules;
	}

	public void setCombinationSubjectCalculateRules(
			List<CombinationSubjectCalculateRule> combinationSubjectCalculateRules) {
		this.combinationSubjectCalculateRules = combinationSubjectCalculateRules;
	}
	
//	private String[] loadMasterSubject() {
//		String filed = SystemConfig.newInstance().getValue(
//				"master.subject");
//		return filed.split(",");
//	}
	


}
