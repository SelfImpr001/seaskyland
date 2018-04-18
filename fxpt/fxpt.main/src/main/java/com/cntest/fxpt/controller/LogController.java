/**
 * <p>
 * <b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b>
 * </p>
 * 
 **/

package com.cntest.fxpt.controller;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.common.query.DefaultQuery;
import com.cntest.common.query.Query;
import com.cntest.fxpt.domain.Log;
import com.cntest.fxpt.service.LogService;
import com.cntest.fxpt.util.TitleUtil;
import com.cntest.web.view.ModelAndViewBuilder;


/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 黄国恒
 * @version 1.0
 **/

@Controller
@RequestMapping("/log")
public class LogController {

  private final static Logger logger = LoggerFactory.getLogger(LogController.class);

  @Autowired(required = false)
  private LogService surveylogservice;

  @RequestMapping(value = "/list/{currentPage}/{pageSize}", method = RequestMethod.GET)
  public ModelAndView list(@PathVariable int currentPage, @PathVariable int pageSize,
      HttpServletRequest request) throws Exception {
    logger.debug("URL: /log/list/{}/{}", currentPage, pageSize);
    String optionName = request.getParameter("optionName");
    String optionValue = request.getParameter("optionValue");
    String startDate = request.getParameter("startDate");
    String endDate = request.getParameter("endDate");
    String status = request.getParameter("status");

    Map<String, String> params = new HashMap<String, String>();
    params.put("optionValue", optionValue);
    params.put("status", status);
    if ("dateTime".equals(optionValue)) {
      if (startDate == null || "".equals(startDate))
        startDate = " ";
      if (endDate == null || "".equals(endDate))
        endDate = " ";
      params.put("optionName", startDate + "," + endDate);
    } else {
      params.put("optionName", optionName);
    }

    Query<Log> query =
        new DefaultQuery.Builder<Log>().pagesize(pageSize).curpage(currentPage).create();
    query.setParameters(request.getParameterMap());


    surveylogservice.surveyLogQuery(query, params);

    String title = TitleUtil.getTitle(request.getServletPath());
    return ModelAndViewBuilder.newInstanceFor("/log/list").append("query", query)
        .append("title", title).build();
  }

  @RequestMapping(value = "/view/{pk}", method = RequestMethod.GET)
  public ModelAndView view(@PathVariable Long pk, HttpServletRequest request) throws Exception {
    logger.debug("URL: /log/view/" + pk);
    Log log = surveylogservice.get(pk);
    return ModelAndViewBuilder.newInstanceFor("/log/view").append("log", log).build();
  }

  @RequestMapping(value = "/takeNotes/{examId}/{urlResourUUID}", method = RequestMethod.POST)
  public ModelAndView takeNotes(@PathVariable Long examId, @PathVariable Object urlResourName,
      HttpServletRequest request) throws Exception {
    logger.debug("URL: /takeNotes/{examId}/{urlResourUUID}---examId=" + examId);
    // 查看报告日志记录
    surveylogservice.addLogFromModule(examId, URLEncoder.encode(urlResourName.toString(), "UTF-8"),
        null);
    return ModelAndViewBuilder.newInstanceFor("").build();
  }

  @RequestMapping(value = "/takeNotes", method = RequestMethod.GET)
  public ModelAndView takeNotes1(HttpServletRequest request) throws Exception {
    Long examId = Long.parseLong(request.getParameter("examId"));
    String urlResourName = request.getParameter("uuid");
    logger.debug("URL: /takeNotes/{examId}/{urlResourUUID}---examId=" + examId);
    // 查看报告日志记录
    surveylogservice.addLogFromModule(examId, urlResourName, null);
    return ModelAndViewBuilder.newInstanceFor("").build();
  }
}
