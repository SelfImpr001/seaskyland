/*
 * @(#)com.cntest.fxpt.util.UploadDir.java	1.0 2014年5月19日:上午11:36:17
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cntest.fxpt.bean.UploadFileContentInfo;
import com.cntest.fxpt.bean.UploadFileInfo;
import com.cntest.fxpt.bean.UploadFileValidateResult;
import com.cntest.fxpt.controller.Plupload;
import com.cntest.fxpt.etl.business.impl.CjUploadFileContentInfoBuild;
import com.cntest.fxpt.etl.business.impl.CjValidateFile;
import com.cntest.fxpt.etl.business.impl.DefaultValidateFile;
import com.cntest.fxpt.etl.business.impl.ItemUploadFileContentInfoBuild;
import com.cntest.fxpt.etl.business.impl.ItemValidateFile;
import com.cntest.fxpt.etl.business.impl.StudentValidateFile;
import com.cntest.fxpt.etl.business.impl.UploadFileContentInfoBuild;
import com.cntest.fxpt.etl.util.DateUtils;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月19日 上午11:36:17
 * @version 1.0
 */
public class UploadHelper {
	private static String getUploadRoot() {
		File f = null;
		try {
			URL url = UploadHelper.class.getClassLoader().getResource("/");
			File tmpFile = new File(url.toURI());
			String filePath = tmpFile.getParentFile().getAbsolutePath()
					+ "/upload";
			f = new File(filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!f.exists()) {
			f.mkdirs();
		}
		return f.getAbsolutePath() + "/";
	}
	//脚本目录
	private static String getUploadReport() {
		File f = null;
		try {
			URL url = UploadHelper.class.getClassLoader().getResource("/");
			File tmpFile = new File(url.toURI());
			String filePath = tmpFile.getParentFile().getParentFile().getParentFile().getAbsolutePath()
					+ "/uploadReport";
			f = new File(filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!f.exists()) {
			f.mkdirs();
		}
		return f.getAbsolutePath() + "/";
	}

	public static String getDir() {
		String dir = SystemConfig.newInstance().getValue("upload.dir");
		if (dir == null || dir.equals("")) {
			dir = getUploadRoot();
		}
		return dir;
	}
	public static String getReport() {
		String dir = SystemConfig.newInstance().getValue("upload.dir");
		if (dir == null || dir.equals("")) {
			dir = getUploadReport();
		}
		return dir;
	}

	public static UploadFileInfo createUploadFileInfo(MultipartFile file)
			throws Exception {
		String fileName = file.getOriginalFilename();
		String suffix = fileName.substring(fileName.lastIndexOf("."),
				fileName.length());
		String newFileName = DateUtils.getMillisecodeToString() + suffix;
		String filePath = UploadHelper.getDir() + newFileName;
		OutputStream out = new FileOutputStream(filePath);
		FileCopyUtils.copy(file.getInputStream(), out);
		

		UploadFileInfo uploadFileInfo = new UploadFileInfo();
		uploadFileInfo.setName(fileName).setDesFileName(newFileName)
				.setPath(filePath).setSuffix(suffix);
		return uploadFileInfo;
	}
	
	public static UploadFileInfo createUploadFileInfo(Plupload plupload)
			throws Exception {
		
		String fileName = plupload.getName();//file.getOriginalFilename();
		String suffix = fileName.substring(fileName.lastIndexOf("."), fileName.length());
		
		String newFileName = DateUtils.getMillisecodeToString() + suffix;
		String filePath = UploadHelper.getDir() + newFileName;
		
		PluploadUtil.upload(plupload, new File(UploadHelper.getDir()),newFileName);

		UploadFileInfo uploadFileInfo = new UploadFileInfo();
		uploadFileInfo.setName(fileName).setDesFileName(newFileName) .setPath(filePath).setSuffix(suffix);
		return uploadFileInfo;
	}
	public static UploadFileInfo createUploadReport(MultipartFile file)
			throws Exception {
		String fileName = file.getOriginalFilename();
		String suffix = fileName.substring(fileName.lastIndexOf("."),
				fileName.length());
//		String newFileName = DateUtils.getMillisecodeToString() + suffix;
		String filePath = UploadHelper.getReport() + fileName;
		OutputStream out = new FileOutputStream(filePath);
		FileCopyUtils.copy(file.getInputStream(), out);

		UploadFileInfo uploadFileInfo = new UploadFileInfo();
		uploadFileInfo.setName(fileName).setDesFileName(fileName)
				.setPath(filePath).setSuffix(suffix);
		return uploadFileInfo;
	}
	
	public static UploadFileInfo createUploadFileInfo(MultipartFile file,String filePaths)
			throws Exception {
		String fileName = file.getOriginalFilename();
		String suffix = fileName.substring(fileName.lastIndexOf("."),
				fileName.length());
		String newFileName = file.getOriginalFilename().split("[.]")[0]+DateUtils.getMillisecodeToString() + suffix;
		String filePath = filePaths + newFileName;
		OutputStream out = new FileOutputStream(filePath);
		FileCopyUtils.copy(file.getInputStream(), out);

		UploadFileInfo uploadFileInfo = new UploadFileInfo();
		uploadFileInfo.setName(fileName).setDesFileName(newFileName)
				.setPath(filePath).setSuffix(suffix);
		return uploadFileInfo;
	}

	public static UploadFileContentInfo getFileContent(int schemeType,
			UploadFileInfo fileInfo, String sheetName) {
		if (schemeType == 2) {
			return new ItemUploadFileContentInfoBuild().build(fileInfo, sheetName);
		} else if (schemeType == 3) {
			return new CjUploadFileContentInfoBuild() .build(fileInfo, sheetName);
		} else if (schemeType == 1) {
			return new UploadFileContentInfoBuild().build(fileInfo, sheetName);
		} else {
			return new UploadFileContentInfoBuild().build(fileInfo, sheetName);
		}
	}

	public static UploadFileValidateResult getValidateResult(int schemeType,
			UploadFileContentInfo fileContent, Long examId,
			boolean isValidataHead) {
		if (schemeType == 2) {
			return new ItemValidateFile().validate(fileContent, isValidataHead,
					examId, schemeType);
		} else if (schemeType == 3) {
			return new CjValidateFile().validate(fileContent, isValidataHead,
					examId, schemeType);
		} else if (schemeType == 1) {
			return new StudentValidateFile().validate(fileContent,
					isValidataHead, examId, schemeType);
		} else {
			return new DefaultValidateFile().validate(fileContent,
					isValidataHead, examId, schemeType);
		}
	}
}
