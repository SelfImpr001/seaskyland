/*
 * @(#)com.cntest.fxpt.anlaysis.filter.StudentAttrFilter.java	1.0 2015年4月22日:下午1:36:26
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.filter;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.fxpt.anlaysis.bean.StudentCj;
import com.cntest.fxpt.domain.ExamStudent;
import com.cntest.util.ExceptionHelper;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年4月22日 下午1:36:26
 * @version 1.0
 */
public class StudentAttrFilter extends AbstractStudentCjFilter {
	private Logger log = LoggerFactory.getLogger(StudentAttrFilter.class);
	private String value;
	private String attrName;

	public StudentAttrFilter(String attrName, String value) {
		this.attrName = attrName;
		this.value = value;
	}

	@Override
	public boolean filter(StudentCj studentCj) {
		ExamStudent student = studentCj.getStudent();
		boolean result = false;
		try {
			String tmpValue = BeanUtils.getProperty(student, attrName);
			result = tmpValue != null ? (tmpValue.compareTo(value) == 0)
					: false;
		} catch (Exception e) {
			log.error("获取学生属性值出错；" + ExceptionHelper.trace2String(e));
		}
		return result;
	}

	@Override
	public String toString() {
		String text = "[" + attrName + ":" + value + "]";
		if (this.filter != null) {
			text += "->" + this.filter.toString();
		}
		return text;
	}
}
