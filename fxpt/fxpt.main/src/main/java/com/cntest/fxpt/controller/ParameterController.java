/*
 * @(#)com.cntest.fxpt.controller.ExamControoler.java 1.0 2014年5月17日:上午10:06:45
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.common.controller.BaseController;
import com.cntest.common.page.Page;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.bean.ParamSetting;
import com.cntest.fxpt.bean.Uplinescore;
import com.cntest.fxpt.domain.AnalysisTestpaper;
import com.cntest.fxpt.domain.CombinationSubject;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.ExamPaprameter;
import com.cntest.fxpt.domain.TestPaper;
import com.cntest.fxpt.domain.UplineScore;
import com.cntest.fxpt.domain.ZFSubjectBuildRule;
import com.cntest.fxpt.service.IAnalysisTestPaperService;
import com.cntest.fxpt.service.ICombinationSubjectService;
import com.cntest.fxpt.service.IExamPaprameterService;
import com.cntest.fxpt.service.IExamService;
import com.cntest.fxpt.service.ITestPaperService;
import com.cntest.fxpt.service.IUplineScoreService;
import com.cntest.fxpt.service.ZFSubjectBuildRuleService;
import com.cntest.fxpt.util.LogUtil;
import com.cntest.fxpt.util.TitleUtil;
import com.cntest.web.view.ModelAndViewBuilder;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月17日 上午10:06:45
 * @version 1.0
 */
@Controller
@RequestMapping("/param")
public class ParameterController extends BaseController {
  @Autowired(required = false)
  @Qualifier("IExamService")
  private IExamService examService;

  @Autowired(required = false)
  @Qualifier("IAnalysisTestPaperService")
  private IAnalysisTestPaperService atpService;

  @Autowired(required = false)
  @Qualifier("ITestPaperService")
  private ITestPaperService testPaperService;

  @Autowired(required = false)
  @Qualifier("ZFSubjectBuildRuleService")
  private ZFSubjectBuildRuleService zfSubjectService;

  @Autowired(required = false)
  @Qualifier("ICombinationSubjectService")
  private ICombinationSubjectService combinationSubjectService;

  @Autowired(required = false)
  @Qualifier("IUplineScoreService")
  private IUplineScoreService uplineScoreService;

  @Autowired(required = false)
  @Qualifier("IExamPaprameterService")
  private IExamPaprameterService paramService;

  @Autowired(required = false)
  private UserService userService;

  @RequestMapping(value = "/list/exam/{currentPage}/{pageSize}", method = RequestMethod.GET)
  public ModelAndView list(@PathVariable int currentPage, @PathVariable int pageSize,
      HttpServletRequest request, HttpServletResponse response) throws Exception {
    Page<Exam> page = newPage(currentPage, pageSize, request);
    boolean isEmpty = page.getParameter().isEmpty();
    page.addParameter("isParamList", "true");

    List<Exam> exams = examService.list(page);
    String title = TitleUtil.getTitle(request.getServletPath());

    if (isEmpty) {
      return ModelAndViewBuilder.newInstanceFor("/param/examList").append("page", page)
          .append("title", title).build();
    } else {
      return ModelAndViewBuilder.newInstanceFor("/param/examListBody").append("page", page).build();
    }
  }

