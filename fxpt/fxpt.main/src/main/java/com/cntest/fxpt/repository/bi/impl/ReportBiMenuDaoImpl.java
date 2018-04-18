/**
 * <p><b>© 1997-2013 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.repository.bi.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.bi.domain.BiTokens;
import com.cntest.fxpt.repository.bi.ReportBiMenuDao;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年6月5日
 * @version 1.0
 **/
@Repository("IReportBiMenuDao")
public class ReportBiMenuDaoImpl extends AbstractHibernateDao<BiTokens,Long> implements ReportBiMenuDao {

	@Override
	protected Class<BiTokens> getEntityClass() {
		return BiTokens.class;
	}
	
	@Override
	public void addTokens(BiTokens tokens) {
		this.save(tokens);
	}
	
}

