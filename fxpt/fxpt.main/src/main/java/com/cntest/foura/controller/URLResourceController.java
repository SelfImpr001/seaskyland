/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.persistence.EnumType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.common.controller.BaseController;
import com.cntest.exception.BusinessException;
import com.cntest.foura.domain.URIType;
import com.cntest.foura.domain.URLResource;
import com.cntest.foura.domain.User;
import com.cntest.foura.service.RoleService;
import com.cntest.foura.service.URLResourceService;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.bi.domain.BiInfo;
import com.cntest.fxpt.bi.domain.BiUser;
import com.cntest.fxpt.domain.ExamType;
import com.cntest.fxpt.domain.Grade;
import com.cntest.fxpt.service.IExamTypeService;
import com.cntest.fxpt.service.IGradeService;
import com.cntest.fxpt.util.LogUtil;
import com.cntest.fxpt.util.TitleUtil;
import com.cntest.security.UserDetails;
import com.cntest.util.ExceptionHelper;
import com.cntest.util.ZTree;
import com.cntest.web.view.ModelAndViewBuilder;
import com.cntest.web.view.ResponseStatus;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 肖 肖 2014年6月17日
 * @version 1.0
 **/
@Controller
@RequestMapping("/res")
public class URLResourceController extends BaseController{
	private static Logger logger = LoggerFactory.getLogger(URLResourceController.class);
	@Autowired
	private URLResourceService resourceService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserService userService;
	
	@Autowired(required = false)
	@Qualifier("IExamTypeService")
	private IExamTypeService examTypeService;
	@Autowired(required = false)
	@Qualifier("IGradeService")
	private IGradeService gradeService;
	
	private String tempPath   ;     //临时文件夹
	private String imagePath  ;     //上传图片文件夹

	
	public URLResourceController() {
		InputStream in = this.getClass().getClassLoader().
				  				getResourceAsStream("properties/resources.properties");
		try {
			Properties pro  = new Properties();
			pro.load(in);
			imagePath = pro.getProperty("uploadimage");
			tempPath  = pro.getProperty("uploadimage") + "temp/";
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/appList/{currentPage}/{pageSize}",method = RequestMethod.GET)
	public ModelAndView appList(@PathVariable int currentPage,@PathVariable int pageSize,HttpServletRequest request) throws Exception{
		logger.debug("URL: /URLRes");
        User user = User.from(userService.getCurrentLoginedUser());//User.currentUser();
        logger.info("appList {} select * From");
        List<URLResource> resList = resourceService.getAppsFor(user);
        
        String title = TitleUtil.getTitle(request.getServletPath());
		return ModelAndViewBuilder
				.newInstanceFor("res/appList")
				.append("resList", resList)
				.append("type", URIType.app)
				.append("title", title)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.resource.appList")
								.msg(user.getName()).build()).build();
	}
	@RequestMapping(value="/menuList/{currentPage}/{pageSize}",method = RequestMethod.GET)
	public ModelAndView menuList(@PathVariable int currentPage,@PathVariable int pageSize,@RequestParam String type,@RequestParam String menuList,HttpServletRequest request) throws Exception{
		logger.debug("URL: /menuList/{}/{}",currentPage,pageSize);
		  User user = User.from(userService.getCurrentLoginedUser());
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("uuid", getAppKey());
		logger.info("menuList {} select * From");
		
		List<URLResource> resList = resourceService.getAppsFor(user);
		for (URLResource urlResource : resList) {
			resList = resourceService.getMenuChildren(urlResource.getUuid(),EnumType.valueOf(URIType.class,type),menuList);
			if(resList.size() != 0)
				break;
		}
		String resurl = "res/menuList";
		if("module".equals(type)){
			resurl = "res/menuListmodule";
		}

		String title = TitleUtil.getTitle(request.getServletPath());
		System.out.println(title);
		
		return ModelAndViewBuilder
				.newInstanceFor(resurl)
				.append("resList", resList)
				.append("type", type)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.resource.menuList")
						.msg(user.getName()).build()).build();
	}
	@RequestMapping(value="/deleteIcon/{pk}",method = RequestMethod.POST)
	public ModelAndView deleteIcon(@PathVariable String pk,HttpServletRequest request) throws Exception{
        logger.info("deleteIcon {} 删除资源图标");
      
        URLResource res =resourceService.findResourseByPk(pk);
        String oldIcon = res.getIcon();
        resourceService.deleteIconByPk(pk);
        //删除旧图标
        if(oldIcon != null) {
        	//String re =request.getSession().getServletContext().getRealPath(oldIcon);
			boolean bo = new File(request.getSession().getServletContext().getRealPath(oldIcon)).delete();
	        logger.info("delete file is {}",bo);
        }
        return ModelAndViewBuilder
				.newInstanceFor("")
				.build();
	}
	
