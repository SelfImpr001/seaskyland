/*
 * @(#)com.cntest.fxpt.service.ExamServiceImpl.java	1.0 2014年5月17日:上午10:56:39
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.common.page.Page;
import com.cntest.common.specification.Specification;
import com.cntest.foura.domain.User;
import com.cntest.foura.service.OrganizationService;
import com.cntest.foura.service.UserBelongService;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.domain.Education;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.ExamCheckin;
import com.cntest.fxpt.domain.ExamStudent;
import com.cntest.fxpt.domain.ExamType;
import com.cntest.fxpt.domain.Grade;
import com.cntest.fxpt.domain.School;
import com.cntest.fxpt.domain.spec.examinee.StudentSpecificationBuilder;
import com.cntest.fxpt.repository.ExamCheckinDao;
import com.cntest.fxpt.repository.IExamDao;
import com.cntest.fxpt.repository.IExamPaprameterDao;
import com.cntest.fxpt.repository.IExamStudentDao;
import com.cntest.fxpt.repository.IUplineScoreDao;
import com.cntest.fxpt.service.IExamService;
import com.cntest.fxpt.util.LogUtil;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月17日 上午10:56:39
 * @version 1.0
 */
@Service("IExamService")
public class ExamServiceImpl implements IExamService {
	private static final Logger loggr = LoggerFactory.getLogger(ExamServiceImpl.class);
	@Autowired(required = false)
	@Qualifier("IExamDao")
	private IExamDao examDao;

	@Autowired(required = false)
	private OrganizationService orgService;

	@Autowired(required = false)
	@Qualifier("IExamPaprameterDao")
	private IExamPaprameterDao examPaprameterDao;

	@Autowired(required = false)
	@Qualifier("IExamStudentDao")
	private IExamStudentDao examStudentDao;

	@Autowired(required = false)
	private ExamCheckinDao examCheckinDao;

	@Autowired(required = false)
	private UserService userService;

	@Autowired(required = false)
	private UserBelongService userBelongService;

	@Autowired(required = false)
	private IUplineScoreDao uplineScoreService;

