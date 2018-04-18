/*
 * @(#)com.cntest.fxpt.etl.business.SynData.java	1.0 2014年6月5日:下午5:17:22
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business;

import java.sql.PreparedStatement;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.Item;
import com.cntest.fxpt.domain.TestPaper;
import com.cntest.fxpt.etl.EtlExecutor;
import com.cntest.fxpt.etl.IEtlContext;
import com.cntest.fxpt.etl.IStep;
import com.cntest.fxpt.etl.module.DBInput;
import com.cntest.fxpt.etl.module.DBLoadItemCj;
import com.cntest.fxpt.service.IItemService;
import com.cntest.fxpt.service.ITestPaperService;
import com.cntest.util.SpringContext;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月5日 下午5:17:22
 * @version 1.0
 */
public class SynData {
	private static final Logger log = LoggerFactory.getLogger(SynData.class);

	public void synProvince(Exam exam) throws Exception {
		log.debug("同步省市数据");
		ParseStepMgr ps = new ParseStepMgr("province.xml");
		ps.appendDataModel("exam", exam);
		IStep beginStep = ps.parse();
		exec(beginStep, beginStep.getStepMetadata().getContext());
		String sql = "update tb_education set isSyn=1 where type=1 AND isSyn=0";
		log.debug("修改省市同步标志");
		setSyn(sql);
		log.debug("修改省市同步标志成功");
		log.debug("同步省市数据成功");
	}

	public void synCity(Exam exam) throws Exception {
		log.debug("同步地市数据");
		ParseStepMgr ps = new ParseStepMgr("city.xml");
		ps.appendDataModel("exam", exam);
		IStep beginStep = ps.parse();
		exec(beginStep, beginStep.getStepMetadata().getContext());
		String sql = "update tb_education set isSyn=1 where type=2 AND isSyn=0";
		log.debug("修改地市同步标志");
		setSyn(sql);
		log.debug("修改地市同步标志成功");
		log.debug("同步地市数据成功");
	}

	public void synCounty(Exam exam) throws Exception {
		log.debug("同步区县数据");
		ParseStepMgr ps = new ParseStepMgr("county.xml");
		ps.appendDataModel("exam", exam);
		IStep beginStep = ps.parse();
		exec(beginStep, beginStep.getStepMetadata().getContext());
		String sql = "update tb_education set isSyn=1 where type=3 AND isSyn=0";
		log.debug("修改区县同步标志");
		setSyn(sql);
		log.debug("修改区县同步标志成功");
		log.debug("同步地市数据成功");
	}

	public void synSchool(Exam exam) throws Exception {
		log.debug("同步学校数据");
		ParseStepMgr ps = new ParseStepMgr("school.xml");
		ps.appendDataModel("exam", exam);
		IStep beginStep = ps.parse();
		exec(beginStep, beginStep.getStepMetadata().getContext());
		String sql = "update tb_school set isSyn=1 where isSyn=0";
		log.debug("修改学校同步标志");
		setSyn(sql);
		log.debug("修改学校同步标志成功");
		log.debug("同步学校数据成功");
	}

	public void synGrade(Exam exam) throws Exception {
		log.debug("同步年级数据");
		ParseStepMgr ps = new ParseStepMgr("grade.xml");
		ps.appendDataModel("exam", exam);
		IStep beginStep = ps.parse();
		exec(beginStep, beginStep.getStepMetadata().getContext());
		String sql = "update tb_grade set isSyn=1 where isSyn=0";
		log.debug("修改年级同步标志");
		setSyn(sql);
		log.debug("修改年级同步标志成功");
		log.debug("同步年级数据成功");
	}

	public void synSubject(Exam exam) throws Exception {
		log.debug("同步科目数据");
		ParseStepMgr ps = new ParseStepMgr("subject.xml");
		ps.appendDataModel("exam", exam);
		IStep beginStep = ps.parse();
		exec(beginStep, beginStep.getStepMetadata().getContext());
		String sql = "update tb_subject set isSyn=1 where isSyn=0";
		log.debug("修改科目同步标志");
		setSyn(sql);
		log.debug("修改科目同步标志成功");
		log.debug("同步科目数据成功");
	}

