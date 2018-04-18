/*
 * @(#)com.cntest.fxpt.controller.UploadFileController.java 1.0 2014年10月15日:上午9:42:41
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.common.controller.BaseController;
import com.cntest.fxpt.bean.DataField;
import com.cntest.fxpt.bean.UploadFileContentInfo;
import com.cntest.fxpt.bean.UploadFileInfo;
import com.cntest.fxpt.bean.UploadFileValidateResult;
import com.cntest.fxpt.domain.TestPaper;
import com.cntest.fxpt.etl.domain.EtlProcessResult;
import com.cntest.fxpt.service.ITestPaperService;
import com.cntest.fxpt.service.etl.IDataFieldService;
import com.cntest.fxpt.util.Json;
import com.cntest.fxpt.util.UploadHelper;
import com.cntest.web.view.ModelAndViewBuilder;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月15日 上午9:42:41
 * @version 1.0
 */
@Controller
@RequestMapping("/upload")
public class UploadFileController extends BaseController {
  private static final Logger log = LoggerFactory.getLogger(UploadFileController.class);

  @Autowired(required = false)
  @Qualifier("etl.IDataFieldService")
  private IDataFieldService dataFieldService;

  @Autowired(required = false)
  @Qualifier("ITestPaperService")
  private ITestPaperService testPaperService;

  @RequestMapping(value = "/ui/{schemeType}/s", method = RequestMethod.GET)
  public ModelAndView ui(@PathVariable int schemeType, @RequestParam Long testPaperId,
      HttpServletRequest request, HttpServletResponse response) throws Exception {

    log.debug("execute create sigle uplod ui....");
    List<DataField> dataFields = dataFieldService.list(schemeType, true);
    log.debug("execute create sigle uplod ui success");
    return ModelAndViewBuilder.newInstanceFor("/etl/sigleUploadFileUI")
        .append("schemeTypePut", schemeType).append("dataFields", dataFields).build();
  }

  @RequestMapping(value = "/ui/{schemeType}/ip", method = RequestMethod.GET)
  public ModelAndView importOrg(@PathVariable int schemeType, HttpServletRequest request,
      @RequestParam Long testPaperId, HttpServletResponse response) throws Exception {
    log.debug("execute create sigle uplod ui....");
    List<DataField> dataFields = dataFieldService.list(schemeType, testPaperId);
    // List<DataField> dataFields = dataFieldService.getOrgDate();
    return ModelAndViewBuilder.newInstanceFor("/etl/orgUploadFileUI")
        .append("schemeTypePut", schemeType).append("dataFields", dataFields).build();
  }

  @RequestMapping(value = "/ui/{schemeType}/m", method = RequestMethod.GET)
  public ModelAndView ui(@PathVariable int schemeType, HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    log.debug("execute create multi uplod ui....");
    String sessionId = request.getSession().getId();
    log.debug("execute create multi uplod ui success");
    return ModelAndViewBuilder.newInstanceFor("/etl/multiUploadFileUI")
        .append("sessionId", sessionId).append("sessionName", "fxptsid").build();
  }

