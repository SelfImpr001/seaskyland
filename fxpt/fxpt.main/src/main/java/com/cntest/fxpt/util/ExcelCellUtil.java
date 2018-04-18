package com.cntest.fxpt.util;

import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.util.CellRangeAddressList;

import com.cntest.fxpt.bean.DataField;



/**
 * 导出Excel单元格样式设置
 * @author LM
 *
 */
public class ExcelCellUtil {
	
	private static String TEXT_FORMAT = "@";  //文本样式
	private static String DATE_FORMAT = "yyyy-mm-dd";  //日期样式
	private static String DECI_FORMAT = "0.00";  //小数样式
	
	private static int LAST_ROW = 1000;  //文本样式
	
	private static HSSFCellStyle getWBStyle(HSSFSheet sheet){
		return sheet.getWorkbook().createCellStyle();
	}
	
	private static HSSFDataFormat getWBFormat(HSSFSheet sheet){
		return sheet.getWorkbook().createDataFormat();
	}
	
	private static HSSFFont getWBFont(HSSFSheet sheet){
		return sheet.getWorkbook().createFont();
	}
	
	//cell格式
	public static HSSFCellStyle getTextStyle(HSSFCell cell, Object value){
		HSSFCellStyle cellStyle = getWBStyle(cell.getSheet());
		HSSFDataFormat dataFormat = getWBFormat(cell.getSheet());
		
		if(value instanceof String){
			cellStyle.setDataFormat(dataFormat.getFormat(TEXT_FORMAT));
		}else if(value instanceof Date){
			cellStyle.setDataFormat(dataFormat.getFormat(DATE_FORMAT));
		}else if(value instanceof Double){ 
			cellStyle.setDataFormat(dataFormat.getFormat(DECI_FORMAT));
		}
		
		return cellStyle;
	}
	
	/**
	 * 设置锁定列
	 * @param sheet 需要设置的工作表
	 * @param colSplit 要冻结的列数
	 * @param rowSplit 要冻结的行数
	 * @param leftmostCol 左边区域可见的首列序号，从1开始计算
	 * @param topRow 下边区域可见的首行序号，从1开始计算
	 */
	public static void setFreezePane(HSSFSheet sheet, int colSplit, int rowSplit, int leftmostCol, int topRow){
		sheet.createFreezePane(colSplit, rowSplit, leftmostCol, topRow);
	}
	
	/**
	 * 设置单元格批注
	 * @param sheet 需要设置的工作表
	 * @param firstRow 起始行
	 * @param lastRow  终止行
	 * @param firstCol 起始列
	 * @param lastCol  中之列
	 * @param annotate 批注文
	 */
	public static void setAnnotateText(HSSFSheet sheet, int firstRow, int lastRow, int firstCol, int lastCol, String annotate){
		HSSFFont font = getWBFont(sheet);
		font.setColor(HSSFColor.RED.index); //设置颜色
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //加粗
		
		HSSFCellStyle style = sheet.getWorkbook().createCellStyle();
		style.setFont(font);
		style.setLocked(true); //锁定
		style.setAlignment(HSSFCellStyle.ALIGN_LEFT); //局左
		
		HSSFCell cell = sheet.createRow(firstRow).createCell(firstCol);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(annotate);
		cell.setCellStyle(style);
		//合并
		sheet.addMergedRegion(mergeRow(firstRow, lastRow, firstCol, lastCol)); 
	}
	
	@SuppressWarnings("deprecation")
	private static CellRangeAddress mergeRow(int firstRow, int lastRow, int firstCol, int lastCol){
		return new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
	}
	
	/**
	 * 设置工作表表头信息
	 * @param sheet 需要设置的工作表
	 * @param headerNum 设置的表头所在行
	 * @param headers 表头字段名称
	 */
	public static void setHeaderValue(HSSFSheet sheet, int headerNum, String[] headers){
		HSSFFont font = getWBFont(sheet);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //加粗
		
		HSSFCellStyle style = sheet.getWorkbook().createCellStyle();
		style.setFont(font);
		style.setLocked(true); //锁定
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); //字体居中
		style.setWrapText(true);//设置自动换行
		
