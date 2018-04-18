/*
 * @(#)com.cntest.fxpt.anlaysis.uitl.ClassTaskProduce.java	1.0 2014年11月25日:下午4:17:17
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.event.produce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.cntest.fxpt.anlaysis.bean.CalculateTask;
import com.cntest.fxpt.anlaysis.bean.City;
import com.cntest.fxpt.anlaysis.bean.County;
import com.cntest.fxpt.anlaysis.bean.Province;
import com.cntest.fxpt.anlaysis.bean.StudentAttribute;
import com.cntest.fxpt.anlaysis.bean.StudentAttributeGroup;
import com.cntest.fxpt.anlaysis.bean.StudentCj;
import com.cntest.fxpt.anlaysis.filter.AbstractStudentCjFilter;
import com.cntest.fxpt.anlaysis.filter.OrgFilter;
import com.cntest.fxpt.anlaysis.filter.StudentAttrFilter;
import com.cntest.fxpt.anlaysis.service.IExamContext;
import com.cntest.fxpt.anlaysis.uitl.CalculateTaskTranslator;
import com.cntest.fxpt.anlaysis.uitl.Container;
import com.cntest.fxpt.anlaysis.uitl.EducationUitl;
import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.Education;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.ExamClass;
import com.cntest.fxpt.domain.ExamStudent;
import com.cntest.fxpt.domain.School;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月25日 下午4:17:17
 * @version 1.0
 */
public class XinjiangCalculateTaskProduce implements Runnable {
	private IExamContext ec;
	private Disruptor<CalculateTask> disruptor;
	private CountDownLatch countDownLatch;
	private List<ExamClass> classes;
	private List<School> schools;
	private List<County> countys;
	private List<City> citys;
	private List<Province> provinces;
	private Container<String, StudentAttributeGroup> studentAttributeGroupContainer = new Container<>();;
	private Container<String, StudentAttributeGroup> studentAreaContainer = new Container<>();;

	public XinjiangCalculateTaskProduce(IExamContext ec) {
		this.ec = ec;
		init();
	}

	private void init() {
		//
		// try {
		// String groovyScripte = FileUtils.readFileToString(new File(this
		// .getClass().getResource("").toString()
		// .replace("file:/", ""), "CreateTask.groovy"));
		//
		// ClassLoader parent = this.getClass().getClassLoader();
		// GroovyClassLoader loader = new GroovyClassLoader(parent);
		// Class groovyClass = loader.parseClass(groovyScripte);
		// GroovyObject groovyObject = (GroovyObject) groovyClass
		// .newInstance();
		// groovyObject.invokeMethod("test", null);
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		classes = ec.getClasses();
		schools = createSchools();
		createEducation();
		int taskTotalNum = calcuateTaskNum();
		ec.setTaskTotalNum(taskTotalNum);
		ec.clearCompleteTask();
		countDownLatch = new CountDownLatch(taskTotalNum);
		// countDownLatch = new CountDownLatch(1);
	}

	private int calcuateTaskNum() {
		Exam exam = ec.getExam();
		int num = 0;
		if (exam.getLevelCode() == 1) {
			num = provinces.size() + citys.size() + countys.size()
					+ schools.size();
		} else if (exam.getLevelCode() == 2) {
			num = citys.size() + countys.size() + schools.size();
		} else if (exam.getLevelCode() == 3) {
			num = countys.size() + schools.size();
		} else if (exam.getLevelCode() == 4) {
			num = schools.size();
		}
		num += classes.size();
		num = ec.getAnalysisTestpaperContainer().size() * num
				* studentAttributeGroupContainer.size()
				+ studentAreaContainer.size()
				* ec.getAnalysisTestpaperContainer().size()
				* studentAttributeGroupContainer.size();
		return num;
	}

