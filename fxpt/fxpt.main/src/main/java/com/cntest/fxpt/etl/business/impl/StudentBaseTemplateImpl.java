/*
 * @(#)com.cntest.fxpt.etl.business.impl.StudentBaseTemplateImpl.java	1.0 2014年10月22日:下午1:41:47
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business.impl;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.cntest.fxpt.etl.business.IImportTemplateService;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2014年10月22日 下午1:41:47
 * @version 1.0
 */
@Service("StudentBase.TemplateService")
public class StudentBaseTemplateImpl extends AbstractTemplateImpl implements IImportTemplateService {

	/* (non-Javadoc)
	 * @see com.cntest.fxpt.etl.business.IImportTemplateService#template(java.io.OutputStream)
	 */
	@Override
	public void template(OutputStream out) throws Exception {
		WritableWorkbook wb = Workbook.createWorkbook(out);
		WritableSheet sheet = wb.createSheet("学生信息", 0);

		String[] names = { "姓名","学号","性别", "学级", "学校代码"};
		int idx = 0;
		for (String name : names) {
			Label label = new Label(idx++, 0, name, getTextFormat());
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