	@RequestMapping(value="/delete/{pk}",method = RequestMethod.POST)
	public ModelAndView delete(@PathVariable String pk,HttpServletRequest request) throws Exception{
        logger.info("deleteIcon {} 删除资源图标");
      
        URLResource res =resourceService.findResourseByPk(pk);
        String oldIcon = res.getIcon();
        resourceService.deleteIconByPk(pk);
        //删除旧图标
        if(oldIcon != null) {
        	//String re =request.getSession().getServletContext().getRealPath(oldIcon);
			boolean bo = new File(request.getSession().getServletContext().getRealPath(oldIcon)).delete();
	        logger.info("delete file is {}",bo);
        }
        return ModelAndViewBuilder
				.newInstanceFor("")
				.build();
	}
	@RequestMapping(method = RequestMethod.DELETE)
	public ModelAndView delete(@RequestBody URLResource resource,HttpServletRequest request,HttpServletResponse response) throws Exception{
        logger.info("delete {} delete ");
        //User user = User.currentUser();
        URLResource re = resourceService.load(resource.getPk());
        String message=request.getParameter("message");
        
//        Boolean isDelete = resourceService.relation(re);
//        if(isDelete) {
//        	throw new BusinessException("4a.res.delete","删除失败,请查看与角色或用户的关联!");
//        }
        String status = "失败",info = " ",erre="",name =re.getName(),title="资源管理>报告管理";
		try {
			if("menu".equals(re.getType().toString())) {
				title="资源管理>菜单管理";
			}
			if(message.equals("1")){
	        	resourceService.removeRoleOrUser(resource.getPk());
	        	resourceService.remove(resource);
	            resourceService.removeUriIconFor(resource);
	        }else{
	        	
	        	resourceService.remove(resource);
	            resourceService.removeUriIconFor(resource);
	        }
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info="报告名称：<b style='color:red;'>"+name+"</b>删除"+status+"</br>";
			LogUtil.log(title, "删除",name,status,info,erre);
		}
        //删除旧图标
        String oldIcon = re.getIcon();
        if(oldIcon != null) {
			boolean bo = new File(request.getSession().getServletContext().getRealPath(oldIcon)).delete();
	        logger.info("delete file is {}",bo);
        }
        
		return ModelAndViewBuilder
				.newInstanceFor("res/list")
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.resource.delete")
						.msg(re.getName()+"删除成功").build()).build();
	}
	
	@RequestMapping(value="/updateUI",method = RequestMethod.GET)
	public ModelAndView updateUI(HttpServletRequest request) throws Exception{
		logger.info("updateUI {} 修改UI ");
		List<Grade> grades = gradeService.list();
		List<ExamType> examTypes = examTypeService.list(true);
		
		String pk   = request.getParameter("pk");
		String type = "";//显示的类型名称
		if(pk!=null && pk!=""){
			URLResource resource= resourceService.findResourseByPk(pk);
			type=resource!=null?resource.getType().toString():"";
		}
	
		URLResource resource = resourceService.load(Long.valueOf(pk));
		
		return ModelAndViewBuilder
				.newInstanceFor("res/update")
				.append("resource", resource)
				.append("grades", grades).append("examTypes", examTypes)
				.append("type", type)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.resource.updateUI")).build();
	}
	
	@RequestMapping(method = RequestMethod.PUT)
	public ModelAndView update(@RequestBody URLResource resource,HttpServletRequest request) throws Exception{
        logger.info("update {} 修改 ");
        String status = "失败",info = " ",erre="",title="资源管理>报告管理";
        URLResource temp = resourceService.load(resource.getPk());
        List<URLResource> resList = new ArrayList<URLResource>();
		try {
			info+= "</br>修改项</br>";
			info+= updatainfo(resource,temp);
			if("menu".equals(resource.getType().toString())){
				title="资源管理>菜单管理";
			}
	        
	        temp.setName(resource.getName());
	        temp.setUuid(resource.getUuid());
	        temp.setUrl(resource.getUrl());
	        temp.setType(resource.getType());
	//        temp.setParent(resource.getParent());
	        temp.setAvailable(resource.getAvailable());
	        temp.setEntry(resource.getEntry());
	        temp.setRemarks(resource.getRemarks());
	        temp.setReorder(resource.getReorder());
	        temp.setGradeids(resource.getGradeids());
	        temp.setExamtypeids(resource.getExamtypeids());
	        
	        String   oldIcon = temp.getIcon();     //旧图标
	        String   newIcon = resource.getIcon(); //新图标
	        /* 上传的图标处理*/
	        File fileIcon    = null;
	        String   iconName = null;
	        if(StringUtils.isNotEmpty(newIcon)) {
	        	  iconName  = fileCopy(newIcon,request);
		        fileIcon    = new File(request.getSession().getServletContext().getRealPath(imagePath + iconName));
		        temp.setIcon(imagePath + iconName);
	        }else {
				resource.setIcon(null);
			}
	        userService.evictSession(temp);
	        resourceService.update(temp);
	        resList.add(temp);
	  
	        //删除旧图标
	        if(fileIcon != null) {
	        	resourceService.saveOrUpdateIcon(fileIcon, temp);
	        	if(oldIcon != null) {
	        		File file  = new File(request.getSession().getServletContext().getRealPath(oldIcon));
	        		boolean bo =file.delete();
	        		logger.info("delete file is {}",bo);
	        	}
	        }
	        status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			LogUtil.log(title, "修改",resource.getName(),status, " <b style='color:red;'>"+resource.getName()+"</b>修改"+status+"</br>"+info,erre);
		}
        String view = getView(resource);
        
        return ModelAndViewBuilder
				.newInstanceFor(view)
				.append("resList", resList)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.resource.update")).build();
	}
	public String updatainfo(URLResource resource,URLResource temp) {
		//resource:新来的数据，temp :之前的数据
		String info="";
		
		if(resource.getAvailable()!= temp.getAvailable()) {
			HashMap<Boolean ,String> span = new HashMap<Boolean,String>();
			span.put(null,"锁定 ");
			span.put(true,"有效");
			span.put(false,"无效");
			info+=("是否有效:<b style='color:red;'>"+span.get(temp.getAvailable()) +"</b> 改  <b style='color:red;'>"+span.get(resource.getAvailable())+"</b><br/>");
		}
		if(resource.getName()!=null && (!resource.getName().equals(temp.getName()))) {
			info+="报告名称：<b style='color:red;'>"+temp.getName()+"</b> 改  <b style='color:red;'>"+resource.getName()+"</b><br/>";
		}
		if(resource.getUrl()!=null && (!resource.getUrl().equals(temp.getUrl()))) {
			info+="报告URI：<b style='color:red;'>"+temp.getUrl()+"</b> 改  <b style='color:red;'>"+resource.getUrl()+"</b><br/>";
		}
		if(resource.getEntry()!=null && (!resource.getEntry().equals(temp.getEntry()))) {
			info+="报告关联文件：<b style='color:red;'>"+temp.getEntry()+"</b> 改  <b style='color:red;'>"+resource.getEntry()+"</b><br/>";
		}
		if(resource.getReorder()!=null && (!resource.getReorder().equals(temp.getReorder()))) {
			info+="报告顺序：<b style='color:red;'>"+temp.getReorder()+"</b> 改  <b style='color:red;'>"+resource.getReorder()+"</b><br/>";
		}
		if(resource.getRemarks()!=null && (!resource.getRemarks().equals(temp.getRemarks()))) {
			info+="描述：<b style='color:red;'>"+temp.getRemarks()+"</b> 改  <b style='color:red;'>"+resource.getRemarks()+"</b><br/>";
		}
		
		if(resource.getGradeids()!=null && (!resource.getGradeids().equals(temp.getGradeids()))) {
			List<Grade> grades = gradeService.list();
			String newA = "",newB="";
			HashMap<String ,String> span = new HashMap<String,String>();
			for (Grade grade : grades) {
				span.put(grade.getId()+"",grade.getName());
			}
			for (String key :resource.getGradeids().split(",")) {
				newA+=(span.get(key)+",");
			}
			for (String key :temp.getGradeids().split(",")) {
				newB+=(span.get(key)+",");
			}
			info+="适用年级：<b style='color:red;'>"+newB+"</b> 改  <b style='color:red;'>"+newA+"</b><br/>";
		}
		if(resource.getExamtypeids()!=null && (!resource.getExamtypeids().equals(temp.getExamtypeids()))) {
			List<ExamType> examTypes = examTypeService.list(true);
			String newA = "",newB="";
			HashMap<String ,String> span = new HashMap<String,String>();
			for (ExamType examType : examTypes) {
				span.put(examType.getId()+"",examType.getName());
			}
			for (String key :resource.getExamtypeids().split(",")) {
				newA+=(span.get(key)+",");
			}
			for (String key :temp.getExamtypeids().split(",")) {
				newB+=(span.get(key)+",");
			}
			info+="适用考试类型：<b style='color:red;'>"+newB+"</b> 改  <b style='color:red;'>"+newA+"</b><br/>";
		}
		if(info.length()==0) {
			info="没有修改项";
		}
		return info;
	}
	@RequestMapping(value="/addUI",method = RequestMethod.GET)
	public ModelAndView addUI() throws Exception{
        logger.info("addUI {} 新增UI");
        return ModelAndViewBuilder
				.newInstanceFor("/res/appAdd")
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.resource.addUI")).build();
	}
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView add(@RequestBody URLResource resource,HttpServletRequest request) throws Exception{
		logger.info("add {} 新增");
		String status = "失败",info = " ",erre="";
		List<URLResource> resList=null;
		try {
			URLResource tempRes = new URLResource.Builder().create();
			resource.setUuid(tempRes.getUuid());
			File imageFile      = null;
			if(StringUtils.isNotEmpty(resource.getIcon())) {
				String iconName   = fileCopy(resource.getIcon(),request);
				imageFile = new File(request.getSession().getServletContext().getRealPath(imagePath + iconName));
				resource.setIcon(imagePath + iconName);
			}else {
				resource.setIcon(null);
			}
			
			resourceService.create(resource);
			
			/* 存储图片*/
			if(imageFile !=null) {
				int row = resourceService.saveOrUpdateIcon(imageFile,resource);
				logger.info("saveImage row {}",row);
			}
			Subject subject = SecurityUtils.getSubject();
			UserDetails userDetail = null;
			if(subject.getPrincipal() instanceof UserDetails) {
				userDetail = (UserDetails) subject.getPrincipal();
				
			} else if(subject.getPrincipal() instanceof String) {
				String username  = (String) subject.getPrincipal();
				User user = userService.findUserBy(username);
				userDetail = user.toUserDetails();
			}
			resList = resourceService.getAppsFor(User.currentUser(userDetail));
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info="报告名称：<b style='color:red;'>"+resource.getName()+"</b>添加"+status+"</br>";
			LogUtil.log("资源管理>报告管理>", "添加",resource.getName(),status,info,erre);
		}
		logger.info("add {} 新增");
		String view = getView(resource);
		return ModelAndViewBuilder
				.newInstanceFor(view)
				.append("resList", resList)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.resource.add").build()).build();
	}
	@RequestMapping(value="/addMenuUI",method = RequestMethod.GET)
	public ModelAndView addMenuUI(@RequestParam String uuid,@RequestParam String type) throws Exception{
        logger.info("addMenuUI {} 新增菜单UI");
		List<Grade> grades = gradeService.list();
		List<ExamType> examTypes = examTypeService.list(true);
        
        URLResource resource = resourceService.getResourceByUuid(uuid);
        String showType      = type;				//显示的类型
        if(resource.getType().equals("")) {
        	type = URIType.menu.toString();
        }
        return ModelAndViewBuilder
				.newInstanceFor("/res/addMenu")
				.append("grades", grades).append("examTypes", examTypes)
				.append("type", type)
				.append("showType", showType)
				.append("parentResource", resource)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.resource.addMenuUI")).build();
	}
	private String fileCopy(String oldIconPath,HttpServletRequest request) throws Exception {
		String  imageName = oldIconPath.substring(oldIconPath.lastIndexOf("/") + 1); //图标名称
		String  iconType  = imageName.substring(imageName.lastIndexOf(".") + 1);
		String iconName   = UUID.randomUUID().toString() + "." + iconType; //存储之后的图片名称
		File    imageFile = new File(request.getSession().getServletContext().getRealPath(imagePath + iconName));
		File    tempFile  = new File(request.getSession().getServletContext().getRealPath(tempPath + imageName));
		if(tempFile.exists() && !imageFile.exists()){
			FileCopyUtils.copy(tempFile,imageFile);
			Thread.sleep(5000);
		}else{
			iconName      = imageName;
		}
		return iconName;
	}
	@RequestMapping(value="/addMenu",method = RequestMethod.POST)
	public ModelAndView addMenu(@RequestBody URLResource resource,HttpServletRequest request) throws Exception{
		logger.info("addMenu {} 新增菜单");
		String status = "失败",info = " ",erre="",title="资源管理>报告管理>";
		List<URLResource> resList=null;
		String temp = request.getParameter("uriType");
		if("menu".equals(temp)) {
			title="资源管理>菜单管理>"; 
		}
		try {
			URLResource tempRes = new URLResource.Builder().create();
			resource.setUuid(tempRes.getUuid());
			
			File imageFile = null;
			if(StringUtils.isNotEmpty(resource.getIcon())) {
				String iconName   = fileCopy(resource.getIcon(),request);
				imageFile = new File(request.getSession().getServletContext().getRealPath(imagePath + iconName));
				resource.setIcon(imagePath + iconName);
			}else {
				resource.setIcon(null);
			}
			resourceService.create(resource);
			/* 存储图片*/
			if(imageFile != null) {
				int row = resourceService.saveOrUpdateIcon(imageFile,resource);
				logger.info("saveImage row {}",row);
			}
			String flag     =request.getParameter("menuList");
	        URIType type    = null;
	        if(temp == null) {
	        	temp = URIType.menu.toString();
	        }else {
	        	type = EnumType.valueOf(URIType.class,temp) ;
	        }
	        URLResource res = resourceService.getResourceByUuid(tempRes.getUuid());
	        
	        String uuid = res.getParent().getUuid();
	        if(res.getType().equals(URIType.menu))
	        	type = URIType.menu;
	        
			 resList  = resourceService.getMenuChildren(uuid, type,flag);
			 status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info="报告名称：<b style='color:red;'>"+resource.getName()+"</b>添加"+status+"</br>";
			
			LogUtil.log(title, "添加",resource.getName(),status,info,erre);
		}
		logger.info("addMenu {} 新增菜单");
		String view = getView(resource);
		return ModelAndViewBuilder
				.newInstanceFor(view)
				.append("resList", resList)
				.append("newResource", resource)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.resource.addMenu").build()).build();
	}
	@RequestMapping(value="/fileUpload",method = RequestMethod.POST)
	public ModelAndView fileUpload(MultipartHttpServletRequest request,HttpServletResponse response) throws Exception{
		logger.info("fileUpload {} 文件上传");
		String fileName = null;
		logger.info("multipartRequest {}",request);
			
		try {
				MultipartFile multipartFile = request.getFile("fileUpload");
				
				String uploadPath = request.getSession().getServletContext().getRealPath(tempPath);
				
				fileName       = file(multipartFile,uploadPath);
		}catch(Exception e) {
				logger.error(ExceptionHelper.trace2String(e));
				throw new BusinessException("4a.res.img.upload","图片上传失败!");
		}
		return ModelAndViewBuilder
				.newInstanceFor("/res/iconSuccess")
				.append("tempPath", tempPath + fileName)
				.append("imagePath", imagePath + fileName)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.resource.fileUpload").build()).build();
	}
	/**
	 * 
	 * @param multipartFile 持有上传的文件
	 * @param path 路径
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	private String file(MultipartFile multipartFile,String path)throws Exception {
		String fileName    = multipartFile.getOriginalFilename();
		//验证文件后缀名 
		if(!fileName.toUpperCase().endsWith(".JPEG") 
			&& !fileName.toUpperCase().endsWith(".TIFF")	
			&& !fileName.toUpperCase().endsWith(".RAW")
			&& !fileName.toUpperCase().endsWith(".BMP")
			&& !fileName.toUpperCase().endsWith(".PNG")
			&& !fileName.toUpperCase().endsWith(".GIF")
			&& !fileName.toUpperCase().endsWith(".JPG")) {
			throw new Exception("请确保文件是正确的图片文件!");
		}
		
		
		String uploadPath = path + File.separator + fileName;
		logger.info("fileName :{}",fileName);
		logger.info("uploadPath :{}",path);
		
		File makdir = new File(path);
		if(!makdir.exists()) {
			makdir.mkdirs();
		}
		
		File file = new File(uploadPath);
		
		FileCopyUtils.copy(multipartFile.getBytes(), file);
		if(fileName == null)
			throw new Exception();
		return fileName;
		
	}
	@RequestMapping(value="/nodelist",method = RequestMethod.GET)
	public ModelAndView treeNodeList(HttpServletRequest request,HttpServletResponse response) throws Exception{;
		logger.info("treeNodeList {}  type{}",request.getParameter("type"));
		User user = User.from(userService.getCurrentLoginedUser());
        logger.info("treeNodeList {} select * From");
        int level  = Integer.valueOf(StringUtils.isNotEmpty(request.getParameter("level"))?
        						request.getParameter("level"):"0");
        List<URLResource> resList = resourceService.getAppsFor(user);
        
        String temp  = request.getParameter("type");
        String flag     =request.getParameter("menuList");
        URIType type = null;
        
        if(temp != null) 
        	type = EnumType.valueOf(URIType.class,temp) ;
        
        
        JsonArray jsonArray  = buildTreeData(resList,level,0,type,flag);
        
        logger.info("treeNodeList {}",jsonArray.toString());
        
		return ModelAndViewBuilder
				.newInstanceFor("")
				.append("resJson",jsonArray.toString())
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.res.nodelist")
						.msg("success").build()).build();
	}
	//三级
	@RequestMapping(value="/chirdren/tree/{uuid}/{menuList}",method = RequestMethod.GET)
	public ModelAndView childNodelist(@PathVariable String uuid,@PathVariable String menuList,HttpServletRequest request,HttpServletResponse response) throws Exception{;
		logger.info("childNodelist {} 异步请求树节点数据");
		String temp     = request.getParameter("type");
        URIType type    = null;
        
        if(temp == null)
        	temp = URIType.menu.toString();
        type = EnumType.valueOf(URIType.class,temp) ;
        
		logger.info("childNodelist {}  type{}",request.getParameter("type"));

		List<URLResource> resList = new ArrayList<URLResource>();
		if(type.equals(URIType.menu))
			resList = resourceService.getMenuChildren(uuid, type,menuList); //flag=0  菜单管理
		else 
			resList = resourceService.findSubURIFor(uuid, type);
		
		//listOrderByChild(resList);
		
		JsonArray jsonArray  = buildTreeData(resList,0,0, null,menuList);
		logger.info("childNodelist {}",jsonArray.toString());
		
		return ModelAndViewBuilder
				.newInstanceFor("")
				.append("resJson",jsonArray.toString())
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.res.chirdren.tree")
						.msg("success").build()).build();
	}
	
	@RequestMapping(value="/chidren/{uuid}/{menuList}",method = RequestMethod.GET)
	public ModelAndView forChildMenu(@PathVariable String uuid,@PathVariable String menuList,HttpServletRequest request) throws Exception{;
		logger.info("forChildMenu {} 异步请求子列表数据");
		logger.info("forChildMenu () type{}",request.getParameter("type"));
		  User user = User.from(userService.getCurrentLoginedUser());
		String temp     = request.getParameter("type");
	    String isParent = request.getParameter("isParent");
		URIType type    = null;
		URLResource resR= resourceService.getResourceByUuid(uuid);
        if(temp == null)
        	temp=resR.getType().toString();
        	//temp = URIType.menu.toString();
        if(temp.equals("module")){
        	type=URIType.module;
        }else{
        	type=URIType.menu;
        }
       // type = EnumType.valueOf(URIType.class,temp) ;
        
        List<URLResource> resList = new ArrayList<URLResource>();
		if(isParent.equals("true")) {
			resList = refreshResListFor(type,uuid,menuList);
		}else {
			resList.add(resourceService.getResourceByUuid(uuid));
		}
        
		return ModelAndViewBuilder
				.newInstanceFor("res/subList")
				.append("resList", resList)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.index")
						.msg(user.getName()).build()).build();
	}
	/**
	 * 加载树形节点 (只加载一层)
	 * @param resList
	 * @param level 加载层级
	 * @param i 迭代次数
	 * @param type TODO
	 * @return
	 */
	private JsonArray buildTreeData(List<URLResource> resList,int level,int i, URIType type,String flag) {
		JsonArray jsonArray = new JsonArray();
        for (URLResource urlResource : resList) {
        	JsonObject object = new JsonObject();
        	logger.debug("PK:{}",urlResource.getPk());
        	ArrayList<URLResource> children = new ArrayList<URLResource>();
        	if(type != null) {
//        		if(i == 1) 
//        			type = null;
        		List<URLResource> res = resourceService.getMenuChildren(urlResource.getUuid(),type,flag);
        		//listOrderByChild(res); //整理顺序:节点(文件夹)--->叶节点(文件)
        		children.addAll(res);
        	}else {
        		children.addAll(urlResource.getChildren());
        	}

        	object = new ZTree().id(urlResource.getPk())
        							   .pId(urlResource.getParent() == null?-1L:urlResource.getParent().getPk())
        							   .open( i < level).isParent(children.size() != 0 ? true:false)
        							   .zTreeJsonObj(urlResource);
        	object.remove("url");
        	object.remove("icon");
        	jsonArray.add(object);
        	if(i < level) 
        		jsonArray.addAll(buildTreeData(children,level,i + 1, type,flag));
        	
        	if(urlResource.getType().equals(URIType.app)) 
        		i = 0;
		}
		return jsonArray;
	}
	/**
	 * 根据有无子节点来进行排序
	 */
	private void listOrderByChild(List<URLResource> resList) {
		List<URLResource> parents = new ArrayList<URLResource>();
		List<URLResource> childs  = new ArrayList<URLResource>();
		for (URLResource urlResource : resList) {
			if(urlResource.getChildren().size() > 0) {
				parents.add(urlResource);
			}else {
				childs.add(urlResource);
			}
		}
		resList.clear();
		resList.addAll(parents);
		resList.addAll(childs);
	}
	/**
	 * 刷新资源列表  根据类型和uuid·
	 * @return
	 */
	private List<URLResource> refreshResListFor(URIType type,String uuid,String flag){
        List<URLResource> resList = new ArrayList<URLResource>();
        URLResource res = resourceService.getResourceByUuid(uuid);
		if(res.getParent() == null) {
			resList = resourceService.getMenuChildren(uuid, type,flag);
		}else {
			resList = resourceService.findSubURIFor(uuid, type);
		}
		
		return resList;
	}
	private String getView(URLResource resource) {
		String view = "res/appList";
		if(!resource.getType().equals(URIType.app))
			 view = "res/subList";
        return view;
	}
	/**
	 * 树形菜单拖拽更新
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dragRes/{id}/{parentId}/{type}/{broId}", method = RequestMethod.POST)
	public ModelAndView dragRes(@PathVariable String id,@PathVariable String parentId,@PathVariable String type,@PathVariable String broId) throws Exception {
		//拖动更改组织关系
		resourceService.updateURLResourceByIds(id,parentId,type,broId);
		return ModelAndViewBuilder.newInstanceFor("")
				.build();
	}
	/**
	 * 检验菜单是否有角色或用户使用
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/checkMenuId/{pk}",method = RequestMethod.GET)
	public ModelAndView checkMenuId(@PathVariable Long pk) throws Exception{
		logger.debug("URL: /URLRes");
		String message="";
        logger.info("checkMenuId {pk}");
        URLResource re = resourceService.load(pk);
        if(resourceService.relation(re)){  //资源有用户或角色在使用
        	message="1";
        }else{
        	message="2";
        };
        
		return ModelAndViewBuilder
				.newInstanceFor("")
				.append("message", message)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.resource.checkMenuId")
								.msg("").build()).build();
	}
}

