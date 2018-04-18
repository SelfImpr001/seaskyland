/*
 * @(#)com.cntest.fxpt.service.impl.CalcualteAnalysisTestPaperServiceImpl.java	1.0 2014年12月2日:上午10:17:00
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service.impl;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.CombinationSubject;
import com.cntest.fxpt.domain.CombinationSubjectCalculateRule;
import com.cntest.fxpt.domain.CombinationSubjectXTestPaper;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.Subject;
import com.cntest.fxpt.domain.TestPaper;
import com.cntest.fxpt.domain.ZFSubjectBuildRule;
import com.cntest.fxpt.repository.IAnalysisTestPaperDao;
import com.cntest.fxpt.repository.ICalcluateResultDao;
import com.cntest.fxpt.repository.ICombinationSubjectDao;
import com.cntest.fxpt.repository.IExamDao;
import com.cntest.fxpt.repository.ISubjectDao;
import com.cntest.fxpt.repository.ITestPaperDao;
import com.cntest.fxpt.service.ICalcualteAnalysisTestPaperService;
import com.cntest.fxpt.service.ICombinationSubjectService;
import com.cntest.fxpt.service.ZFSubjectBuildRuleService;
import com.cntest.util.ExceptionHelper;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年12月2日 上午10:17:00
 * @version 1.0
 */
@Service("ICalcualteAnalysisTestPaperService")
public class CalcualteAnalysisTestPaperServiceImpl implements ICalcualteAnalysisTestPaperService {
	private Logger log = LoggerFactory.getLogger(CalcualteAnalysisTestPaperServiceImpl.class);
	@Autowired(required = false)
	@Qualifier("ICombinationSubjectService")
	private ICombinationSubjectService csService;

	@Autowired(required = false)
	@Qualifier("ICalcluateResultDao")
	private ICalcluateResultDao crDao;

	@Autowired(required = false)
	@Qualifier("IExamDao")
	private IExamDao examDao;
	
	@Autowired(required = false)
	@Qualifier("ITestPaperDao")
	private ITestPaperDao testPaperDao;
	

	@Autowired(required = false)
	@Qualifier("ICombinationSubjectDao")
	private ICombinationSubjectDao csDao;

	@Autowired(required = false)
	@Qualifier("IAnalysisTestPaperDao")
	private IAnalysisTestPaperDao atpDao;

	@Autowired(required = false)
	@Qualifier("ITestPaperDao")
	private ITestPaperDao tpDao;

	@Autowired(required = false)
	@Qualifier("ISubjectDao")
	private ISubjectDao subjectDao;

	@Autowired(required = false)
	@Qualifier("ZFSubjectBuildRuleService")
	private ZFSubjectBuildRuleService zfSubjectBuildRuleService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.service.ICalcualteAnalysisTestPaperService#
	 * CalcualteAnalysisTestPaper(java.lang.Long)
	 */
	@Override
	public void addAndCalcualteAnalysisTestPaper(Long examId) {
		Exam exam = examDao.findById(examId);
		
		/**
		 * MaintainZfTestpaper构造器
		 * 1、初始化总分
		 * 2、初始化TestpaperMap，老的combinationsubject放入
		 * 3、初始化自定义合并科目放进List<CombinationSubject> combinationSubjects;
		 * maintain
		 * 处理组合科目
		 */
		new MaintainZfTestpaper(exam).maintain();

		// 不使用了，由自定义科目的时候维护
		// new MaintainCustomizeTestpaper(exam).maintain();

	}
	
	private abstract class MaintainTestpaper {
		protected Exam exam;
		protected Subject zfSubject;
		protected Subject msgSubject;
		protected Subject asgSubject;
		protected Subject suSubject;
		protected HashMap<String, AnalysisTestpaper> testpaperMap = new HashMap<>();
		protected List<CombinationSubject> combinationSubjects;
		protected HashMap<Long, AnalysisTestpaper> beUpdateTestpaperMap = new HashMap<>();

