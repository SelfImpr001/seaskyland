package com.cntest.fxpt.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.common.controller.BaseController;
import com.cntest.common.page.Page;
import com.cntest.fxpt.anlaysis.event.handler.DefaultCalculateHandler;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.etl.domain.EtlProcessResult;
import com.cntest.fxpt.service.IDownLoadReportService;
import com.cntest.fxpt.service.IExamService;
import com.cntest.fxpt.util.FileFactory;
import com.cntest.fxpt.util.TitleUtil;
import com.cntest.web.view.ModelAndViewBuilder;

/**
 * 报表下载
 */
@Controller
@RequestMapping("/downloadreport")
public class DownLoadReportController extends BaseController {
	@Autowired(required = false)
	@Qualifier("IExamService")
	private IExamService examService;

	@Autowired(required = false)
	@Qualifier("IDownLoadReportService")
	private IDownLoadReportService downLoadReportService;
	
	private static final Logger log = LoggerFactory
			.getLogger(DefaultCalculateHandler.class);

	/**
	 * @description 进入主页面查询考试方法
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/list/{currentPage}/{pageSize}", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable int currentPage,
			@PathVariable int pageSize, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Page<Exam> page = newPage(currentPage, pageSize, request);
		boolean isEmpty = page.getParameter().isEmpty();
		page.addParameter("examStatus", "5");
		page.addParameter("isParamList", "true");
		List<Exam> exams = examService.list(page);
		String title = TitleUtil.getTitle(request.getServletPath());
		if (isEmpty) {
			return ModelAndViewBuilder
					.newInstanceFor("/downloadreport/examDlList")
					.append("title",title)
					.append("page", page).build();
		} else {
			return ModelAndViewBuilder
					.newInstanceFor("/downloadreport/examDlListBody")
					.append("page", page).build();
		}
	}

	/**
	 * @description 下载服务器上发不过的excel文件
	 * @param examId
	 *            考试ID
	 */
	@RequestMapping(value = "/downloadexcel/{examId}")
	public void dlexcel(@PathVariable Long examId, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long startTime = System.currentTimeMillis();
		log.debug("开始下载了~~~~~~~~~~~");
		/** 设置下载参数 */
		// 学生信息模板
		String fileName=examId + "totalscoreStatistic.xls";
		String fileName1=examId + "knowledgeStatistic.xlsx";
		
//		response.setContentType("application/excel");
//		response.setHeader("Content-Disposition", "attachment; filename="
//				+ new String(fileName.getBytes(), "ISO-8859-1"));

		String fileSaveRootPath = request.getSession().getServletContext()
					.getRealPath("/") + "dlexcel/";
		String path = findFileSavePathByFileName(fileName, fileSaveRootPath);
		String path1 = findFileSavePathByFileName(fileName1, fileSaveRootPath);
		// 生成文件成功与否的标志
		boolean completeFlag = true;
		// 得到要下载的文件
		File file = new File(path);
		File file1 = new File(path1);
		// 如果文件不存在
		if (!file.exists()&& !file1.exists() && examId!=0) {
			/** 获取表头的流数据 */
			String fileName2 = "rowField_province.xml";
			String fileName3 = "rowField_city.xml";
			String fileName4 = "rowField_area.xml";
			String fileName5 = "paragraphScore.xml";
			String fileName6 = "totalParagraphScore.xml";
			String fileName7 = "singleCurricularScore.xml";
			String fileName8 = "singleCurricularScoreTotal.xml";
			String fileName9 = "knowledgeAnalysis.xml";
			String fileName10 = "subjectiveObjectiveAnalysis.xml";
			InputStream in_province = request.getServletContext()
					.getResourceAsStream("/dlexcel/" + fileName2);
			InputStream in_city = request.getServletContext().getResourceAsStream(
					"/dlexcel/" + fileName3);
			InputStream in_area = request.getServletContext().getResourceAsStream(
					"/dlexcel/" + fileName4);
			InputStream in_score = request.getServletContext().getResourceAsStream(
					"/dlexcel/" + fileName5);
			InputStream in_totalScore = request.getServletContext().getResourceAsStream(
					"/dlexcel/" + fileName6);
			InputStream in_singleScore = request.getServletContext().getResourceAsStream(
					"/dlexcel/" + fileName7);
			InputStream in_singleTotalScore = request.getServletContext().getResourceAsStream(
					"/dlexcel/" + fileName8);
			InputStream in_knowledgeAnalysis = request.getServletContext().getResourceAsStream(
					"/dlexcel/" + fileName9);
			InputStream in_subjectiveObjectiveAnalysis = request.getServletContext().getResourceAsStream(
					"/dlexcel/" + fileName10);
			
			/** 生成新的excel文件，以供下载 */
			//生产数据
			Map<String, List<?>> holeSheetDataMap = downLoadReportService.produceHoleData(new Long(examId));
			
			//导出POI
			org.apache.poi.ss.usermodel.Workbook workbook = downLoadReportService.examTemplateCreateXlsx(holeSheetDataMap, file1, in_knowledgeAnalysis, in_subjectiveObjectiveAnalysis);
			FileOutputStream outputStream = new FileOutputStream(file1);
			workbook.write(outputStream);
			outputStream.flush();
			outputStream.close();
			
			//导出jxl
			completeFlag = downLoadReportService.examTemplateCreate(
					holeSheetDataMap, file, in_province, in_city, in_area,
					in_score, in_totalScore, in_singleScore,in_singleTotalScore,
					in_knowledgeAnalysis,in_subjectiveObjectiveAnalysis,new Long(examId));
			
			in_province.close();
			in_city.close();
			in_area.close();
			in_score.close();
			in_totalScore.close();
			in_singleScore.close();
			in_singleTotalScore.close();
			in_knowledgeAnalysis.close();
			in_subjectiveObjectiveAnalysis.close();
		}

		if (completeFlag) {
			
			List<File> fileList = new ArrayList<File>();
			fileList.add(file);
			fileList.add(file1);
			FileFactory.zipFiles(fileList, "新疆报告"+FileFactory.getNowDate(),response,false);
//			//读取要下载的文件，保存到文件输入流
//			FileInputStream in = new FileInputStream(path);
//			// 创建输出流
//			ServletOutputStream out = response.getOutputStream();
//			// 创建缓冲区
//			byte buffer[] = new byte[1024];
//			int len = 0;
//			// 循环将输入流中的内容读取到缓冲区当中
//			try {
//				while ((len = in.read(buffer)) != -1) {
//					// 输出缓冲区的内容到浏览器，实现文件下载
//					out.write(buffer, 0, len);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			// 关闭文件输入流
//			in.close();
//			// 关闭输出流
//			out.flush();
//			out.close();
		}

	}

