/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.domain.spec.examinee;

import java.util.ArrayList;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cntest.common.specification.AbstractSpecification;
import com.cntest.fxpt.domain.ExamStudent;
import com.cntest.util.ExceptionHelper;
import com.cntest.util.SpringContext;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2014年11月7日
 * @version 1.0
 **/
public class StudentEqualsSpecification extends AbstractSpecification<ExamStudent> {

	private static final Logger loggr = LoggerFactory.getLogger(StudentEqualsSpecification.class);

	private StringBuilder sql = new StringBuilder("SELECT COUNT(1) FROM kn_studentbase a  ")
			.append("LEFT JOIN dw_dim_school c ON c.code=a.schoolCode where 1=1 ");
	private ArrayList<String> args = new ArrayList<String>();

	@Override
	public boolean isInSatified(ExamStudent t) {
		JdbcTemplate jdbcTemplate = SpringContext.getBean("jdbcTemplate");
		Object[] os = new Object[args.size()];
		int i = 0;
		try {
			for (String arg : args) {
				os[i] = BeanUtils.getProperty(t, arg);
				i++;
			}
		} catch (Exception e) {
			loggr.error(ExceptionHelper.trace2String(e));
			os[i++] = "%";
		}

		int k = jdbcTemplate.queryForInt(sql.toString(), os);
//		int k = DataAccessUtils.requiredSingleResult(jdbcTemplate.query(sql.toString(), os, new RowMapper<Integer>() {
//
//			@Override
//			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
//				return rs.getInt(rowNum);
//			}
//		}));
		return k > 0;
	}

	public void addSpecification(String spec) {
		if ("SameStudentIDSpecification".equalsIgnoreCase(spec)) {
			this.sql.append(" and a.xh=?");
			args.add("studentId");
		}
		if ("SameNameSpecification".equalsIgnoreCase(spec)) {
			this.sql.append(" and a.name=?");
			args.add("name");
		}
		if ("SameSchoolSpecification".equalsIgnoreCase(spec)) {
			this.sql.append(" and a.schoolCode=?");
			args.add("school.code");
		}
		if ("SameClazzSpecification".equalsIgnoreCase(spec)) {
			this.sql.append(" and a.grade=?");
			args.add("exam.examStudentJiebieName");
		}
	}
}