	public void synParameter(Exam exam) throws Exception {
		log.debug("同步考试分析参数数据");
		String sql = "DELETE FROM tb_parameters WHERE examId=" + exam.getId();
		execSQL(sql);

		ParseStepMgr ps = new ParseStepMgr("parameters.xml");
		ps.appendDataModel("exam", exam);
		IStep beginStep = ps.parse();
		IEtlContext context = beginStep.getStepMetadata().getContext();
		complieSQL(context, ps);
		exec(beginStep, context);
		log.debug("同步考试分析参数数据成功");
	}

	public void synCombinationSubject(Exam exam) throws Exception {
		log.debug("同步组合科目数据");
		String sql = "DELETE FROM tb_dim_combinationsubject WHERE examId="
				+ exam.getId();
		execSQL(sql);
		sql = "DELETE FROM tb_childtestpaper WHERE examId=" + exam.getId();
		execSQL(sql);

		ParseStepMgr ps = new ParseStepMgr("combinationSubject.xml");
		ps.appendDataModel("exam", exam);
		IStep beginStep = ps.parse();
		IEtlContext context = beginStep.getStepMetadata().getContext();
		complieSQL(context, ps);
		exec(beginStep, context);
		synChildTestpaper(exam);
		log.debug("同步组合科目数据成功");
	}

	private void synChildTestpaper(Exam exam) throws Exception {
		log.debug("同步组合科目对应的试卷数据");
		ParseStepMgr ps = new ParseStepMgr("childTestpaper.xml");
		ps.appendDataModel("exam", exam);
		IStep beginStep = ps.parse();
		IEtlContext context = beginStep.getStepMetadata().getContext();
		complieSQL(context, ps);
		exec(beginStep, context);
		log.debug("同步组合科目对应的试卷数据成功");
	}

	public void synExam(Exam exam) throws Exception {
		log.debug("同步考试数据");
		String sql = "DELETE FROM tb_dim_exam WHERE examId=" + exam.getId();
		execSQL(sql);

		ParseStepMgr ps = new ParseStepMgr("exam.xml");
		ps.appendDataModel("exam", exam);
		IStep beginStep = ps.parse();
		IEtlContext context = beginStep.getStepMetadata().getContext();
		complieSQL(context, ps);
		exec(beginStep, context);
		log.debug("同步考试数据成功");
	}

	public void synExamClass(Exam exam) throws Exception {
		log.debug("同步考试班级数据");
		String sql = "DELETE FROM tb_dim_examclass WHERE examId="
				+ exam.getId();
		execSQL(sql);

		ParseStepMgr ps = new ParseStepMgr("examClass.xml");
		ps.appendDataModel("exam", exam);
		IStep beginStep = ps.parse();
		IEtlContext context = beginStep.getStepMetadata().getContext();
		complieSQL(context, ps);
		exec(beginStep, context);
		log.debug("同步班级数据成功");
	}

	public void synExamStudent(Exam exam) throws Exception {
		log.debug("同步考试学生信息数据");
		String sql = "DELETE FROM dw_examstudent_fact WHERE examId="
				+ exam.getId();
		execSQL(sql);

		ParseStepMgr ps = new ParseStepMgr("examStudent.xml");
		ps.appendDataModel("exam", exam);
		IStep beginStep = ps.parse();
		IEtlContext context = beginStep.getStepMetadata().getContext();
		complieSQL(context, ps);
		exec(beginStep, context);
		log.debug("同步考试学生信息数据成功");
	}

	public void synTestPaper(Exam exam) throws Exception {
		log.debug("同步试卷数据");
		String sql = "DELETE FROM tb_dim_testpaper WHERE examId="
				+ exam.getId();
		execSQL(sql);

		ParseStepMgr ps = new ParseStepMgr("testPaper.xml");
		ps.appendDataModel("exam", exam);
		IStep beginStep = ps.parse();
		IEtlContext context = beginStep.getStepMetadata().getContext();
		complieSQL(context, ps);
		exec(beginStep, context);
		log.debug("同步试卷数据成功");
	}