	private List<School> createSchools() {
		List<StudentCj> cjList = ec.getStudentCjContainer().toList();
		ArrayList<School> schools = new ArrayList<>();

		HashMap<Long, Integer> cMap = new HashMap<>();
		for (StudentCj cj : cjList) {
			ExamStudent student = cj.getStudent();
			School s = student.getSchool();
			if (cMap.get(s.getId()) == null) {
				schools.add(s);
				cMap.put(s.getId(), 1);
			}
			addStudentAreaGroup(student);
			addStudentAttributeGroup(student);
		}

		return schools;
	}

	private void addStudentAreaGroup(ExamStudent student) {
		if (student.getArea() == null || "".equals(student.getArea())) {
			return;
		}

		StudentAttributeGroup studentAreaGroup = new StudentAttributeGroup();
		studentAreaGroup.add(createStudentAttribute("area", student.getArea()));

		if (studentAreaContainer.get(studentAreaGroup.getKey()) == null) {
			studentAreaContainer.put(studentAreaGroup.getKey(),
					studentAreaGroup);
		}
	}

	private void addStudentAttributeGroup(ExamStudent student) {
		StudentAttributeGroup studentAttributeGroup = new StudentAttributeGroup();
		studentAttributeGroup.add(createStudentAttribute("learLanguage",
				student.getLearLanguage()));
		studentAttributeGroup.add(createStudentAttribute("studentType",
				student.getStudentType()));
		studentAttributeGroup.add(createStudentAttribute("languagePattern",
				student.getLanguagePattern()));

		if (studentAttributeGroupContainer.get(studentAttributeGroup.getKey()) == null) {
			studentAttributeGroupContainer.put(studentAttributeGroup.getKey(),
					studentAttributeGroup);
		}
	}

	private StudentAttribute createStudentAttribute(String attributeName,
			String attributeValue) {
		StudentAttribute attribute = new StudentAttribute();
		attribute.setStudentAttributeName(attributeName);
		attribute.setStudentAttributevalue(attributeValue);
		return attribute;
	}

	private void createEducation() {
		HashMap<Long, Integer> cMap = new HashMap<>();
		HashMap<Long, Integer> ccMap = new HashMap<>();
		HashMap<Long, Integer> pMap = new HashMap<>();

		countys = new ArrayList<>();
		citys = new ArrayList<>();
		provinces = new ArrayList<>();
		for (School s : schools) {
			Education county = s.getEducation();
			Education city = county.getParent();
			Education province = city.getParent();

			if (pMap.get(province.getId()) == null) {
				pMap.put(province.getId(), 1);
				provinces.add(EducationUitl.createProvince(province));
			}

			if (cMap.get(city.getId()) == null) {
				cMap.put(city.getId(), 1);
				citys.add(EducationUitl.createCity(city));
			}
			if (ccMap.get(county.getId()) == null) {
				ccMap.put(county.getId(), 1);
				countys.add(EducationUitl.createCounty(county));
			}
		}
	}

	public XinjiangCalculateTaskProduce setDisruptor(
			Disruptor<CalculateTask> disruptor) {
		this.disruptor = disruptor;
		return this;
	}

	public CountDownLatch getCountDownLatch() {
		return countDownLatch;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		classCalculator();
		schoolCalculator();
		countyCalculator();
		cityCalculator();
		provinceCalculator();
		areaCalculator();
	}

	private void classCalculator() {
		for (ExamClass c : classes) {
			calculateAnalysisTestpaper(c, null);
		}
	}

	private void schoolCalculator() {
		Exam exam = ec.getExam();
		if (exam.getLevelCode() <= 4) {
			for (School school : schools) {
				calculateAnalysisTestpaper(school, null);
			}
		}

	}

	private void countyCalculator() {
		Exam exam = ec.getExam();
		if (exam.getLevelCode() <= 3) {
			for (County county : countys) {
				calculateAnalysisTestpaper(county, null);
			}
		}
	}

	private void cityCalculator() {
		Exam exam = ec.getExam();
		if (exam.getLevelCode() <= 2) {
			for (City city : citys) {
				calculateAnalysisTestpaper(city, null);
			}
		}
	}

	private void provinceCalculator() {
		Exam exam = ec.getExam();
		if (exam.getLevelCode() <= 1) {
			for (Province province : provinces) {
				calculateAnalysisTestpaper(province, null);
			}
		}
	}

