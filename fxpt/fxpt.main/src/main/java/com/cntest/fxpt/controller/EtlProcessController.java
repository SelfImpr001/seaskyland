/*
 * @(#)com.cntest.fxpt.controller.EtlProcessController.java 1.0 2014年5月19日:上午9:12:46
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.common.controller.BaseController;
import com.cntest.foura.service.OrganizationService;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.bean.DataField;
import com.cntest.fxpt.bean.ItemXlsUploadFileContentInfo;
import com.cntest.fxpt.bean.OmrStrAndScoreStr;
import com.cntest.fxpt.bean.UploadFileContentInfo;
import com.cntest.fxpt.bean.UploadFileInfo;
import com.cntest.fxpt.bean.UploadFileValidateResult;
import com.cntest.fxpt.bean.WebFieldRelation;
import com.cntest.fxpt.bean.WebRetrieveResult;
import com.cntest.fxpt.bean.XlsUploadFileContentInfo;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.TestPaper;
import com.cntest.fxpt.etl.business.IEtlExecuteor;
import com.cntest.fxpt.etl.business.util.ImportUtil;
import com.cntest.fxpt.etl.domain.EtlProcessResult;
import com.cntest.fxpt.service.ICjService;
import com.cntest.fxpt.service.IExamService;
import com.cntest.fxpt.service.IExamStudentService;
import com.cntest.fxpt.service.IItemService;
import com.cntest.fxpt.service.ITestPaperService;
import com.cntest.fxpt.service.etl.IDataFieldService;
import com.cntest.fxpt.util.LogUtil;
import com.cntest.fxpt.util.SystemConfig;
import com.cntest.fxpt.util.UploadHelper;
import com.cntest.util.SpringContext;
import com.cntest.web.view.ModelAndViewBuilder;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月19日 上午9:12:46
 * @version 1.0
 */
@Controller
@RequestMapping("/etl")
public class EtlProcessController extends BaseController {
  private static final Logger log = LoggerFactory.getLogger(EtlProcessController.class);
  @Autowired(required = false)
  @Qualifier("etl.IDataFieldService")
  private IDataFieldService dataFieldService;

  @Autowired(required = false)
  @Qualifier("IExamStudentService")
  private IExamStudentService examStudentService;

  @Autowired(required = false)
  @Qualifier("ITestPaperService")
  private ITestPaperService testPaperService;

  @Autowired(required = false)
  private UserService userService;

  @Autowired(required = false)
  @Qualifier("ICjService")
  private ICjService cjService;

  @Autowired(required = false)
  @Qualifier("IExamService")
  private IExamService examService;

  @Autowired(required = false)
  @Qualifier("IItemService")
  private IItemService itemService;

  @Autowired
  private OrganizationService organizationService;

  HttpSession session = null;
  int execnum = 1;

  /**
   * \ 组织架构导入
   * 
   * @param webRetrieveResult
   * @return
   * @throws Exception
   */
  @RequestMapping("/importOrg")
  public ModelAndView importOrg(@RequestBody WebRetrieveResult webRetrieveResult,
      HttpServletRequest request) throws Exception {
    log.debug("进入导入数据Controller");
    String status = "失败", info = "", erre = "";
    EtlProcessResult processResult = new EtlProcessResult();
    String etl = "";
    try {
      log.debug("进入导入数据Controller");
      setWebRetrieveResult(webRetrieveResult);
      webRetrieveResult.setValidate(false);
      String message = "";
      IEtlExecuteor executeor = getIEtlExecuteor(webRetrieveResult.getSchemeType());

      // 获取导入类型（覆盖导入 0 ---- 追加导入 1）
      int importType = webRetrieveResult.getImportType();
      if (importType == 1) {
        processResult = executeor.execute(webRetrieveResult);
        // processResult.setHasError(false);
      } else {
        if ((organizationService.deleteAllOrg())) {
          processResult = executeor.execute(webRetrieveResult);
        } else {
          processResult.setHasError(true);
          message += "删除原始数据失败！";
        }

      }
      // 如果出现Excel数据不合法
      if (processResult.isHasError()) {
        for (int i = 0; i < processResult.getLogs().size(); i++) {
          etl += processResult.getLogs().get(i);
        }
        String[] strL = etl.split("ErrorMessage");
        if (strL.length >= 2) {
          message += strL[1];
        }
        info = "导入失败，" + message;
      } else {
        info = "导入成功，读取" + processResult.getLoadNum() + "行，成功导入" + processResult.getExtractNum();
        status = "成功";
      }
      processResult.setMessage(message);
    } catch (Exception e) {
      erre = LogUtil.e(e);
      throw e;
    } finally {
      LogUtil.log("组织管理>组织列表", "组织导入", "组织", status, info, erre + "<br/>" + etl);
    }
    log.debug("执行完毕导入数据Controller");

    if (processResult.isHasError()) {
      return ModelAndViewBuilder.newInstanceFor("/organization/processResult")
          .append("processResult", processResult).build();
    } else {
      return ModelAndViewBuilder.newInstanceFor("/etl/processResult")
          .append("processResult", processResult).build();
    }
  }

