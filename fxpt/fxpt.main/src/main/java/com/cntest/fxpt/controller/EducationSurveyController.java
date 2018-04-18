package com.cntest.fxpt.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.cntest.common.controller.BaseController;
import com.cntest.common.page.Page;
import com.cntest.common.query.Query;
import com.cntest.foura.domain.User;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.bean.UploadFileInfo;
import com.cntest.fxpt.domain.EducationMonitor;
import com.cntest.fxpt.domain.EducationSurvey;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.FSDataNnalysis;
import com.cntest.fxpt.service.EducationMonitorService;
import com.cntest.fxpt.service.EducationSurveyService;
import com.cntest.fxpt.service.IExamService;
import com.cntest.fxpt.util.TitleUtil;
import com.cntest.fxpt.util.UploadHelper;
import com.cntest.util.ExceptionHelper;
import com.cntest.util.ZTree;
import com.cntest.web.view.ModelAndViewBuilder;
import com.cntest.web.view.ResponseStatus;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * 教育监测
 * @author Administrator
 *
 */

@Controller
@RequestMapping("/academicSupervise")
public class EducationSurveyController extends BaseController {
	private static Logger logger = LoggerFactory
			.getLogger(EducationSurveyController.class);

	@Autowired
	private UserService userService;
	
	
	@Autowired
	private EducationMonitorService educationMonitorService;

	@Autowired
	private EducationSurveyService educationSurveyService;
	
	@Autowired
	private IExamService examService;
   

	@RequestMapping(value = "/indexset", method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest request)
			throws Exception {
		logger.info("list {}",EducationSurvey.class);
		User user = User.from(userService.getCurrentLoginedUser());
		List<EducationSurvey> eduList = educationSurveyService.getTopEduList(null);
		
		String title = TitleUtil.getTitle(request.getServletPath());
		return ModelAndViewBuilder
				.newInstanceFor("educationSurvey/list")
				.append("orgList", eduList)
				.append("title",title)
				.build();
		
	}
	
