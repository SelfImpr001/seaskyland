/**
 * <p><b>© 1997-2013 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.repository;

import java.util.List;

import com.cntest.common.page.Page;
import com.cntest.common.query.Query;
import com.cntest.common.repository.Repository;
import com.cntest.fxpt.domain.FSDataNnalysis;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 陈勇2014年6月5日
 * @version 1.0
 **/
public interface IAnalysisDao extends Repository<FSDataNnalysis, Long> {
	void list(Long pk,Query<FSDataNnalysis> query);
	public List<FSDataNnalysis> reportgeneratelist(Page<FSDataNnalysis> page);
	
}