  /**
   * \ 学生和细目表导入
   * 
   * @param webRetrieveResult
   * @return
   * @throws Exception
   */
  @RequestMapping("/exec")
  public ModelAndView exec(@RequestBody WebRetrieveResult webRetrieveResult,
      HttpServletRequest request) throws Exception {
    log.debug("进入导入数据Controller");

    String status = "失败", info = "", erre = "", title = "";
    EtlProcessResult processResult = new EtlProcessResult();
    String etl = "";
    try {
      session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
          .getRequest().getSession();
      execnum = 1;
      session.setAttribute("step", "初始化导入...");
      session.setAttribute("pc", "0");
      setWebRetrieveResult(webRetrieveResult);
      String message = "";
      IEtlExecuteor executeor = getIEtlExecuteor(webRetrieveResult.getSchemeType());
      // 获取导入类型（覆盖导入 0 ---- 追加导入 1）
      /**
       * SchemeType 1:Student 2:Item双向细目表 3:CJ成绩导入 4:School学校导入 5:StudentBase 11:Organization组织导入
       */
      int importType = webRetrieveResult.getImportType();
      // 获取项目目录的上级目录
      String filePath = getParentFilePath(request);
      webRetrieveResult.setValidate(false);
      webRetrieveResult.setFileParentDir(filePath);
      if (webRetrieveResult.getSchemeType() == 1) {
        title = "报名库导入";
      } else if (webRetrieveResult.getSchemeType() == 2) {
        title = "细目表导入";
      }
      if (importType == 1) {
        processResult = executeor.execute(webRetrieveResult);
      } else {
        if (executeor.executeClean(webRetrieveResult)) {
          processResult = executeor.execute(webRetrieveResult);
          // processResult.setHasError(false);
        } else {
          log.debug("导入学生时清理原始数据失败！");
          message = message + "导入时清理原始数据失败！";
          processResult.setHasError(true);
        }
      }

      if (processResult.isHasError()) {
        info = title + "失败，";
        if (processResult.getLogs() != null && processResult.getLogs().size() > 0) {
          info += StringUtils.join(processResult.getLogs(), ",");
        }
      } else {
        info = title + "成功！加载" + processResult.getLoadNum() + "条数据，导入"
            + processResult.getExtractNum() + "条数据";
        status = "成功";
      }
      if ("".equals(processResult.getMessage()) || processResult.getMessage() == null) {
        processResult.setMessage(message);
      }
      log.debug("执行完毕导入数据Controller");

    } catch (Exception e) {
      erre = LogUtil.e(e);
      throw e;
    } finally {
      processResult.setLogs(null);
      Exam exam = examService.findById(webRetrieveResult.getExamId());
      userService.evictSession(exam);
      LogUtil.log("考试管理>" + title, title, exam.getName(), status, info, erre + "<br>" + etl);
      //
    }
    return ModelAndViewBuilder.newInstanceFor("/etl/processResult")
        .append("processResult", processResult).build();
    // return ModelAndViewBuilder.newInstanceFor("").build();
  }