	/**
	 * 
	 */
	public ExamServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createExams(Date examDate, int schoolYear, int schoolTerm, int levelCode, String levelName,
			String ownerCode, String ownerName, String createUserName, ExamType examType, List<Grade> grades) {
		if (grades == null || grades.isEmpty()) {
			loggr.error("增加考试失败，没有参加考试的年级");
			return;
		}
		for (Grade grade : grades) {
			Exam exam = new Exam();
			exam.setExamDate(examDate);
			exam.setSchoolYear(schoolYear);
			createSchoolYearName(exam);
			exam.setSchoolTerm(schoolTerm);
			exam.setOwnerCode(ownerCode);
			exam.setOwnerName(ownerName);
			exam.setLevelCode(levelCode);
			exam.setLevelName(levelName);
			exam.setCreateUserName(createUserName);
			exam.setStatus(0);

			exam.setExamType(examType);
			exam.setGrade(grade);

			String name = exam.getSchoolYearName() + exam.getSchoolTermName(false) + exam.getGrade().getName()
					+ exam.getExamType().getName();
			exam.setName(name);
			String sortName = exam.getGrade().getName() + exam.getSchoolTermName(false) + examType.getName();
			exam.setSortName(sortName);

			add(exam);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.service.IExamService#add(com.cntest.fxpt.domain.Exam)
	 */
	@Override
	public void add(Exam exam) {
		String status = "失败", info = "新增考试<b style='color:red;'>", erre = "";
		try {
			exam.setCreateDate(new Date());
			calcuateExamAttr(exam);
			examDao.add(exam);
			// 给新增此次考试的人赋此次考试权限
			User user = User.from(userService.getCurrentLoginedUser());
			User users = userService.findUserBy(user.getName());
			if (exam.getId() != null) {
				userService.addUserExamByPKandExamids(exam.getId().toString(), users.getPk());
			}
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		} finally {
			info += exam.getName() + "</b>" + status;
			LogUtil.log("考试管理>考试列表", "新增", exam.getName(), status, info, erre);
		}

		// ArrayList<ExamPaprameter> eps = new ArrayList<ExamPaprameter>();
		// ExamPaprameter ep = new
		// ExamPaprameter().setExam(exam).setParamName("scoreSegment")
		// .setParamAsName("分数段").setParamType(0).setParamValue("10");
		// eps.add(ep);
		// ep = new ExamPaprameter().setExam(exam).setParamName("rankSegment")
		// .setParamAsName("名次段").setParamType(0).setParamValue("10");
		// eps.add(ep);
		// ep = new ExamPaprameter().setExam(exam).setParamName("yxl")
		// .setParamAsName("优秀率").setParamType(0).setParamValue("85");
		// eps.add(ep);
		// ep = new ExamPaprameter().setExam(exam).setParamName("lhl")
		// .setParamAsName("良好率").setParamType(0).setParamValue("70");
		// eps.add(ep);
		// ep = new ExamPaprameter().setExam(exam).setParamName("jgl")
		// .setParamAsName("及格率").setParamType(0).setParamValue("60");
		// eps.add(ep);
		// ep = new ExamPaprameter().setExam(exam).setParamName("bjgl")
		// .setParamAsName("不及格率").setParamType(0).setParamValue("60");
		// eps.add(ep);
		//
		// examPaprameterDao.saves(eps);
	}

	private void calcuateExamAttr(Exam exam) {
		int gradeId = exam.getGrade().getId().intValue();

		String gradeLevel = "";
		String gradeType = "";
		int gradeNum = 0;
		// 生成界别，界别格式 ： 年级+学年 例如：小学2017届---12017，1代表小学 2代表初中 3代表高中
		if (gradeId <= 6) {
			gradeLevel = "小学";
			gradeNum = gradeId;
			gradeType = "1";
		} else if (gradeId > 6 && gradeId <= 9) {
			gradeLevel = "初";
			gradeNum = gradeId - 6;
			gradeType = "2";
		} else if (gradeId > 9 && gradeId <= 12) {
			gradeLevel = "高";
			gradeNum = gradeId - 9;
			gradeType = "3";
		}
		// 界别获取：选择的学年和年级自动班别界别，例如：2017-2018学年 一年级，则为2017届学生
		int curYear = exam.getSchoolYear();
		int jiebie = curYear - gradeNum;
		exam.setExamStudentJiebie(Integer.parseInt(gradeType + jiebie));
		exam.setExamStudentJiebieName(gradeLevel + jiebie + "级");
		createSchoolYearName(exam);
	}

	private void createSchoolYearName(Exam exam) {
		exam.setSchoolYearName(exam.getSchoolYear() - 1 + "-" + exam.getSchoolYear() + "学年");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.service.IExamService#update(com.cntest.fxpt.domain.Exam)
	 */
	@Override
	public void update(Exam exam) {
		calcuateExamAttr(exam);
		examDao.update(exam);
	}

	@Override
	public void updateStatus(Long examId, int status) {
		examDao.updateStatus(examId, status);
	}

	@Override
	public boolean getHasChoice(Long examid) {
		return examDao.getHasChoice(examid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.service.IExamService#delete(com.cntest.fxpt.domain.Exam)
	 */
	@Override
	public void delete(Exam exam) {
		examDao.delete(exam);
		examPaprameterDao.deleteParameterByExamId(exam.getId());

		examCheckinDao.deleteBy(exam);
		// 删除考试权限
		userService.deleteUserExamByExamId(exam.getId());
		// 删除考试分数线
		uplineScoreService.delete(exam.getId());
	}

	@Override
	public Exam findById(Long examId) {
		return examDao.findById(examId);
	}

	@Override
	public List<Exam> list(Page<Exam> page) {
		return examDao.list(page);
	}

	private List<Exam> convertToExam(List<Object[]> exams) {
		ArrayList<Exam> result = new ArrayList<Exam>();
		for (Object[] obj : exams) {
			Exam exam = (Exam) obj[0];
			Grade grade = (Grade) obj[1];
			exam.setGrade(grade);
			result.add(exam);
		}
		return result;
	}

	@Override
	public boolean hasExamStudent(Exam exam) {
		int count = examDao.getExamStudentCount(exam);
		return count > 0 ? true : false;
	}

	@Override
	public boolean hasTestPaper(Exam exam) {
		int count = examDao.getTestPaperCount(exam);
		return count > 0 ? true : false;
	}

	@Override
	public ExamCheckin createCheckin(Exam exam, String... specs) {
		Specification spec = StudentSpecificationBuilder.build(specs);

		if (spec == null)
			return null;

		Page<ExamStudent> pager = new Page<ExamStudent>();
		pager.setPagesize(1);
		pager.addParameter("q", "false");
		examStudentDao.list(pager, exam.getId());
		ExamCheckin examCheckin = new ExamCheckin(exam, specs);
		examCheckin.setExamineeTotal(pager.getTotalRows());
		examCheckin.begainCheckin();
		examCheckinDao.save(examCheckin);

		return examCheckin;
	}

	@Override
	public Exam tryGetAnalysisExam() {
		return examDao.tryGetAnalysisExam();
	}

	@Override
	public String[] getExamNations(Exam exam) {
		String[] nations = examDao.findExamNations(exam);
		if (nations.length == 1 && (nations[0] == null || "null".equals(nations[0].toLowerCase())))
			return null;
		return nations;
	}

	@Override
	public Education getExamRootEducation(Exam exam) {
		List<School> schools = examDao.findExamSchools(exam);
		Education root = new Education();
		root.setName(exam.getOwnerName());
		root.setCode(exam.getOwnerCode());

		HashMap<String, Education> cities = new HashMap<String, Education>();
		HashMap<String, Education> counties = new HashMap<String, Education>();
		if (schools != null) {
			for (School school : schools) {
				Education county = school.getEducation();
				Education edu = counties.get(county.getCode());
				Education eduSchool = new Education();
				eduSchool.setId(school.getId());
				eduSchool.setCode(school.getCode());
				eduSchool.setName(school.getName());
				if (edu == null) {
					edu = county;
					counties.put(edu.getCode(), edu);
				}
				// eduSchool.setParent(edu);
				edu.addChild(eduSchool);

				Education city = edu.getParent();
				if (city != null) {
					if (cities.containsKey(city.getCode())) {
						city = cities.get(city.getCode());
					} else {
						cities.put(city.getCode(), city);
					}
					city.addChild(edu);
				}
			}
		}
		// List edus = new ArrayList();

		if (cities.size() > 1) {
			Iterator it = cities.keySet().iterator();
			while (it.hasNext()) {
				root.addChild(cities.get(it.next()));
			}
		} else {
			Iterator it = counties.keySet().iterator();
			while (it.hasNext()) {
				root.addChild(counties.get(it.next()));
			}
		}
		// root.addChild(child);

		return root;
	}

	@Override
	public boolean hasStudentsAndSubjcetsAndCj(Long examid) {
		return examDao.hasStudentsAndSubjcetsAndCj(examid);
	}

	@Override
	public List<Exam> getExamAllList() {
		return examDao.getExamAllList();
	}

	@Override
	public Page<Exam> getPowerList(String username, Page<Exam> page) {
		User userA = userService.findUserBy(username);
		String examids = userService.getUserExamByRoleId(userA.getPk()).replaceAll(" ", "");
		// 因授权的默认选择
		for (int i = page.getList().size() - 1; i >= 0; i--) {
			if (page.getList().get(i) != null
					&& examids.indexOf(("," + page.getList().get(i).getId().toString() + ",")) == -1) {
				page.getList().remove(i);
			}
		}
		// 重新设置数据总条数
		if (examids != "") {
			page.setTotalRows(examids.split(",").length - 2);
		} else {
			page.setTotalRows(0);
		}
		return page;
	}

	@Override
	public void examlist(com.cntest.common.query.Query<Exam> query, Long userId) {
		examDao.examlist(query, userId);
	}

	@Override
	public List<Exam> getExamByorgCodes(String codes) {
		return examDao.getExamByorgCodes(codes);
	}

	@Override
	public List<Exam> listBybach(Page<Exam> page, Integer... status) {
		return examDao.listBybach(page, status);
	}

	@Override
	public List<Exam> getExamAllSchoolYears() {
		return examDao.getExamAllSchoolYears();
	}
}
