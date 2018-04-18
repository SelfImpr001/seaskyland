package com.cntest.birt.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.birt.core.exception.BirtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.birt.domain.OrgBirt;
import com.cntest.birt.domain.ReportScript;
import com.cntest.birt.service.orgBirtService;
import com.cntest.birt.service.imp.ReportScriptServiceImpl;
import com.cntest.common.controller.BaseController;
import com.cntest.common.query.Query;
import com.cntest.exception.BusinessException;
import com.cntest.fxpt.bean.UploadFileInfo;
import com.cntest.fxpt.util.Json;
import com.cntest.fxpt.util.TitleUtil;
import com.cntest.fxpt.util.UploadHelper;
import com.cntest.web.view.ModelAndViewBuilder;
import com.cntest.web.view.ResponseStatus;

@Controller
@RequestMapping("/birt")
public class ReportScriptController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(ReportScriptController.class);

	@Autowired
	private ReportScriptServiceImpl reportScriptService;
	@Autowired
	private orgBirtService orgBirtService;
	
	@RequestMapping(value = "/list/{currentPage}/{pageSize}", method = RequestMethod.GET)
 
	public ModelAndView findAll(@PathVariable int currentPage, @PathVariable int pageSize, HttpServletRequest request) throws BusinessException {
		Query<ReportScript> query = newQuery(currentPage, pageSize, request);
		List<OrgBirt> orgList = orgBirtService.getTopOrgList(null);
		reportScriptService.query(query,orgList);
		String title = TitleUtil.getTitle(request.getServletPath());
		return ModelAndViewBuilder.newInstanceFor("/birt/list").append("title",title).append("query", query).append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("4a.userBelong.list").msg("脚本").build()).build();
	}
	@RequestMapping(value = "/merge/list/{currentPage}/{pageSize}", method = RequestMethod.GET)
	public ModelAndView findAlls(@PathVariable int currentPage, @PathVariable int pageSize, ServletRequest request) throws BusinessException {
		Query<ReportScript> query = newQuery(currentPage, pageSize, request);
		List<OrgBirt> orgList = orgBirtService.getTopOrgList(null);
		reportScriptService.queryMerge(query);
		return ModelAndViewBuilder.newInstanceFor("/birtmerge/list").append("query", query).append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("4a.userBelong.list").msg("脚本").build()).build();
	}
	/**
	 * 增加脚本
	 * 
	 * @param reportScript
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView add(@RequestBody ReportScript reportScript) throws BusinessException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		reportScript.setCreatedTime(new Date());
		reportScriptService.save(reportScript);
		return ModelAndViewBuilder.newInstanceFor("permission/dataList")
				.append(ResponseStatus.NAME, new ResponseStatus.Builder(Boolean.TRUE).code("4a.permission.dataList")
						.msg("4a.permission.data.list").build())
				.build();
	}

	/**
	 * 合并脚本页面
	 * 
	 * @param reportScript
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/mergeReport", method = RequestMethod.GET)
	public ModelAndView merge() throws BusinessException {
		List<ReportScript> reports = reportScriptService.list();
		return ModelAndViewBuilder.newInstanceFor("birt/mergeReport").append("reports", reports)
				.append(ResponseStatus.NAME, new ResponseStatus.Builder(Boolean.TRUE).code("4a.permission.dataList")
						.msg("4a.permission.data.list").build())
				.build();
	}

	/**
	 * 合并脚本页面
	 * 
	 * @param reportScript
	 * @return
	 * @throws BusinessException
	 * @throws IOException
	 * @throws BirtException
	 */
	@RequestMapping(value = "/mergeReports", method = RequestMethod.POST)
	public ModelAndView mergeReports(@RequestBody ReportScript reportScript)
			throws BusinessException, BirtException, IOException {
		reportScriptService.mutiRoport(reportScript);
		return ModelAndViewBuilder.newInstanceFor("birt/mergeReport")
				.append(ResponseStatus.NAME, new ResponseStatus.Builder(Boolean.TRUE).code("4a.permission.dataList")
						.msg("4a.permission.data.list").build())
				.build();
	}

	/**
	 * 生成wrod
	 * 
	 * @param reportScript
	 * @return
	 * @throws BusinessException
	 * @throws BirtException
	 */
	@RequestMapping(value = "/generateWord", method = RequestMethod.PUT)
	public ModelAndView generateWord(@RequestBody ReportScript reportScript) throws BusinessException, BirtException {

		reportScriptService.update(reportScriptService.mutiRoportWord(reportScript));
		return ModelAndViewBuilder.newInstanceFor("birt/mergeReport")
				.append(ResponseStatus.NAME, new ResponseStatus.Builder(Boolean.TRUE).code("4a.permission.dataList")
						.msg("4a.permission.data.list").build())
				.build();
	}

	/**
	 * 生成wrod页面
	 * 
	 * @param reportScript
	 * @return
	 * @throws BusinessException
	 * @throws BirtException
	 */
	@RequestMapping(value = "/generateWords", method = RequestMethod.GET)
	public ModelAndView generateWords() throws BusinessException, BirtException {
		return ModelAndViewBuilder.newInstanceFor("birt/generateWord")
				.append(ResponseStatus.NAME, new ResponseStatus.Builder(Boolean.TRUE).code("4a.permission.dataList")
						.msg("4a.permission.data.list").build())
				.build();
	}

	/**
	 * 下载wrod
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/diwnWord/{pk}")
	public void dowloadLog(HttpServletRequest request, HttpServletResponse response, @PathVariable Long pk)
			throws Exception {
		ReportScript re = reportScriptService.load(pk);
		String fileName = re.getWordName();
		response.setContentType("application/octet-stream;charset=UTF-8"); // 下载文件类型
		response.setHeader("Content-Disposition",
				"attachment; filename=" + new String(fileName.getBytes(), "ISO-8859-1")); // 下载文件命名

		ServletOutputStream out = response.getOutputStream();
		FileInputStream in = new FileInputStream(re.getWordDocment());
		byte[] bs = new byte[1024];
		int len = 0;
		while ((len = in.read(bs)) != -1) {
			out.write(bs, 0, len);
		}
		in.close();
		out.flush();
	}

	/**
	 * 修改用户
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public ModelAndView update(@RequestBody ReportScript reportScript) throws Exception {
		logger.debug("URL: /user  method PUT  update {}", reportScript);
		reportScriptService.update(reportScript);

		return ModelAndViewBuilder.newInstanceFor("permission/dataList")

		.append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.update").msg("脚本修改").build()).build();
	}

	/**
	 * 删除脚本
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public ModelAndView delete(@RequestBody ReportScript reportScript) throws Exception {
		logger.debug("URL: /birt  method Delete  ");
		reportScriptService.remove(reportScript);
		;
		return ModelAndViewBuilder.newInstanceFor("/birt/list")
				// .append("hasUrlResource", hasUrlResource)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.delete").msg("脚本删除成功").build())
				.build();
	}

	/**
	 * 增加用户
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/view/{pk}", method = RequestMethod.GET)
	public ModelAndView view(@PathVariable Long pk, HttpServletRequest request) throws Exception {
		logger.debug("URL: /user  method POST create {}", pk);
		ReportScript reportScript = new ReportScript();
		if (pk > 0) {
			reportScript = reportScriptService.load(pk);
		}
		return ModelAndViewBuilder.newInstanceFor("/birt/view")
				.append("report", reportScript)
				.append("org",request.getParameter("org_id") )
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.view").msg("脚本修改").build())
				.build();
	}

	@RequestMapping(value = "/upload/", method = RequestMethod.POST)
	public ModelAndView upload(@RequestParam MultipartFile file, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UploadFileInfo fileInfo = UploadHelper.createUploadReport(file);
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("fileInfo", fileInfo);
		result.put("status", true);
		String json = Json.toJson(result);
		return ModelAndViewBuilder.newInstanceFor("/etl/dataJSON").append("json", json).build();
	}

}