	@RequestMapping(value="/view/{parentPk}/{pk}",method = RequestMethod.GET)
	public ModelAndView view(@PathVariable Long parentPk,@PathVariable Long pk, HttpServletRequest request) throws Exception{

		logger.info("view {} pk = {}",EducationSurvey.class,pk);
		EducationSurvey edu = new EducationSurvey();
		EducationSurvey parent = null;
		boolean hasDataRef=false;
		boolean isAvailable= false;
		//-1 为新增
		if(pk==-1){
			hasDataRef=true;
			if(parentPk!=-1) {
				parent=educationSurveyService.load(parentPk);
			}
		}else {
			edu=educationSurveyService.getEduByPk(pk);
			parent = edu.getParent();
			if(edu.getAvailable()) {
				isAvailable=true;
			}
		}
		return ModelAndViewBuilder
				.newInstanceFor("educationSurvey/edit")
				.append("org",edu)
				.append("parent",parent)
				.append("hasDataRef", hasDataRef)
				.append("isAvailable", isAvailable)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.EducationSurvey.view")
						.msg("").build()).build();
		
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView add(@RequestBody EducationSurvey edu) throws Exception{
		logger.info("add {}",EducationSurvey.class);
		if(edu.getParent()!=null) {
			EducationSurvey parent =educationSurveyService.getEduByPk(edu.getParent().getPk());	
			edu.setParent(parent);
			edu.setType(parent.getType()+1);
		}else {
			edu.setType(1);
		}
		//新增
		educationSurveyService.create(edu);
		List<EducationSurvey> eduList = new ArrayList<EducationSurvey>();
		if(edu.getParent()!=null) {
			eduList=educationSurveyService.getEduSubList(edu.getParent().getPk());
		}else {
			eduList=educationSurveyService.getTopEduList(null);
		}
		return ModelAndViewBuilder
				.newInstanceFor("educationSurvey/subList")
				.append("orgList", eduList)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.EducationSurvey.add")
						.msg("保存成功,PK：" + edu.getPk()).build()).build();
		
	}
	
	@RequestMapping(method = RequestMethod.PUT)
	public ModelAndView update(@RequestBody EducationSurvey org) throws Exception{
		logger.info("update {}",EducationSurvey.class);
		
		if(org.getParent() == null || org.getParent().getPk() == null)
			org.setParent(null);
		else{
			EducationSurvey temParent = educationSurveyService.getEduByPk(org.getParent().getPk());
			org.setParent(temParent);
		}
		educationSurveyService.update(org);
		
		List<EducationSurvey> orgList = new ArrayList<EducationSurvey>();
		if(org.getParent() != null)
			 orgList = educationSurveyService.getEduSubList(org.getParent().getPk());
		else 
			orgList = educationSurveyService.getTopEduList(null);
		
		return ModelAndViewBuilder
				.newInstanceFor("educationSurvey/subList")
				.append("orgList", orgList)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.EducationSurvey.update")
						.msg("修改成功").build()).build();
		
	}
	
	@RequestMapping(method = RequestMethod.DELETE)
	public ModelAndView delete(@RequestBody EducationSurvey org) throws Exception{
		logger.info(" pk = {}  delete {}",org.getPk(),EducationSurvey.class);
		org = educationSurveyService.load(org.getPk());
		educationSurveyService.remove(org);
		return ModelAndViewBuilder
				.newInstanceFor("/educationSurvey/subList")
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.EducationSurvey.delete")
						.msg("指标"+org.getName()+"删除成功").build()).build();
		
	}
	
	@RequestMapping(value="/tree/list/{level}",method = RequestMethod.GET)
	public ModelAndView treeNodeList(@PathVariable int level,HttpServletRequest request,HttpServletResponse response) throws Exception{;
		logger.info("treeNodeList {} 异步请求树节点数据");
        logger.info("treeNodeList {} select * From");

        List<EducationSurvey> orgList = educationSurveyService.getTopEduList(null);       
        JsonArray jsonArray  = buildTreeData(orgList,level,0);        
        logger.info("treeNodeList {}",jsonArray.toString());
		return ModelAndViewBuilder
				.newInstanceFor("")
				.append("orgJson",jsonArray.toString())
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.EducationSurvey.tree.list")
						.msg("success").build()).build();
	}
	
	@RequestMapping(value="/orExist",method = RequestMethod.POST)
	public ModelAndView orExist(HttpServletRequest request,HttpServletResponse response) throws Exception{;
		int num = educationSurveyService.findOrgList();
		return ModelAndViewBuilder
				.newInstanceFor("")
				.append("num",num)
				.build();
	}
	
	@RequestMapping(value="/tree/children",method = RequestMethod.GET)
	public ModelAndView childNodelist(HttpServletRequest request,HttpServletResponse response) throws Exception{;
		logger.info("childNodelist {} 异步请求树节点数据");
		logger.info("childNodelist {} select * From");
		String pk = request.getParameter("pk");
		
		List<EducationSurvey> orgList = educationSurveyService.getEduSubList(Long.valueOf(pk));
		JsonArray jsonArray  = buildTreeData(orgList,0,0);
		logger.info("childNodelist {}",jsonArray.toString());

		return ModelAndViewBuilder
				.newInstanceFor("")
				.append("orgJson",jsonArray.toString())
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.EducationSurvey.tree.children")
						.msg("success").build()).build();
	}
	
	@RequestMapping(value="/subList",method = RequestMethod.GET)
	public ModelAndView forChildMenu(HttpServletRequest request) throws Exception{;
		logger.info("forChildMenu {} 异步请求子列表数据");
		logger.info("forChildMenu {} select * From");
		 User user = User.from(userService.getCurrentLoginedUser());
		String pk 		= request.getParameter("pk");
		String isParent = request.getParameter("isParent");
		String qname = request.getParameter("qname");
		List<EducationSurvey> orgList = new ArrayList<EducationSurvey>();
		if(pk.equals("-1")) {
			orgList = educationSurveyService.getTopEduList(qname);
		}else {
			if(isParent.equals("true")) {
				orgList = educationSurveyService.getEduSubList(Long.valueOf(pk),qname);
			}else {
				orgList.add(educationSurveyService.getEduByPk(Long.valueOf(pk)));
			}
		}
		
		return ModelAndViewBuilder
				.newInstanceFor("educationSurvey/subList")
				.append("orgList", orgList)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.EducationSurvey.forChildMenu")
						.msg(user.getName()).build()).build();
	}
	
	@RequestMapping(value="/subList/{currentPage}/{pageSize}",method = RequestMethod.GET)
	public ModelAndView forChildMenu(@PathVariable int currentPage,@PathVariable int pageSize,HttpServletRequest request) throws Exception{;
		logger.info("forChildMenu {} 异步请求子列表数据");
		logger.info("forChildMenu {} select * From");
		String pk 		= request.getParameter("pk");

		Query<EducationSurvey> query  =newQuery(currentPage, pageSize, request);
		if(pk.equals("-1")) {
			query.setResults(educationSurveyService.getTopEduList(null));
		}else {			
			educationSurveyService.getEduSubList(Long.valueOf(pk),query);			
		}
		
		return ModelAndViewBuilder
				.newInstanceFor("educationSurvey/subList")
				.append("orgList", query.getResults())
				.append("query",query)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.EducationSurvey.forChildMenu")
						.msg("学校列表").build()).build();
	}
	
	private JsonArray buildTreeData(List<EducationSurvey> orgList, int level, int i) {
		JsonArray jsonArray = new JsonArray();
        for (EducationSurvey org : orgList) {
        	JsonObject object = new JsonObject();
        	logger.debug("PK:{}",org.getPk());
        	ArrayList<EducationSurvey> children = new ArrayList<EducationSurvey>();
        	
        	children.addAll(educationSurveyService.getEduChildrenNoLeaf(org.getPk()));
        	
        	object = new ZTree().id(org.getPk())
        							   .pId(org.getParent()!=null?org.getParent().getPk():-1L)
        							   .open( i < level).isParent(children.size() > 0).expandDIY("type", org.getType())
        							   .zTreeJsonObj(org);
        	object.remove("url");
        	if(i < level) {
        		jsonArray.addAll(buildTreeData(children,level,i + 1));
        	}
        	jsonArray.add(object);
		}
		return jsonArray;
	}

	//监测管理
	@RequestMapping(value="/monitorlist/{currentPage}/{pageSize}",method = RequestMethod.GET)
	public ModelAndView monitorList(@PathVariable int currentPage,@PathVariable int pageSize,HttpServletRequest request,HttpServletResponse response) throws Exception{;
		logger.info("monitorList {} 监测管理");
		Query<EducationMonitor> query  =newQuery(currentPage, pageSize, request);
			educationMonitorService.Querylist(query);
		return ModelAndViewBuilder
				.newInstanceFor("educationSurvey/monitorlist")
				.append("query",query)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.EducationSurvey.tree.list")
						.msg("success").build()).build();
	}
	
	//监测管理
	@RequestMapping(value="/newAdd",method = RequestMethod.GET)
	public ModelAndView newAdd(HttpServletRequest request) throws Exception{
		logger.info("monitorList {} 监测管理");
		User user = User.from(userService.getCurrentLoginedUser());
		EducationMonitor  m =  new EducationMonitor();
		m.setCreateUser(user.getName());
		return ModelAndViewBuilder
				.newInstanceFor("educationSurvey/newAdd")
				.append("m",m)
				.build();
	}
	//监测管理
	@RequestMapping(value="/view/{pk}",method = RequestMethod.GET)
	public ModelAndView view(@PathVariable Long pk ,HttpServletRequest request) throws Exception{
			logger.info("monitorList {} 监测管理");
			EducationMonitor  m = educationSurveyService.get(pk);
			User user = User.from(userService.getCurrentLoginedUser());
			m.setCreateUser(user.getName());
			return ModelAndViewBuilder
					.newInstanceFor("educationSurvey/newAdd")
					.append("m",m)
					.build();
		}
	
	//监测管理
	@RequestMapping(value="/addMonitor",method = RequestMethod.POST)
	public ModelAndView addMonitor(@RequestBody EducationMonitor mon ) throws Exception{;
		logger.info("monitorList {} 监测管理");
		User user = User.from(userService.getCurrentLoginedUser());
		mon.setCreateUser(user.getName());
		educationSurveyService.savem(mon);
		return ModelAndViewBuilder
				.newInstanceFor("educationSurvey/monitorlist/1/15").build();
	}
	
	//监测管理删除
	@RequestMapping(value="/monitorDelete/{pk}",method = RequestMethod.DELETE)
	public ModelAndView monitorDelete(@PathVariable Long pk) throws Exception{
		logger.info(" pk = {}  monitorDelete {}",pk,EducationMonitor.class);
		educationSurveyService.deletem(pk);
		return ModelAndViewBuilder
				.newInstanceFor("academicSupervise/monitorlist/1/15")
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE)
						.msg("删除成功").build()).build();
		
	}
	//监测管理
	@RequestMapping(value="/updateMonitor",method = RequestMethod.POST)
	public ModelAndView updateMonitor(@RequestBody EducationMonitor mon ) throws Exception{;
		logger.info("monitorList {} 监测管理");
		User user = User.from(userService.getCurrentLoginedUser());
		mon.setCreateUser(user.getName());
		educationSurveyService.updatem(mon);
		return ModelAndViewBuilder
				.newInstanceFor("educationSurvey/monitorlist/1/15").build();
	}
		
	/*************************************（我是分割线-开始分割) by陈颖 ************************************/
	@RequestMapping(value = "/reportgeneratelist/{currentPage}/{pageSize}", method = RequestMethod.GET)
	public ModelAndView reportgeneratelist(@PathVariable int currentPage,
			@PathVariable int pageSize,ServletRequest request)
			throws Exception {
		logger.info("list {}",EducationSurvey.class);
		Page<FSDataNnalysis> page= newPage(currentPage, pageSize, request);
		User user = User.from(userService.getCurrentLoginedUser());
		
		boolean isEmpty = page.getParameter().isEmpty();
		List<FSDataNnalysis> reportList = educationSurveyService.reportgeneratelist(page);
		if (isEmpty) {
			return ModelAndViewBuilder.newInstanceFor("/educationSurvey/reportgeneratelist")
					.append("page", page).build();
		} else {
			return ModelAndViewBuilder.newInstanceFor("/educationSurvey/reportgenerateBody")
					.append("page", page).build();
		}
	}
	
	/**
	 * 新增考试数据上传之前
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/viewReport",method = RequestMethod.GET)
	public ModelAndView viewReport() throws Exception{

		logger.info("viewReport");
        List<Exam> list=examService.getExamAllList();
		return ModelAndViewBuilder
				.newInstanceFor("educationSurvey/reportedit")
				.append("list", list)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.EducationSurvey.view")
						.msg("").build()).build();
		
	}
	
	/**
	 * 新增考试数据上传
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/addReportData",method = RequestMethod.POST)
	public ModelAndView addReportData(@RequestBody FSDataNnalysis fsdn,HttpServletRequest request) throws Exception{

		logger.info("viewReport");
//		String serverPath = request.getSession().getServletContext().getRealPath("uploadFile")+File.separator;
//		File fi = new File(serverPath);
//		if(!fi.exists()){
//			fi.mkdir();
//		}
//		String monitorHref=serverPath+fsdn.getMonitorHref();
		String monitorHref=fsdn.getMonitorHref();
		Exam exam=examService.findById(Long.valueOf(fsdn.getMonitorName()));
		fsdn.setCreateTime(new Date());
		fsdn.setMonitorDate(exam.getCreateDate());
		fsdn.setMonitorName(exam.getName());
		fsdn.setMonitorHref(monitorHref);
		fsdn.setMonitorSchoolYear(exam.getSchoolYearName());
		if(exam.getSchoolTerm()==1){
			fsdn.setMonitorSemester("上学期");
		}else{
			fsdn.setMonitorSemester("下学期");
		}
		fsdn.setMonitorType(exam.getExamType().getName());
		educationSurveyService.saveReportData(fsdn); //添加数据上传
        return ModelAndViewBuilder
				.newInstanceFor("academicSupervise/reportgeneratelist/1/15").build();
		
	}
	
	//使用MD5加密上传文件路径
	public static String getMd5ByFile(File file){
		String value=null;
		FileInputStream in=null;
		try{
			in=new FileInputStream(file);
		}catch(FileNotFoundException el){
			el.printStackTrace();
		}
		
		try{
			MappedByteBuffer bytebuffer=in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			MessageDigest md5=MessageDigest.getInstance("MD5");
			md5.update(bytebuffer);
			BigInteger bi=new BigInteger(1, md5.digest());
			value=bi.toString(16);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null!=in){
				try{
					in.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		return value;
	}
	
	/**
	 * 批量导入检查
	 * @param file
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/importAll", method = RequestMethod.POST)
	public ModelAndView uploadCheck(@RequestParam MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String serverPath = request.getSession().getServletContext().getRealPath("uploadFile")+File.separator;;
		File fi = new File(serverPath);
		if(!fi.exists()){
			fi.mkdir();
		}
		UploadFileInfo fileInfo = UploadHelper.createUploadFileInfo(file,serverPath);
		return ModelAndViewBuilder.newInstanceFor("/educationSurvey/fileNameData").append("fileName",fileInfo.getDesFileName()).build();
	}
	
	@RequestMapping(value = "/erreExpor/{pk}", method = RequestMethod.GET)
	public void erreExpor(@PathVariable Long pk,HttpServletRequest request, HttpServletResponse response) throws Exception {
 		logger.debug("URL: /academicSupervise/erreExpor");
 		FSDataNnalysis fsdn = educationSurveyService.getFSDataNnalysisById(pk);
 		String name = fsdn.getMonitorHref();
 		//获取路径
 		String serverPath = request.getSession().getServletContext().getRealPath("uploadFile"+File.separator);
 		checkDown(response, serverPath, name.split("[.]")[0]);
 	}
	
	/**
	 * 下载
	 * @param response
	 * @param urlFile
	 * @param fileName
	 */
	public static boolean checkDown(HttpServletResponse response, String urlFile, String fileName) {
		boolean isSuccess = true;
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		response.setContentType("text/plain; charset=utf-8");
		try {
			response.setHeader("Content-Disposition", 
					"attachment; filename="+ URLEncoder.encode(fileName.concat(".zip"),"UTF-8"));
			in = new BufferedInputStream(new FileInputStream(urlFile+"\\"+fileName.concat(".zip")));
			out = new BufferedOutputStream(response.getOutputStream());
			byte buff[] = new byte[2048];
			int len = 0;
			while(-1 != (len = (in.read(buff, 0, buff.length)))) {
				out.write(buff, 0 ,buff.length);
			}
			isSuccess = true;
		} catch (Exception e) {
			isSuccess = false;
			e.printStackTrace();
			ExceptionHelper.trace2String(e);
		}finally {
			try {
				if(in != null)
					in.close();
				if(out != null)
					out.close();
			} catch (IOException e) {
				ExceptionHelper.trace2String(e);
			}
		}
		return isSuccess;
	}
	
	/*************************************（我是分割线-结束分割) by陈颖 ************************************/
	
	/*************************************（我是分割线-开始分割) by陈勇 ************************************/
	@RequestMapping(value = "/analysislist/{currentPage}/{pageSize}", method = RequestMethod.GET)
	public ModelAndView analysislist( @PathVariable int currentPage, @PathVariable int pageSize, HttpServletRequest request)
			throws Exception {
		Query<FSDataNnalysis> query  =newQuery(currentPage, pageSize, request);
		educationSurveyService.list(null, query);
		
		List<FSDataNnalysis> list = query.getResults();
		return ModelAndViewBuilder.newInstanceFor("/educationSurvey/processdataanalysislist")
				.append("list", list)
				.append("query", query).build();
	}
	/*************************************（我是分割线-结束分割) by陈勇 ************************************/
}
