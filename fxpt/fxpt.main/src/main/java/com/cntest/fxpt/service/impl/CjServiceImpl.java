/*
 * @(#)com.cntest.fxpt.service.impl.CjServiceImpl.java 1.0 2014年10月17日:下午3:38:15
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.common.page.Page;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.anlaysis.bean.Param;
import com.cntest.fxpt.anlaysis.bean.StudentCj;
import com.cntest.fxpt.anlaysis.bean.TempItemScore;
import com.cntest.fxpt.anlaysis.bean.TempTotalScore;
import com.cntest.fxpt.bean.CjWebRetrieveResult;
import com.cntest.fxpt.bean.DataField;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.ExamCjDataSum;
import com.cntest.fxpt.domain.ExamStudent;
import com.cntest.fxpt.domain.FileManage;
import com.cntest.fxpt.domain.TestPaper;
import com.cntest.fxpt.repository.IAnalysisTestPaperDao;
import com.cntest.fxpt.repository.ICjDao;
import com.cntest.fxpt.repository.IExamDao;
import com.cntest.fxpt.repository.IExamStudentDao;
import com.cntest.fxpt.repository.IFileManageDao;
import com.cntest.fxpt.repository.IITemDao;
import com.cntest.fxpt.repository.IShowMessageDao;
import com.cntest.fxpt.repository.ITestPaperDao;
import com.cntest.fxpt.service.ICjService;
import com.cntest.fxpt.service.etl.IDataFieldService;
import com.cntest.security.UserDetails;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月17日 下午3:38:15
 * @version 1.0
 */
@Service("ICjService")
public class CjServiceImpl implements ICjService {

  private static final Logger log = LoggerFactory.getLogger(CjServiceImpl.class);

  @Autowired(required = false)
  @Qualifier("IExamDao")
  private IExamDao examDao;

  @Autowired(required = false)
  @Qualifier("IITemDao")
  private IITemDao itemDao;

  @Autowired(required = false)
  @Qualifier("ICjDao")
  private ICjDao cjDao;

  @Autowired(required = false)
  @Qualifier("ITestPaperDao")
  private ITestPaperDao testPaperDao;

  @Autowired(required = false)
  @Qualifier("IAnalysisTestPaperDao")
  private IAnalysisTestPaperDao analysisTestPaperDao;

  @Autowired(required = false)
  @Qualifier("IExamStudentDao")
  private IExamStudentDao examStudentDao;

  @Autowired(required = false)
  @Qualifier("IFileManageDao")
  private IFileManageDao fileManageDao;

  @Autowired
  private UserService userService;

  @Autowired(required = false)
  @Qualifier("etl.IDataFieldService")
  private IDataFieldService dataFieldService;

  @Autowired(required = false)
  @Qualifier("IShowMessageDao")
  private IShowMessageDao showMessageDao;

  @Override
  public int deleteCj(TestPaper testPaper) {

    Long testPaperId = testPaper.getId();
    int count = cjDao.deleteCj(testPaperId);
    testPaperDao.updateTestPaperImportCjStatus(testPaperId, false, 0);
    analysisTestPaperDao.updatePaperType(testPaper.getId(), 0);
    Exam exam = examDao.findById(testPaper.getExam().getId());
    if (exam.getImpCjCount() > 0) {
      exam.setImpCjCount(exam.getImpCjCount() - 1);
    } else {
      exam.setImpCjCount(0);
    }
    examDao.update(exam);
    // 删除成绩时，对应删除原始文件信息
    List<FileManage> fileList = fileManageDao.findFileByTestPaperId(testPaperId);
    fileManageDao.deleteFileByTestPaperId(testPaperId);
    fileManageDao.deleteFiels(fileList);

    showMessageDao.deleteMessageByExamid(exam.getId(), 3, null);
    return count;
  }

  @Override
  public int deleteExamAllCj(Long examId) {
    int count = cjDao.deleteExamAllCj(examId);
    testPaperDao.updateExamAllTestPaperImportCjStatus(examId, false, 0);
    analysisTestPaperDao.updateExamAllAnalysisTestPaperPaperType(examId, 0);
    Exam exam = examDao.findById(examId);
    exam.setImpCjCount(0);
    examDao.update(exam);

    showMessageDao.deleteMessageByExamid(examId, 3, null);
    return count;
  }

