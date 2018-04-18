/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.domain.spec.examinee;

import org.springframework.jdbc.core.JdbcTemplate;

import com.cntest.common.specification.AbstractSpecification;
import com.cntest.fxpt.domain.ExamStudent;
import com.cntest.util.SpringContext;

/** 
 * <pre>
 * 学籍号规则，判断一个考生的学籍号是否与给定的学籍号相同
 * </pre>
 *  
 * @author 李贵庆2014年10月28日
 * @version 1.0
 **/
public class SameStudentIDSpecification extends AbstractSpecification<ExamStudent> {

	//private String studentId;
	private static String sql = "SELECT COUNT(id) FROM kn_studentbase WHERE  StudentID= ? ";
	public SameStudentIDSpecification( ) {
		//this.studentId = studentId;
	}
	
	@Override
	public boolean isInSatified(ExamStudent t) {
		// 目前学生库没有学籍号，暂时不实现
		JdbcTemplate jdbcTemplate = SpringContext.getBean("jdbcTemplate");
		return true;
	}

}

