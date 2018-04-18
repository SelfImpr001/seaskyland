/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.domain.validate;

import org.springframework.jdbc.core.JdbcTemplate;

import com.cntest.common.validation.Validator;
import com.cntest.exception.BusinessException;
import com.cntest.foura.domain.Organization;
import com.cntest.util.SpringContext;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2014年10月31日
 * @version 1.0
 **/
public class OrgExamDataNotNullValidator implements Validator<Organization> {

//	private static String sql = "SELECT COUNT(1) FROM (SELECT 1 FROM dw_dim_school WHERE CODE=? UNION "
//			+ "SELECT 1 FROM  dw_dim_province WHERE CODE =? UNION "
//			+ "SELECT 1 FROM dw_dim_city WHERE CODE=? UNION "
//			+ "SELECT 1 FROM dw_dim_county WHERE CODE=? ) t";
	
	private static String sql = "SELECT count(1) FROM 4a_org a INNER JOIN dw_examstudent_fact b ON a.org_id = b.schoolId WHERE  a.org_code=?";

	private Validator<Organization> next;

	public OrgExamDataNotNullValidator() {

	}

	public OrgExamDataNotNullValidator(Validator<Organization> validator) {
		this.next = validator;
	}

	@Override
	public void validate(Organization org) throws BusinessException {
		if(org.getType() == 4) {
			JdbcTemplate jdbcTemplate = SpringContext.getBean("jdbcTemplate");
			int i = jdbcTemplate.queryForInt(sql, org.getCode());
			if (i > 0)
				throw new BusinessException("10003", "组织已有考试数据");
		}
		if(next != null)
			this.next.validate(org);
	}

}
