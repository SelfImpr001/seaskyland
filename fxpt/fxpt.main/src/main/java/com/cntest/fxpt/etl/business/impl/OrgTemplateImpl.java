/*
 * @(#)com.cntest.fxpt.etl.business.impl.ItemTemplate.java	1.0 2014年10月10日:上午10:04:39
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business.impl;

import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.bean.DataField;
import com.cntest.fxpt.etl.business.IImportTemplateService;
import com.cntest.fxpt.service.etl.IDataFieldService;
import com.cntest.fxpt.util.ExcelCellUtil;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月10日 上午10:04:39
 * @version 1.0
 */
@Service("Org.TemplateService")
public class OrgTemplateImpl extends AbstractTemplateImpl implements
		IImportTemplateService {
	@Autowired(required = false)
	@Qualifier("etl.IDataFieldService")
	private IDataFieldService dataFieldService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.etl.business.IImportTemplateService#itemTemplate(java
	 * .io.OutputStream)
	 */
	@Override
	public void template(OutputStream out) throws Exception {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet= wb.createSheet("组织架构");
		//获取需要导出的字段
		List<DataField> dataFields = dataFieldService.list(11, 0L);
		int idx = 0;
		int idxto=1;
		//定义数组
		String[] headers = new String[dataFields.size()];
		String[] fieldNames = new String[dataFields.size()];
		StringBuffer sb = new StringBuffer();
		sb.append( "表体信息填写说明：\r\n");
		//获取描述信息
		for (DataField dataField : dataFields){
			String description = dataField.getDescription();
			if(description!="" && description!=null){
				String[] defNames = dataField.getDefaultName().split("\\|");
				sb.append((idxto++)+". “"+defNames[0]+"”栏："+description+"\r\n");
			}
		}
		if(idxto==1){
			sb.append("暂无说明！");
		}
		//设置描述信息的样式
		ExcelCellUtil.setHeader0Value(sheet, 0,(short)dataFields.size(),sb.toString());
		for (DataField dataField : dataFields){
				String defNames = dataField.getAsName();
				fieldNames[idx]=dataField.getFieldName();
				headers[idx++]=defNames;
		}
		//ExcelCellUtil.setAnnotateText(sheet, 0, 0, 0, headers.length-1, "设置单元格批注"); //批注
		ExcelCellUtil.setHeaderValue(sheet, 1, headers);
		
		for(int i=0;i<fieldNames.length;i++){
			if(fieldNames[i].equalsIgnoreCase("org_type")){
				String[] textList= new String[]{"0-教育局","1-省", "2-市", "3-区", "4-学校"};
				sheet.addValidationData(ExcelCellUtil.setSeletedValldation(sheet, 2, i, textList) );
			}else if(fieldNames[i].equalsIgnoreCase("schoolTypeId")){
				String[] textList= new String[]{"1-公办学校", "2-民办优质学校", "3-民办农民工子女学校"};
				sheet.addValidationData(ExcelCellUtil.setSeletedValldation(sheet, 2, i, textList) );
			}else if(fieldNames[i].equalsIgnoreCase("schoolSegmentId")){
				String[] textList = new String[]{"1-小学", "2-初中", "3-九年制","4-高中","5-中职"};
				sheet.addValidationData(ExcelCellUtil.setSeletedValldation(sheet, 2,i, textList) );
			}
		}
		//ExcelCellUtil.setFreezePane(sheet, 0, 1, 1, 2); //冻结
		//sheet.protectSheet("password");  //excel全部屏蔽了
		
		wb.write(out);
		out.close();
	}
	private void template1(OutputStream out) throws Exception{
		WritableWorkbook wb = Workbook.createWorkbook(out);
		WritableSheet sheet = wb.createSheet("组织信息", 0);
		List<DataField> dataFields = dataFieldService.list(2, 0L);
		int idx = 0;
		int idxto=1;
		StringBuffer sb = new StringBuffer();
		sb.append( "表体信息填写说明：\r\n");
		//获取描述信息
		for (DataField dataField : dataFields){
			String description = dataField.getDescription();
			if(description!="" && description!=null){
				String[] defNames = dataField.getDefaultName().split("\\|");
				sb.append((idxto++)+". “"+defNames[0]+"”栏："+description+"\r\n");
			}
		}
		if(idxto==1){
			sb.append("暂无说明！");
		}
		sheet.mergeCells(0, 0, dataFields.size()-1, 0);//设置合并单元格式
		sheet.setRowView(0, (idxto+1)*300,false);
		for (DataField dataField : dataFields) {
			sheet.setColumnView(idx, 20);//设置列宽
			Label label = new Label(idx, 0, sb.toString(), getDescriptionFormat());
			sheet.addCell(label);
			String[] defNames = dataField.getDefaultName().split("\\|");
			label = new Label(idx++, 1, defNames[0], getTextFormat());
			sheet.addCell(label);
		}
		
	}
	@Override
	public void template(OutputStream out, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	//作废
	/*public void template(OutputStream out) throws Exception {
		WritableWorkbook wb = Workbook.createWorkbook(out);
		WritableSheet sheet = wb.createSheet("组织架构", 0);

		WritableCellFormat headFormat = new WritableCellFormat();

		Label label = new Label(0, 0, "组织名称", getTextFormat());
		sheet.addCell(label);
		label = new Label(1, 0, "组织代码", getTextFormat());
		sheet.addCell(label);
		label = new Label(2, 0, "组织类型", getTextFormat());
		sheet.addCell(label);
		label = new Label(3, 0, "所属机构", getTextFormat());
		sheet.addCell(label);
		label = new Label(4, 0, "学校类型", getTextFormat());
		sheet.addCell(label);
		label = new Label(5, 0, "学校学段", getTextFormat());
		sheet.addCell(label);
		label = new Label(6, 0, "上级组织代码", getTextFormat());
		sheet.addCell(label);
		
		Label label1 = new Label(0, 1, "", getTextFormat());
		sheet.addCell(label1);
		
		label1 = new Label(2, 1, "1-省，2-市，3-区，4-县", getTextFormat());
		sheet.addCell(label1);
		label1 = new Label(3, 1, "1-省教育厅，2-市教育局，3-县（区）教育局，4-学校", getTextFormat());
		sheet.addCell(label1);
		label1 = new Label(4, 1, "学校类型", getTextFormat());
		sheet.addCell(label1);
		label1 = new Label(5, 1, "学校学段", getTextFormat());
		sheet.addCell(label1);
		label1 = new Label(6, 1, "上级组织代码", getTextFormat());
		sheet.addCell(label1);

		wb.write();
		wb.close();
	}*/
	
}