		public MaintainTestpaper(Exam exam) {
			this.exam = exam;
			init();
		}

		private void init() {
			initZfSubject();//初始化获取总分
			initTestpaperMap();//初始化试卷放在map中//
			initCombinationSubjects();//初始化自定义合并科目放进map
		}

		protected abstract void initZfSubject();

		protected abstract void initTestpaperMap();

		protected abstract void initCombinationSubjects();

		protected abstract String createTestpaperKeyWithCombinationSubject(CombinationSubject combinationSubject);

		public void maintain() {
			for (CombinationSubject combinationSubject : combinationSubjects) {
				AnalysisTestpaper testpaper = getZfTestpaperWithCombinationSubject(combinationSubject);//获取组合科目取得组合试卷 testpaperMap中通过key取得
				deleteCombinationSubjectOfZfTestpaper(testpaper);//删除数据库中原有的组合科目试卷
				testpaper = ifNullCreateZfTestpaper(testpaper, combinationSubject);//把组合科目的一些信息赋值给组合试卷
				setTestpaperAttr(testpaper, combinationSubject);
				saveZfTestpaper(testpaper);
			}
			clearOldZfTestpaper();
		}

		private AnalysisTestpaper getZfTestpaperWithCombinationSubject(CombinationSubject combinationSubject) {
			String zfSubjectKey = createTestpaperKeyWithCombinationSubject(combinationSubject);
			return testpaperMap.get(zfSubjectKey);
		}

		private void deleteCombinationSubjectOfZfTestpaper(AnalysisTestpaper testpaper) {
			if (testpaper != null) {
				csDao.delete(testpaper.getCombinationSubject());
			}
		}

		private AnalysisTestpaper ifNullCreateZfTestpaper(AnalysisTestpaper testpaper,
				CombinationSubject combinationSubject) {
			if (testpaper != null) {
				//删除原先的总分，重新生成（可以和之前生成的总分不一致：例如成绩删除了）
				atpDao.delete(testpaper);
				//return testpaper;
			}
			testpaper = new AnalysisTestpaper();
			testpaper.setName(combinationSubject.getName());
			testpaper.setFullScore(combinationSubject.getFullScore(atpDao));
			testpaper.setKgScore(combinationSubject.getKgScore(atpDao));
			testpaper.setZgScore(combinationSubject.getZgScore(atpDao));
			testpaper.setPaperType(combinationSubject.getPaperType());
			testpaper.setExam(exam);
			testpaper.setCombinationSubject(combinationSubject);
			if (combinationSubject.isSysCreate()) {
				if("总分".equals(combinationSubject.getName())){
					testpaper.setSubject(zfSubject);
				}else if("全科毕业总分".equals(combinationSubject.getName())){
					testpaper.setSubject(asgSubject);
				}else if("主科毕业总分".equals(combinationSubject.getName())){
					testpaper.setSubject(msgSubject);
				}else if("升学总分".equals(combinationSubject.getName())){
					testpaper.setSubject(suSubject);
				}
			}
			return testpaper;
		}

		private AnalysisTestpaper setTestpaperAttr(AnalysisTestpaper testpaper, CombinationSubject combinationSubject) {
			testpaper.setName(combinationSubject.getName());
			testpaper.setFullScore(combinationSubject.getFullScore(atpDao));
			testpaper.setKgScore(combinationSubject.getKgScore(atpDao));
			testpaper.setZgScore(combinationSubject.getZgScore(atpDao));
			testpaper.setPaperType(combinationSubject.getPaperType());
			testpaper.setCombinationSubject(combinationSubject);
			return testpaper;
		}

		private void saveZfTestpaper(AnalysisTestpaper testpaper) {
			if (testpaper.getId() != null) {
				beUpdateTestpaperMap.put(testpaper.getId(), testpaper);
			}
			//保存组合科目试卷信息
			atpDao.add(testpaper);
		}

