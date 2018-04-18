/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.service;

import com.cntest.common.page.Page;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.ExamCheckin;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年10月20日
 * @version 1.0
 **/
public interface ExamCheckinService {

	void listExamCheckin(Page<ExamCheckin> page);

	void getCheckedExamniees(Long examId,Page page);

	void getCheckedlessExamniees(Long examId, Page page);
	
	ExamCheckin getOf(Exam exam);
}

