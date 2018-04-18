package com.cntest.fxpt.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.common.page.Page;
import com.cntest.fxpt.anlaysis.uitl.NumberFormat;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.repository.IDownLoadReportDao;
import com.cntest.fxpt.service.IDownLoadReportService;

@Service("IDownLoadReportService")
public class DownLoadReportServiceImpl implements IDownLoadReportService {
	private static final Logger loggr = LoggerFactory
			.getLogger(DownLoadReportServiceImpl.class);
	@Autowired(required = false)
	@Qualifier("IDownLoadReportDao")
	private IDownLoadReportDao iDownLoadReportDao;

	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	DocumentBuilder builder = null;

	public DownLoadReportServiceImpl() {
	}

	@Override
	public Exam findById(Long examId) {
		return iDownLoadReportDao.findById(examId);
	}

	@Override
	public List<Exam> list(Page<Exam> page) {
		return iDownLoadReportDao.list(page);
	}

	@Override
	public String[] findExamNations(Exam exam) {
		String[] nations = iDownLoadReportDao.findExamNations(exam);
		if (nations.length == 1
				&& (nations[0] == null || "null".equals(nations[0]
						.toLowerCase())))
			return null;
		return nations;
	}

	@Override
	public Map<String, List<?>> produceHoleData(Long examId) {
		Map<String, List<?>> nations = iDownLoadReportDao.produceHoleData(examId);
		return nations;
	}
	@Override
	public Workbook examTemplateCreateXlsx(Map<String, List<?>> holeSheetDataMap,File file,
			InputStream in_knowledgeAnalysis,InputStream in_subjectiveObjectiveAnalysis){
		Workbook workbook = new SXSSFWorkbook();
		Sheet sheet = workbook.createSheet("知识点统计");
		int rowIndex = 0;// 行下标
		short colShift = 0;// 列下标
		/** 表头 **/
		Row headerRow = sheet.createRow(rowIndex);
		buildHeader(headerRow, colShift, in_knowledgeAnalysis);
		rowIndex++;
		List<?> knowledgeData = holeSheetDataMap.get("knowledgeData") == null ? new ArrayList<>()
				: holeSheetDataMap.get("knowledgeData");
		for (int i = 0; i < knowledgeData.size(); i++) {
			Row row = sheet.createRow(rowIndex++);
			createRow((List<?> )knowledgeData.get(i), row);
		}
		return workbook;
	}
	
	
	private void createRow(List<?> knowledgeList,Row row){
		for (int i = 0; i < knowledgeList.size(); i++) {
			Cell codeValCell = row.createCell(i);
			codeValCell.setCellValue(knowledgeList.get(i).toString());
		}
	}
	
	private void buildHeader(Row headerRow, short colShift,InputStream in_knowledgeAnalysis){
		List<String> headerlist = readXml(in_knowledgeAnalysis);
		if(headerlist!=null){
			for (int i = 0; i < headerlist.size(); i++) {
				Cell unitNameCell = headerRow.createCell(i);
				unitNameCell.setCellValue(headerlist.get(i));
			}
		}
	}
	