  @Override
  public void importFail(Long testPaperId) {
    cjDao.deleteCjKn(testPaperId);
  }

  @Override
  public void importSuccess(CjWebRetrieveResult wrr) throws Exception {
    Long examId = wrr.getExamId();
    Long testPaperId = wrr.getTestPaperId();
    int importType = wrr.getImportType();
    // 选做题处理(不适应选做题有小题的情况，需单独处理) 暂时屏蔽
    /*
     * List<Item> list = itemDao.findAllQk(examId, testPaperId); if (list.size() > 0) { String rules
     * = SystemConfig.newInstance().getValue("take.score.rules");
     * cjDao.updateChoiceData(testPaperId, rules); }
     */
    // 数据.25和.75的处理,初三和高三的总分四舍五入，其他的保留一位小数
    // cjDao.dataValidate(examId, testPaperId);
    copyCjandItemCj(wrr);
    List<Integer> wl = cjDao.getExamStudentWlFor(testPaperId);
    int paperType = 0;
    if (wl.size() > 1) {
      paperType = 0;
    } else if (wl.size() == 1) {
      paperType = wl.get(0);
    }
    testPaperDao.updateTestPaperImportCjStatus(testPaperId, true, paperType);
    analysisTestPaperDao.updatePaperType(testPaperId, paperType);
    // 设置考试 已导入的成绩 科目数量
    Exam exam = examDao.findById(examId);
    if (importType == 1) {
      int num = testPaperDao.findByid(examId);
      exam.setImpCjCount(num);
    } else {
      exam.setImpCjCount(exam.getImpCjCount() + 1);
    }
    examDao.update(exam);

    // 添加文件管理信息
    UserDetails user = userService.getCurrentLoginedUser();
    String username = user.getRealName();
    long type = 3L;
    fileManageDao.saveFileMsg(wrr, type, testPaperId, username);
    fileManageDao.copyFile(wrr);

    // 添加导入字段和显示字段一致信息
    List<DataField> dataFields = dataFieldService.list(10L);
    List<DataField> dataFieldsTmp = dataFieldService.list(7L);
    for (DataField dataField : dataFieldsTmp) {
      dataFields.add(dataField);
    }
    // 添加字段导入信息(保存最后一次的设置)
    showMessageDao.addMessageByExamid(examId, testPaperId, 3, dataFields);

  }

  /*
   * (non-Javadoc)
   * 
   * @see com.cntest.fxpt.service.ICjService#list(java.lang.Long, java.lang.Long, java.lang.Long,
   * com.cntest.common.page.Page)
   */
  @Override
  public List<Map<String, Object>> listCj(Long examId, Long testPaperId, Long schoolId,
      Page<Map<String, Object>> page) {

    TestPaper testPaper = testPaperDao.get(testPaperId);

    Map<String, Integer> studentCount = examStudentDao.statStudentCount(examId);
    int totalRows = 0;
    if (testPaper.getPaperType() == 1) {
      totalRows = studentCount.get("numL");
    } else if (testPaper.getPaperType() == 2) {
      totalRows = studentCount.get("numW");
    } else {
      totalRows = studentCount.get("num");
    }
    page.setTotalRows(totalRows);

    int firstNum = (page.getCurpage() - 1) * page.getPagesize();
    firstNum = firstNum < 0 ? 0 : firstNum;
    int maxNum = page.getPagesize();

    List<Map<String, Object>> studentCjList =
        cjDao.list(examId, testPaper.getPaperType(), testPaperId, schoolId, firstNum, maxNum);

    LinkedHashMap<Long, Map<String, Object>> studentCjMap =
        new LinkedHashMap<Long, Map<String, Object>>();
    for (Map<String, Object> cj : studentCjList) {
      Long id = Long.parseLong(cj.get("id").toString());
      Map<String, Object> tmpCj = studentCjMap.get(id);
      if (tmpCj == null) {
        tmpCj = new HashMap<String, Object>();
        studentCjMap.put(id, tmpCj);

        tmpCj.put("id", id);
        tmpCj.put("name", cj.get("NAME").toString());
        tmpCj.put("zkzh", cj.get("zkzh").toString());

        boolean isQk =
            cj.get("isQk") == null ? false : Boolean.parseBoolean(cj.get("isQk").toString());
        tmpCj.put("isQk", isQk ? 1 : 0);

        double totalScore =
            cj.get("totalScore") == null ? 0 : Double.parseDouble(cj.get("totalScore").toString());
        double zgScore =
            cj.get("zgScore") == null ? 0 : Double.parseDouble(cj.get("zgScore").toString());
        double kgScore =
            cj.get("kgScore") == null ? 0 : Double.parseDouble(cj.get("kgScore").toString());
        tmpCj.put("totalScore", totalScore);
        tmpCj.put("zgScore", zgScore);
        tmpCj.put("kgScore", kgScore);
      }

      long itemId = cj.get("itemId") == null ? 0L : Long.parseLong(cj.get("itemId").toString());
      double score = cj.get("score") == null ? 0 : Double.parseDouble(cj.get("score").toString());
      String selOpt = cj.get("selOpt") == null ? "" : cj.get("selOpt").toString();

      tmpCj.put("score" + itemId, score);
      tmpCj.put("selOpt" + itemId, selOpt);
    }

    List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
    for (Map<String, Object> cj : studentCjMap.values()) {
      result.add(cj);
    }
    page.setList(result);
    return result;
  }