	/**
	 * @description 下载服务器上发不过的excel(组织架构)文件
	 */
	@RequestMapping(value = "/downloadexcelForZZJG/{examId}")
	public void dlexcelzzjg(@PathVariable Long examId, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		log.debug("开始下载了~~~~~~~~~~~");
		/** 设置下载参数 */
		String fileName="zzjg.xls";
		
		response.setContentType("application/excel");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ new String(fileName.getBytes(), "ISO-8859-1"));

		String path = request.getSession().getServletContext()
				.getRealPath("/") + "WEB-INF/upload/zzjg.xls";
		//String path = findFileSavePathByFileName(fileName, fileSaveRootPath);
		// 得到要下载的文件
		File file = new File(path);
		if (file.exists()) {
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
	/**
	 * @Method: findFileSavePathByFileName
	 * @Description: 通过文件名和存储上传文件根目录找出要下载的文件的所在路径
	 * @param filename
	 *            要下载的文件名
	 * @param saveRootPath
	 *            上传文件保存的根目录
	 * @return 要下载的文件的存储目录
	 * @throws Exception
	 */
	public String findFileSavePathByFileName(String filename,
			String saveRootPath) throws Exception {
		File file = new File(saveRootPath);
		if (!file.exists()) {
			// 创建目录
			file.mkdirs();
		}
		String dir = saveRootPath  + filename;
		return dir;
	}
	
	
	@RequestMapping(value = "/dele/{examId}")
	public ModelAndView delete(@PathVariable Long examId, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		String fileName=examId + "totalscoreStatistic.xls";
		String fileName1=examId + "knowledgeStatistic.xlsx";
		

		String fileSaveRootPath = request.getSession().getServletContext()
					.getRealPath("/") + "dlexcel/";
		String xls = findFileSavePathByFileName(fileName, fileSaveRootPath);
		String xls1 = findFileSavePathByFileName(fileName1, fileSaveRootPath);
		

			File file = new File(xls);
			File file1 = new File(xls1);
			EtlProcessResult processResult = new EtlProcessResult();
			try{
				if(file.exists()){
					//删除xlsx文件
					file.delete();
				}
				if(file1.exists()){
					//删除xlsx文件
					file1.delete();
				}
				processResult.setHasError(false);
				processResult.setMessage("重置成功，可重新下载报表！");
			}catch(Exception e) {
				processResult.setHasError(true);
				processResult.setMessage("删除失败"+e);
				e.printStackTrace();
			}
			return ModelAndViewBuilder
					.newInstanceFor("/error/processResult")
					.append("processResult", processResult).build();
	}
	
	/**
	 * 使用POI导出下载EXCEL
	 * @param filename 导出文件名称
	 * @param os
	 * @param response
	 * @param startTime  开始时间
	 */
	public static void downloadPoiExcel(String filename, ByteArrayOutputStream os, HttpServletResponse response, Long startTime){
		if(startTime == null)
			startTime = System.currentTimeMillis();
		ByteArrayInputStream byin = new ByteArrayInputStream(os.toByteArray());
 		response.setHeader("Pragma", "public");
		response.setHeader("Cache-Control", "max-age=30");
		response.setContentType("application/x-download;charset=UTF-8"); // 下载文件类型
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			response.setHeader("Content-Disposition", "attachment;filename="+new String(("3.xlsx").getBytes(), "iso-8859-1"));
			ServletOutputStream out = response.getOutputStream();
			bis = new BufferedInputStream(byin);
			bos = new BufferedOutputStream(out);
			
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				bos.close();
				bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("共用时间"+(System.currentTimeMillis() - startTime)/1000 +"秒");
	}
	
	
}
