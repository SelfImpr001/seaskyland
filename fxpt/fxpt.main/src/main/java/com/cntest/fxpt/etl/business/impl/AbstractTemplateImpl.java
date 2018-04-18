/*
 * @(#)com.cntest.fxpt.etl.business.impl.AbstractTemplateImpl.java	1.0 2014年10月21日:上午11:17:28
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business.impl;

import jxl.format.Alignment;
import jxl.format.UnderlineStyle;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月21日 上午11:17:28
 * @version 1.0
 */
public abstract class AbstractTemplateImpl {
	protected WritableCellFormat getTextFormat() throws Exception {
		WritableFont headFont = new WritableFont(WritableFont.createFont("宋体"),
				10, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE);

		WritableCellFormat headFormat = new WritableCellFormat();
		headFormat.setAlignment(jxl.format.Alignment.CENTRE);
		headFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);

		headFormat.setBorder(jxl.format.Border.ALL,
				jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
		return headFormat;
	}
	
	protected WritableCellFormat getDescriptionFormat() throws Exception {
		WritableFont headFont = new WritableFont(WritableFont.createFont("宋体"),
				10, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE);

		WritableCellFormat headFormat = new WritableCellFormat(headFont);
		headFormat.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		headFormat.setWrap(true);//设置自动换行
		headFormat.setAlignment(Alignment.LEFT);//设置对齐方式
		headFormat.setBorder(jxl.format.Border.ALL,
				jxl.format.BorderLineStyle.THIN, jxl.format.Colour.BLACK);
		
		return headFormat;
	}

}
