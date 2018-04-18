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
 * 
 * </pre>
 * 
 * @author 李贵庆2014年10月28日
 * @version 1.0
 **/
public class SameSchoolSpecification extends AbstractSpecification<ExamStudent> {
	private static String sql = "SELECT COUNT(id) FROM kn_studentbase WHERE  schoolCode= ? ";

	@Override
	public boolean isInSatified(ExamStudent t) {
		JdbcTemplate jdbcTemplate = SpringContext.getBean("jdbcTemplate");
		int i = jdbcTemplate.queryForInt(sql, t.getSchool().getCode());
		return i > 0;
	}

}
