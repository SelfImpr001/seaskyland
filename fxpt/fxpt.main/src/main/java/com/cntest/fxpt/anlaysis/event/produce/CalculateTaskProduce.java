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
import com.cntest.fxpt.anlaysis.service.IExamContext;
import com.cntest.fxpt.anlaysis.uitl.CalculateTaskTranslator;
import com.cntest.fxpt.anlaysis.uitl.EducationUitl;
import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.Education;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.ExamClass;
import com.cntest.fxpt.domain.School;
import com.lmax.disruptor.dsl.Disruptor;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月25日 下午4:17:17
 * @version 1.0
 */
public class CalculateTaskProduce implements Runnable {
	private IExamContext ec;
	private Disruptor<CalculateTask> disruptor;
	private CountDownLatch countDownLatch;
	private List<ExamClass> classes;
	private List<School> schools;
	private List<County> countys;
	private List<City> citys;
	private List<Province> provinces;

	public CalculateTaskProduce(IExamContext ec) {
		this.ec = ec;
		init();
	}

	private void init() {
		classes = ec.getClasses();
		schools = ec.getSchools();
		createEducation();
		int taskTotalNum = calcuateTaskNum();
		ec.setTaskTotalNum(taskTotalNum);
		ec.clearCompleteTask();
		countDownLatch = new CountDownLatch(taskTotalNum);
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
		num = ec.getAnalysisTestpaperContainer().size() * num;
		return num;
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

	public CalculateTaskProduce setDisruptor(Disruptor<CalculateTask> disruptor) {
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

		List<AnalysisTestpaper> testpapers = ec.getAnalysisTestpaperContainer()
				.toList();
		for (ExamClass c : classes) {
			for (AnalysisTestpaper at : testpapers) {
				CalculateTask task = new CalculateTask();
				task.setObj(c);
				task.setContext(ec);
				task.setAnalysisTestpaper(at);
				disruptor.publishEvent(new CalculateTaskTranslator(task));
			}
		}

		Exam exam = ec.getExam();
		if (exam.getLevelCode() <= 4) {
			for (School school : schools) {
				for (AnalysisTestpaper at : testpapers) {
					CalculateTask task = new CalculateTask();
					task.setObj(school);
					task.setContext(ec);
					task.setAnalysisTestpaper(at);
					disruptor.publishEvent(new CalculateTaskTranslator(task));
				}
			}
		}

		if (exam.getLevelCode() <= 3) {
			for (County county : countys) {
				for (AnalysisTestpaper at : testpapers) {
					CalculateTask task = new CalculateTask();
					task.setObj(county);
					task.setContext(ec);
					task.setAnalysisTestpaper(at);
					disruptor.publishEvent(new CalculateTaskTranslator(task));
				}
			}
		}

		if (exam.getLevelCode() <= 2) {
			for (City city : citys) {
				for (AnalysisTestpaper at : testpapers) {
					CalculateTask task = new CalculateTask();
					task.setObj(city);
					task.setContext(ec);
					task.setAnalysisTestpaper(at);
					disruptor.publishEvent(new CalculateTaskTranslator(task));
				}
			}
		}

		if (exam.getLevelCode() <= 1) {
			for (Province province : provinces) {
				for (AnalysisTestpaper at : testpapers) {
					CalculateTask task = new CalculateTask();
					task.setObj(province);
					task.setContext(ec);
					task.setAnalysisTestpaper(at);
					disruptor.publishEvent(new CalculateTaskTranslator(task));
				}
			}
		}
	}

}