  @Override
  public List<TestPaper> statTestPaperExamCount(Long examId) {
    Map<String, Integer> studentCount = examStudentDao.statStudentCount(examId);
    List<TestPaper> testPapers = testPaperDao.list(examId);
    ArrayList<TestPaper> result = new ArrayList<TestPaper>();
    Map<Long, Integer> testPaperSKRS = cjDao.statTestPaperSKRS(examId);
    for (TestPaper tp : testPapers) {
      if (tp.isHasCj()) {
        Integer skrs = testPaperSKRS.get(tp.getId());
        skrs = skrs == null ? 0 : skrs;
        int bkrs = 0;
        if (tp.getPaperType() == 1) {
          bkrs = studentCount.get("numL");
        } else if (tp.getPaperType() == 2) {
          bkrs = studentCount.get("numW");
        } else {
          bkrs = studentCount.get("num");
        }
        tp.setSkrs(skrs);
        tp.setBkrs(bkrs);
        result.add(tp);
      }
    }
    return result;
  }

  @Override
  public List<TempTotalScore> loadCj(Long examId, Param... params) {
    return cjDao.loadCj(examId, params);
  }

  @Override
  public List<TempItemScore> loadItemCj(Long examId, Param... params) {
    return cjDao.loadItemCj(examId, params);
  }

  @Override
  public StudentCj loadStudentCj(Long examId, String studentId) {
    ExamStudent examStudent = examStudentDao.get(examId, studentId);
    return null;
  }

  @Override
  public boolean appendCleanBySchoolIds(Long testPaper, List<String> hashSet) {
    return cjDao.appendCleanBySchoolIds(testPaper, hashSet);
  }

  @Override
  public void copyCjandItemCj(CjWebRetrieveResult wrr) {
    cjDao.copyCjandItemCj(wrr);
  }

  @Override
  public List<ExamCjDataSum> dataSumList(String examData, String examName) {
    return cjDao.dataSumList(examData, examName);
  }

  @Override
  public void setPageMsg(List<ExamCjDataSum> list, Page<ExamCjDataSum> page) {
    List<ExamCjDataSum> datalist = new ArrayList<ExamCjDataSum>();

    int first = (page.getCurpage() - 1) * page.getPagesize();
    first = first < 0 ? 0 : first;
    int last = 0;
    if (list.size() > first && first + 15 >= list.size()) {
      last = list.size();
    } else if (list.size() > first && first + 15 < list.size()) {
      last = first + 15;
    }
    for (int i = first; i < last; i++) {
      datalist.add(list.get(i));
    }

    page.setList(datalist);
    page.setTotalRows(list.size());
  }

  // 删除临时表信息
  @Override
  public void deleteCjKn() {
    cjDao.deleteCjKn(0L);
  }

  @Override
  public void dataValidate(Long examId, Long testPaperId) throws Exception {
    cjDao.dataValidate(examId, testPaperId);

  }

  @Override
  public List<String> cjValidate(CjWebRetrieveResult wrr) throws Exception {
    return cjDao.cjValidate(wrr);
  }
}
