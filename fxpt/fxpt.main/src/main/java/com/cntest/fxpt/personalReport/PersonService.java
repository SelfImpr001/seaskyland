/*
 * @(#)com.cntest.fxpt.personalReport.PersonService.java	1.0 2014年7月11日:下午5:30:10
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.personalReport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.cntest.fxpt.anlaysis.service.IPesonalReportExamService;
import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.Subject;
import com.cntest.fxpt.service.IAnalysisTestPaperService;
import com.cntest.fxpt.service.IExamService;
import com.cntest.fxpt.service.IExamStudentService;
import com.cntest.fxpt.util.LogUtil;
import com.cntest.fxpt.util.SystemConfig;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2014年7月11日 下午5:30:10
 * @version 1.0
 */
@Controller
@RequestMapping("/personalReport")
@Service("PersonService")
public class PersonService {
	public SubjectSumScorePosition subjectSumScorePosition;
	public RateOfProgress rateOfProgress;
	public SubjectContent subjectContent;
	public SubjectTitleScore subjectTitleScore;
    public RateOfProgressStds rateOfProgressStds;
    public Avgs avgs;
	public List<Object> allsubjectSumScore;
	public List<Object> subjectList;
	public List<Object> detailList;
	public List<Object> studentList;
	public Object[] student;
	public Exam exam;
	
	public List<AnalysisTestpaper> atpList;
	
	@Autowired(required = false)
	private IExamStudentService examStudentService;

	public PersonService() {
		super();
		subjectContent = new SubjectContent();
		subjectContent.setPersonService(this);
		rateOfProgress = new RateOfProgress();
		rateOfProgress.setPersonService(this);
		rateOfProgressStds = new RateOfProgressStds();
		rateOfProgressStds.setPersonService(this);
		subjectSumScorePosition = new SubjectSumScorePosition();
		subjectSumScorePosition.setPersonService(this);
		subjectTitleScore = new SubjectTitleScore();
		subjectTitleScore.setPersonService(this);
		avgs = new Avgs();
		avgs.setPersonService(this);
	}

	@Autowired(required = false)
	@Qualifier("IPesonalReportExamService")
	private IPesonalReportExamService reportExamService;

	@Autowired
	private IExamService examService;

	@Autowired
	private IAnalysisTestPaperService atpService;