	public void synItem(Exam exam) throws Exception {
		log.debug("同步所有科目试题信息数据");
		String sql = "DELETE FROM tb_dim_item WHERE examId=" + exam.getId();
		execSQL(sql);

		ParseStepMgr ps = new ParseStepMgr("item.xml");
		ps.appendDataModel("exam", exam);
		IStep beginStep = ps.parse();
		IEtlContext context = beginStep.getStepMetadata().getContext();
		complieSQL(context, ps);
		exec(beginStep, context);
		log.debug("同步所有科目试题信息数据成功");
	}

	public void synCj(Exam exam) throws Exception {
		log.debug("同步考试试卷成绩数据");
		String sql = "DELETE FROM tb_testpapercj_fact WHERE examId="
				+ exam.getId();
		execSQL(sql);
		sql = "DELETE FROM tb_itemcj_fact WHERE examId=" + exam.getId();
		execSQL(sql);

		List<TestPaper> testPapers = getTestPapers(exam);
		for (TestPaper testPaper : testPapers) {
			synTestPaperCj(exam, testPaper);
		}
		log.debug("考试试卷成绩数据成功");
	}

	private void synTestPaperCj(Exam exam, TestPaper testPaper)
			throws Exception {
		log.debug("同步" + testPaper.getName() + "成绩数据");
		List<Item> items = getItem(testPaper);
		ParseStepMgr ps = new ParseStepMgr("cj.xml");
		ps.appendDataModel("exam", exam);
		ps.appendDataModel("testPaper", testPaper);
		StringBuffer itemFields = new StringBuffer();
		for (Item item : items) {
			itemFields.append(",score" + item.getSortNum());
			if (item.getOptionType() != 0) {
				itemFields.append(",sel" + item.getSortNum());
			}
		}
		ps.appendDataModel("itemFields", itemFields.toString());
		ps.appendDataModel("items", items);
		IStep beginStep = ps.parse();
		IEtlContext context = beginStep.getStepMetadata().getContext();
		complieSQL(context, ps);
		exec(beginStep, context);

		log.debug("同步" + testPaper.getName() + "成绩数据成功");
	}

	private void complieSQL(IEtlContext context, ParseStepMgr ps) {

		for (IStep step : context.getSteps()) {
			if (step instanceof DBInput) {
				DBInput tmp = (DBInput) step;
				String sql = tmp.getSql();
				sql = StringTemplateUtil.transformTemplate(ps.getDataModel(),
						sql);
				tmp.setSql(sql);
			} else if (step instanceof DBLoadItemCj) {
				DBLoadItemCj tmp = (DBLoadItemCj) step;
				List<Item> items = (List<Item>) ps.getDataModel().get("items");
				tmp.setItems(items);
			}
		}
	}

	private void setSyn(String sql) throws Exception {

		PreparedStatement updateStatement = getSrcDS().getConnection()
				.prepareStatement(sql);
		updateStatement.executeUpdate();
		updateStatement.close();
	}

	private DataSource getSrcDS() {
		DataSource srcDS = SpringContext.getBean("dataSource");
		return srcDS;
	}

	private DataSource getDesDS() {
		DataSource desDS = SpringContext.getBean("houseDataSource");
		return desDS;
	}

	private List<TestPaper> getTestPapers(Exam exam) {
		ITestPaperService testPaperService = SpringContext
				.getBean("ITestPaperService");
		return testPaperService.listByExamId(exam.getId());
	}

	private List<Item> getItem(TestPaper testPaper) {
		IItemService itemService = SpringContext.getBean("IItemService");
		return itemService.listByTestPaperId(testPaper.getId());
	}

	private void exec(IStep beginStep, IEtlContext context) throws Exception {
		EtlExecutor executor = new EtlExecutor(beginStep, context);
		executor.execute();
		log.debug(executor.messageToString());
	}

	private void execSQL(String sql) {
		log.debug("执行sql：" + sql);
		JdbcTemplate jdbcTemplate = getJdbcTemplate();
		jdbcTemplate.execute(sql);
	}

	private JdbcTemplate getJdbcTemplate() {
		return SpringContext.getBean("houseJdbcTemplate");
	}

}
