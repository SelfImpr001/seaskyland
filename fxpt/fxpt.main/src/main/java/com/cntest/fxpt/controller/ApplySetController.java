package com.cntest.fxpt.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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

import com.cntest.exception.BusinessException;
import com.cntest.fxpt.domain.ApplySet;
import com.cntest.fxpt.domain.Subject;
import com.cntest.fxpt.service.IApplySetService;
import com.cntest.fxpt.service.ISubjectService;
import com.cntest.util.ExceptionHelper;
import com.cntest.web.view.ModelAndViewBuilder;
import com.cntest.web.view.ResponseStatus;


@Controller
@RequestMapping("/apply")
public class ApplySetController {
	private static Logger logger = LoggerFactory.getLogger(ApplySetController.class);

	@Autowired(required = false)
	@Qualifier("ISubjectService")
	private ISubjectService subjectService;
	
	@Autowired(required = false)
	@Qualifier("IApplySetService")
	private IApplySetService applySetService;

	private String tempPath   ;     //临时文件夹
	private String imagePath  ;     //上传图片文件夹

	
	public ApplySetController() {
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
	
	@RequestMapping(value="/list",method = RequestMethod.GET)
	public ModelAndView list( @RequestParam String applyName,ServletRequest request) throws Exception {
		List<ApplySet> applyList=null;
		if(applyName!="" && applyName.length()>0){
			applyList= applySetService.findApplyByName(applyName);
		}else{
			applyList= applySetService.findAllApply();
		}
		return ModelAndViewBuilder.newInstanceFor("/apply/list")
				.append("applys", applyList).build();
	}

	@RequestMapping(value="/newAdd",method = RequestMethod.GET)
	public ModelAndView newAdd() throws Exception {
		List<ApplySet> applist =applySetService.findApplyByStatus("3");
		if(applist.size()>0){
			applySetService.deleteByStatus("3");
		}
		return ModelAndViewBuilder.newInstanceFor("/apply/newAdd").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("")
				.msg("").build()).build();
	}

	@RequestMapping(value="/addApply",method = RequestMethod.POST)
	public ModelAndView add(@RequestBody ApplySet applySet,HttpServletRequest request) throws Exception {
		applySetService.save(applySet);
		return ModelAndViewBuilder.newInstanceFor("").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("status")
				.msg("添加成功").build()).build();
	}
	//图片上传后添加临时数据，方便预览
	@RequestMapping(value="/justImageAdd/{type}",method = RequestMethod.POST)
	public ModelAndView justImageAdd(@RequestBody ApplySet applySet,@PathVariable String type,HttpServletRequest request) throws Exception {
		List<ApplySet> applist =applySetService.findApplyByStatus("3");
		applySetService.justAddImage(applist,type,applySet);
		return ModelAndViewBuilder.newInstanceFor("").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("status")
				.msg("添加成功").build()).build();
	}
	
	//图片恢复默认
	@RequestMapping(value="/defaultApply/{id}/{type}",method = RequestMethod.POST)
	public ModelAndView defaultApply(@PathVariable Long id,@PathVariable String type,HttpServletRequest request) throws Exception {
		ApplySet apply =applySetService.get(id);
		if("login".equalsIgnoreCase(type)){
			apply.setLoginIcon("");
		}else{
			apply.setHandleIcon("");
		}
		applySetService.update(apply);
		return ModelAndViewBuilder.newInstanceFor("").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("status")
				.msg("添加成功").build()).build();
	}
	@RequestMapping("/newUpdate/{id}")
	public ModelAndView newUpdate(@PathVariable Long id) throws Exception {
		List<ApplySet> applist =applySetService.findApplyByStatus("3");
		if(applist.size()>0){
			applySetService.deleteByStatus("3");
		}
		ApplySet apply = applySetService.get(id);
		return ModelAndViewBuilder.newInstanceFor("/apply/newUpdate")
				.append("apply", apply).append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("status")
						.msg("").build()).build();
	}

	@RequestMapping(value="/applyUpdate",method = RequestMethod.PUT)
	public ModelAndView update(@RequestBody ApplySet applySet) throws Exception {
		applySetService.update(applySet);
		return ModelAndViewBuilder.newInstanceFor("").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("status")
				.msg("更新成功").build()).build();
	}

	@RequestMapping(value="/delete/{pk}",method = RequestMethod.POST)
	public ModelAndView delete(@PathVariable Long pk) throws Exception {
		ApplySet applySet = applySetService.get(pk);
		applySetService.delete(applySet);
		return ModelAndViewBuilder.newInstanceFor("").append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("status")
						.msg("删除成功").build()).build();
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
	@RequestMapping(value="/fileupload",method = RequestMethod.POST)
	public ModelAndView fileupload(MultipartHttpServletRequest request,HttpServletResponse response) throws Exception{
		logger.info("fileUpload {} 文件上传");
		String fileName = null;
		logger.info("multipartRequest {}",request);
			
		try {
				MultipartFile multipartFile = request.getFile("fileUploadLogin");
				
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
	
	@RequestMapping(value="/fileUploadHandle",method = RequestMethod.POST)
	public ModelAndView fileUploadHandle(MultipartHttpServletRequest request,HttpServletResponse response) throws Exception{
		logger.info("fileUpload {} 文件上传");
		String fileName = null;
		logger.info("multipartRequest {}",request);
			
		try {
				MultipartFile multipartFile = request.getFile("fileUploadHandle");
				
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
	private String fileCopy(String oldIconPath,HttpServletRequest request) throws Exception {
		//项目名称
		String webName =request.getContextPath();
		//项目绝对路径
		String realPath =request.getSession().getServletContext().getRealPath("/");
		//防止项目重新启动时,清除了图片信息
		String www=webName.substring(1,webName.length());
		realPath=realPath.replace(www,"applyImags");
		String  imageName = oldIconPath.substring(oldIconPath.lastIndexOf("/") + 1); //图标名称
		String  iconType  = imageName.substring(imageName.lastIndexOf(".") + 1);
		String iconName   = UUID.randomUUID().toString() + "." + iconType; //存储之后的图片名称
		pathValidate(realPath);
		File    imageFile =new File(realPath+iconName);
		File    tempFile  = new File(request.getSession().getServletContext().getRealPath(tempPath + imageName));
		if(tempFile.exists() && !imageFile.exists()){
			FileCopyUtils.copy(tempFile,imageFile);
		}else{
			iconName      = imageName;
		}
		return realPath+"/"+iconName;
	}
	
	private File pathValidate(String path){
		File    imageFile = new File(path);
		if(!imageFile.exists()){
			imageFile.mkdir();
		}
		return imageFile;
		
	}
}
