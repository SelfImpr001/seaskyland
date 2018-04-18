/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cntest.common.page.Page;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.ExamCheckin;
import com.cntest.fxpt.repository.ExamCheckinDao;
import com.cntest.fxpt.service.ExamCheckinService;
import com.cntest.web.view.Progress;
import com.cntest.web.view.ProgressListener;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年10月20日
 * @version 1.0
 **/

@Service("ExamCheckinService")
public class ExamCheckinServiceImpl  implements ExamCheckinService ,ProgressListener{
	private static Logger logger = LoggerFactory.getLogger(ExamCheckinServiceImpl.class);
	
	@Autowired
	private ExamCheckinDao checkinDao;

	@Override
	public void getCheckedExamniees(Long examId,Page page) {
		logger.debug("get checkined list for {}",examId);
		checkinDao.findExamChecked(examId,page);
		logger.debug("get checkined list for {} success");
	}


	@Override
	public void getCheckedlessExamniees(Long examId, Page page) {
		logger.debug("get checkinedless list for {}",examId);
		checkinDao.findExamCheckedless(examId,page);
		logger.debug("get checkinedless list for {} success",examId);
	}


	@Override
	public void listExamCheckin(Page<ExamCheckin> page) {
		logger.debug("List ExamCheckin list ");
		checkinDao.findAll(page);
		logger.debug("List ExamCheckin list success");
	}


	@Override
	public ExamCheckin getOf(Exam exam) {
		return checkinDao.findByExam(exam);
	}


	@Override
	@Transactional(readOnly=true)
	public Progress on(Map<String, String> params) {
		
		String pk = params.get("pk");
		logger.debug("ExamCheckin ProgressListener for{}",pk);
		if(pk == null)
		    return new Progress(100,100);
		else {
			ExamCheckin checkin = checkinDao.get(Long.valueOf(pk));
			return new Progress(checkin.getExamineeTotal(),checkin.getCheckedTotal()+checkin.getFailureTotal());
		}
	}
}

