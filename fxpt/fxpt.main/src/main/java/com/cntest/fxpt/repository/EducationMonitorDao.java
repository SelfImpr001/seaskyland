/**
 * <p><b>© 1997-2013 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.repository;

import java.util.List;

import com.cntest.common.query.Query;
import com.cntest.common.repository.Repository;
import com.cntest.fxpt.domain.EducationMonitor;
import com.cntest.fxpt.domain.EducationSurvey;
import com.cntest.fxpt.domain.FSDataNnalysis;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年6月5日
 * @version 1.0
 **/
public interface EducationMonitorDao extends Repository<EducationMonitor, Long> {

	void findMonitor(Query<EducationMonitor> query);
	
	EducationMonitor get(Long pk);
	
	void deletem(Long pk);
	
	void updatem(EducationMonitor m);
	
	void savem(EducationMonitor m );
	
	//考试数据上传--结果导出增加验证
	void  saveReportData(FSDataNnalysis fsdn);
}