		private void clearOldZfTestpaper() {
			for (AnalysisTestpaper testpaper : testpaperMap.values()) {
				if (beUpdateTestpaperMap.get(testpaper.getId()) != null) {
					continue;
				}
				csDao.delete(testpaper.getCombinationSubject());
				atpDao.delete(testpaper);
			}
		}

	}
	
	private class MaintainZfTestpaper extends MaintainTestpaper {

		public MaintainZfTestpaper(Exam exam) {
			super(exam);
		}

		@Override
		protected void initZfSubject() {
			zfSubject= subjectDao.getZF();
//			for (Subject subject : allZFSubjects) {
//				if("总分".equals(subject.getName())){
//					zfSubject = subject;
//				}else if("全科毕业总分".equals(subject.getName())){
//					asgSubject= subject;
//				}else if("主科毕业总分".equals(subject.getName())){
//					msgSubject= subject;
//				}else if("升学总分".equals(subject.getName())){
//					suSubject= subject;
//				}
//			}
		}

		@Override
		protected void initTestpaperMap() {
			//找出所有的analysisTestPaper
			List<AnalysisTestpaper> atps = atpDao.listAndCombinationSubjectIsNotNull(exam.getId());//找出合并不为空的科目
			if(atps!=null&&atps.size()>0){
				for (AnalysisTestpaper atp : atps) {
					if (atp.getCombinationSubject().isSysCreate()) {
						String key = createTestpaperKeyWithCombinationSubject(atp.getCombinationSubject());
						testpaperMap.put(key, atp);
					}
				}
			}
		}

		@Override
		protected void initCombinationSubjects() {
			combinationSubjects = new CombinationSubjectBuilderMgr(exam).getCombinationSubjects();
		}

		@Override
		protected String createTestpaperKeyWithCombinationSubject(CombinationSubject combinationSubject) {
			StringBuffer key = new StringBuffer();
			key.append(combinationSubject.getPaperType());
			List<CombinationSubjectCalculateRule> combinationSubjectCalculateRules = combinationSubject
					.getCombinationSubjectCalculateRules();
			if (combinationSubjectCalculateRules != null) {
				for (CombinationSubjectCalculateRule rule : combinationSubjectCalculateRules) {
					key.append(".").append(rule.getStudentAttributeValue());
				}
			}
			return key.toString();
		}
	}
	
	private class CombinationSubjectBuilderMgr {
		private Exam exam;
		private List<ZFSubjectBuildRule> zfSubjectBuildRules;
		private ArrayList<CombinationSubject> combinationSubjects = new ArrayList<>();

		/**
		 * 所有的组合科目计算规则
		 */
		public CombinationSubjectBuilderMgr(Exam exam) {
			this.exam = exam;
			//组合科目所有规则
			zfSubjectBuildRules = zfSubjectBuildRuleService.list();
			build();
		}

		/**
		 * 循环遍历组合科目计算规则
		 */
		private void build() {
			for (ZFSubjectBuildRule zfSubjectBuildRule : zfSubjectBuildRules) {
				//组合科目构建器
				CombinationSubjectBuilder combinationSubjectBuilder = new CombinationSubjectBuilder(exam,
						zfSubjectBuildRule);
				combinationSubjects.addAll(combinationSubjectBuilder.getCombinationSubjects());
			}
		}

		public List<CombinationSubject> getCombinationSubjects() {
			return combinationSubjects;
		}
	}

	private class CombinationSubjectBuilder {
		private Exam exam;
		private ZFSubjectBuildRule zfSubjectBuildRule;
		private ZFSubjectBuildRuleParser parser;
		private Map<Long, TestPaper> testpaperMap;
		private ArrayList<CombinationSubject> combinationSubjects = new ArrayList<>();

		public CombinationSubjectBuilder(Exam exam, ZFSubjectBuildRule zfSubjectBuildRule) {
			this.exam = exam;
			this.zfSubjectBuildRule = zfSubjectBuildRule;
			loadTestpaperMap();//所有单科试卷的信息
			/**
			 * 组合科目构建规则解析
			 * 根据单科map<id,testpaper>和组合科目计算规则解析
			 * 为了获取CombinationSubjectDescs
			 */
			this.parser = new ZFSubjectBuildRuleParser(exam, zfSubjectBuildRule, testpaperMap);
			build();
		}