	@Override
	public boolean examTemplateCreate(Map<String, List<?>> holeSheetDataMap,
			File file, InputStream in_province, InputStream in_city,
			InputStream in_area, InputStream in_score,
			InputStream in_totalScore, InputStream in_singleScore,
			InputStream in_singleTotalScore,InputStream in_knowledgeAnalysis,
			InputStream in_subjectiveObjectiveAnalysis,Long examId) {
		try {
			// 打开文件 生成多个工作表
			WritableWorkbook book = jxl.Workbook.createWorkbook(file);
			WritableSheet sheet = book.createSheet("全疆及下属各地州", 0);
			WritableSheet sheet1 = book.createSheet("全地州及下属各县", 1);
			WritableSheet sheet2 = book.createSheet("全县及下属各学校", 2);
			WritableSheet sheet3 = book.createSheet("总分（非累计）", 3);
			WritableSheet sheet4 = book.createSheet("总分（累计）", 4);
			WritableSheet sheet5 = book.createSheet("单科（非累计）", 5);
			WritableSheet sheet6 = book.createSheet("单科（累计）", 6);
//			WritableSheet sheet7 = book.createSheet("知识点统计", 7);
			WritableSheet sheet8 = book.createSheet("主客观统计", 8);

			// 设置excel表头样式
			WritableFont color = new WritableFont(WritableFont.ARIAL, 10,
					WritableFont.BOLD);
			color.setColour(Colour.BLUE2);
			color.setBoldStyle(WritableFont.BOLD);
			WritableCellFormat colorFormat = new WritableCellFormat(color);
			colorFormat.setAlignment(Alignment.CENTRE);

			// 构建数据
			List<?> provicedata = holeSheetDataMap.get("provicedata") == null ? new ArrayList<>()
					: holeSheetDataMap.get("provicedata");
			List<?> citydata = holeSheetDataMap.get("citydata") == null ? new ArrayList<>()
					: holeSheetDataMap.get("citydata");
			;
			List<?> areadata = holeSheetDataMap.get("areadata") == null ? new ArrayList<>()
					: holeSheetDataMap.get("areadata");
			;
			List<?> scoredata = holeSheetDataMap.get("scoredata") == null ? new ArrayList<>()
					: holeSheetDataMap.get("scoredata");
			;
			List<?> totalScoredata = holeSheetDataMap.get("totalScoredata") == null ? new ArrayList<>()
					: holeSheetDataMap.get("totalScoredata");
			;
			List<?> singleScoredata = holeSheetDataMap.get("singleScoredata") == null ? new ArrayList<>()
					: holeSheetDataMap.get("singleScoredata");
			;
			List<?> singleTotalScoredata = holeSheetDataMap.get("singleTotalScoredata") == null ? new ArrayList<>()
					: holeSheetDataMap.get("singleTotalScoredata");
			
//			List<?> knowledgeData = holeSheetDataMap.get("knowledgeData") == null ? new ArrayList<>()
//					: holeSheetDataMap.get("knowledgeData");
			
			List<?> subjectiveObjectiveData = holeSheetDataMap.get("subjectiveObjectiveData") == null ? new ArrayList<>()
					: holeSheetDataMap.get("subjectiveObjectiveData");

			//分数段
			Map<String, List<Map<String, Integer>>> scoreSegments = iDownLoadReportDao.gettotalscoreSegment(examId);
			
			// 填充数据
			provinceTemplate(colorFormat, sheet, readXml(in_province),
					provicedata);
			cityTemplate(colorFormat, sheet1, readXml(in_city), citydata,
					"city");
			cityTemplate(colorFormat, sheet2, readXml(in_area), areadata, "");
			totalScoreTemplate(colorFormat, sheet3, readXml(in_score),
					scoredata,scoreSegments.get("unaccumulation"));
			totalScoreTemplate(colorFormat, sheet4, readXml(in_totalScore),
					totalScoredata,scoreSegments.get("accumulation"));
			singleScoreTemplate(examId,colorFormat, sheet5, readXml(in_singleScore),
					singleScoredata,scoreSegments.get("unaccumulation"),false);
			singleScoreTemplate(examId,colorFormat, sheet6,
					readXml(in_singleTotalScore), singleTotalScoredata,scoreSegments.get("accumulation"),true);
//			knowledgeTemplate(colorFormat, sheet7,
//					readXml(in_knowledgeAnalysis), knowledgeData);
			subjectiveObjectiveTemplate(colorFormat, sheet8,
					readXml(in_subjectiveObjectiveAnalysis), subjectiveObjectiveData);

			// 写入数据并关闭文2件
			book.write();
			book.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	/**
	 * @description 全疆及下属各地州的 sheet构造
	 * @param colorFormat
	 *            表头样式
	 * @param sheet
	 *            工作区
	 * @param rowFeildList
	 *            表头数据
	 * @param provicedata
	 *            数据集
	 */
	private void provinceTemplate(WritableCellFormat colorFormat,
			WritableSheet sheet, List<String> rowFeildList, List<?> provicedata) {
		try {
			int idx = 0;
			if (rowFeildList != null) {
				// 构建表头
				for (String str : rowFeildList) {
					Label label = new Label(idx++, 0, str, colorFormat);
					sheet.addCell(label);
				}
				for (int i = 0; i < provicedata.size(); i++) {
					Map<?, ?> map = (Map<?, ?>) provicedata.get(i);
					String rowColor = map.get("provinceId") == null ? "0" : map
							.get("provinceId").toString();
					Label label1 = new Label(0, i + 1,
							map.get("languageType") == null ? "0" : map.get(
									"languageType").toString(),
							getTextFormat(rowColor));
					Label label2 = new Label(1, i + 1,
							map.get("testpaperName") == null ? "0" : map.get(
									"testpaperName").toString(),
							getTextFormat(rowColor));
					Label label3 = new Label(2, i + 1,
							map.get("provinceName") == null ? "0" : map.get(
									"provinceName").toString(),
							getTextFormat(rowColor));
					Label label4 = new Label(3, i + 1,
							map.get("skrs") == null ? "0" : map.get("skrs")
									.toString(), getTextFormat(rowColor));
					Label label5 = new Label(4, i + 1,
							map.get("yxrs") == null ? "0" : map.get("yxrs")
									.toString(), getTextFormat(rowColor));
					Label label6 = new Label(5, i + 1,
							map.get("yxl") == null ? "0" : map.get("yxl")
									.toString(), getTextFormat(rowColor));
					Label label7 = new Label(6, i + 1,
							map.get("lhrs") == null ? "0" : map.get("lhrs")
									.toString(), getTextFormat(rowColor));
					Label label8 = new Label(7, i + 1,
							map.get("lhl") == null ? "0" : map.get("lhl")
									.toString(), getTextFormat(rowColor));
					Label label9 = new Label(8, i + 1,
							map.get("jgrs") == null ? "0" : map.get("jgrs")
									.toString(), getTextFormat(rowColor));
					Label label10 = new Label(9, i + 1,
							map.get("jgl") == null ? "0" : map.get("jgl")
									.toString(), getTextFormat(rowColor));
					Label label11 = new Label(10, i + 1,
							map.get("jbhgrs") == null ? "0" : map.get("jbhgrs")
									.toString(), getTextFormat(rowColor));
					Label label12 = new Label(11, i + 1,
							map.get("jbhgl") == null ? "0" : map.get("jbhgl")
									.toString(), getTextFormat(rowColor));
					Label label13 = new Label(12, i + 1,
							map.get("dfrs") == null ? "0" : map.get("dfrs")
									.toString(), getTextFormat(rowColor));
					Label label14 = new Label(13, i + 1,
							map.get("dfl") == null ? "0" : map.get("dfl")
									.toString(), getTextFormat(rowColor));
					Label label15 = new Label(14, i + 1,
							map.get("avgs") == null ? "0" : map.get("avgs")
									.toString(), getTextFormat(rowColor));
					Label label16 = new Label(15, i + 1,
							map.get("stds") == null ? "0" : map.get("stds")
									.toString(), getTextFormat(rowColor));
					Label label17 = new Label(16, i + 1,
							map.get("cyxs") == null ? "0" : map.get("cyxs")
									.toString(), getTextFormat(rowColor));
					Label label18 = new Label(17, i + 1,
							map.get("maxs") == null ? "" : NumberFormat.clearZero(map.get("maxs")
									.toString()), getTextFormat(rowColor));
					Label label19 = new Label(18, i + 1,
							map.get("mins") == null ? "" : NumberFormat.clearZero(map.get("mins")
									.toString()), getTextFormat(rowColor));
					Label label20 = new Label(19, i + 1,
							map.get("qj") == null ? "" : NumberFormat.clearZero(map.get("qj")
									.toString()), getTextFormat(rowColor));
					Label label21 = new Label(20, i + 1,
							map.get("medians") == null ? "" : NumberFormat.clearZero(map
									.get("medians").toString()),
							getTextFormat(rowColor));
					Label label22 = new Label(21, i + 1,
							map.get("modes") == null ? "" : map.get("modes")
									.toString(), getTextFormat(rowColor));
					Label label23 = new Label(22, i + 1,
							map.get("kurtosis") == null ? "" : map.get(
									"kurtosis").toString(),
							getTextFormat(rowColor));
					Label label24 = new Label(23, i + 1,
							map.get("skewness") == null ? "" : map.get(
									"skewness").toString(),
							getTextFormat(rowColor));
					Label label25 = new Label(24, i + 1,
							map.get("quartile25") == null ? "" : NumberFormat.clearZero(map.get(
									"quartile25").toString()),
							getTextFormat(rowColor));
					Label label26 = new Label(25, i + 1,
							map.get("quartile50") == null ? "" : NumberFormat.clearZero(map.get(
									"quartile50").toString()),
							getTextFormat(rowColor));
					Label label27 = new Label(26, i + 1,
							map.get("quartile75") == null ? "" : NumberFormat.clearZero(map.get(
									"quartile75").toString()),
							getTextFormat(rowColor));
					sheet.addCell(label1);
					sheet.addCell(label2);
					sheet.addCell(label3);
					sheet.addCell(label4);
					sheet.addCell(label5);
					sheet.addCell(label6);
					sheet.addCell(label7);
					sheet.addCell(label8);
					sheet.addCell(label9);
					sheet.addCell(label10);
					sheet.addCell(label11);
					sheet.addCell(label12);
					sheet.addCell(label13);
					sheet.addCell(label14);
					sheet.addCell(label15);
					sheet.addCell(label16);
					sheet.addCell(label17);
					sheet.addCell(label18);
					sheet.addCell(label19);
					sheet.addCell(label20);
					sheet.addCell(label21);
					sheet.addCell(label22);
					sheet.addCell(label23);
					sheet.addCell(label24);
					sheet.addCell(label25);
					sheet.addCell(label26);
					sheet.addCell(label27);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @description 全地州及下属各县的 sheet构造
	 * @param colorFormat
	 *            表头样式
	 * @param sheet
	 *            工作区
	 * @param rowFeildList
	 *            表头数据
	 * @param provicedata
	 *            数据集
	 */
	private void cityTemplate(WritableCellFormat colorFormat,
			WritableSheet sheet, List<String> rowFeildList,
			List<?> provicedata, String cityOrArea) {
		try {
			int idx = 0;
			if (rowFeildList != null) {
				// 构建表头
				for (String str : rowFeildList) {
					Label label = new Label(idx++, 0, str, colorFormat);
					sheet.addCell(label);
				}
				for (int i = 0; i < provicedata.size(); i++) {
					Map<?, ?> map = (Map<?, ?>) provicedata.get(i);
					String rowColor = map.get("org_type") == null ? "" : map
							.get("org_type").toString();
					Label label1 = new Label(
							0,
							i + 1,
							map.get("languageType") == null ? "0" : map.get(
									"languageType").toString(),
							"city".equals(cityOrArea) ? getTextFormat1(rowColor)
									: getTextFormat3(rowColor));
					Label label2 = new Label(
							1,
							i + 1,
							map.get("testpaperName") == null ? "0" : map.get(
									"testpaperName").toString(),
							"city".equals(cityOrArea) ? getTextFormat1(rowColor)
									: getTextFormat3(rowColor));
					Label label26 = new Label(
							2,
							i + 1,
							map.get("dz") == null ? "" : map.get("dz")
									.toString(),
							"city".equals(cityOrArea) ? getTextFormat1(rowColor)
									: getTextFormat3(rowColor));
					Label label3 = new Label(
							3,
							i + 1,
							map.get("provinceName") == null ? "0" : map.get(
									"provinceName").toString(),
							"city".equals(cityOrArea) ? getTextFormat1(rowColor)
									: getTextFormat3(rowColor));
					Label label4 = new Label(
							4,
							i + 1,
							map.get("skrs") == null ? "0" : map.get("skrs")
									.toString(),
							"city".equals(cityOrArea) ? getTextFormat1(rowColor)
									: getTextFormat3(rowColor));
					Label label5 = new Label(
							5,
							i + 1,
							map.get("yxrs") == null ? "0" : map.get("yxrs")
									.toString(),
							"city".equals(cityOrArea) ? getTextFormat1(rowColor)
									: getTextFormat3(rowColor));
					Label label6 = new Label(
							6,
							i + 1,
							map.get("yxl") == null ? "0" : map.get("yxl")
									.toString(),
							"city".equals(cityOrArea) ? getTextFormat1(rowColor)
									: getTextFormat3(rowColor));
					Label label7 = new Label(
							7,
							i + 1,
							map.get("lhrs") == null ? "0" : map.get("lhrs")
									.toString(),
							"city".equals(cityOrArea) ? getTextFormat1(rowColor)
									: getTextFormat3(rowColor));
					Label label8 = new Label(
							8,
							i + 1,
							map.get("lhl") == null ? "0" : map.get("lhl")
									.toString(),
							"city".equals(cityOrArea) ? getTextFormat1(rowColor)
									: getTextFormat3(rowColor));
					Label label9 = new Label(
							9,
							i + 1,
							map.get("jgrs") == null ? "0" : map.get("jgrs")
									.toString(),
							"city".equals(cityOrArea) ? getTextFormat1(rowColor)
									: getTextFormat3(rowColor));
					Label label10 = new Label(
							10,
							i + 1,
							map.get("jgl") == null ? "0" : map.get("jgl")
									.toString(),
							"city".equals(cityOrArea) ? getTextFormat1(rowColor)
									: getTextFormat3(rowColor));
					Label label11 = new Label(
							11,
							i + 1,
							map.get("jbhgrs") == null ? "0" : map.get("jbhgrs")
									.toString(),
									"city".equals(cityOrArea) ? getTextFormat1(rowColor)
											: getTextFormat3(rowColor));
					Label label12 = new Label(
							12,
							i + 1,
							map.get("jbhgl") == null ? "0" : map.get("jbhgl")
									.toString(),
									"city".equals(cityOrArea) ? getTextFormat1(rowColor)
											: getTextFormat3(rowColor));
					Label label13 = new Label(
							13,
							i + 1,
							map.get("dfrs") == null ? "0" : map.get("dfrs")
									.toString(),
							"city".equals(cityOrArea) ? getTextFormat1(rowColor)
									: getTextFormat3(rowColor));
					Label label14 = new Label(
							14,
							i + 1,
							map.get("dfl") == null ? "0" : map.get("dfl")
									.toString(),
							"city".equals(cityOrArea) ? getTextFormat1(rowColor)
									: getTextFormat3(rowColor));
					Label label15 = new Label(
							15,
							i + 1,
							map.get("avgs") == null ? "0" : map.get("avgs")
									.toString(),
							"city".equals(cityOrArea) ? getTextFormat1(rowColor)
									: getTextFormat3(rowColor));
					Label label16 = new Label(
							16,
							i + 1,
							map.get("stds") == null ? "0" : map.get("stds")
									.toString(),
							"city".equals(cityOrArea) ? getTextFormat1(rowColor)
									: getTextFormat3(rowColor));
					Label label17 = new Label(
							17,
							i + 1,
							map.get("cyxs") == null ? "0" : map.get("cyxs")
									.toString(),
							"city".equals(cityOrArea) ? getTextFormat1(rowColor)
									: getTextFormat3(rowColor));
					Label label18 = new Label(
							18,
							i + 1,
							map.get("maxs") == null ? "" : NumberFormat.clearZero(map.get("maxs")
									.toString()),
							"city".equals(cityOrArea) ? getTextFormat1(rowColor)
									: getTextFormat3(rowColor));
					Label label19 = new Label(
							19,
							i + 1,
							map.get("mins") == null ? "" : NumberFormat.clearZero(map.get("mins")
									.toString()),
							"city".equals(cityOrArea) ? getTextFormat1(rowColor)
									: getTextFormat3(rowColor));
					Label label20 = new Label(
							20,
							i + 1,
							map.get("qj") == null ? "" : NumberFormat.clearZero(map.get("qj")
									.toString()),
							"city".equals(cityOrArea) ? getTextFormat1(rowColor)
									: getTextFormat3(rowColor));
					Label label21 = new Label(
							21,
							i + 1,
							map.get("medians") == null ? "" : NumberFormat.clearZero(map
									.get("medians").toString()),
							"city".equals(cityOrArea) ? getTextFormat1(rowColor)
									: getTextFormat3(rowColor));
					Label label22 = new Label(
							22,
							i + 1,
							map.get("modes") == null ? "" : map.get("modes")
									.toString(),
							"city".equals(cityOrArea) ? getTextFormat1(rowColor)
									: getTextFormat3(rowColor));
					Label label23 = new Label(
							23,
							i + 1,
							map.get("kurtosis") == null ? "" : map.get(
									"kurtosis").toString(),
							"city".equals(cityOrArea) ? getTextFormat1(rowColor)
									: getTextFormat3(rowColor));
					Label label24 = new Label(
							24,
							i + 1,
							map.get("skewness") == null ? "" : map.get(
									"skewness").toString(),
							"city".equals(cityOrArea) ? getTextFormat1(rowColor)
									: getTextFormat3(rowColor));
					Label label25 = new Label(
							25,
							i + 1,
							map.get("quartile25") == null ? "" : NumberFormat.clearZero(map.get(
									"quartile25").toString()),
							"city".equals(cityOrArea) ? getTextFormat1(rowColor)
									: getTextFormat3(rowColor));
					Label label261 = new Label(
							26,
							i + 1,
							map.get("quartile50") == null ? "" : NumberFormat.clearZero(map.get(
									"quartile50").toString()),
							"city".equals(cityOrArea) ? getTextFormat1(rowColor)
									: getTextFormat3(rowColor));
					Label label27 = new Label(
							27,
							i + 1,
							map.get("quartile75") == null ? "" : NumberFormat.clearZero(map.get(
									"quartile75").toString()),
							"city".equals(cityOrArea) ? getTextFormat1(rowColor)
									: getTextFormat3(rowColor));
					sheet.addCell(label1);
					sheet.addCell(label2);
					sheet.addCell(label26);
					sheet.addCell(label3);
					sheet.addCell(label4);
					sheet.addCell(label5);
					sheet.addCell(label6);
					sheet.addCell(label7);
					sheet.addCell(label8);
					sheet.addCell(label9);
					sheet.addCell(label10);
					sheet.addCell(label11);
					sheet.addCell(label12);
					sheet.addCell(label13);
					sheet.addCell(label14);
					sheet.addCell(label15);
					sheet.addCell(label16);
					sheet.addCell(label17);
					sheet.addCell(label18);
					sheet.addCell(label19);
					sheet.addCell(label20);
					sheet.addCell(label21);
					sheet.addCell(label22);
					sheet.addCell(label23);
					sheet.addCell(label24);
					sheet.addCell(label25);
					sheet.addCell(label261);
					sheet.addCell(label27);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void totalScoreTemplate(WritableCellFormat colorFormat,
			WritableSheet sheet, List<String> rowFeildList, List<?> data,List<Map<String, Integer>> scoreSegment) {
		try {
			int idx = 0;
			if (rowFeildList != null) {
				// 构建表头
				for (String str : rowFeildList) {
					Label label = new Label(idx++, 0, str, colorFormat);
					sheet.addCell(label);
				}
				for (int i = 0; i < scoreSegment.size(); i++) {
					int Startscore = scoreSegment.get(i).get("Startscore");
					int Endscore = scoreSegment.get(i).get("Endscore");
					
					Label label = new Label(idx++, 0,Endscore+"-"+Startscore,colorFormat);
					sheet.addCell(label);
				}
				
				for (int i = 0; i < data.size(); i++) {
					Map<?, ?> map = (Map<?, ?>) data.get(i);
					String rowColor = map.get("countyId") == null ? "" : map
							.get("countyId").toString();
					Label label1 = new Label(0, i + 1,
							map.get("languageType") == null ? "0" : map.get(
									"languageType").toString(),
							getTextFormat1(rowColor));
					Label label2 = new Label(1, i + 1,
							map.get("testpaperName") == null ? "0" : map.get(
									"testpaperName").toString(),
							getTextFormat1(rowColor));
					Label label3 = new Label(2, i + 1,
							map.get("fullscore") == null ? "0" : NumberFormat.clearZero(map.get(
									"fullscore").toString()),
							getTextFormat1(rowColor));
					Label label4 = new Label(3, i + 1,
							map.get("cityName") == null ? "" : map.get(
									"cityName").toString(),
							getTextFormat1(rowColor));
					Label label5 = new Label(4, i + 1,
							map.get("countyName") == null ? "0" : map.get(
									"countyName").toString(),
							getTextFormat1(rowColor));
					Label label6 = new Label(5, i + 1,
							map.get("skrs") == null ? "0" : map.get("skrs")
									.toString(), getTextFormat1(rowColor));
					sheet.addCell(label1);
					sheet.addCell(label2);
					sheet.addCell(label3);
					sheet.addCell(label4);
					sheet.addCell(label5);
					sheet.addCell(label6);
					
					for (int j = 0; j < scoreSegment.size(); j++) {
						int Startscore = scoreSegment.get(j).get("Startscore");
						int Endscore = scoreSegment.get(j).get("Endscore");
						
						Label label = new Label(j+6, i + 1,
								map.get(Endscore+"-"+Startscore) == null ? "0" : map.get(
										Endscore+"-"+Startscore).toString(),
										getTextFormat1(rowColor));
						sheet.addCell(label);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void singleScoreTemplate(Long examId,WritableCellFormat colorFormat,WritableSheet sheet,List<String> rowFeildList,List<?> data,List<Map<String, Integer>> scoreSegment,boolean accumulate){
		try {
			int singlemaxscore = iDownLoadReportDao.getSingleMaxScore(examId);
//			int singlemaxscore = 100;
			
			int idx = 0;
			int maxadvancescore = 0;
			if(rowFeildList!=null){
				//构建表头
				for (String str : rowFeildList) {
					Label label = new Label(idx++, 0, str, colorFormat);
					sheet.addCell(label);
				}
				
				boolean flag = false;
				for (int i = 0; i < scoreSegment.size(); i++) {
					int Startscore = scoreSegment.get(i).get("Startscore");
					int Endscore = scoreSegment.get(i).get("Endscore");
					
					if(maxadvancescore< Startscore){
						maxadvancescore = Startscore;
					}
					
					
					if(Endscore == (singlemaxscore - 10)){
						flag = true;
					}
					if(flag){
						Label label = new Label(idx++, 0,Endscore+"-"+(accumulate?singlemaxscore:Startscore),colorFormat);
						sheet.addCell(label);
					}
				}
				
				for (int i = 0; i < data.size(); i++) {
					Map<?, ?> map = (Map<?, ?>) data.get(i);
					String rowColor = map.get("countyId")==null?"":map.get("countyId").toString();
					Label label1 = new Label(0,i+1, map.get("languageType")==null?"0":map.get("languageType").toString(), getTextFormat1(rowColor));
					Label label2 = new Label(1,i+1, map.get("testpaperName")==null?"0":map.get("testpaperName").toString(), getTextFormat1(rowColor));
					int fullScore = map.get("fullscore")==null?0:(int)Double.parseDouble(map.get("fullscore").toString());
					Label label3 = new Label(2,i+1, fullScore+"", getTextFormat1(rowColor));
					Label label4 = new Label(3,i+1,map.get("cityName")==null?"":map.get("cityName").toString(), getTextFormat1(rowColor));
					Label label5 = new Label(4,i+1, map.get("countyName")==null?"0":map.get("countyName").toString(), getTextFormat1(rowColor));
					Label label6 = new Label(5,i+1, map.get("skrs")==null?"0":map.get("skrs").toString(), getTextFormat1(rowColor));
					sheet.addCell(label1);
					sheet.addCell(label2);
					sheet.addCell(label3);
					sheet.addCell(label4);
					sheet.addCell(label5);
					sheet.addCell(label6);
					
					int startscore = singlemaxscore-10;
					int endscore = singlemaxscore;
					int advancescore = singlemaxscore+10;
					if(!accumulate){
						int pScore1 = 0;
						int pScore2 = 0;
						if(map.get(startscore+"-"+endscore)!=null){
							pScore1 = Integer.parseInt(map.get(startscore+"-"+endscore).toString());
						}
						if(map.get(endscore+"-"+advancescore)!=null){
							pScore2 = Integer.parseInt(map.get(endscore+"-"+advancescore).toString()); 
						}
						
						Label label7 = null;
						if(fullScore<=startscore){
							label7 = new Label(6,i+1,(""), getTextFormat2(rowColor));
						}else{
							label7 = new Label(6,i+1,(pScore1 + pScore2 + ""), getTextFormat1(rowColor));
						}
						sheet.addCell(label7);
						
						int circulationscore = startscore;
						
						startscore = startscore-10;
						endscore = endscore-10;
						advancescore = advancescore-10;
						
						
						for (int j = 0; j < circulationscore/10; j++) {
							Label tmplabel = null;
							if(fullScore<= startscore){
								tmplabel = new Label(7+j,i+1, "", getTextFormat2(rowColor));
							}else{
								int a = Integer.parseInt(map.get(startscore+"-"+endscore)==null?"":map.get(startscore+"-"+endscore).toString());
								int b = Integer.parseInt(map.get(endscore+"-"+advancescore)==null?"":map.get(endscore+"-"+advancescore).toString());
								if(fullScore<=endscore){
									tmplabel = new Label(7+j,i+1, a + b + "", getTextFormat1(rowColor));
								}else{
									tmplabel = new Label(7+j,i+1, a + "", getTextFormat1(rowColor));
								}
								
							} 
							
							sheet.addCell(tmplabel);
							
							startscore = startscore-10;
							endscore = endscore-10;
							advancescore = advancescore-10;
						}
					
					}else{
						int pScore1 = 0;
						int pScore2 = 0;
						if(map.get(startscore+"-"+maxadvancescore)!=null){
							pScore1 = Integer.parseInt(map.get(startscore+"-"+maxadvancescore).toString());
						}
						if(map.get(endscore+"-"+maxadvancescore)!=null){
							pScore2 = Integer.parseInt(map.get(endscore+"-"+maxadvancescore).toString()); 
						}
						
						Label label7 = null;
						if(fullScore<=startscore){
							label7 = new Label(6,i+1,(""), getTextFormat2(rowColor));
						}else{
							label7 = new Label(6,i+1,(pScore1 + pScore2 + ""), getTextFormat1(rowColor));
						}
						
						sheet.addCell(label7);
						
						int circulationscore = startscore;
						
						startscore = startscore-10;
						endscore = endscore-10;
						advancescore = advancescore-10;
						
						for (int j = 0; j < circulationscore/10; j++) {
							Label tmplabel = null;
							if(fullScore<= startscore){
								tmplabel = new Label(7+j,i+1, "", getTextFormat2(rowColor));
							}else{
								int a = Integer.parseInt(map.get(startscore+"-"+maxadvancescore)==null?"":map.get(startscore+"-"+maxadvancescore).toString());
								int b = Integer.parseInt(map.get(endscore+"-"+maxadvancescore)==null?"":map.get(endscore+"-"+maxadvancescore).toString());
								if(fullScore<=endscore){
									tmplabel = new Label(7+j,i+1, a + b + "", getTextFormat1(rowColor));
								}else{
									tmplabel = new Label(7+j,i+1, a + "", getTextFormat1(rowColor));
								}
							} 
							
							sheet.addCell(tmplabel);
							
							startscore = startscore-10;
							endscore = endscore-10;
						}
						
					}
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * @description 知识点 sheet构造
	 * @param colorFormat
	 *            表头样式
	 * @param sheet
	 *            工作区
	 * @param rowFeildList
	 *            表头数据
	 * @param provicedata
	 *            数据集
	 */
	private void knowledgeTemplate(WritableCellFormat colorFormat,
			WritableSheet sheet, List<String> rowFeildList, List<?> knowledgeData) {
		try {
			int idx = 0;
			if (rowFeildList != null) {
				// 构建表头
				for (String str : rowFeildList) {
					Label label = new Label(idx++, 0, str, colorFormat);
					sheet.addCell(label);
				}
				for (int i = 0; i < knowledgeData.size(); i++) {
					Map<?, ?> map = (Map<?, ?>) knowledgeData.get(i);
					String rowColor = map.get("provinceId") == null ? "0" : map
							.get("provinceId").toString();
					Label label1 = new Label(0, i + 1,
							map.get("languagetype") == null ? "0" : map.get(
									"languagetype").toString(),
							getTextFormat(rowColor));
					Label label2 = new Label(1, i + 1,
							map.get("testpapername") == null ? "0" : map.get(
									"testpapername").toString(),
							getTextFormat(rowColor));
					Label label3 = new Label(2, i + 1,
							map.get("provinceName") == null ? "0" : map.get(
									"provinceName").toString(),
							getTextFormat(rowColor));
					Label label4 = new Label(3, i + 1,
							map.get("NAME") == null ? "0" : map.get("NAME")
									.toString(), getTextFormat(rowColor));
					Label label5 = new Label(4, i + 1,
							map.get("itemno") == null ? "0" : map.get("itemno")
									.toString(), getTextFormat(rowColor));
					Label label6 = new Label(5, i + 1,
							map.get("titletype") == null ? "0" : map.get("titletype")
									.toString(), getTextFormat(rowColor));
					Label label7 = new Label(6, i + 1,
							map.get("fullscore") == null ? "0" : map.get("fullscore")
									.toString(), getTextFormat(rowColor));
					Label label8 = new Label(7, i + 1,
							map.get("avgs") == null ? "0" : map.get("avgs")
									.toString(), getTextFormat(rowColor));
					Label label9 = new Label(8, i + 1,
							map.get("stds") == null ? "0" : map.get("stds")
									.toString(), getTextFormat(rowColor));
					Label label10 = new Label(9, i + 1,
							map.get("dd") == null ? "0" : map.get("dd")
									.toString(), getTextFormat(rowColor));
					Label label11 = new Label(10, i + 1,
							map.get("forecastDifficulty") == null ? "0" : map.get("forecastDifficulty")
									.toString(), getTextFormat(rowColor));
					Label label12 = new Label(11, i + 1,
							map.get("difficulty") == null ? "0" : map.get("difficulty")
									.toString(), getTextFormat(rowColor));
					sheet.addCell(label1);
					sheet.addCell(label2);
					sheet.addCell(label3);
					sheet.addCell(label4);
					sheet.addCell(label5);
					sheet.addCell(label6);
					sheet.addCell(label7);
					sheet.addCell(label8);
					sheet.addCell(label9);
					sheet.addCell(label10);
					sheet.addCell(label11);
					sheet.addCell(label12);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @description 主客观sheet构造
	 * @param colorFormat
	 *            表头样式
	 * @param sheet
	 *            工作区
	 * @param rowFeildList
	 *            表头数据
	 * @param provicedata
	 *            数据集
	 */
	private void subjectiveObjectiveTemplate(WritableCellFormat colorFormat,
			WritableSheet sheet, List<String> rowFeildList, List<?> subjectiveObjectiveData) {
		try {
			int idx = 0;
			if (rowFeildList != null) {
				// 构建表头
				for (String str : rowFeildList) {
					Label label = new Label(idx++, 0, str, colorFormat);
					sheet.addCell(label);
				}
				for (int i = 0; i < subjectiveObjectiveData.size(); i++) {
					Map<?, ?> map = (Map<?, ?>) subjectiveObjectiveData.get(i);
					String rowColor = map.get("provinceId") == null ? "0" : map
							.get("provinceId").toString();
					Label label1 = new Label(0, i + 1,
							map.get("languagetype") == null ? "0" : map.get(
									"languagetype").toString(),
							getTextFormat(rowColor));
					Label label2 = new Label(1, i + 1,
							map.get("testpapername") == null ? "0" : map.get(
									"testpapername").toString(),
							getTextFormat(rowColor));
					Label label3 = new Label(2, i + 1,
							map.get("provinceName") == null ? "0" : map.get(
									"provinceName").toString(),
							getTextFormat(rowColor));
					Label label5 = new Label(3, i + 1,
							map.get("titletype") == null ? "0" : map.get("titletype")
									.toString(), getTextFormat(rowColor));
					Label label6 = new Label(4, i + 1,
							map.get("fullscore") == null ? "0" : map.get("fullscore")
									.toString(), getTextFormat(rowColor));
					Label label7 = new Label(5, i + 1,
							map.get("avgs") == null ? "0" : map.get("avgs")
									.toString(), getTextFormat(rowColor));
					Label label8 = new Label(6, i + 1,
							map.get("stds") == null ? "0" : map.get("stds")
									.toString(), getTextFormat(rowColor));
					Label label9 = new Label(7, i + 1,
							map.get("dfl") == null ? "0" : map.get("dfl")
									.toString(), getTextFormat(rowColor));
					Label label10 = new Label(8, i + 1,
							map.get("dd") == null ? "0" : map.get("dd")
									.toString(), getTextFormat(rowColor));
					Label label11 = new Label(9, i + 1,
							map.get("rd") == null ? "0" : map.get("rd")
									.toString(), getTextFormat(rowColor));
					sheet.addCell(label1);
					sheet.addCell(label2);
					sheet.addCell(label3);
					sheet.addCell(label5);
					sheet.addCell(label6);
					sheet.addCell(label7);
					sheet.addCell(label8);
					sheet.addCell(label9);
					sheet.addCell(label10);
					sheet.addCell(label11);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<String> readXml(InputStream in) {
		List<String> rowFeildList = null;
		try {
			SAXReader reader = new SAXReader();
			rowFeildList = new ArrayList<String>();
			if (in != null) {
				Document doc = reader.read(in);
				Element root = doc.getRootElement();
				List<Element> childnodes = root.elements();
				for (Element e : childnodes) {
					rowFeildList.add(e.getText());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rowFeildList;
	}

	protected WritableCellFormat getTextFormat(String colorFlag)
			throws Exception {
		// 设置excel行样式
		WritableFont color = new WritableFont(WritableFont.ARIAL, 10);
		if ("1000".equals(colorFlag)) {
			color.setColour(Colour.RED);
		}
		WritableCellFormat colorFormat = new WritableCellFormat(color);
		colorFormat.setAlignment(Alignment.CENTRE);
		colorFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		colorFormat.setBorder(jxl.format.Border.NONE,
				jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
		return colorFormat;
	}

	protected WritableCellFormat getTextFormat1(String colorFlag)
			throws Exception {
		// 设置excel行样式
		WritableFont color = new WritableFont(WritableFont.ARIAL, 10);

		if ("2".equals(colorFlag)) {
			color.setColour(Colour.RED);
		}
		if ("1".equals(colorFlag)) {
			color.setColour(Colour.RED);
		}
		WritableCellFormat colorFormat = new WritableCellFormat(color);
		colorFormat.setAlignment(Alignment.CENTRE);
		colorFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		colorFormat.setBorder(jxl.format.Border.NONE,
				jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
		return colorFormat;
	}

	protected WritableCellFormat getTextFormat3(String colorFlag)
			throws Exception {
		// 设置excel行样式
		WritableFont color = new WritableFont(WritableFont.ARIAL, 10);

		if ("3".equals(colorFlag)) {
			color.setColour(Colour.RED);
		}
		WritableCellFormat colorFormat = new WritableCellFormat(color);
		colorFormat.setAlignment(Alignment.CENTRE);
		colorFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		colorFormat.setBorder(jxl.format.Border.NONE,
				jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
		return colorFormat;
	}

	protected WritableCellFormat getTextFormat2(String colorFlag)
			throws Exception {
		// 设置excel行样式
		WritableFont color = new WritableFont(WritableFont.ARIAL, 10);
		if ("1".equals(colorFlag)) {
			color.setColour(Colour.RED);
		}
		WritableCellFormat colorFormat = new WritableCellFormat(color);
		colorFormat.setAlignment(Alignment.CENTRE);
		colorFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		colorFormat.setBorder(jxl.format.Border.NONE,
				jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
		colorFormat.setBackground(Colour.GREY_40_PERCENT);
		return colorFormat;
	}

}
