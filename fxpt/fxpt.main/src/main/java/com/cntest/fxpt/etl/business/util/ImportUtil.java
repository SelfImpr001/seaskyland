/*
 * @(#)com.cntest.fxpt.etl.business.util.ItemTemplateUtil.java	1.0 2014年10月15日:下午2:32:00
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.fxpt.bean.DataField;
import com.cntest.fxpt.bean.OmrStrAndScoreStr;
import com.cntest.fxpt.domain.TestPaper;
import com.cntest.fxpt.service.etl.IDataFieldService;
import com.cntest.util.ExceptionHelper;
import com.cntest.util.ListUtil;
import com.cntest.util.SpringContext;

/**
 * <Pre>
 * 在进行数据导入的时候，提出的一些
 * </Pre>
 * 
 * @author 刘海林 2014年10月15日 下午2:32:00
 * @version 1.0
 */
public class ImportUtil {
	private static final Logger log = LoggerFactory.getLogger(ImportUtil.class);

	public static TestPaper createTestPaper(List<String> row) {
		String testPaperName = null;
		double fullScore = -1;
		if (row != null && row.size() >= 4) {
			if ("试卷名称".equals(row.get(0))) {
				testPaperName = row.get(1);
			}

			if ("试卷满分".equals(row.get(2))) {
				try {
					fullScore = Double.parseDouble(row.get(3));
				} catch (Exception e) {
					log.equals("转换试卷的总分的时候出错！"
							+ ExceptionHelper.trace2String(e));
				}
			}
		}

		TestPaper testPaper = null;
		if (testPaperName != null && fullScore != -1) {
			testPaper = new TestPaper();
			testPaper.setName(testPaperName);
			testPaper.setFullScore(fullScore);
		}

		return testPaper;
	}

	public static String getMapingFiledName(DataField dataField,
			List<String> head) {
		String[] defNames = dataField.getDefaultName().split("\\|");
		String name = null;
		for (String defName : defNames) {
			boolean isFind = false;
			name = ListUtil.getValue(head, defName);
			isFind = name != null;
			if (isFind) {
				break;
			}
		}
		return name;
	}

	public static OmrStrAndScoreStr getOrmStrAndScoreStr(List<String> head) {
		IDataFieldService dataFieldService = SpringContext
				.getBean("etl.IDataFieldService");
		List<DataField> dataFields = dataFieldService.list(6, 0L);
		String omrStr = null;
		String scoreStr = null;
		for (DataField d : dataFields) {
			if (d.getFieldName().equalsIgnoreCase("omrstr")) {
				omrStr = ImportUtil.getMapingFiledName(d, head);
			} else if (d.getFieldName().equalsIgnoreCase("scorestr")) {
				scoreStr = ImportUtil.getMapingFiledName(d, head);
			}
		}

		return new OmrStrAndScoreStr().setOmrStr(omrStr).setScoreStr(scoreStr)
				.setHasScoreStr(scoreStr != null).setHasOmrStr(omrStr != null);
	}

}
