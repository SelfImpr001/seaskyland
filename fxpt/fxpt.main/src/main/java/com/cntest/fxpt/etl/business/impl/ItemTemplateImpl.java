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

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.bean.DataField;
import com.cntest.fxpt.etl.business.IImportTemplateService;
import com.cntest.fxpt.service.etl.IDataFieldService;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月10日 上午10:04:39
 * @version 1.0
 */
@Service("Item.TemplateService")
public class ItemTemplateImpl extends AbstractTemplateImpl implements
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
		WritableWorkbook wb = Workbook.createWorkbook(out);
		WritableSheet sheet = wb.createSheet("双向细目表", 0);

		WritableCellFormat headFormat = new WritableCellFormat();

		Label label = new Label(0, 0, "试卷名称", getTextFormat());
		sheet.addCell(label);
		label = new Label(1, 0, "", getTextFormat());
		sheet.addCell(label);
		label = new Label(2, 0, "试卷满分", getTextFormat());
		sheet.addCell(label);
		label = new Label(3, 0, "", getTextFormat());
		sheet.addCell(label);
		List<DataField> dataFields = dataFieldService.list(2, 0L);
		int idx = 0;
		int idxto=1;
		StringBuffer sb = new StringBuffer();
		sb.append( "表体信息填写说明：\r\n");
		//明细表考试名称单独处理
		String testPaperName=dataFieldService.getTestPaperDescription(2, "testPaperName");
		if(testPaperName!=null && testPaperName!=""){
			sb.append((idxto++)+". “试卷名称”栏："+testPaperName+"\r\n");
		}
		//获取描述信息
		for (DataField dataField : dataFields){
			String description = dataField.getDescription();
			if(description!="" && description!=null && !"".equals(description)){
				String[] defNames = dataField.getDefaultName().split("\\|");
				sb.append((idxto++)+". “"+defNames[0]+"”栏："+description+"\r\n");
			}
		}
		//无字段说明时
		if(idxto==1){
			sb.append("暂无说明！");
		}
		sheet.mergeCells(0, 0, dataFields.size()-1, 0);//设置合并单元格式
		sheet.setRowView(0, (idxto+1)*320,false);
		//
		label = new Label(idx, 0, sb.toString(), getDescriptionFormat());
		sheet.addCell(label);
		label = new Label(0, 1, "试卷名称", getDescriptionFormat());
		sheet.addCell(label);
		label = new Label(2, 1, "试卷满分", getDescriptionFormat());
		sheet.addCell(label);
		for (DataField dataField : dataFields) {
			sheet.setColumnView(idx, 15);//设置列宽
			String[] defNames = dataField.getDefaultName().split("\\|");
			label = new Label(idx++, 2, defNames[0], getTextFormat());
			sheet.addCell(label);
		}

		wb.write();
		wb.close();
	}

	@Override
	public void template(OutputStream out, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
	}

	
}