		sheet.setColumnWidth(0, headers.length*3000); //设置第一列说明文字的列宽
		sheet.addMergedRegion(new Region((short)0,(short)0,(short)0,(short) ((short)(headers.length)-1)));//合并第一行单元
		
		HSSFRow row = sheet.createRow(headerNum);
		HSSFCell cell = null;
		
		for (int i = 0; i < headers.length; i++) {
			cell = row.createCell(i);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(headers[i]);
			cell.setCellStyle(style);
			/*sheet.autoSizeColumn(i);*/
			sheet.setColumnWidth(i, headers[i].getBytes().length*500+10); //设置列宽
		}
	}
	/**
	 * 设置说明文字样式
	 * @param sheet  需要设置的工作表
	 * @param headerNum 设置的表头所在行
	 */
	
	public static void setHeader0Value(HSSFSheet sheet, int headerNum,short size,String sb){
		//设置说明文字的格式
		HSSFCellStyle style = sheet.getWorkbook().createCellStyle();
		style.setWrapText(true);//设置自动换行
		style.setAlignment(HSSFCellStyle.VERTICAL_CENTER); //字体垂直居中
		//控制说明文字行
		HSSFRow row = sheet.createRow(0);
		row.setHeight((short) (size*380));;
		HSSFCell cell = null;
		cell =row.createCell(0);
		cell.setCellStyle(style);
		cell.setCellValue(sb.toString());
	}
	
	/**
	 * 设置列的下拉列表样式 --255以内的下拉
	 * @param sheet <工作表> 
	 * @param firstRow <设置起始行>
	 * @param colNum <设置列>
	 * @param textList <设置的下拉选项>
	 * @return
	 */
	public static DataValidation setSeletedValldation(HSSFSheet sheet, int firstRow, int colNum, String[] textList){
		return setSeletedValldation(sheet, new int[]{firstRow, LAST_ROW, colNum, colNum}, textList);
	}
	
	/* initNum {firstRow, lastRow, firstCol, lastCol} */
	public static DataValidation setSeletedValldation(HSSFSheet sheet, int[] initNum, String[] textList){
		
		DataValidationHelper helper = sheet.getDataValidationHelper();
		DataValidationConstraint constraint = helper.createExplicitListConstraint(textList);
		
		constraint.setExplicitListValues(textList);
		CellRangeAddressList regions = new CellRangeAddressList(initNum[0], initNum[1], initNum[2], initNum[3]);
		DataValidation data_validation = helper.createValidation(constraint, regions);
		      
		return data_validation;
	}
	
	
	/** 
	 * 设置列的下拉列表样式 --元素很多的情况
	 * @param sheet <工作表> 
	 * @param firstRow <设置起始行>
	 * @param colNum <设置列>
	 * @param strFormula <工作表中的列作为数据来源> "Sheet2!$A$2:$A$59"
	 * @return
	 */
	public static HSSFDataValidation setSeletedValldation2(HSSFSheet sheet, int firstRow, int colNum, String strFormula){
		return setSeletedValldation2(sheet, new int[]{firstRow, LAST_ROW, colNum, colNum}, strFormula);
	}
	
	/* initNum {firstRow, lastRow, firstCol, lastCol} */
	public static HSSFDataValidation setSeletedValldation2(HSSFSheet sheet, int[] initNum, String strFormula){
		CellRangeAddressList regions = new CellRangeAddressList(initNum[0], initNum[1], initNum[2], initNum[3]);
		DVConstraint constraint = DVConstraint.createFormulaListConstraint(strFormula);
		HSSFDataValidation data_validation = new HSSFDataValidation(regions, constraint);
		
		data_validation.createErrorBox("Error", "Error");
		data_validation.createPromptBox("", null);
		
		return data_validation;
	}
	
	public static void main(String[] args) {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet= wb.createSheet();
		
		//设置导出excel的下拉列表
		String[] textList = new String[]{"1-省", "2-市", "3-区", "4-县"};
		sheet.addValidationData(ExcelCellUtil.setSeletedValldation(sheet, 1, 3, textList) );
	}

}