  @RequestMapping(value = "/{isValidataHead}/{examId}/{schemeType}", method = RequestMethod.POST)
  public ModelAndView upload(@PathVariable boolean isValidataHead, @PathVariable Long examId,
      @PathVariable int schemeType, @RequestParam MultipartFile file, HttpServletRequest request,
      HttpServletResponse response) throws Exception {

    UploadFileInfo fileInfo = UploadHelper.createUploadFileInfo(file);
    UploadFileContentInfo fileContent = UploadHelper.getFileContent(schemeType, fileInfo, null);

    UploadFileValidateResult validateResult =
        UploadHelper.getValidateResult(schemeType, fileContent, examId, isValidataHead);

    // 验证系统设置字段在导入的excel中是否都有
    List<String> listaHead = fileContent.getHead();// 获取头部信息
    // 验证必须导入字段是否在excel中
    String msg = dataFieldService.initDataHead(listaHead, schemeType);
    List<String> message = new ArrayList<String>();
    message.add(msg);
    if (msg != "") {
      validateResult.setMessages(message);
    }
    HashMap<String, Object> result = new HashMap<String, Object>();
    EtlProcessResult etlResult = new EtlProcessResult();
    // 成绩重复性验证
    if (schemeType == 3 && !validateResult.isHasError()) {
      String name = fileInfo.getNameNoSuffix();
      // 根据名称查询testPaperId
      TestPaper testpaper = testPaperService.get(examId, name);
      if (testpaper != null && testpaper.isHasCj()) {
        Map<String, Object> fileInfoMap = new HashMap<>();
        String suffix = fileInfo.getSuffix().toString();
        String desFileName = fileInfo.getDesFileName();

        fileInfoMap.put("examId", examId);
        fileInfoMap.put("fileName", fileInfo.getDesFileName());
        fileInfoMap.put("desFileName", desFileName);
        fileInfoMap.put("suffix", suffix);
        fileInfoMap.put("schemeType", 3);
        fileInfoMap.put("overlayImport", false);
        fileInfoMap.put("importType", 1);
        fileInfoMap.put("fileRealName", fileInfo.getName());
        //
        // boolean isOverlayImport =
        // Boolean.parseBoolean(fileInfoMap.get("overlayImport").toString());
        // int importType =0;
        // if(fileInfoMap.get("importType")!=null) {
        // importType= Integer.parseInt(fileInfoMap.get("importType")
        // .toString());
        // }
        // String suffix =fileInfo.getSuffix().toString();
        // String desFileName = fileInfo.getDesFileName();
        // boolean isOverlayImport =
        // true;//Boolean.parseBoolean(fileInfoMap.get("overlayImport").toString());
        // String filePath = UploadHelper.getDir() + desFileName;
        // WebRetrieveResult wrr = new EtlProcessController().creawteWebRetrieveResult(examId,
        // desFileName, suffix, schemeType, isOverlayImport,fileContent, validateResult, fileInfo);
        // //wrr.setFileRealName(fileInfoMap.get("fileRealName").toString());
        // wrr.setImportType(1);
        // wrr.setValidate(false);
        // //获取项目目录的上级目录
        // String fileParentPath=getParentFilePath(request);
        // wrr.setFileParentDir(fileParentPath);
        // wrr.setTableName("kn_examtestpapercj_tmp");
        // IEtlExecuteor executeor = SpringContext.getBean("CJ.IEtlExecuteor");
        // etlResult = new EtlProcessController().validate(fileInfoMap, request);
        // msg=etlResult.getMessage();
        List<String> mm = new ArrayList<>();
        msg = "已经导入该成绩，是否覆盖？";
        mm.add(msg);
        validateResult.setMessages(mm);
      }
    }
    result.put("fileInfo", fileInfo);
    result.put("fileContent", fileContent);
    result.put("validateResult", validateResult);
    result.put("msg", msg);
    String json = Json.toJson(result);
    return ModelAndViewBuilder.newInstanceFor("/etl/dataJSON").append("json", json).build();
  }

  private String getParentFilePath(HttpServletRequest request) {
    String filePath = request.getSession().getServletContext().getRealPath("");
    int last = filePath.lastIndexOf("/");
    if (last == -1) {
      last = filePath.lastIndexOf("\\");
    }
    return filePath = filePath.substring(0, last);
  }


  @RequestMapping("/changeSheet")
  public ModelAndView changeSheet(@RequestBody Map<String, Object> data) throws Exception {
    String suffix = data.get("suffix").toString();
    String fileName = data.get("fileName").toString();
    String desFileName = data.get("desFileName").toString();
    String sheetName = data.get("sheetName").toString();
    int schemeType = Integer.parseInt(data.get("schemeType").toString());
    Long examId = Long.parseLong(data.get("examId").toString());
    boolean isValidataHead = Boolean.parseBoolean(data.get("isValidataHead").toString());

    String filePath = UploadHelper.getDir() + desFileName;
    UploadFileInfo fileInfo = new UploadFileInfo().setPath(filePath).setSuffix(suffix)
        .setDesFileName(desFileName).setName(fileName);
    UploadFileContentInfo fileContent =
        UploadHelper.getFileContent(schemeType, fileInfo, sheetName);
    UploadFileValidateResult validateResult =
        UploadHelper.getValidateResult(schemeType, fileContent, examId, isValidataHead);

    return ModelAndViewBuilder.newInstanceFor("/etl/dataJSON")
        .append("validateResult", validateResult).append("fileContent", fileContent).build();
  }

}
