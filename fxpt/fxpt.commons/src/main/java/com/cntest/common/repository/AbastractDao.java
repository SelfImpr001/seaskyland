/**
 * <p><b>© 1997-2013 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.common.repository;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2013年12月20日
 * @version 1.0
 **/
public abstract class AbastractDao {

	protected JdbcTemplate jdbcTemplate;
	
	@Resource(name="jdbcTemplate")
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
}

