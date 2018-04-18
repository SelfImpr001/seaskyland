/*
 * @(#)com.cntest.fxpt.anlaysis.service.impl.ExamContext.java	1.0 2014年11月24日:下午1:56:11
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.service.impl;

import com.cntest.fxpt.anlaysis.bean.*;
import com.cntest.fxpt.anlaysis.filter.AbstractStudentCjFilter;
import com.cntest.fxpt.anlaysis.filter.CjFilter;
import com.cntest.fxpt.anlaysis.service.IExamContext;
import com.cntest.fxpt.anlaysis.service.ISaveCalcluateResultToDBService;
import com.cntest.fxpt.anlaysis.uitl.Container;
import com.cntest.fxpt.anlaysis.uitl.ScoreObjMgr;
import com.cntest.fxpt.domain.*;
import com.cntest.fxpt.service.IUplineScoreService;
import com.cntest.util.SpringContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月24日 下午1:56:11
 * @version 1.0
 */
public class ExamContext implements IExamContext {
	private Exam exam;
	private AnalysisTestpaperContainer analysisTestpaperContainer = new AnalysisTestpaperContainer();
	private StudentCjContainer sCjContainer = new StudentCjContainer();
	private LoadExamData loadService = new LoadExamData();
	private Lock studentLock = new ReentrantLock();
	private StatisticSetting dkParam;
	private StatisticSetting zfParam;
	private AtomicBoolean isComplate = new AtomicBoolean();

	private AtomicInteger taskTotalNum = new AtomicInteger();
	private AtomicInteger completeTaskNum = new AtomicInteger();

	private ISaveCalcluateResultToDBService saveResult;

	private	List<UplineScore> uplineScores;

	IUplineScoreService iuss = SpringContext.getBean("IUplineScoreService");

	private boolean hasChoice = false;

	private Container<Long, Education> countys = new Container<>();

	@Override
	public StudentCjContainer getStudentCjContainer() {
		return sCjContainer;
	}

	public ExamContext(Exam exam) {
		this.exam = exam;
		loadService.loadAnlaysisTestPaper(analysisTestpaperContainer, exam);
		dkParam = loadService.loadDkStatParam(exam);
		zfParam = loadService.loadZfStatParam(exam);
		saveResult = new DefaultSaveCalcluateResultToDBService();
		uplineScores = iuss.listByUpline(exam.getId());
//		saveResult = new BatchSaveCalcluateResultToDBService();
	}

	@Override
	public AnalysisTestpaperContainer getAnalysisTestpaperContainer() {
		return analysisTestpaperContainer;
	}

	@Override
	public Exam getExam() {
		return exam;
	}

	@Override
	public void loadStudent(Param... params) {
		List<ExamStudent> students = loadService.loadStudentList(exam, params);

		if(students!=null){
			for (ExamStudent student : students) {
				student = filterExamAttribute(student);
				sCjContainer.addStudentCj(student);
			}
		}
		// sCjContainer.add(result);
	}

	private ExamStudent filterExamAttribute(ExamStudent student) {
		student.setExam(exam);
		School school = student.getSchool();
		Education county = countys.get(school.getEducation().getId());
		if (county == null) {
			county = school.getEducation();
			countys.put(school.getEducation().getId(), county);
		}
		school.setEducation(county);
		ExamClass examClass = student.getClazz();
		if (examClass != null) {
			examClass.setExam(exam);
		}
		return student;
	}

	@Override
	public void loadCj(Param... params) {
		List<TempTotalScore> tss = loadService.loadTestPaperCj(exam, params);
		for (TempTotalScore ts : tss) {
			AnalysisTestpaper atp = analysisTestpaperContainer.get(ts
					.getAnalysisTestpaperId());
			StudentCj scj = sCjContainer.getStudentCj(ts.getStudentId());
			StudentSubjectScore ssc = createStudentSubjectScore(atp, ts);
			scj.addCj(ssc);
		}

		HashMap<Long, Item> itemMap = new HashMap<>();
		List<AnalysisTestpaper> ats = analysisTestpaperContainer.toList();
		for (AnalysisTestpaper at : ats) {
			for (Item item : at.getItems()) {
				itemMap.put(item.getId(), item);
			}
		}
		List<TempItemScore> tiss = loadService.loadItemCj(exam, params);
		for (TempItemScore tis : tiss) {
			ItemScore itemScore = createTempItemScore(
					itemMap.get(tis.getItemId()), tis);
			StudentCj scj = sCjContainer.getStudentCj(tis.getStudentId());
			StudentSubjectScore cj = scj.getStudentSubjectScore(tis
					.getAnalysisTestpaperId());
			if (cj == null) {
				System.out.println();
			}
			if (itemScore.getItem() == null) {
				System.out.println();
			}
			cj.addItemScore(itemScore);
		}
	}