  @RequestMapping(value = "/execProgress")
  public ModelAndView execProgress(HttpServletRequest request) throws Exception {
    session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()
        .getSession();
    if (execnum == 1) {
      execnum = 2;
      session.setAttribute("step", "初始化导入...");
      session.setAttribute("pc", "0");
    }
    Object obj = null;
    Object percent = null;
    String result = "";
    String result1 = "";
    try {
      obj = session.getAttribute("step");
      percent = session.getAttribute("pc");
      if (obj == null) {
        result = "执行:抽取数据步骤。正在处理第1条数据";
      } else {
        result = obj.toString();
      }
      if (percent == null) {
        result1 = "0";
      } else {
        result1 = percent.toString();
      }
      // if(obj!=null){
      // dealRowNum = Integer.parseInt(obj.toString());
      // }
      // progress= new Progress(dealRowNum, 4992);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ModelAndViewBuilder.newInstanceFor("/etl/progressbarexec").append("progress", result)
        .append("percent", result1).build();
  }

  private String getParentFilePath(HttpServletRequest request) {
    String filePath = request.getSession().getServletContext().getRealPath("");
    int last = filePath.lastIndexOf("/");
    if (last == -1) {
      last = filePath.lastIndexOf("\\");
    }
    return filePath = filePath.substring(0, last);
  }

  /**
   * 导入验证数据重复
   * 
   * @param fileInfoMap
   * @return
   * @throws Exception
   */
  @RequestMapping("/batchExecValidate")
  public ModelAndView batchExecValidate(@RequestBody WebRetrieveResult webRetrieveResult,
      HttpServletRequest request) throws Exception {
    EtlProcessResult processResult = new EtlProcessResult();
    setWebRetrieveResult(webRetrieveResult);
    String message = "";
    IEtlExecuteor executeor = getIEtlExecuteor(webRetrieveResult.getSchemeType());
    int importType = webRetrieveResult.getImportType();
    int schemeType = webRetrieveResult.getSchemeType();
    Long examId = webRetrieveResult.getExamId();
    // 获取项目目录的上级目录
    String filePathW = getParentFilePath(request);
    webRetrieveResult.setValidate(true);
    webRetrieveResult.setFileParentDir(filePathW);

    if (schemeType != 2) {
      Exam exam = examService.findById(examId);
      // 验证导入成绩和导入学生的重复性
      if (exam.isHasExamStudent()) {
        processResult = executeor.execute(webRetrieveResult);
      }
      return ModelAndViewBuilder.newInstanceFor("/etl/dataJSON")
          .append("is", processResult.isValidateError())
          .append("message", processResult.getMessage()).append("processResult", processResult)
          .build();
    }

    String filePath = UploadHelper.getDir() + webRetrieveResult.getFileName();
    UploadFileInfo fileInfo = new UploadFileInfo().setPath(filePath)
        .setSuffix(webRetrieveResult.getSuffix()).setDesFileName(webRetrieveResult.getFileName())
        .setName(webRetrieveResult.getFileRealName());
    UploadFileContentInfo fileContent =
        UploadHelper.getFileContent(schemeType, fileInfo, webRetrieveResult.getSheetName());
    UploadFileValidateResult validateResult =
        UploadHelper.getValidateResult(schemeType, fileContent, examId, true);
    ItemXlsUploadFileContentInfo itemFileContent = (ItemXlsUploadFileContentInfo) fileContent;
    String subjectName = itemFileContent.getTestPaperInfo().size() > 1
        ? itemFileContent.getTestPaperInfo().get(1) : "";
    boolean is = itemService.validate(subjectName, examId + "");

    return ModelAndViewBuilder.newInstanceFor("/etl/dataJSON").append("message", "已存在相同的数据是否覆盖")
        .append("processResult", processResult).append("is", is).build();
  }

  /**
   * 成绩导入
   * 
   * @param fileInfoMap
   * @return
   * @throws Exception
   */
  @RequestMapping("/batchexec")
  public ModelAndView batchExec(@RequestBody Map<String, Object> fileInfoMap,
      HttpServletRequest request) throws Exception {
    log.debug("进入导入数据Controller");
    String status = "失败", info = "", erre = "", title = "", name = "";
    EtlProcessResult processResult = new EtlProcessResult();
    String etl = "";
    Long examId = Long.parseLong(fileInfoMap.get("examId").toString());
    try {
      String message = "";
      String fileName = fileInfoMap.get("fileName").toString();
      String desFileName = fileInfoMap.get("desFileName").toString();
      String suffix = fileInfoMap.get("suffix").toString();
      int schemeType = Integer.parseInt(fileInfoMap.get("schemeType").toString());
      // 1覆盖导入 2 不覆盖
      boolean isOverlayImport = Boolean.parseBoolean(fileInfoMap.get("overlayImport").toString());
      int importType = 0;
      if (fileInfoMap.get("importType") != null) {
        importType = Integer.parseInt(fileInfoMap.get("importType").toString());
      }

      String filePath = UploadHelper.getDir() + desFileName;
      UploadFileInfo fileInfo = new UploadFileInfo().setPath(filePath).setSuffix(suffix)
          .setDesFileName(desFileName).setName(fileName);
      UploadFileContentInfo fileContent = UploadHelper.getFileContent(schemeType, fileInfo, null);
      UploadFileValidateResult validateResult =
          UploadHelper.getValidateResult(schemeType, fileContent, examId, true);
      WebRetrieveResult wrr = creawteWebRetrieveResult(examId, desFileName, suffix, schemeType,
          isOverlayImport, fileContent, validateResult, fileInfo);
      // wrr.setFileDir(UploadHelper.getDir() +File.separator+ desFileName);
      wrr.setFileRealName(fileInfoMap.get("fileRealName").toString());
      wrr.setImportType(importType);
      wrr.setValidate(false);
      // 获取项目目录的上级目录
      String fileParentPath = getParentFilePath(request);
      wrr.setFileParentDir(fileParentPath);
      setWebRetrieveResult(wrr);
      IEtlExecuteor executeor = getIEtlExecuteor(wrr.getSchemeType());
      if (wrr.getSchemeType() == 1) {
        title = "报名库导入";
      } else if (wrr.getSchemeType() == 2) {
        title = "细目表导入";
      } else if (wrr.getSchemeType() == 3) {
        title = "成绩信息导入";
      }
      // 追加导入
      if (importType == 1 && isOverlayImport) {
        processResult = executeor.execute(wrr);
      } else {
        if (!validateResult.isExistContent()
            || (validateResult.isExistContent() && isOverlayImport)) {
          if (executeor.executeClean(wrr)) {
            processResult = executeor.execute(wrr);
          } else {
            log.debug("追加导入时清理原始数据失败！");
            message = message + "导入时清理原始数据失败";
            processResult.setHasError(true);
          }
        } else {
          processResult.setHasError(true);
          processResult.setMessage("未导入，导入的内容已经存在。");
        }
      }

      if (processResult.isHasError()) {
        etl += StringUtils.join(processResult.getLogs(), ",");
        if (message != null && message.length() > 0) {
          message = "导入失败原因：" + message;
        }
        if (processResult.getMessage() == null)
          processResult.setMessage("");
        info = title + "失败，" + processResult.getMessage();

        if (processResult.getLogs() != null && processResult.getLogs().size() > 0) {
          info += StringUtils.join(processResult.getLogs(), ",");
        }
      } else {
        int num = processResult.getLoadNum();
        if ("wuhou".equals(SystemConfig.newInstance().getValue("area.org.code"))) {
          // 武侯的成绩分A、B、升学成绩，导致显示数据会比excel中的数据多2或者4倍
          TestPaper testPaper =
              testPaperService.selectPaperByExamIdAndPaperId(examId, wrr.getTestPaperId());
          // 如果有AB卷，是4倍，没有：是2倍
          if (testPaper != null && testPaper.isContainPaper()) {
            num = num / 4;
          } else {
            num = num / 2;
          }

        }
        message = "导入成功!读取数据" + processResult.getExtractNum() + "行，导入数据" + num + "行;";
        if (message == null)
          message = "";
        info = title + "," + message;
        status = "成功";
      }
      // if (processResult.isHasLog()) {
      // message += "查看详细结果<a href='#'>下载</a>日志文件。";
      // }

      if (!processResult.isHasError()) {
        // if(!processResult.isHasLog()){
        processResult.setMessage(message);
      } else {
        if (!(validateResult.isExistContent() && isOverlayImport)) {
          processResult.setMessage(processResult.getMessage());
        } else {
          processResult.setMessage(
              processResult.getMessage() + "查看详细结果<a href='#' class='logFileDown'>下载</a>日志文件。");
        }
      }

    } catch (Exception e) {
      erre = LogUtil.e(e);
      throw e;
    } finally {
      if (examId != null) {
        Exam exam = examService.findById(examId);
        name = exam.getName();
      }
      if (etl == null || etl.equals("null"))
        etl = "";

      LogUtil.log("考试管理>" + title, title, name, status, info, erre + "<br/>" + etl);
    }
    log.debug("执行完毕导入数据Controller");
    return ModelAndViewBuilder.newInstanceFor("/etl/dataJSON")
        .append("processResult", processResult).build();
  }

  public EtlProcessResult validate(Map<String, Object> fileInfoMap, HttpServletRequest request)
      throws Exception {
    EtlProcessResult processResult = new EtlProcessResult();
    Long examId = Long.parseLong(fileInfoMap.get("examId").toString());
    String fileName = fileInfoMap.get("fileName").toString();
    String desFileName = fileInfoMap.get("desFileName").toString();
    String suffix = fileInfoMap.get("suffix").toString();
    int schemeType = Integer.parseInt(fileInfoMap.get("schemeType").toString());

    boolean isOverlayImport = Boolean.parseBoolean(fileInfoMap.get("overlayImport").toString());
    int importType = 0;
    if (fileInfoMap.get("importType") != null) {
      importType = Integer.parseInt(fileInfoMap.get("importType").toString());
    }

    String filePath = UploadHelper.getDir() + desFileName;
    UploadFileInfo fileInfo = new UploadFileInfo().setPath(filePath).setSuffix(suffix)
        .setDesFileName(desFileName).setName(fileName);
    UploadFileContentInfo fileContent = UploadHelper.getFileContent(schemeType, fileInfo, null);
    UploadFileValidateResult validateResult =
        UploadHelper.getValidateResult(schemeType, fileContent, examId, true);
    WebRetrieveResult wrr = creawteWebRetrieveResult(examId, desFileName, suffix, schemeType,
        isOverlayImport, fileContent, validateResult, fileInfo);
    // wrr.setFileDir(UploadHelper.getDir() +File.separator+ desFileName);
    wrr.setFileRealName(fileInfoMap.get("fileRealName").toString());
    wrr.setImportType(importType);
    wrr.setValidate(false);
    // 获取项目目录的上级目录
    String fileParentPath = getParentFilePath(request);
    wrr.setFileParentDir(fileParentPath);
    setWebRetrieveResult(wrr);
    IEtlExecuteor executeor = getIEtlExecuteor(wrr.getSchemeType());
    return processResult = executeor.execute(wrr);
  }

  private void setWebRetrieveResult(WebRetrieveResult webRetrieveResult) throws Exception {
    String userName = userService.getCurrentLoginedUser().getUserName();
    webRetrieveResult.setUserName(userName);

    webRetrieveResult.setFileDir(UploadHelper.getDir());
    DataSource dataScource = SpringContext.getBean("ds");
    webRetrieveResult.setDataScource(dataScource);

    int schemeType = webRetrieveResult.getSchemeType();
    if (1 == schemeType) {
      webRetrieveResult.setTableName("kn_examstudent");
      webRetrieveResult.setTestPaperId(0L);
    } else if (2 == schemeType) {
      webRetrieveResult.setTableName("dw_dim_item");
      webRetrieveResult.setTestPaperId(0L);
      webRetrieveResult.setHeadRowIndex(1);
    } else if (3 == schemeType) {
      webRetrieveResult.setTableName("kn_examtestpapercj_tmp");
    } else if (4 == schemeType) {
      webRetrieveResult.setTableName("kn_school");
      webRetrieveResult.setTestPaperId(0L);
    } else if (5 == schemeType) {
      webRetrieveResult.setTableName("kn_studentbase");
      webRetrieveResult.setTestPaperId(0L);
    } else if (11 == schemeType) {
      // 组织架构零时表
      webRetrieveResult.setTableName("4a_org_tmp");
    }
  }

  private IEtlExecuteor getIEtlExecuteor(int schemeType) {
    IEtlExecuteor executeor = null;
    if (1 == schemeType) {
      executeor = SpringContext.getBean("Student.IEtlExecuteor");
    } else if (2 == schemeType) {
      executeor = SpringContext.getBean("Item.IEtlExecuteor");
    } else if (3 == schemeType) {
      executeor = SpringContext.getBean("CJ.IEtlExecuteor");
    } else if (4 == schemeType) {
      executeor = SpringContext.getBean("School.IEtlExecuteor");
    } else if (5 == schemeType) {
      executeor = SpringContext.getBean("StudentBase.IEtlExecuteor");
    } else if (11 == schemeType) {
      // 组织架构
      executeor = SpringContext.getBean("Organization.IEtlExecuteor");
    }
    return executeor;
  }

  public WebRetrieveResult creawteWebRetrieveResult(Long examId, String desFileName, String suffix,
      int schemeType, boolean isOverlayImport, UploadFileContentInfo fileContent,
      UploadFileValidateResult validateResult, UploadFileInfo fileInfo) {
    WebRetrieveResult result = new WebRetrieveResult();
    result.setExamId(examId);
    result.setSchemeType(schemeType);
    result.setFileName(desFileName);
    result.setSuffix(suffix);
    result.setFileDir(UploadHelper.getDir());
    DataSource dataScource = SpringContext.getBean("ds");
    result.setDataScource(dataScource);
    result.setOverlayImport(isOverlayImport);

    if (fileInfo.isExcel()) {
      String sheetName = ((XlsUploadFileContentInfo) fileContent).getSheetNames().get(0);
      result.setSheetName(sheetName);
    }

    if (schemeType == 3) {
      if (validateResult != null && validateResult.getTestPaper() != null) {
        result.setTestPaperId(validateResult.getTestPaper().getId());
      }
      result.setWebFieldRelation(
          createCjWebFieldRelations(result, fileContent.getHead(), schemeType));
    } else {
      result.setWebFieldRelation(createDefaultWebFieldRelations(fileContent.getHead(), schemeType));
    }
    return result;
  }

  private List<WebFieldRelation> createCjWebFieldRelations(WebRetrieveResult wrr, List<String> head,
      int schemeType) {
    OmrStrAndScoreStr omrStrAndScoreStr = ImportUtil.getOrmStrAndScoreStr(head);

    ArrayList<WebFieldRelation> webFieldRelations = new ArrayList<WebFieldRelation>();
    List<DataField> dataFields = dataFieldService.list(schemeType, wrr.getTestPaperId());
    int socreStrIdx = 1;
    int ormStrIdx = 1;
    for (DataField d : dataFields) {
      if (d.isSelItem()) {
        WebFieldRelation r =
            createSelItemWebFieldRelation(d, head, socreStrIdx, ormStrIdx, omrStrAndScoreStr);
        webFieldRelations.add(r);

        if (isEmpty(d)) {
          if (d.isSelOption()) {
            ormStrIdx += 1;
          } else {
            socreStrIdx += 1;
          }
        }

      } else {
        WebFieldRelation r = createWebFieldRelation(d, head);
        webFieldRelations.add(r);
      }
    }

    if (socreStrIdx > 1) {
      wrr.setScoreStr(omrStrAndScoreStr.getScoreStr());
    }

    if (ormStrIdx > 1) {
      wrr.setOmrStr(omrStrAndScoreStr.getOmrStr());
    }
    return webFieldRelations;
  }

  private WebFieldRelation createSelItemWebFieldRelation(DataField d, List<String> head,
      int socreStrIdx, int ormStrIdx, OmrStrAndScoreStr omrStrAndScoreStr) {
    WebFieldRelation r = null;
    if (isEmpty(d)) {
      if (d.isSelOption()) {
        r = new WebFieldRelation();
        r.setFrom(omrStrAndScoreStr.getOmrStr() + ormStrIdx);
        r.setTo(d.getFieldName());
      } else {
        r = new WebFieldRelation();
        r.setFrom(omrStrAndScoreStr.getScoreStr() + socreStrIdx);
        r.setTo(d.getFieldName());
      }
    } else {
      r = createWebFieldRelation(d, head);
    }

    return r;
  }

  private boolean isEmpty(DataField d) {
    String[] defNames = d.getDefaultName().split("\\|");
    return defNames[0].equals("");
  }

  private List<WebFieldRelation> createDefaultWebFieldRelations(List<String> head, int schemeType) {
    ArrayList<WebFieldRelation> webFieldRelations = new ArrayList<WebFieldRelation>();
    List<DataField> dataFields = dataFieldService.list(schemeType, 0L);
    for (DataField d : dataFields) {
      WebFieldRelation r = createWebFieldRelation(d, head);
      webFieldRelations.add(r);
    }
    return webFieldRelations;
  }

  private WebFieldRelation createWebFieldRelation(DataField d, List<String> head) {
    String name = ImportUtil.getMapingFiledName(d, head);
    WebFieldRelation r = new WebFieldRelation();
    r.setFrom(name);
    r.setTo(d.getFieldName());
    return r;
  }
}
