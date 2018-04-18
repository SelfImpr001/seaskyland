/*
 * @(#)com.cntest.fxpt.etl.business.impl.StudentTemplateImpl.java	1.0 2014年10月21日:上午11:17:05
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business.impl;

import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.bean.DataField;
import com.cntest.fxpt.etl.business.IImportTemplateService;
import com.cntest.fxpt.service.etl.IDataFieldService;

import jxl.CellView;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月21日 上午11:17:05
 * @version 1.0
 */
@Service("Student.TemplateService")
public class StudentTemplateImpl extends AbstractTemplateImpl implements
		IImportTemplateService {
	@Autowired(required = false)
	@Qualifier("etl.IDataFieldService")
	private IDataFieldService dataFieldService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.etl.business.IImportTemplateService#template(java.io.
	 * OutputStream)
	 */
	@Override
	public void template(OutputStream out) throws Exception {
		WritableWorkbook wb = Workbook.createWorkbook(out);
		WritableSheet sheet = wb.createSheet("学生信息", 0);
		List<DataField> dataFields = dataFieldService.list(1, 0L);
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

		wb.write();
		wb.close();
	}

	@Override
	public void template(OutputStream out, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
