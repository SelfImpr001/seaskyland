/*
 * @(#)com.cntest.fxpt.statistical.StatisticalCombinationSubject.java	1.0 2014年6月16日:下午3:50:37
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.statistical;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cntest.fxpt.domain.Exam;
import com.cntest.util.SpringContext;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年6月16日 下午3:50:37
 * @version 1.0
 */
public class CalculateExam {
	private static final Logger log = LoggerFactory
			.getLogger(CalculateExam.class);
	private JdbcTemplate jdbcTemplate;

	public CalculateExam() {
		jdbcTemplate = SpringContext.getBean("houseJdbcTemplate");
	}

	public void calculateExam(Exam exam) throws Exception {
		log.debug("调用存储过程进行计算");
		jdbcTemplate.execute("call SP_calcluateExam(" + exam.getId() + ")");
		log.debug("调用存储过程进行计算完毕");
	}
}
