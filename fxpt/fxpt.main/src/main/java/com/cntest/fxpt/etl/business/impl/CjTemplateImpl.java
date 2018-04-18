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

import jxl.Workbook;
import jxl.write.Label;
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
 * @author 刘海林 2014年10月21日 上午11:17:05
 * @version 1.0
 */
@Service("Cj.TemplateService")
public class CjTemplateImpl extends AbstractTemplateImpl implements
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
		WritableSheet sheet = wb.createSheet("成绩信息", 0);
		
		String[] names = {"小题分1", "小题分2", "...", "小题分N" };
		String[] fieldNames={"isQk","omrstr","scorestr"};
		String[] messages={"缺考","客观题选择串", "客观题得分串"};
		int idx = 0;
		int idxto= 1;
		StringBuffer sb = new StringBuffer();
		sb.append( "表体信息填写说明：\r\n");
		//成绩的 缺考，客观题选择串，客观题得分串 特殊处理------成绩信息字段不管有效无效，都要遭模板中导出names的选项（所以写死了）
		for(int m=0;m<fieldNames.length;m++){
			String testPaperName=dataFieldService.getTestPaperDescription(m==0?3:6, fieldNames[m]);
			if(testPaperName!=null && testPaperName!=""){
				sb.append((idxto++)+". “"+messages[m]+"”栏："+testPaperName+"\r\n");
			}
		}	
		//无字段说明时
		if(idxto==1){
			sb.append("暂无说明！");
		}
		Label label = new Label(idx, 0, sb.toString(), getDescriptionFormat());
		sheet.addCell(label);
		//3 是  表kn_etl_datacategory schemeType的值 
		List<DataField> dataFields = dataFieldService.list(3, 0L);
		//试卷成绩信息
		for (DataField dataField : dataFields) {
			sheet.setColumnView(idx, 15);//设置列宽
			String[] defNames = dataField.getDefaultName().split("\\|");
			label = new Label(idx++, 1, defNames[0], getTextFormat());
			sheet.addCell(label);
		}
		List<DataField> dataFieldKGs = dataFieldService.list(6, 0L);
		//客观题信息
		for (DataField dataField : dataFieldKGs) {
			sheet.setColumnView(idx, 15);//设置列宽
			String[] defNames = dataField.getDefaultName().split("\\|");
			label = new Label(idx++, 1, defNames[0], getTextFormat());
			sheet.addCell(label);
		}
		//小题模拟写死
		for (String name : names) {
			sheet.setColumnView(idx, 15);//设置列宽
			label = new Label(idx++, 1, name, getTextFormat());
			sheet.addCell(label);
		}
		sheet.mergeCells(0, 0, idx-1, 0);//设置合并单元格式
		sheet.setRowView(0, 1800,false);

		wb.write();
		wb.close();
	}
	@Override
	public void template(OutputStream out, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