	private void areaCalculator() {
		Exam exam = ec.getExam();
		Object org = null;
		switch (exam.getLevelCode()) {
		case 1:
			org = provinces.get(0);
			break;
		}

		if (org == null) {
			return;
		}

		for (StudentAttributeGroup studentAearGroup : studentAreaContainer
				.toList()) {
			calculateAnalysisTestpaper(org, studentAearGroup);
		}
	}

	private void calculateAnalysisTestpaper(Object org,
			StudentAttributeGroup studentAreaGroup) {
		List<AnalysisTestpaper> testpapers = ec.getAnalysisTestpaperContainer()
				.toList();
		for (AnalysisTestpaper atp : testpapers) {
			// if (atp.getId() != 269L) {
			// continue;
			// }

			calcluateStudentAttributeGroup(atp, atp.getPaperType(), org,
					studentAreaGroup);
		}
	}

	private void calcluateStudentAttributeGroup(AnalysisTestpaper atp, int wl,
			Object org, StudentAttributeGroup studentAreaGroup) {

		for (StudentAttributeGroup studentAttributeGroup : studentAttributeGroupContainer
				.toList()) {

			// boolean ok = false;
			// for (StudentAttribute attribute : studentAttributeGroup
			// .getStudentAttributes()) {
			// if (attribute.getStudentAttributevalue().equals("民考汉")) {
			// ok = true;
			// }
			// }
			//
			// if (!ok) {
			// continue;
			// }

			String zfResultTableName = "dw_agg_xinjiang_totalscore";
			String itemResultTableName = "dw_agg_xinjiang_item";
			String itemGroupTableName = "dw_agg_xinjiang_itemGroup";
			String segmentTableName = "dw_agg_xinjiang_segment";

			AbstractStudentCjFilter filter = createOrgFilter(org);

			List<StudentAttribute> attributes = studentAttributeGroup
					.getStudentAttributes();
			Container<String, Object> studentRangeValues = new Container<>();
			studentRangeValues.put("wl", wl);
			for (StudentAttribute attribute : attributes) {
				studentRangeValues.put(attribute.getStudentAttributeName(),
						attribute.getStudentAttributevalue());
				filter.next(createStudentFilter(
						attribute.getStudentAttributeName(),
						attribute.getStudentAttributevalue()));

			}

			if (studentAreaGroup != null) {
				for (StudentAttribute attribute : studentAreaGroup
						.getStudentAttributes()) {
					studentRangeValues.put(attribute.getStudentAttributeName(),
							attribute.getStudentAttributevalue());
					filter.next(createStudentFilter(
							attribute.getStudentAttributeName(),
							attribute.getStudentAttributevalue()));
				}
				zfResultTableName = "dw_agg_xinjiang_fourArea_totalscore";
				itemResultTableName = "dw_agg_xinjiang_fourArea_item";
				itemGroupTableName = "dw_agg_xinjiang_fourArea_itemGroup";
				segmentTableName = "dw_agg_xinjiang_fourArea_segment";
			}

			CalculateTask task = new CalculateTask();
			task.setObj(org);
			task.setContext(ec);
			task.setAnalysisTestpaper(atp);
			task.setStudentRangeValues(studentRangeValues);
			task.setFindStudentFilter(filter);

			task.setZfResultTableName(zfResultTableName);
			task.setItemResultTableName(itemResultTableName);
			task.setItemGroupTableName(itemGroupTableName);
			task.setSegmentTableName(segmentTableName);

			publishEvent(task);
		}
	}

	private AbstractStudentCjFilter createOrgFilter(Object org) {
		return new OrgFilter(org);
	}

	private AbstractStudentCjFilter createStudentFilter(String attrName,
			String attrValue) {
		return new StudentAttrFilter(attrName, attrValue);
	}

	private void publishEvent(CalculateTask task) {
		disruptor.publishEvent(new CalculateTaskTranslator(task));
	}
}
