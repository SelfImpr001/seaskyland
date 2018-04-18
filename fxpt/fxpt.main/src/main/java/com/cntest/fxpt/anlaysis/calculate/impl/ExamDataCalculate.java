/*
 * @(#)com.cntest.fxpt.anlaysis.calculate.impl.ExamDataCalculate.java	1.0 2015年1月5日:下午3:21:35
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.calculate.impl;

import java.util.ArrayList;
import java.util.List;

import com.cntest.fxpt.anlaysis.bean.CalculateTask;
import com.cntest.fxpt.anlaysis.bean.GroupScore;
import com.cntest.fxpt.anlaysis.bean.ItemGroup;
import com.cntest.fxpt.anlaysis.bean.ScoreInfo;
import com.cntest.fxpt.anlaysis.bean.SubjectScoreContainer;
import com.cntest.fxpt.anlaysis.filter.ExamCjFilter;
import com.cntest.fxpt.anlaysis.filter.WLFilter;
import com.cntest.fxpt.anlaysis.uitl.Container;
import com.cntest.fxpt.anlaysis.uitl.ItemGroupScoreGroupBuild;
import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.Item;
import com.cntest.fxpt.domain.Subject;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年1月5日 下午3:21:35
 * @version 1.0
 */
public class ExamDataCalculate {
	private ArrayList<String> sqls = new ArrayList<>();

	public List<String> getSQL(CalculateTask event) {
		calculateExamData(event);
		return sqls;
	}

	private void calculateExamData(CalculateTask event) {

		AnalysisTestpaper atp = event.getAnalysisTestpaper();
		ExamCjFilter filter = new ExamCjFilter(atp);
		SubjectScoreContainer ssc = event.getSubjectScoreContainer()
				.getSubjectScoreContainer(filter);

		Exam exam = event.getContext().getExam();
		if (atp.getPaperType() == 0 && exam.isWlForExamStudent()) {
			SubjectScoreContainer tmpssc = ssc
					.getSubjectScoreContainer(new WLFilter(1));
			calucateExamData(event, 1, tmpssc);
			tmpssc = ssc.getSubjectScoreContainer(new WLFilter(2));
			calucateExamData(event, 2, tmpssc);
		} else {
			calucateExamData(event, atp.getPaperType(), ssc);
		}
	}

	private void calucateExamData(CalculateTask event, int wl,
			SubjectScoreContainer ssc) {
		Exam exam = event.getContext().getExam();
		AnalysisTestpaper atp = event.getAnalysisTestpaper();
		Long examId = exam.getId();
		Long subjectId = null;
		Subject subject = atp.getSubject();
		if (subject != null) {
			subjectId = subject.getId();
		}

		List<Item> items = event.getAnalysisTestpaper().getItems();
		if (items != null && !items.isEmpty()) {
			ability(atp, examId, subjectId, atp.getId(), wl, ssc);
			titleType(atp, examId, subjectId, atp.getId(), wl, ssc);
			knowledgeContent(atp, examId, subjectId, atp.getId(), wl, ssc);
		}
		zf(examId, subjectId, atp.getId(), wl, ssc);
	}

	private void zf(Long examId, Long subjectId, Long atpId, int wl,
			SubjectScoreContainer ssc) {
		GroupScore groupScore = ssc.getGroupScore();
		List<ScoreInfo> sis = groupScore.getScoreInfos();

		for (ScoreInfo si : sis) {
			StringBuffer sql = new StringBuffer();
			sql.append("INSERT INTO dw_agg_exam_totalscore_scoreinfo(examId,subjectId,analysisTestpaperId,wl,score,rank,num) VALUES(");
			sql.append(examId);
			sql.append(",");
			sql.append(subjectId);
			sql.append(",");
			sql.append(atpId);
			sql.append(",");
			sql.append(wl);
			sql.append(",");
			sql.append(si.getScore().getValue());
			sql.append(",");
			sql.append(si.getRank());
			sql.append(",");
			sql.append(si.getNum());
			sql.append(")");
			sqls.add(sql.toString());
		}

	}

