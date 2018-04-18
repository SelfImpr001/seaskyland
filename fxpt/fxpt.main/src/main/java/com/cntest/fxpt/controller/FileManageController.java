package com.cntest.fxpt.controller;

import java.io.FileInputStream;

import javax.servlet.ServletOutputStream;
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

import com.cntest.common.controller.BaseController;
import com.cntest.fxpt.domain.FileManage;
import com.cntest.fxpt.service.IFileManageService;


@Controller
@RequestMapping("/file")
public class FileManageController extends BaseController {
	private static Logger logger = LoggerFactory
			.getLogger(FileManageController.class);
	@Autowired(required = false)
	@Qualifier("IFileManageService")
	private IFileManageService fileManageService;

	
	//原始文件管理-----》文件下载
	@RequestMapping(value = "/downLoad/{fileId}", method = RequestMethod.POST)
	public void list(@PathVariable long fileId,HttpServletRequest request,
			HttpServletResponse response)
			throws Exception {
			FileManage fileManage =fileManageService.findFileByFileId(fileId);
			if(fileManage!=null) {
				//获取文件路径
				String path = fileManage.getFilePath();
				String fileName =fileManage.getFileName();
				response.setContentType("application/excel");
				response.setHeader("Content-Disposition", "attachment; filename="
						+ new String(fileName.getBytes(), "ISO-8859-1"));

				//读取要下载的文件，保存到文件输入流
				FileInputStream in = new FileInputStream(path);
				// 创建输出流
				ServletOutputStream out = response.getOutputStream();
				// 创建缓冲区
				byte buffer[] = new byte[1024];
				int len = 0;
				// 循环将输入流中的内容读取到缓冲区当中
				try {
					while ((len = in.read(buffer)) != -1) {
						// 输出缓冲区的内容到浏览器，实现文件下载
						out.write(buffer, 0, len);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 关闭文件输入流
				in.close();
				// 关闭输出流
				out.flush();
				out.close();
			}
		}
	private String getParentFilePath(HttpServletRequest request) {
		String filePath=request.getSession().getServletContext().getRealPath("");
		int last =filePath.lastIndexOf("/");
		if(last==-1) {
			last=filePath.lastIndexOf("\\");
		}
		return filePath=filePath.substring(0, last);
	}
	
	 }
