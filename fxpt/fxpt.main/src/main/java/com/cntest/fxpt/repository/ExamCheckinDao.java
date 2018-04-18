/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.repository;

import com.cntest.common.page.Page;
import com.cntest.common.repository.Repository;
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
public interface ExamCheckinDao extends Repository<ExamCheckin, Long> {

	ExamCheckin findByExam(Exam exam);

	void findExamChecked(Long examId,Page page);

	void findExamCheckedless(Long examId, Page page);

	void findAll(Page<ExamCheckin> page);

	void deleteBy(Exam exam);
}