		private void build() {
			//创建描述，用来构建组合科目
			List<CombinationSubjectDesc> combinationSubjectDescs = parser.getCombinationSubjectDescs();
			for (CombinationSubjectDesc combinationSubjectDesc : combinationSubjectDescs) {
				//根据组合科目描述创建组合科目
				CombinationSubject combinationSubject = createCombinationSubject(combinationSubjectDesc);
				combinationSubjects.add(combinationSubject);
			}
		}

		public List<CombinationSubject> getCombinationSubjects() {
			return combinationSubjects;
		}

		private CombinationSubject createCombinationSubject(CombinationSubjectDesc combinationSubjectDesc) {
			ArrayList<CombinationSubjectXTestPaper> childTestPaper = new ArrayList<>();
			int paperType = 0;
			for (Long testpaperId : combinationSubjectDesc.getTestpaperIds()) {
			
				TestPaper testpaper = testpaperMap.get(testpaperId);
				if (testpaper != null) {
					//之前的逻辑
					// if (testpaper.getPaperType() != 0) {
					// paperType = testpaper.getPaperType() != paperType ?
					// testpaper.getPaperType() : paperType;
					// }
					
					//改之后的
				    List<CombinationSubjectCalculateRule> cs= combinationSubjectDesc.combinationSubjectCalculateRules;
					if(cs.size()>0)
						paperType=Integer.parseInt(cs.get(0).getStudentAttributeValue()!=""?cs.get(0).getStudentAttributeValue():"0");
					CombinationSubjectXTestPaper cxt = new CombinationSubjectXTestPaper();
					cxt.setTestPaper(testpaper);
					childTestPaper.add(cxt);
				}
			}

			CombinationSubject combinationSubject = new CombinationSubject();
			combinationSubject.setExam(exam);
			combinationSubject.setName(zfSubjectBuildRule.getName());
			combinationSubject.setPaperType(paperType);
			combinationSubject.setChildTestPaper(childTestPaper);
			combinationSubject
					.setCombinationSubjectCalculateRules(combinationSubjectDesc.getCombinationSubjectCalculateRules());
			combinationSubject.setSysCreate(true);
			csService.add(combinationSubject);
			return combinationSubject;
		}

		private void loadTestpaperMap() {
			//原本：获得导入所有的考试科目
			List<TestPaper> tpList = tpDao.list(exam.getId());
			HashMap<Long, TestPaper> result = new HashMap<>();
			for (TestPaper tp : tpList) {
				result.put(tp.getId(), tp);
			}
			testpaperMap = result;
		}
	}

	private class ZFSubjectBuildRuleParser {
		private ZFSubjectBuildRule zfSubjectBuildRule;
		private Exam exam;
		private HashMap<String, CombinationSubjectDesc> combinationSubjectDescCache = new HashMap<>();
		private Map<Long, TestPaper> testpaperMap;
		private String[] classifyFileds;

		public ZFSubjectBuildRuleParser(Exam exam, ZFSubjectBuildRule zfSubjectBuildRule,
				Map<Long, TestPaper> testpaperMap) {
			this.zfSubjectBuildRule = zfSubjectBuildRule;
			this.exam = exam;
			this.testpaperMap = testpaperMap;
			parse();//解析
		}

		public List<CombinationSubjectDesc> getCombinationSubjectDescs() {
			return new ArrayList<>(combinationSubjectDescCache.values());
		}

		private void parse() {
			parseClassifyFileds();//解析ClassifyFileds
			List<Map> ruleDatas = loadDataFromZFSubjectBuildRule();//获取数据库构建规则列表数据
			for (Map ruleData : ruleDatas) {
				addCombinationSubjectDescToCache(ruleData);//把合成科目描述放入缓存中
			}
		}