	/**
	 * 拷贝依赖资源
	 * 
	 * @throws Exception
	 */
	public void copyInitResource(String fileDir) {
		try {
			String fileUrl = VelocityUtil.getFileUrl();
			File file = new File(fileDir);
			File sourceFile = new File(fileDir + "/FusionCharts");
			boolean ishave = sourceFile.exists();
			if (ishave == false) {
				FileUtils.copyDirectoryToDirectory(new File(fileUrl
						+ "/resources/FusionCharts"), file);
				FileUtils.deleteDirectory(new File(file, "FusionCharts/.svn"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * // 一 、总体表现 1、各科及总分在群体中的位置
	 * 
	 * @return
	 * @return String
	 * @author:黄洪成 2014年7月14日 上午11:10:10
	 */
	public String getSubjectSumScorePosition() {
		long begin = System.currentTimeMillis();
		String str = subjectSumScorePosition.exec();
		System.out.println("一 、总体表现 1、各科及总分在群体中的位置,处理时间:【"
				+ (System.currentTimeMillis() - begin) + "ms 】");
		return str;
	}

	/**
	 * // 一 、总体表现 2、进步幅度（百分等级）
	 * 
	 * @return
	 * @return String
	 * @author:黄洪成 2014年7月14日 上午11:12:50
	 */
	public String getRateOfProgress() {
		long begin = System.currentTimeMillis();
		String str = rateOfProgress.exec();
		System.out.println("一 、总体表现 2、进步幅度（百分等级）,处理时间:【"
				+ (System.currentTimeMillis() - begin) + "ms 】");
		return str;
	}
	
	
	/**
	 * // 一 、总体表现 2、进步幅度（标准分）
	 * 
	 * @return
	 * @return String
	 * @author:黄洪成 2014年7月14日 上午11:12:50
	 */
	public String getRateOfProgressStds() {
		long begin = System.currentTimeMillis();
		String str = rateOfProgressStds.exec();
		System.out.println("一 、总体表现 2、进步幅度(标准分),处理时间:【"
				+ (System.currentTimeMillis() - begin) + "ms 】");
		return str;
	}
	
	
	public String getAvgs(){
		long begin = System.currentTimeMillis();
		String str = avgs.exec();
		System.out.println("一 、总体表现 3、个人与群体的比较,处理时间:【"
				+ (System.currentTimeMillis() - begin) + "ms 】");
		return str;
	}
	
	

	/**
	 * // 二 、 各科内容表现
	 * 
	 * @return
	 * @return String
	 * @author:黄洪成 2014年7月14日 上午11:13:44
	 */
	public String getSubjectContent() {
		long begin = System.currentTimeMillis();
		String str = subjectContent.exec();
		System.out.println("二 、 各科内容表现,处理时间:【"
				+ (System.currentTimeMillis() - begin) + "ms 】");
		return str;
	}

	/**
	 * // 三 、各科小题得分统计及分项表现
	 * 
	 * @return
	 * @throws NumberFormatException
	 * @throws Exception
	 * @return String
	 * @author:黄洪成 2014年7月14日 上午11:14:10
	 */
	public String getSubjectTitleScore() throws NumberFormatException,
			Exception {
		long begin = System.currentTimeMillis();
		String str = subjectTitleScore.exec();
		System.out.println("三 、各科小题得分统计及分项表现,处理时间:【"
				+ (System.currentTimeMillis() - begin) + "ms 】");
		return str;
	}
   /**
   * <Pre>
   * 百分等级
   * </Pre>
   * @param rank 排名
   * @param skrs 实考人数
   * @param sameLevel 并列人数
   * @param score  得分
   * @param subjectTotalFullScorre  科目满分
   * @return
   * @return double
   * @author:黄洪成 2016年11月11日 上午10:31:40
    */
	public double getPerLevel(double rank, double skrs, int sameLevel,
			double score, double subjectTotalFullScorre) {
		double dd = 0.0;
		// 0分
		if (score == 0 || skrs==0) {
			dd = 0;
		}
		// 满分
		else if (score == subjectTotalFullScorre) {
			dd = 100;
		} else {
			//没有并列的情况
			if (sameLevel == 1) {
				dd = 100.0000 - ((100.0000 * rank - 50.0000) / skrs);
			//有并列的情况	
			} else {
				// (rank - 1.0000)： 排名在前面的人数         r=N+[（n+1）/2]其中N为排名在之前的人数，n为并列人数
				dd = (rank - 1.0000) + (sameLevel + 1.0000) / 2.0000;
				dd = 100.0000 - ((100.0000 * dd - 50.0000) / skrs);
			}
		}
		return Double.parseDouble((FormatUtil.format(dd, "#")));
	}

	public int getTopOrgId (String orgcode){
		return reportExamService.getTopOrgCode(orgcode);
	}
	
	public int getSameRank(Long examid,int wl,int rank){
		return reportExamService.getSameRank(examid,wl,rank);
	}
	
	public int getExamstudentNum(Long examid,Long subjectid,int wl,String objid){
		return reportExamService.getExamstudentNum(examid, subjectid, wl,objid);
	}
	
	
	public List<Object[]> getDetailList(Long examId,int wl, Long subjectId,String stuid) {
		List<Object[]> list = reportExamService.getDetailList(examId,wl, subjectId,stuid);
		return list;

	}
	
	public List<Object> getStudentSubjectLevelProc(Long examId,int wl,String studentId){
		List<Object> list = reportExamService.getStudentSubjectLevelProc(examId,wl, studentId);
		return list;
	}

	public List<Object> getAllsubjectSumScore(Long examId,int wl, String studentId,Long subjectid) {
		List<Object> list = reportExamService.getAllsubjectSumScoreProc(examId,wl,studentId,subjectid);
		return list;
	}
	
	public Object getzfSumScore(Long examId,int wl, String studentId) {
		return reportExamService.getzfSumScore(examId,wl,studentId);
	}

	public List<Object> getAllHissubjectSumScore(Long examId, int wl,String studentId) {
		List<Object> list = reportExamService.getAllHissubjectSumScoreProc(
				examId, wl,studentId);
		return list;
	}
	
	public List<Object> getAllHissubjectSumScoreStds(Long examId, int wl,String studentId,String objid) {
		List<Object> list = reportExamService.getAllHissubjectSumScoreStdsProc(
				examId, wl,studentId,objid);
		return list;
	}

	public List<Object[]> getAllContentRatio(Long examId,int wl,String objid) {
		List<Object[]> list = reportExamService.getAllContentRatioProc(examId,wl,objid);
		return list;
	}

	public List<Object[]> getTopRatio(Long examId,int wl,String orgcode) {
		List<Object[]> list = reportExamService
				.getTopRatioProc(examId,wl,orgcode);
		return list;
	}

	public List<Object> getMyContent(Long examId, Long subjectId,
			String studentId) {
		List<Object> list = reportExamService.getMyContentRatio(examId,
				subjectId, studentId);
		return list;
	}

	public List<Object[]> getMyContentRatio(Long examId, int wl,Long subjectId,
			String studentId) {
		List<Object[]> list = reportExamService.getMyContentRatioProc(examId,wl,
				subjectId, studentId);
		return list;
	}

	public List<Object[]> getMyAbilityRatio(Long examId,int wl, Long subjectId,
			String studentId) {
		List<Object[]> list = reportExamService.getMyAbilityRatioProc(examId,wl,
				subjectId, studentId);
		return list;
	}

	public List<Object[]> getMyTitleTypeRatio(Long examId,int wl, Long subjectId,
			String studentId) {
		List<Object[]> list = reportExamService.getMyTitleTypeRatioProc(examId,wl,
				subjectId, studentId);
		return list;
	}

	public Map<String, List<Object[]>> getAllStudentContentPreProc(Long examId,
			Long subjectId) {
		Map<String, List<Object[]>> map = reportExamService
				.getAllStudentContentPreProc(examId, subjectId);
		return map;
	}

	public Map<String, List<Object[]>> getAllStudentAbilityPreProc(Long examId,
			Long subjectId) {
		Map<String, List<Object[]>> map = reportExamService
				.getAllStudentAbilityPreProc(examId, subjectId);
		return map;
	}

	public Map<String, List<Object[]>> getAllStudentTitleTypePreProc(
			Long examId, Long subjectId) {
		Map<String, List<Object[]>> map = reportExamService
				.getAllStudentTitleTypePreProc(examId, subjectId);
		return map;
	}

	public int getAllExamStudentNumProc(Long examid, Long subjectId) {
		return reportExamService.getAllExamStudentNumProc(examid, subjectId);
	}
	
	
	public Map<Long, Double> getStudentAllSubjectScore(Long examid,String studentid,int wl){
		return reportExamService.getStudentAllSubjectScore(examid, studentid,wl);
	}
	
	public Map<Long,Double> getSocreByObjID(Long examid,int objid,int wl){
		return reportExamService.getSocreByObjID(examid, objid,wl);
	}

	public Long getHisExamid(Long examid){
		return reportExamService.getHisExamidByCurrExamid(examid);
	}

	public boolean hasHisExam(){
		boolean b = false;
		if(getHisExamidMap.get(this.exam.getId())!=null){
			b= true;
		}
		return b;
	}
	public Boolean Islog = true;
	public void setIslog(Boolean isLog){
		this.Islog = isLog;
	}
	@RequestMapping("/exec/{examid}/{studentId}")
	public String exec(@PathVariable Long examid, @PathVariable String studentId)
			throws Exception {
		String status = "失败",info = "<b style='color:red;'>",erre="";
		try {
			synchronized (this) {
				/**记录访问量*/
				if("1190201271".equals(studentId)){
					System.out.println("报错信息");
				}
				System.out.println(studentId+"开始生成");
				String fileDir = "";
				String dir = SystemConfig.newInstance().getValue("personReport.dir");
				if (dir == null || dir.equals("")) {
					WebApplicationContext webApplicationContext = ContextLoader
							.getCurrentWebApplicationContext();
					if (webApplicationContext != null) {
						ServletContext servletContext = webApplicationContext
								.getServletContext();
						fileDir = servletContext.getRealPath("/") + "student/report"
								+ File.separator + examid;
					} else {
						fileDir = VelocityUtil.class
								.getResource("")
								.getPath()
								.replace(
										"/target/classes/com/cntest/fxpt/personalReport/",
										"/src/main/webapp/student/" + examid);
					}
				} else {
					fileDir = SystemConfig.newInstance().getValue("personReport.dir")
							+ "/student/report/" + examid;
				}
				// 拷贝依赖资源
				copyInitResource(fileDir);
				String filePath = "";
				
				// 生成reportFirstPage

				filePath = fileDir + File.separator + "R_" + examid + "_"+ studentId + ".html";
				File file = new File(filePath);
				boolean ishave = file.exists();
				if (ishave == true) {
					String str = "/student/report/" + examid+"/R_" + examid+ "_" + studentId + ".html";
					return "redirect:" + str;
				} else {
					student = (Object[]) reportExamService.findStuentByExamIdAndStudentId(examid, studentId);
					exam = examService.findById(examid);
					atpList = atpService.listAllWith(examid);
					this.calculate();
					long begin, end;
					begin = System.currentTimeMillis();
					// 设置模板参数
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("examName", exam.getName());
					map.put("studentName", String.valueOf(student[0]));
					map.put("schoolName", String.valueOf(student[1]));
					map.put("className", String.valueOf(student[2]));
					map.put("studentCode", String.valueOf(student[3]));
					// 相关模块
					map.put("subjectSumScorePosition", getSubjectSumScorePosition());
					
					//个人报告版本	
//			String version = SystemConfig.newInstance().getValue("person.version");
//			if(version!=null && version.equals("2")){
//				//新版（进步幅度为标准分）
//				map.put("rateOfProgress", getRateOfProgressStds());
//			}else{
						//老版（进步幅度为百分等级）
						map.put("rateOfProgress", getRateOfProgress());
//			}
					//个人与群体的平均分对比
					map.put("avgs", getAvgs());
					map.put("subjectContent", getSubjectContent());
					map.put("subjectTitleScore", getSubjectTitleScore());
					String strHtml = "newFtStudent.html";
					String pageDate = VelocityUtil.getContent(strHtml, map);
					FileFactory.createHtml(filePath, pageDate);
					end = System.currentTimeMillis();
					System.out.println(allSubjecetRankMap.size()+"="+examid+"【单个报告完成，用时" + (end - begin) + "ms 】路径为："
							+ filePath);
				}
				String str = "/student/report/" + exam.getId() + "/R_" + exam.getId() + "_"
						+ student[3] + ".html";
				status="成功";
				return "redirect:" + str;
			}
		} catch (Exception e) {
			e.printStackTrace();
			erre = LogUtil.e(e);
			throw e;
		}finally {
			if(Islog) {
				Exam exam = examService.findById(examid);
				LogUtil.log("报告管理>查看报告", "查看报告",studentId,status,"查看个人报告:考试:"+exam.getName(),erre);
			}
		}
	}
	
	/**
	 * 是否已计算各科成绩及排名
	 * <br>key = examid+"_"+subjectid
	 */
	public final static HashMap<String,Boolean> isCacluteAllSubjectRankMap = new HashMap<String, Boolean>();
	
	
	/**
	 * 获取各科成绩排名
	 * <br>key = examid+"wl"
	 * <br>key = examid+"_"+subjectid+"_"+rank;
	 */
	public final HashMap<String,HashMap<String, Integer>> allSubjecetRankMap = new HashMap<String, HashMap<String, Integer>>();
	
	/**
	 * 获取(历次)各科成绩排名
	 * <br>key = examid+"wl"
	 * <br>key = examid+"_"+subjectid+"_"+rank;
	 */
	public final HashMap<String,HashMap<String, Integer>> allHisSubjecetRankMap = new HashMap<String, HashMap<String, Integer>>();
	
	
	/**
	 * 计算所有学生个人各科得分
	 * <br>key = examid+"_"+wl
	 * <br>key = stuExamid+"_"+stuDentId+"_"+stuSubjectid;
	 */
	public final HashMap<String,HashMap<String, Map<String, Object>>> allStudentSubjecetRankMap = new HashMap<String,HashMap<String, Map<String, Object>>>();
	
	/**
	 * 计算所有学生(历次)个人各科得分
	 * <br>key = examid+"_"+wl
	 * <br>key = stuExamid+"_"+stuDentId+"_"+stuSubjectid;
	 */
	public final HashMap<String,HashMap<String, Map<String, Object>>> allHisStudentSubjecetRankMap = new HashMap<String,HashMap<String, Map<String, Object>>>();
	
	
	/**
	 * 计算所有学生总分
	 * <br>key = examid+"_"+wl
	 * <br>key=studentid
	 */
	public final HashMap<String,HashMap<String, HashMap<String, Object>>> allStudentZFScoreMap = new HashMap<String, HashMap<String, HashMap<String, Object>>>();

	/**
	 * 计算总分排名人数
	 * <br>key = examid+"_"+wl  
	 * <br>key=rank
	 */
	public final HashMap<String,HashMap<Integer,Integer>> zfRankNumMap = new HashMap<String,HashMap<Integer,Integer>>();

	/**
	 * 计算所有科目实考人数
	 * <br>key = examid +"_"+wl
	 * <br>key = subjectid
	 */
	public final HashMap<String,HashMap<Long, Integer>> allSubjecetSkrsMap = new HashMap<String, HashMap<Long, Integer>>();
	
	
	/**
	 * 计算(历次)所有科目实考人数
	 * <br>key = examid +"_"+wl
	 * <br>key = subjectid
	 */
	public final HashMap<String,HashMap<Long, Integer>> allHisSubjecetSkrsMap = new HashMap<String, HashMap<Long, Integer>>();
	
	
	/**
	 * 计算所有学生标准分
	 * <br>key = examid+"_"+wl
	 * <br>key = studentid
	 */
	public final HashMap<String,HashMap<String, List<Object[]>>> allStduentStdsMap = new HashMap<String,HashMap<String, List<Object[]>>>();
	
	
	/**
	 * 计算所有学生标准分等级
	 * <br>key = examid+"_"+wl
	 * <br>key = studentid
	 */
	public final HashMap<String,HashMap<String, List<Object[]>>> allStduentStdsLevelMap = new HashMap<String,HashMap<String, List<Object[]>>>();
	
	
	/**
	 * 计算所有科目高低分组得分率
	 * <br>key = examid+"_"+wl
	 * <br>key =  subjectid+"_"+wl;
	 */
	public final HashMap<String,HashMap<String,  List<Map<String, Object>>>> allSubjectHLRatioMap = new HashMap<String,HashMap<String,  List<Map<String, Object>>>>();
	
	
	/**
	 * 计算所有科目所有小题得分率
	 * <br>key = examid+"_"+wl
	 * <br>key = subjectid
	 */
	public final HashMap<String,HashMap<String, List<Object[]>>> allSubjectImtesRatioMap = new HashMap<String,HashMap<String, List<Object[]>>>();
	
	
	/**
	 * 计算所有科目所有小题得分率
	 * <br>key = examid+"_"+wl
	 * <br>key = studentid+"_"+subjectid
	 */
	public final HashMap<String,HashMap<String, List<Object[]>>> allStudentImtesScoreMap = new HashMap<String,HashMap<String, List<Object[]>>>();
	
	
	/**
	 * 计算各科知识内容名次段人数
	 * <br>key = examid+"_"+wl
	 * <br>key = subjectid+"_"+wl+"_"+name+"_"+score
	 */
	public final HashMap<String,HashMap<String, Object[]>> allSubjectKnowledgeContentMap = new HashMap<String,HashMap<String,Object[]>>();
	
	/**
	 * 计算各科能力结构名次段人数
	 * <br>key = examid+"_"+wl
	 * <br>key = subjectid+"_"+wl+"_"+name+"_"+score
	 */
	public final HashMap<String,HashMap<String, Object[]>> allSubjectAbilityMap = new HashMap<String,HashMap<String, Object[]>>();
	
	/**
	 * 计算各科题型名次段人数
	 * <br>key = examid+"_"+wl
	 * <br>key = subjectid+"_"+wl+"_"+name+"_"+score
	 */
	public final HashMap<String,HashMap<String, Object[]>> allSubjectTitleTypeMap = new HashMap<String,HashMap<String, Object[]>>();
	
	
	/**
	 * 计算各科所有学生各知识内容得分
	 * <br>key = examid+"_"+wl
	 * <br>key = studentid+"_"+subjectid+"_"+wl+"_"+name+"_"+wl
	 */
	public final HashMap<String,HashMap<String, List<Object[]>>> allSubjectStduentKnowledgeContentScoreMap = new HashMap<String,HashMap<String, List<Object[]>>>();
	

	/**
	 * 计算各科所有学生各能力结构得分
	 * <br>key = examid+"_"+wl
	 * <br>key = studentid+"_"+subjectid+"_"+wl+"_"+name+"_"+wl
	 */
	public final HashMap<String,HashMap<String, List<Object[]>>> allSubjectStduentAbilityScoreMap = new HashMap<String,HashMap<String,List<Object[]>>>();
	

	/**
	 * 计算各科所有学生各题型得分
	 * <br>key = examid+"_"+wl
	 * <br>key = studentid+"_"+subjectid+"_"+wl+"_"+name+"_"+wl
	 */
	public final HashMap<String,HashMap<String, List<Object[]>>> allSubjectStduentTitleTypeScoreMap = new HashMap<String,HashMap<String,List<Object[]>>>();
	
	/**
	 * 历次考试ID
	 */
	public HashMap<Long, Long> getHisExamidMap = new HashMap<Long, Long>();
	
	
	public void clearTempTable(){
		reportExamService.clearTempTable();
	}
	
	/**
	 * 
	* <Pre>
	* 清理缓存
	* </Pre>
	* 
	* @return
	* @return boolean
	* @author:黄洪成 2016年12月27日 上午9:27:28
	 */
	public void clearMap(){
		allSubjecetRankMap.clear();
		allHisStudentSubjecetRankMap.clear();
		allStudentSubjecetRankMap.clear();
		allHisSubjecetRankMap.clear();
		allStudentZFScoreMap.clear();
		zfRankNumMap.clear();
		allSubjecetSkrsMap.clear();
		allHisSubjecetSkrsMap.clear();
		allStduentStdsMap.clear();
		allStduentStdsLevelMap.clear();
		allSubjectHLRatioMap.clear();
		allSubjectImtesRatioMap.clear();
		allStudentImtesScoreMap.clear();
		allSubjectKnowledgeContentMap.clear();
		allSubjectAbilityMap.clear();
		allSubjectTitleTypeMap.clear();
		allSubjectStduentKnowledgeContentScoreMap.clear();
		allSubjectStduentAbilityScoreMap.clear();
		allSubjectStduentTitleTypeScoreMap.clear();
		getHisExamidMap.clear();
	}
	
	
	/**
	 * 分析计算
	 * 
	 * @throws Exception
	 */
	public void calculate() throws Exception {
	int wl = Integer.parseInt(student[4].toString());
	long examid = exam.getId();	
	String stuid = student[3].toString();
	String mainKey = examid+"_"+wl;
	List<AnalysisTestpaper> subjectList = this.atpList;
	
	//存储历次考试ID
	if(!getHisExamidMap.containsKey(examid)){
		if(getHisExamid(examid)!=null){
			getHisExamidMap.put(examid, getHisExamid(examid));
		}
	}
	
	//计算所有学生个人各科得分
	if(!allStudentSubjecetRankMap.containsKey(mainKey)){
		System.out.println("计算所有学生个人各科得分");
		HashMap<String, Map<String, Object>> res = new HashMap<String, Map<String, Object>>();
		for (int i = 0; i < subjectList.size(); i++) {
			AnalysisTestpaper atp = (AnalysisTestpaper) subjectList.get(i);
			Subject subject = atp.getSubject();
			Long subjectid = subject.getId(); 
			if(subject.isZF()==false){
				if (atp.isComposite()|| (atp.getTestPaper().getPaperType() > 0 && atp.getTestPaper().getPaperType() != wl)) {
					continue;
				}
				List<Map<String, Object>> studentSubjectList = reportExamService.cacluteAllSubjectRankS(examid, wl,subjectid);
				for(Map<String, Object> o : studentSubjectList){
				    String stuExamid = o.get("examid").toString();
				    String stuDentId = o.get("studentid").toString();
				    String stuSubjectid = o.get("subjectid").toString();
				    String stuKey = stuExamid+"_"+stuDentId+"_"+stuSubjectid;
				    res.put(stuKey, o);
				}
			}
		}
		allStudentSubjecetRankMap.put(mainKey, res);
	}
	
	//计算(历次)所有学生个人各科得分
	if(this.hasHisExam()){
		Long hisExamid = getHisExamidMap.get(examid);
		String hisMainKey = hisExamid+"_"+wl;
		if(!allHisStudentSubjecetRankMap.containsKey(hisMainKey)){
			System.out.println("计算(历次)所有学生个人各科得分");
			HashMap<String, Map<String, Object>> res = new HashMap<String, Map<String, Object>>();
			for (int i = 0; i < subjectList.size(); i++) {
				AnalysisTestpaper atp = (AnalysisTestpaper) subjectList.get(i);
				Subject subject = atp.getSubject();
				Long subjectid = subject.getId(); 
				if(subject.isZF()==false){
					if (atp.isComposite()|| (atp.getTestPaper().getPaperType() > 0 && atp.getTestPaper().getPaperType() != wl)) {
						continue;
					}
					List<Map<String, Object>> studentSubjectList = reportExamService.cacluteAllSubjectRankS(hisExamid, wl,subjectid);
					for(Map<String, Object> o : studentSubjectList){
					    String stuExamid = o.get("examid").toString();
					    String stuDentId = o.get("studentid").toString();
					    String stuSubjectid = o.get("subjectid").toString();
					    String stuKey = stuExamid+"_"+stuDentId+"_"+stuSubjectid;
					    res.put(stuKey, o);
					}
				}
			}
			allHisStudentSubjecetRankMap.put(hisMainKey, res);
		}
	}
	
	
	//计算所有学生总分
	if(!allStudentZFScoreMap.containsKey(mainKey)){
		System.out.println("计算所有学生总分");
		List<Map<String, Object>> list = reportExamService.cacluteZFScoreS(examid, wl);
		HashMap<String, HashMap<String, Object>> res = new HashMap<String, HashMap<String, Object>>();
		for(Map<String, Object> o : list){
		    String stuDentId = o.get("studentid").toString();
		    String key = stuDentId;
		    res.put(key, (HashMap<String, Object>) o);
		}
		allStudentZFScoreMap.put(mainKey, res);
	}
	//计算总分排名人数
	if(!zfRankNumMap.containsKey(mainKey)){
		System.out.println("计算总分排名人数");
		HashMap<String, HashMap<String, Object>> ress = allStudentZFScoreMap.get(mainKey);
		HashMap<Integer,Integer> res = new HashMap<Integer,Integer>();
		Iterator it = ress.keySet().iterator();
		while(it.hasNext()){
			String key = (String) it.next();
			Map<String, Object> m =ress.get(key);
			int rank = Double.valueOf(m.get("rank").toString()).intValue();
			if(res.containsKey(rank)){
				int samenum = res.get(rank);
				samenum+=1;
				res.put(rank, samenum);
			}else{
				res.put(rank, 1);
			}
		}
		zfRankNumMap.put(mainKey, res);
	}
	
	
	//获取各科成绩排名
	if(!allSubjecetRankMap.containsKey(mainKey)){
		System.out.println("获取各科成绩排名");
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		Map<String, Map<String, Object>> ress= allStudentSubjecetRankMap.get(mainKey);
		Iterator it = ress.keySet().iterator();
		while(it.hasNext()){
			String key = (String) it.next();
			Map<String, Object> m =ress.get(key);
			String subjectid = m.get("subjectid").toString();
			String rank = Double.valueOf(m.get("rank").toString()).intValue()+"";
			String sKey = examid+"_"+subjectid+"_"+rank;
			if(map.containsKey(sKey)){
				int samenum = map.get(sKey);
				samenum+=1;
				map.put(sKey, samenum);
			}else{
				map.put(sKey, 1);
			}
		}
		allSubjecetRankMap.put(mainKey, map);
		
	}
	
	if(this.hasHisExam()){
		Long hisExamid = getHisExamidMap.get(examid);
		String hisMainKey = hisExamid+"_"+wl;
		if(!allHisSubjecetRankMap.containsKey(hisMainKey)){
			System.out.println("获取(历次)各科成绩排名");
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			Map<String, Map<String, Object>> ress= allHisStudentSubjecetRankMap.get(hisMainKey);
			Iterator it = ress.keySet().iterator();
			while(it.hasNext()){
				String key = (String) it.next();
				Map<String, Object> m =ress.get(key);
				String subjectid = m.get("subjectid").toString();
				String rank = Double.valueOf(m.get("rank").toString()).intValue()+"";
				String sKey = hisExamid+"_"+subjectid+"_"+rank;
				if(map.containsKey(sKey)){
					int samenum = map.get(sKey);
					samenum+=1;
					map.put(sKey, samenum);
				}else{
					map.put(sKey, 1);
				}
			}
			allHisSubjecetRankMap.put(hisMainKey, map);
		}
	}
	
	
	//计算所有科目实考人数
	if(!allSubjecetSkrsMap.containsKey(mainKey)){
		System.out.println("计算所有科目实考人数");
		List<Object[]> resList = reportExamService.getAllSubjectSkrs(examid, wl, exam.getOwnerCode());
		HashMap<Long, Integer> res = new HashMap<Long, Integer>(); 
		for(Object[] o : resList){
			Long subjectid = Long.parseLong(o[2].toString());
			int skrs = Integer.parseInt(o[3].toString());
			if(!res.containsKey(subjectid)){
				res.put(subjectid, skrs);
			}
		}
		allSubjecetSkrsMap.put(mainKey, res);
	 }
	
	
	//计算（历次）所有科目实考人数
	if(this.hasHisExam()){
		Long hisExamid = getHisExamidMap.get(examid);
		String hisMainKey = hisExamid+"_"+wl;
		if(!allHisSubjecetSkrsMap.containsKey(hisMainKey)){
			System.out.println("计算（历次）所有科目实考人数");
			List<Object[]> resList = reportExamService.getAllSubjectSkrs(hisExamid, wl, exam.getOwnerCode());
			HashMap<Long, Integer> res = new HashMap<Long, Integer>(); 
			for(Object[] o : resList){
				Long subjectid = Long.parseLong(o[2].toString());
				int skrs = Integer.parseInt(o[3].toString());
				if(!res.containsKey(subjectid)){
					res.put(subjectid, skrs);
				}
			}
			allHisSubjecetSkrsMap.put(hisMainKey, res);
		 }
	}
	
	//计算标准分
	if(!allStduentStdsMap.containsKey(mainKey)){
		System.out.println("计算标准分");
		List<Object[]> list = reportExamService.cacluteStudentAllSubjectsStd(examid,exam.getOwnerCode());
		HashMap<String, List<Object[]>> res = new HashMap<String, List<Object[]>>();
		for(Object[] o : list){
			String studentid = o[2].toString();
			if(res.containsKey(studentid)){
				List<Object[]> stuList = res.get(studentid);
				stuList.add(o);
				res.put(studentid, stuList);
			}else{
				List<Object[]> stuList = new ArrayList<Object[]>();
				stuList.add(o);
				res.put(studentid, stuList);
			}
		}
		allStduentStdsMap.put(mainKey, res);
	}
	
	//计算标准分等级
	if(!allStduentStdsLevelMap.containsKey(mainKey))
	{
		System.out.println("计算标准分等级");
		List<Object[]> list = reportExamService.getStduentSLevel(examid);
		HashMap<String, List<Object[]>> res = new HashMap<String, List<Object[]>>();
		for(Object[] o : list){
		  String studentid = o[4].toString();
		  if(res.containsKey(studentid)){
			  List<Object[]> stuList = res.get(studentid);
			  stuList.add(o);
			  res.put(studentid, stuList);
		  }else{
			  List<Object[]> stuList = new ArrayList<Object[]>();
			  stuList.add(o);
			  res.put(studentid, stuList);
		  }
		}
		allStduentStdsLevelMap.put(mainKey, res);
	}
	
	//计算所有科目高低分组得分
	if(!allSubjectHLRatioMap.containsKey(mainKey)){
		System.out.println("计算所有科目高低分组得分");
		List<Map<String, Object>> list = reportExamService.getAllContentRatioProcS(examid,wl,exam.getOwnerCode());
		HashMap<String,  List<Map<String, Object>>> res = new HashMap<String,  List<Map<String, Object>>>();
		for(Map<String, Object> o : list){
		  String subjectid = o.get("subjectid").toString();
		  String swl =  o.get("wl").toString();
		  String skey = subjectid+"_"+swl;
		  if(res.containsKey(skey)){
			  List<Map<String, Object>> stuList = res.get(skey);
			  stuList.add(o);
			  res.put(skey, stuList);
		  }else{
			  List<Map<String, Object>> stuList = new ArrayList<Map<String, Object>>();
			  stuList.add(o);
			  res.put(skey, stuList);
		  }
		}
		allSubjectHLRatioMap.put(mainKey, res);
	}
	//计算所有科目所有小题得分率
	if(!allSubjectImtesRatioMap.containsKey(mainKey)){
		System.out.println("计算所有学生各科成绩小题成绩");
		List<Object[]> list = reportExamService.getTopRatioProc(examid, wl, exam.getOwnerCode());
		HashMap<String, List<Object[]>> res = new HashMap<String, List<Object[]>>();
		for(Object[] o : list){
		  String subjectid = o[2].toString();
		  if(res.containsKey(subjectid)){
			  List<Object[]> stuList = res.get(subjectid);
			  stuList.add(o);
			  res.put(subjectid, stuList);
		  }else{
			  List<Object[]> stuList = new ArrayList<Object[]>();
			  stuList.add(o);
			  res.put(subjectid, stuList);
		  }
		}
		allSubjectImtesRatioMap.put(mainKey, res);
	}
	
	//计算所有学生各科成绩
		HashMap<String, List<Object[]>> res = new HashMap<String, List<Object[]>>();
			for (int i = 0; i < subjectList.size(); i++) {
				AnalysisTestpaper atp = subjectList.get(i);
				Subject subject = atp.getSubject();
				Long subjectid = subject.getId(); 
				if(subject.isZF()==false){
					if (atp.isComposite()|| (atp.getTestPaper().getPaperType() > 0 && atp.getTestPaper().getPaperType() != wl)) {
						continue;
					}
					List<Object[]> list = reportExamService.getDetailList(examid, wl, subjectid,stuid);
					for(Object[] o : list){
					  String studentid = o[6].toString();
					  String ssKey = studentid+"_"+subjectid;
					  if(res.containsKey(ssKey)){
						  List<Object[]> stuList = res.get(ssKey);
						  stuList.add(o);
						  res.put(ssKey, stuList);
					  }else{
						  List<Object[]> stuList = new ArrayList<Object[]>();
						  stuList.add(o);
						  res.put(ssKey, stuList);
					  }
					}
					}
		allStudentImtesScoreMap.put(mainKey, res);
		
			}
	if(!allSubjectKnowledgeContentMap.containsKey(mainKey)){
		List<Object[]> list = reportExamService.getAllSubjectKnowledgeContentMap(examid, wl);
		HashMap<String, Object[]> ress = new HashMap<String,Object[]>();
		for(Object[] o : list){
		  String kcKey =  o[1].toString()+"_"+o[4].toString()+"_"+o[3].toString()+"_"+o[5].toString();
		  if(!ress.containsKey(kcKey)){
			  ress.put(kcKey, o);
		  }
		}
		allSubjectKnowledgeContentMap.put(mainKey, ress);
	}
	
	if(!allSubjectAbilityMap.containsKey(mainKey)){
		List<Object[]> list = reportExamService.getAllSubjectAbilityMapMap(examid, wl);
		HashMap<String,Object[]> ress = new HashMap<String, Object[]>();
		for(Object[] o : list){
		  String kcKey =  o[1].toString()+"_"+o[4].toString()+"_"+o[3].toString()+"_"+o[5].toString();
		  if(!ress.containsKey(kcKey)){
			  ress.put(kcKey, o);
		  }
		}
		allSubjectAbilityMap.put(mainKey, ress);
	}
	
	if(!allSubjectTitleTypeMap.containsKey(mainKey)){
		List<Object[]> list = reportExamService.getAllSubjectTitleTypeMap(examid, wl);
		HashMap<String, Object[]> ress = new HashMap<String,Object[]>();
		for(Object[] o : list){
		  String kcKey =  o[1].toString()+"_"+o[4].toString()+"_"+o[3].toString()+"_"+o[5].toString();
		  if(!ress.containsKey(kcKey)){
			  ress.put(kcKey, o);
		  }
		}
		allSubjectTitleTypeMap.put(mainKey, ress);
	}
	if(!allSubjectStduentKnowledgeContentScoreMap.containsKey(mainKey)){
		System.out.println("开始计算各科所有学生知识内容得分");
		long begin = System.currentTimeMillis();
		HashMap<String, List<Object[]>> ress = new HashMap<String,List<Object[]>>();
		for (int i = 0; i < subjectList.size(); i++) {
			AnalysisTestpaper atp = (AnalysisTestpaper) subjectList.get(i);
			Subject subject = atp.getSubject();
			Long subjectid = subject.getId(); 
			if(subject.isZF()==false){
				if (atp.isComposite()|| (atp.getTestPaper().getPaperType() > 0 && atp.getTestPaper().getPaperType() != wl)) {
					continue;
				}else{
					
					System.out.println("开始计算["+subject.getName()+"]所有学生知识内容得分.....");
					List<Object[]> list = reportExamService.getMyContentRatioProc(examid, wl, subjectid,stuid);
					for(Object[] o : list){
						  String kcKey =  o[5].toString()+"_"+o[1].toString()+"_"+o[6].toString();
						  if(ress.containsKey(kcKey)){
							  List<Object[]> r = ress.get(kcKey);
							  r.add(o);
							  ress.put(kcKey, r);
						  }else{
							  List<Object[]> r = new ArrayList<Object[]>();
							  r.add(o);
							  ress.put(kcKey, r);
						  }
					}
					
				}
			}
			
		}
		allSubjectStduentKnowledgeContentScoreMap.put(mainKey, ress);
		System.out.println("所有学生知识内容计算完毕用时：【"+(System.currentTimeMillis()-begin)+"ms】");
	}
	
	if(!allSubjectStduentAbilityScoreMap.containsKey(mainKey)){
		System.out.println("开始计算各科所有学生能力结构得分");
		long begin = System.currentTimeMillis();
		HashMap<String, List<Object[]>> ress = new HashMap<String,List<Object[]>>();
		for (int i = 0; i < subjectList.size(); i++) {
			AnalysisTestpaper atp = (AnalysisTestpaper) subjectList.get(i);
			Subject subject = atp.getSubject();
			Long subjectid = subject.getId(); 
			if(subject.isZF()==false){
				if (atp.isComposite()|| (atp.getTestPaper().getPaperType() > 0 && atp.getTestPaper().getPaperType() != wl)) {
					continue;
				}else{
					
					System.out.println("开始计算["+subject.getName()+"]所有学生能力结构得分.....");
					List<Object[]> list = reportExamService.getMyAbilityRatioProc(examid, wl, subjectid,stuid);
					for(Object[] o : list){
						  String kcKey =  o[5].toString()+"_"+o[1].toString()+"_"+o[6].toString();
						  if(ress.containsKey(kcKey)){
							  List<Object[]> r = ress.get(kcKey);
							  r.add(o);
							  ress.put(kcKey, r);
						  }else{
							  List<Object[]> r = new ArrayList<Object[]>();
							  r.add(o);
							  ress.put(kcKey, r);
						  }
					}
					
				}
			}
			
		}
		allSubjectStduentAbilityScoreMap.put(mainKey, ress);
		//System.gc();
		System.out.println("所有学生能力结构计算完毕用时：【"+(System.currentTimeMillis()-begin)+"ms】");
	}
	
	if(!allSubjectStduentTitleTypeScoreMap.containsKey(mainKey)){
		System.out.println("开始计算各科所有学生题型得分");
		long begin = System.currentTimeMillis();
		HashMap<String, List<Object[]>> ress = new HashMap<String,List<Object[]>>();
		for (int i = 0; i < subjectList.size(); i++) {
			AnalysisTestpaper atp = (AnalysisTestpaper) subjectList.get(i);
			Subject subject = atp.getSubject();
			Long subjectid = subject.getId(); 
			if(subject.isZF()==false){
				if (atp.isComposite()|| (atp.getTestPaper().getPaperType() > 0 && atp.getTestPaper().getPaperType() != wl)) {
					continue;
				}else{
					
					System.out.println("开始计算["+subject.getName()+"]所有学生题型得分.....");
					List<Object[]> list = reportExamService.getMyTitleTypeRatioProc(examid, wl, subjectid,stuid);
					for(Object[] o : list){
						  String kcKey =  o[5].toString()+"_"+o[1].toString()+"_"+o[6].toString();
						  if(ress.containsKey(kcKey)){
							  List<Object[]> r = ress.get(kcKey);
							  r.add(o);
							  ress.put(kcKey, r);
						  }else{
							  List<Object[]> r = new ArrayList<Object[]>();
							  r.add(o);
							  ress.put(kcKey, r);
						  }
					}
					
				}
			}
			
		}
		allSubjectStduentTitleTypeScoreMap.put(mainKey, ress);
		//System.gc();
		System.out.println("所有学生题型计算完毕用时：【"+(System.currentTimeMillis()-begin)+"ms】");
	}
  }
	

}