	private ItemScore createTempItemScore(Item item, TempItemScore tis) {
		ScoreObjMgr scoreMgr = ScoreObjMgr.newInstance();
		ItemScore result = new ItemScore();
		result.setItem(item);
		result.setScore(scoreMgr.getScore(tis.getScore()));
		result.setSelOption(tis.getSelOption());
		return result;
	}

	private StudentSubjectScore createStudentSubjectScore(
			AnalysisTestpaper atp, TempTotalScore ts) {
		StudentSubjectScore ssc = new StudentSubjectScore();
		ssc.setAnalysisTestpaper(atp);
		ScoreObjMgr scoreMgr = ScoreObjMgr.newInstance();
		ssc.setScore(scoreMgr.getScore(ts.getTotalScore()));
		ssc.setKgScore(scoreMgr.getScore(ts.getKgScore()));
		ssc.setZgScore(scoreMgr.getScore(ts.getZgScore()));
		boolean isQk = ts.getIsQk() == null ? true : ts.getIsQk();
		ssc.setQk(isQk);
		return ssc;
	}

	@Override
	public List<ExamClass> getClasses() {
		List<StudentCj> cjList = sCjContainer.toList();
		ArrayList<ExamClass> result = new ArrayList<>();
		HashMap<Long, Integer> cMap = new HashMap<>();
		for (StudentCj cj : cjList) {
			ExamClass c = cj.getStudent().getClazz();
			if (c != null && cMap.get(c.getId()) == null) {
				result.add(c);
				cMap.put(c.getId(), 1);
			}
		}
		return result;
	}

	@Override
	public List<School> getSchools() {
		List<StudentCj> cjList = sCjContainer.toList();
		ArrayList<School> result = new ArrayList<>();
		HashMap<Long, Integer> cMap = new HashMap<>();
		for (StudentCj cj : cjList) {
			School s = cj.getStudent().getSchool();
			if (cMap.get(s.getId()) == null) {
				result.add(s);
				cMap.put(s.getId(), 1);
			}
		}
		return result;
	}

	@Override
	public StudentCjContainer getStudentCjContainer(
			AbstractStudentCjFilter filter) {
		if (filter == null) {
			return sCjContainer;
		} else {
			return sCjContainer.getStudentCjContainer(filter);
		}
	}

	@Override
	public AbstractStudentCjFilter getStatRankFilter(AnalysisTestpaper atp) {
		//统计口径，单科包括缺考，总分不包括缺考
		StatisticSetting param = dkParam;
		if (atp.getCombinationSubject() != null) {
			param = zfParam;
		}
		return new CjFilter(atp, param);
	}

    public void updateScore(){
	    for (UplineScore uplineScore:uplineScores){
            iuss.updateScore(uplineScore);
        }
    }

	@Override
	public void setTaskTotalNum(int taskTotalNum) {
		this.taskTotalNum.getAndSet(taskTotalNum);
	}

	@Override
	public int setCompleteTaskNum(int completeTaskNum) {
		return this.completeTaskNum.addAndGet(completeTaskNum);
	}

	@Override
	public int completeTask() {
		return completeTaskNum.incrementAndGet();
	}

	@Override
	public int getTaskTotalNum() {
		return taskTotalNum.get();
	}

	@Override
	public int getCompleteTaskNum() {
		return completeTaskNum.get();
	}

	@Override
	public void addTaskNumToTaskTotalNum(int taskTotalNum) {
		this.taskTotalNum.addAndGet(taskTotalNum);
	}

	@Override
	public void clearCompleteTask() {
		completeTaskNum.getAndSet(0);
	}

	@Override
	public boolean isAllComplate() {
		return isComplate.get();
	}

	@Override
	public void setAllComplate() {
		isComplate.getAndSet(true);
	}

	/**********************************************************/

	@Override
	public ISaveCalcluateResultToDBService getSaveCalcluateResultToDBService() {
		// TODO Auto-generated method stub
		return saveResult;
	}

	public void setSaveResult(ISaveCalcluateResultToDBService saveResult) {
		this.saveResult = saveResult;
	}

	@Override
	public boolean isHasChoice() {
		return hasChoice;
	}

	@Override
	public void setHasChoice(boolean hasChoice) {
		this.hasChoice = hasChoice;
	}

    @Override
    public List<UplineScore> getUplineScores() {
        return uplineScores;
    }
}
