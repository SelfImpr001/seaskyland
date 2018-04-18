/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.service;


import com.cntest.common.query.Query;
import com.cntest.common.service.EntityService;
import com.cntest.fxpt.domain.EducationMonitor;

/** 
 * <pre>
 * 
 * </pre>
 *  监测管理
 * @author 肖 肖 2014年6月18日
 * @version 1.0
 **/
public interface EducationMonitorService extends EntityService<EducationMonitor,Long>{

	void Querylist(Query<EducationMonitor> query);

	
}

