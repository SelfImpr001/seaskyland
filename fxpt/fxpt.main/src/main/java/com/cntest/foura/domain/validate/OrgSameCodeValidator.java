/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.domain.validate;



import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

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
public class OrgSameCodeValidator implements Validator<Organization> {

	//private static String sql =""
	
	private Validator<Organization> next;
	
	public OrgSameCodeValidator() {
		
	}
	
	public OrgSameCodeValidator(Validator<Organization> next) {
		this.next = next;
	}


	@Override
	public void validate(Organization org ) throws BusinessException{
		JdbcTemplate jdbcTemplate = SpringContext.getBean("jdbcTemplate");
		Organization otherOrg  = jdbcTemplate.query("select org_id,org_code,org_name from 4a_org where org_code = ?", new  ResultSetExtractor<Organization>() {

			@Override
			public Organization extractData(ResultSet rs) throws SQLException, DataAccessException {
				if(rs.next()) {
					Organization thisOrg = new Organization(rs.getString(2),rs.getString(3));
					thisOrg.setPk(rs.getLong(1));
					return thisOrg;
				}
				return null;
			}},org.getCode());
		
		//OrganizationRepositoryHibernateImpl orgRepository = SpringContext.getBean(OrganizationRepositoryHibernateImpl.class);
		//Organization otherOrg = null;
		
		//String hql = "From Organization where code=?";
		//otherOrg = orgRepository.findEntityByHql(hql,org.getCode());
		
		if(otherOrg != null && !otherOrg.getPk().equals(org.getPk()))
			throw new BusinessException("10002","组织代码已被占用：【"+otherOrg.getCode()+"-"+otherOrg.getName()+"】");
		if(next != null)
			next.validate(org);
	}
}