		private void parseClassifyFileds() {
			if (zfSubjectBuildRule.getClassifyFiled() != null) {
				classifyFileds = zfSubjectBuildRule.getClassifyFiled().split(",");
			}
		}

		private List<Map> loadDataFromZFSubjectBuildRule() {
			String ruleSql = resovleSQL();
			List<Map> rules = crDao.getZfCreateRule(ruleSql);
			return rules;
		}

		private String resovleSQL() {
			String result = "";
			try {
				StringTemplateLoader templateLoader = new StringTemplateLoader();
				templateLoader.putTemplate("test", zfSubjectBuildRule.getBuildRuleSQL());
				Configuration cf = new Configuration();
				cf.setNumberFormat("0");
				cf.setTemplateLoader(templateLoader);
				Template tmplate = cf.getTemplate("test");

				Map<String, Object> variableMap = createVariable();
				StringWriter writer = new StringWriter();
				tmplate.process(variableMap, writer);
				result = writer.toString();
			} catch (Exception e) {
				log.error(ExceptionHelper.trace2String(e));
			}
			return result;
		}

		private Map<String, Object> createVariable() {
			HashMap<String, Object> variableMap = new HashMap<>();
			variableMap.put("exam", exam);
			return variableMap;
		}

		private void addCombinationSubjectDescToCache(Map ruleData) {
			String classifyFiledsString = parseClassifyFiledsToString(ruleData);
			//创建组合科目描述（包括组合科目的所有id和组合科目计算规则）
			CombinationSubjectDesc combinationSubjectDesc = combinationSubjectDescCache.get(classifyFiledsString);
			if (combinationSubjectDesc == null) {
				combinationSubjectDesc = createCombinationSubjectDesc(ruleData);
				//放入组合科目描述缓存
				combinationSubjectDescCache.put(classifyFiledsString, combinationSubjectDesc);
			}
			Long testPaperId = Long.parseLong(ruleData.get(zfSubjectBuildRule.getTestPaperIdField()).toString());
			combinationSubjectDesc.addTestpaperId(testPaperId);
		}

		private String parseClassifyFiledsToString(Map ruleData) {
			if (classifyFileds == null) {
				return null;
			}
			StringBuffer keyBuffer = new StringBuffer();
			for (String f : classifyFileds) {
				String value = ruleData.get(f).toString();
				keyBuffer.append(value).append(".");
			}
			return keyBuffer.toString();
		}

		private CombinationSubjectDesc createCombinationSubjectDesc(Map ruleData) {
			CombinationSubjectDesc combinationSubjectDesc = new CombinationSubjectDesc();
			if (classifyFileds != null) {
				for (String name : classifyFileds) {
					String value = ruleData.get(name).toString();
					CombinationSubjectCalculateRule combinationSubjectCalculateRule = new CombinationSubjectCalculateRule();
					combinationSubjectCalculateRule.setStudentAttributeName(name);
					combinationSubjectCalculateRule.setStudentAttributeValue(value);
					combinationSubjectDesc.addCombinationSubjectCalculateRule(combinationSubjectCalculateRule);
				}
			}
			return combinationSubjectDesc;
		}

	}

	private class CombinationSubjectDesc {
		private List<CombinationSubjectCalculateRule> combinationSubjectCalculateRules = new ArrayList<>();
		private List<Long> testpaperIds = new ArrayList<>();

		public void addCombinationSubjectCalculateRule(
				CombinationSubjectCalculateRule combinationSubjectCalculateRule) {
			combinationSubjectCalculateRules.add(combinationSubjectCalculateRule);
		}

		public void addTestpaperId(long testpaperId) {
			testpaperIds.add(testpaperId);
		}

		public List<CombinationSubjectCalculateRule> getCombinationSubjectCalculateRules() {
			return combinationSubjectCalculateRules;
		}

		public List<Long> getTestpaperIds() {
			return testpaperIds;
		}
	}
}
