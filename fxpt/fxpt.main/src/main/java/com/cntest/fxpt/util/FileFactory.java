/*
 * @(#)com.cntest.fxpt.util.FileFactory.java	1.0 2016年3月11日:上午9:45:05
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;


/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2016年3月11日 上午9:45:05
 * @version 1.0
 */
public class FileFactory {
	/**
	* <Pre>
	* 创建文本文件
	* </Pre>
	* @param fileName   路径及文件名  如："D://hhc.txt"
	* @param txt  文本内容
	* @return
	* @return boolean
	* @author:黄洪成 2016年3月11日 上午9:47:01
	 */
	public static boolean createTxt(String fileName,String txt){
		try {
			File file=new File(fileName);
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			if(!file.exists()){
				file.createNewFile();
			}
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileName), "UTF-8"));
			bw.write(txt);
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static String getDir() {
		String dir = SystemConfig.newInstance().getValue("upload.dir");
		if (dir == null || dir.equals("")) {
			dir = getUploadRoot();
		}
		return dir;
	}
	
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
	
	/**
	* <Pre>
	* ftp文件上传
	* </Pre>
	* @param url ftp服务器地址
	* @param port ftp服务器端口路
	* @param username  ftp登录账号
	* @param password  ftp登录密码
	* @param path ftp服务器文件保存目录
	* @param filename 将要上传到ftp服务器的文件名
	* @param input 输入流
	* @return
	* @throws Exception
	* @return boolean
	* @author:黄洪成 2016年3月11日 下午3:08:05
	 */
	public static boolean uploadFiletoFtp(String url,int port,String username,String password,String path,String filename,InputStream input) throws Exception{
		boolean success = false;
		FTPClient ftp = new FTPClient();
		try {
			ftp.connect(url,port);
		} catch (Exception e) {
			System.out.println("连接ftp服务器失败");
		}
		int reply;
		ftp.login(username, password);
		ftp.enterLocalPassiveMode();
		ftp.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
		ftp.setFileType(FTP.BINARY_FILE_TYPE);
		reply = ftp.getReplyCode();
		if(!FTPReply.isPositiveCompletion(reply)){
			ftp.disconnect();
			System.out.println("----->>连接ftp服务器失败");
		}
		System.out.println("------>>连接服务器成功");
		ftp.makeDirectory(path);
		ftp.changeWorkingDirectory(path);
		ftp.storeFile(new String(filename.getBytes("GBK"),"iso-8859-1"), input);
		ftp.logout();
		success = true;
		System.out.println("----->>文件上传成功");
		if(ftp.isConnected()){
			try {
				ftp.disconnect();
			} catch (IOException ioe) {
				System.out.println("----->>ftp连接关闭失败"+ioe.getMessage());
			}
		}
		return success;
	}
	
	/**
	 * 
	* <Pre>
	* 测试FTP目录服务器是否可用
	* </Pre>
	* 
	* @param url
	* @param port
	* @param username
	* @param password
	* @param path
	* @return
	* @throws Exception
	* @return boolean  true 通过   false 失败
	* @author:黄洪成 2016年3月28日 下午1:45:07
	 */
	public static boolean testFtpSuccess(String url,int port,String username,String password,String path) throws Exception{
		boolean success = false;
		FTPClient ftp = new FTPClient();
		try {
			ftp.connect(url,port);
		} catch (Exception e) {
			System.out.println("连接ftp服务器失败");
		}
		int reply;
		ftp.login(username, password);
		ftp.enterLocalPassiveMode();
		reply = ftp.getReplyCode();
		if(!FTPReply.isPositiveCompletion(reply)){
			ftp.disconnect();
			System.out.println("----->>连接ftp服务器失败");
		}
		ftp.makeDirectory(path);
		success = ftp.changeWorkingDirectory(path);
		ftp.logout();
		if(ftp.isConnected()){
			try {
				ftp.disconnect();
			} catch (IOException ioe) {
				System.out.println("----->>ftp连接关闭失败"+ioe.getMessage());
			}
		}
		return success;
	}
	
	
	/**
	 * 
	* <Pre>
	* 获得当前系统时间  年月日时分秒
	* </Pre>
	* 
	* @return
	* @return String
	* @author:黄洪成 2016年3月21日 上午10:40:38
	 */
	public static String getNowDate(){
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String nowDate = dateFormat.format(date);
		return nowDate;
	}
	
	/**
	 * 
	* <Pre>
	* 打包多个FILE文件到zip包中
	* </Pre>
	* 
	* @param files
	* @param zipFileName
	* @param response
	* @return void
	* @author:黄洪成 2016年5月6日 下午1:54:21
	 */
	public static void zipFiles(List<File> files,String zipFileName,HttpServletResponse response,boolean isdelete){
		byte[] buf= new byte[1024];
		try {
			response.reset();
			response.setHeader("Content-Disposition", "attachment; filename="+ URLEncoder.encode(zipFileName.concat(".zip"),"UTF-8"));
			response.setContentType("APPLICATION/octet-stream");
			ZipOutputStream out = new ZipOutputStream(response.getOutputStream());
			for (int i = 0; i < files.size(); i++) {
				File file = files.get(i);
				FileInputStream in = new FileInputStream(file);
				out.putNextEntry(new ZipEntry(file.getName()));
				
				int len;
				while((len=in.read(buf))>0){
					out.write(buf,0,len);
				}
				
				out.setEncoding("GBK");
				out.closeEntry();
				in.close();
				if(isdelete){
					file.delete();
				}
			}
			out.flush();
			out.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public static void appendData(String fileName,String content){
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileName,true), "UTF-8"));
			bw.newLine();
			bw.write(content);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
