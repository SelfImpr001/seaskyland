/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.controller;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.common.page.Page;
import com.cntest.fxpt.anlaysis.service.impl.ExamMgr;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.ExamStudent;
import com.cntest.fxpt.personalReport.PersonService;
import com.cntest.fxpt.service.IAanalysisService;
import com.cntest.fxpt.service.IExamService;
import com.cntest.fxpt.service.IExamStudentService;
import com.cntest.fxpt.service.impl.BachServiceImpl;
import com.cntest.fxpt.util.LogUtil;
import com.cntest.fxpt.util.TitleUtil;
import com.cntest.util.SpringContext;
import com.cntest.web.view.ModelAndViewBuilder;
import com.cntest.web.view.ProgressListener;
import com.cntest.web.view.ResponseStatus;
/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 陈勇2016年12月21日
 * @version 1.0
 **/
@Controller
@RequestMapping("/batch")
public  class BatchController{
	private static Logger logger = LoggerFactory
			.getLogger(BatchController.class);
	
	@Autowired(required = false)
	@Qualifier("IExamService")
	private  IExamService examService;
	
	@Autowired(required = false)
	private  PersonService personService;
	
	@Autowired(required = false)
	private  IExamStudentService examStudentService;
	
	@Autowired(required = false)
	@Qualifier("IBachService")
	private BachServiceImpl bachServiceImpl;
	
	@Autowired(required = false)
	@Qualifier("IAanalysisService")
	private IAanalysisService analysisService;
	
	
	@RequestMapping(value = "/list/{currentPage}/{pageSize}", method = RequestMethod.GET)
	public ModelAndView examList(@PathVariable int currentPage,@PathVariable int pageSize, HttpServletRequest request)
			throws Exception {
		logger.debug("URL /batch/list/{}/{}", currentPage, pageSize);
		Page<Exam> page = newPage(currentPage, pageSize, request);
		List<Exam> exams = examService.listBybach(page,5,11);
		String title = TitleUtil.getTitle(request.getServletPath());
		return ModelAndViewBuilder
					.newInstanceFor("/batch/list")
					.append("query", page).append("title",title).build();
	}
	