	private void ability(AnalysisTestpaper atp, Long examId, Long subjectId,
			Long atpId, int wl, SubjectScoreContainer ssc) {

		List<ItemGroup> itemGroups = ItemGroup.createItemGroupsWithItemAttr(
				"ability", atp.getItems(),false);
		Container<String, GroupScore> itemGroupScoreMap = new ItemGroupScoreGroupBuild()
				.build(itemGroups, ssc);
		for (ItemGroup itemGroup : itemGroups) {
			GroupScore groupScore = itemGroupScoreMap.get(itemGroup.getName());
			List<ScoreInfo> sis = groupScore.getScoreInfos();

			for (ScoreInfo si : sis) {
				StringBuffer sql = new StringBuffer();
				sql.append("INSERT INTO dw_agg_exam_ability_scoreinfo(examId,subjectId,analysisTestpaperId,name,wl,score,rank,num) VALUES(");
				sql.append(examId);
				sql.append(",");
				sql.append(subjectId);
				sql.append(",");
				sql.append(atpId);
				sql.append(",'");
				sql.append(itemGroup.getName());
				sql.append("',");
				sql.append(wl);
				sql.append(",");
				sql.append(si.getScore().getValue());
				sql.append(",");
				sql.append(si.getRank());
				sql.append(",");
				sql.append(si.getNum());
				sql.append(")");
				sqls.add(sql.toString());
			}
		}

	}

	private void titleType(AnalysisTestpaper atp, Long examId, Long subjectId,
			Long atpId, int wl, SubjectScoreContainer ssc) {

		List<ItemGroup> itemGroups = ItemGroup.createItemGroupsWithItemAttr(
				"titleType", atp.getItems(),false);
		Container<String, GroupScore> itemGroupScoreMap = new ItemGroupScoreGroupBuild()
				.build(itemGroups, ssc);
		for (ItemGroup itemGroup : itemGroups) {
			GroupScore groupScore = itemGroupScoreMap.get(itemGroup.getName());
			List<ScoreInfo> sis = groupScore.getScoreInfos();

			for (ScoreInfo si : sis) {
				StringBuffer sql = new StringBuffer();
				sql.append("INSERT INTO dw_agg_exam_titleType_scoreinfo(examId,subjectId,analysisTestpaperId,name,wl,score,rank,num) VALUES(");
				sql.append(examId);
				sql.append(",");
				sql.append(subjectId);
				sql.append(",");
				sql.append(atpId);
				sql.append(",'");
				sql.append(itemGroup.getName());
				sql.append("',");
				sql.append(wl);
				sql.append(",");
				sql.append(si.getScore().getValue());
				sql.append(",");
				sql.append(si.getRank());
				sql.append(",");
				sql.append(si.getNum());
				sql.append(")");
				sqls.add(sql.toString());
			}
		}

	}

	private void knowledgeContent(AnalysisTestpaper atp, Long examId,
			Long subjectId, Long atpId, int wl, SubjectScoreContainer ssc) {

		List<ItemGroup> itemGroups = ItemGroup.createItemGroupsWithItemAttr(
				"knowledgeContent", atp.getItems(),false);
		Container<String, GroupScore> itemGroupScoreMap = new ItemGroupScoreGroupBuild()
				.build(itemGroups, ssc);
		for (ItemGroup itemGroup : itemGroups) {
			GroupScore groupScore = itemGroupScoreMap.get(itemGroup.getName());
			List<ScoreInfo> sis = groupScore.getScoreInfos();

			for (ScoreInfo si : sis) {
				StringBuffer sql = new StringBuffer();
				sql.append("INSERT INTO dw_agg_exam_knowledgeContent_scoreinfo(examId,subjectId,analysisTestpaperId,name,wl,score,rank,num) VALUES(");
				sql.append(examId);
				sql.append(",");
				sql.append(subjectId);
				sql.append(",");
				sql.append(atpId);
				sql.append(",'");
				sql.append(itemGroup.getName());
				sql.append("',");
				sql.append(wl);
				sql.append(",");
				sql.append(si.getScore().getValue());
				sql.append(",");
				sql.append(si.getRank());
				sql.append(",");
				sql.append(si.getNum());
				sql.append(")");
				sqls.add(sql.toString());
			}
		}

	}

}
