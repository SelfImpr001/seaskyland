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
public class OrgHasChildrenValidator implements Validator<Organization> {

	private Validator<Organization> next;
	
	private static String sql = "select count(1) from 4a_org where p_id = ? ";
	
	public OrgHasChildrenValidator() {
		
	}
	
	public OrgHasChildrenValidator(Validator<Organization> next) {
		this.next = next;
	}



	@Override
	public void validate(Organization org ) throws BusinessException{
		JdbcTemplate jdbcTemplate = SpringContext.getBean("jdbcTemplate");
		int i = jdbcTemplate.queryForInt(sql,org.getPk());
		if(i > 0)		
			throw new BusinessException("10001","组织【" +org.getCode()+"-" +org.getName()+"】存在下级组织");
		
		if(next != null)
			next.validate(org);
	}
	
}

