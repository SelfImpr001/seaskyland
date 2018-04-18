/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cntest.common.query.Query;
import com.cntest.common.service.AbstractEntityService;
import com.cntest.fxpt.domain.EducationMonitor;
import com.cntest.fxpt.repository.EducationMonitorDao;
import com.cntest.fxpt.service.EducationMonitorService;


@Transactional
@Service("EducationMonitorService")														   
public class EducationMonitorServiceImpl extends AbstractEntityService<EducationMonitor,Long> implements EducationMonitorService {

	@Autowired(required = false)
	@Qualifier("EducationMonitorDao")
	private EducationMonitorDao educationMonitorDao;
	
	@Override
	@Transactional(readOnly=true)
	public void Querylist(Query<EducationMonitor> query) {
		educationMonitorDao.findMonitor(query);
	}
}