	@RequestMapping(value = "/batchExec/{examId}", method = RequestMethod.POST)
	public ModelAndView batchExec(@PathVariable  Long examId,HttpServletRequest request,HttpServletResponse response)
			throws Exception {
		logger.debug("URL /batch/batchExec/{}", examId);
		final Long examIds = examId;
		final String fileDir = request.getServletContext().getRealPath("/") + "student/report" + File.separator + examId;
		final String ip = LogUtil.getIP(request);
		Thread  tt = new  Thread() {
			public void run() {
				try {
					synchronized (this) {
						batchExecs(examIds,fileDir,ip);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		tt.setName("examId-"+examId);
		tt.start();
		//设置为正在生成报告
		examService.updateStatus(examId, 11);
		return ModelAndViewBuilder.newInstanceFor("")
					.append(ResponseStatus.NAME,
							new ResponseStatus.Builder(Boolean.TRUE).code("status")
							.msg("").build()).build();
	}
	
	public  void batchExecs(Long examId,String fileDir,String ip) throws Exception{
		String status = "失败",info = "生成报告<b style='color:red;'>",erre="",name=examId+"";
		List<ExamStudent> list = examStudentService.examByStudentForList(examId);
		int io = 0,oi=0,o = list.size();;
		PersonService p =  SpringContext.getBean("PersonService");
		if(list!=null) {
			deleteFile(fileDir);//删除以前的文件
			p.Islog = false;//批量不记录访问记录
			//personService.setIslog(false);//批量不记录访问记录
			try {
				//把状态置为正在生成报告
				examService.updateStatus(examId, 11);
				name = examService.findById(examId).getName();
				for (int i = 0; i < o ; i++) {
					try {
						p.exec(examId, list.get(i).getStudentId());
						io++;
					} catch (Exception e) {
						e.printStackTrace();
						oi++;
						erre+=e.getMessage();
					}
				}
				status = "成功";
			} catch (Exception e) {
				erre += LogUtil.e(e);
				throw e;
			}finally {
				examService.updateStatus(examId, 5);
				p.clearMap();
				//p.setIslog(true);
				//p.Islog = true;//设置回原来的样子,以免单个进来不能记录
				info+=name+"</b>"+status;
				info+=("</b>共"+o+"条数据,执行成功"+io+"条,失败"+oi+"条,异常"+((o-io)-oi));
				LogUtil.logSetIp("报告管理>生成报告", "生成",name,status, info,erre,ip);
				//设置为已发布(原有的状态)
			}
		}
	}
	
	@RequestMapping(value = "/batchExecNum/{examId}", method = RequestMethod.POST)
	public ModelAndView batchExecNum(@PathVariable Long examId,ServletRequest request)
			throws Exception {
		logger.debug("URL /batch/batchExec/{}", examId);
		
		List<ExamStudent> list = examStudentService.examByStudentForList(examId);
		String size = list.size()+"";
		String fileDir = request.getServletContext().getRealPath(File.separator) + "student"+File.separator+"report" + File.separator + examId;
		int io = getFileSize(fileDir);
		return ModelAndViewBuilder.newInstanceFor("")
					.append(ResponseStatus.NAME,
							new ResponseStatus.Builder(Boolean.TRUE).code("status")
							.msg(io+"").build()).append("size",size).build();
	}
	public int getFileSize(String fileDir){
		File file = new File(fileDir);
		int io = 0;
		if (file.exists()) {
			String[] fis = file.list();
			File tems = null;
			for (int i = 0; i < fis.length; i++) {
				if(fileDir.endsWith(file.separator)) {
					tems = new File(fileDir+fis[i]);
				}else {
					tems = new File(fileDir+File.separator+fis[i]);
				}
				if(tems.isFile()) {
					io++;
				}
			}
		}
		return io;
	}
	
	@RequestMapping(value = "/batchReset/{examId}", method = RequestMethod.POST)
	public ModelAndView batchReset(@PathVariable Long examId,ServletRequest request)
			throws Exception {
		logger.debug("URL /batch/batchExec/{}", examId);
		String status = "失败",info = "重置<b style='color:red;'>",erre="",name=examId+"";
		try {
			name = examService.findById(examId).getName();
			ServletContext servletContext = request.getServletContext();
			String fileDir = request.getServletContext().getRealPath(File.separator) + "student"+File.separator+"report" + File.separator + examId;
			deleteFile(fileDir);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info+=name+"</b>"+status;
			LogUtil.log("报告管理>生成报告", "重置",name,status, info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("")
					.append(ResponseStatus.NAME,
							new ResponseStatus.Builder(Boolean.TRUE).code("status")
							.msg("").build()).build();
	}
	@RequestMapping(value = "/c", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView c(HttpServletRequest request){
		String exmaId = request.getParameter("exmaid");
		Exam ex = examService.findById(Long.parseLong(exmaId));
		
		List<ExamStudent> list = examStudentService.examByStudentForList(Long.parseLong(exmaId));
		
		String size = list.size()+"";
		String fileDir = request.getServletContext().getRealPath(File.separator) + "student"+File.separator+"report" + File.separator + exmaId;
		int io = getFileSize(fileDir);
		
		int status = 0;
		if(ex!=null){
			status = ex.getStatus();
		}
		return ModelAndViewBuilder.newInstanceFor("").append("status",status).append("length",io+"/"+size).build();
	}
	
	@Deprecated
	protected <T> Page<T> newPage(int currentPage, int pageSize,
			ServletRequest request) {
		Page<T> page = new Page<T>().setCurpage(currentPage).setPagesize(pageSize);
		if (request != null) {
			Map<String, String[]> tmpMap = request.getParameterMap();
			for (String key : tmpMap.keySet()) {
				String[] value = tmpMap.get(key);
				if (value == null) {
					value = new String[] { "" };
				}
				page.addParameter(key, value[0]);
			}
		}
		return page;
	}
	
	protected static void deleteFile(String fileDir) {
		File file = new File(fileDir);
		if (file.exists()) {
			String[] fis = file.list();
			File tems = null;
			for (int i = 0; i < fis.length; i++) {
				if(fileDir.endsWith(file.separator)) {
					tems = new File(fileDir+fis[i]);
				}else {
					tems = new File(fileDir+File.separator+fis[i]);
				}
				if(tems.isFile()) {
					tems.delete();
				}
			}
		}
	}
}