  @RequestMapping(value = "/setting/{examId}", method = RequestMethod.GET)
  public ModelAndView newSetting(@PathVariable Long examId, HttpServletRequest request)
      throws Exception {
    Exam exam = examService.findById(examId);
    List<TestPaper> testPapers = testPaperService.listByExamId(examId);


    List<AnalysisTestpaper> analysisTestpaper = atpService.listAllWith(examId);
    List<AnalysisTestpaper> analysisTestpaperL = new ArrayList<>();// 理科的科目 and 不分文理
    List<AnalysisTestpaper> analysisTestpaperW = new ArrayList<>();// 文科的科目 2文1理
    for (AnalysisTestpaper paper : analysisTestpaper) {
      if (paper.getPaperType() == 2) {
        analysisTestpaperW.add(paper);
      } else if (paper.getPaperType() == 1) {
        analysisTestpaperL.add(paper);
      } else if (paper.getSubject().getId() != 98) {
        analysisTestpaperL.add(paper);
        analysisTestpaperW.add(paper);
      }
    }
    List<CombinationSubject> combinationSubjects = combinationSubjectService.listByExamId(examId);

    HashMap<String, String> scoreMap = new HashMap<String, String>();
    scoreMap.put("yuce", "1");
    scoreMap.put("youxiao", "2");
    scoreMap.put("jianzi", "3");
    scoreMap.put("difen", "3");
    scoreMap.put("dabiao", "4");
    HashMap<String, Integer> levelMap = new HashMap<String, Integer>();
    levelMap.put("jianzi", 1);
    levelMap.put("difen", 2);
    HashMap<String, Uplinescore<UplineScore>> score_m = new HashMap<>();
    for (String key : scoreMap.keySet()) {
      Uplinescore<UplineScore> score = new Uplinescore<>();
      String keyFoVal = scoreMap.get(key);
      Integer levelForVal = levelMap.get(key);
      // 边缘生设置
      if (exam.isWlForExamStudent()) {
        score.setParamslsitL(uplineScoreService.list(exam.getId(), 1, keyFoVal, levelForVal));// 理科
        score.setParamslsitW(uplineScoreService.list(exam.getId(), 2, keyFoVal, levelForVal));// 文科
      } else {
        score.setParamslsitN(uplineScoreService.list(exam.getId(), 0, keyFoVal, levelForVal));// 不分文理。。。
      }
      score_m.put(key, score);
    }

    // 设置有效总分（kn_zfsubjectbuildrule）
    List<ZFSubjectBuildRule> zfAll = zfSubjectService.list();
    boolean zfwl = false;
    boolean zflanguage = false;
    for (ZFSubjectBuildRule zfList : zfAll) {
      if (zfList.getClassifyFiled().equalsIgnoreCase("wl")) {
        zfwl = zfList.isAvailable();
      } else if (zfList.getClassifyFiled().equalsIgnoreCase("languageType")) {
        zflanguage = zfList.isAvailable();
      }
    }
    List<ExamPaprameter> params = paramService.listByExamId(examId);
    HashMap<String, ExamPaprameter> model = new HashMap<String, ExamPaprameter>();
    boolean flg = false;
    for (ExamPaprameter p : params) {
      model.put(p.getParamName(), p);
      if (p.getParamName() == "scoreDip") {
        flg = p.getParamValue() == "1" ? true : false;
      }
    }
    LinkedHashMap<String, String> setting = new LinkedHashMap<String, String>();
    setting.put("yuce", "预测上线分数线");
    setting.put("dabiao", "达标分析分数线");
    setting.put("youxiao", "有效分分析分数线");
    setting.put("jianzi", "尖子生统计分数线");
    setting.put("difen", "低分段统计分数线");

    // 分析参数设置
    return ModelAndViewBuilder.newInstanceFor("/param/setting").append("params", model)
        .append("exam", exam).append("testPapers", testPapers)
        .append("combinationSubjects", combinationSubjects).append("flg", flg).append("zfwl", zfwl)
        .append("zflanguage", zflanguage).append("setting", setting).append("score_m", score_m)
        .append("analysisTestpaperW", analysisTestpaperW)
        .append("analysisTestpaperL", analysisTestpaperL).append("scoreMap", scoreMap).build();
  }

  @RequestMapping(value = "/setting/{examId}", method = RequestMethod.POST)
  public ModelAndView setting(@PathVariable Long examId, @RequestBody ParamSetting paramSetting)
      throws Exception {
    Exam exam = examService.findById(examId);
    String status = "失败", info = "", erre = "";
    try {
      paramSetting.setExam(exam);
      List<TestPaper> testPapers = paramSetting.getTestPapers();
      // 设置科目是否是主科，AB卷
      testPaperService.updateMainSubject(testPapers);
      // 设置有效总分（wl或者 languageType）
      zfSubjectService.updateZF(paramSetting.getZfSubject());
      combinationSubjectService.add(exam.getId(), paramSetting.getCustomizeSubjects());
      uplineScoreService.add(exam.getId(), paramSetting.getUplineScores());
      paramService.saves(paramSetting.getParams());
      status = "成功";
    } catch (Exception e) {
      erre = LogUtil.e(e);
      throw e;
    } finally {
      info += "操作成功";
      LogUtil.log("考试管理>分析参数设置	", "设置分析参数", exam.getName(), status, info, erre);
    }
    return ModelAndViewBuilder.newInstanceFor("").build();
  }
}
