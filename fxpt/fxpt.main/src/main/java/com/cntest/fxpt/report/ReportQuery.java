/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.report;

import java.util.List;

import com.cntest.common.query.Query;
import com.cntest.fxpt.domain.Exam;
import com.cntest.security.UserDetails;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年12月19日
 * @version 1.0
 **/
public interface ReportQuery {

	List<Exam> queryFor(UserDetails user,Query<Exam> query);
	
	List<Exam> querySameTermExamReport(UserDetails user,Exam exam);
}

