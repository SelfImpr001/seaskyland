/*
 * @(#)com.cntest.fxpt.service.impl.ExamStudentServiceImpl.java	1.0 2014年5月20日:下午1:09:29
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.cntest.cache.EhCacheMgr;
import com.cntest.cache.ICache;
import com.cntest.common.page.Page;
import com.cntest.exception.BusinessException;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.anlaysis.bean.Param;
import com.cntest.fxpt.anlaysis.bean.StudentCjContainer;
import com.cntest.fxpt.bean.DataField;
import com.cntest.fxpt.bean.WebRetrieveResult;
import com.cntest.fxpt.domain.ExamStudent;
import com.cntest.fxpt.domain.FileManage;
import com.cntest.fxpt.repository.ExamCheckinDao;
import com.cntest.fxpt.repository.IExamDao;
import com.cntest.fxpt.repository.IExamStudentDao;
import com.cntest.fxpt.repository.IFileManageDao;
import com.cntest.fxpt.repository.IShowMessageDao;
import com.cntest.fxpt.service.ICjService;
import com.cntest.fxpt.service.IExamStudentService;
import com.cntest.fxpt.service.etl.IDataFieldService;
import com.cntest.security.UserDetails;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月20日 下午1:09:29
 * @version 1.0
 */
@Service("IExamStudentService")
public class ExamStudentServiceImpl implements IExamStudentService {
	@Autowired(required = false)
	@Qualifier("IExamStudentDao")
	private IExamStudentDao examStudentDao;

	@Autowired(required = false)
	@Qualifier("IExamDao")
	private IExamDao examDao;

	@Autowired(required = false)
	@Qualifier("IExamStudentService")
	private IExamStudentService examStudentService;

	@Autowired(required = false)
	private ExamCheckinDao examCheckinDao;

	@Autowired(required = false)
	@Qualifier("etl.IDataFieldService")
	private IDataFieldService dataFieldService;
	
	@Autowired(required = false)
	@Qualifier("ICjService")
	private ICjService cjService;

	@Autowired(required = false)
	@Qualifier("IFileManageDao")
	private IFileManageDao fileManageDao;
	
	@Autowired(required = false)
	@Qualifier("IShowMessageDao")
	private IShowMessageDao showMessageDao;

	@Autowired
	private UserService userService;
	
	
	
	HttpSession session = null;
	
	@Override
	public void importFail(Long examId) {
		examStudentDao.deleteTmpExamStudent(examId);
	}

	@Override
	public long getStudentMaxId() {
		return examStudentDao.getStudentMaxIdInDW();
	}

	@Override
	public void statSchoolImportPersonNum(Long examId,
			Page<Map<String, Object>> page) {
		examStudentDao.statSchoolImportPersonNum(examId, page);
	}

	@Override
	public void importSuccess(WebRetrieveResult wrr) throws BusinessException {
		session = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		Long examId = wrr.getExamId();
		int num = examStudentDao.getImportStudentNum(examId);
		long basic = Math.round(num/5000.0);
		double multiple = 0.005;
		for (int i = 1; i < basic; i++) {
			multiple = multiple + 0.001;
		}
		System.out.println(multiple);
		session.setAttribute("step", "正在导入数据，需要导入数据："+num+"条。"+"预计需要时间："+Math.round(num*multiple)+"秒");
		session.setAttribute("pc", "15");
		long starttime = System.currentTimeMillis();
		final Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			int i  = 15;
			@Override
			public void run() {
				System.out.println(i);
				if(i == 95){
					System.out.println("timer停止");
					timer.cancel();
				}
				session.setAttribute("pc", i++);
			}
		};
		/*double aa = ((num*multiple)/80)*1000;
		long aaa = Math.round(aa);
		timer.schedule(task,0,aaa);*/
		
		examStudentDao.copyStudent(wrr);
		session.setAttribute("pc", "97");
		long endtime = System.currentTimeMillis();
		System.out.println("正在导入数据，导入数据："+num+"条"+"总共用时："+(endtime-starttime)/1000+"秒");
		session.setAttribute("step", "正在导入数据，导入数据："+num+"条"+"总共用时："+(endtime-starttime)/1000+"秒");
		examStudentDao.statSchoolPersonCount(examId);
		boolean hasWlInStudentInfo = examStudentDao.isHasWL(examId);
		examDao.updateHasExamStudent(examId, true, hasWlInStudentInfo);
		examStudentDao.deleteTmpExamStudent(examId);
		//添加考试的组织权限（那些组织的用户可以查看该考试的报告）
		examDao.updateExamById(examId);
		//查询当前系统的导入字段信息
		List<DataField> dataField=dataFieldService.list(5L);
		//添加字段导入信息(保存最后一次的设置)
		//showMessageDao.deleteMessageByExamid(examId, 1);
		showMessageDao.addMessageByExamid(examId, null, 1, dataField);
		//改文件名称，并且把文件信息保存到数据库（kn_file_manage）
		UserDetails user =userService.getCurrentLoginedUser();
		fileManageDao.copyFile(wrr);
		String username= user.getRealName();
		fileManageDao.saveFileMsg(wrr,1L,0L,username);
		session.setAttribute("pc", "100");
		
	}

	@Override
	public void deleteAndUpdateExam(Long examId) {
		ICache cache = EhCacheMgr.newInstance("student-Cache");
		cache.clean(examId + ".bmk");
		cjService.deleteExamAllCj(examId);
		examStudentDao.deleteSchoolStatPersonCount(examId);
		examStudentDao.deleteExamClass(examId);
		examStudentDao.deleteStudentInDW(examId);
		examDao.updateHasExamStudent(examId, false, false);
		examCheckinDao.deleteBy(examDao.findById(examId));
		//删除字段显示信息
		showMessageDao.deleteMessageByExamid(examId, 1,null);
		List<FileManage> fileList = fileManageDao.fileList(examId, "1,3");
		fileManageDao.deleteFileByExamid(examId,"1,3");
		//删除对应原始文件
		fileManageDao.deleteFiels(fileList);
	}

	@Override
	public List<Map<String, Object>> list(Long examId) {
		return examStudentDao.list(examId);
	}

	@Override
	public void saveRollinStudent(Long targetExamId, Long srcExamId) {
		this.deleteAndUpdateExam(targetExamId);

		examStudentDao.copyStudentTo(targetExamId, srcExamId);

		boolean hasWlInStudentInfo = examStudentDao.isHasWL(targetExamId);
		int i = examDao.updateHasExamStudent(targetExamId, true, hasWlInStudentInfo);
		System.out.println(i);
		// Exam exam = examDao.findById(targetExamId);
		// exam.setHasExamStudent(true);
		// examDao.update(exam);
	}

	@Override
	public void findStudentListBySchoolCode(Long examId, String schoolCode,
			@SuppressWarnings("rawtypes") Page page) {
		examStudentDao.findStudentListBySchoolCode(examId, schoolCode, page);
	}

	@Override
	public int getExamStudentNum(Long examId) {
		return examStudentDao.getExamStudentNum(examId);
	}

	@Override
	public StudentCjContainer listStudentWith(Long examId) {
		return listStudentWith(examId, null);
	}

	@Override
	public int getExamSchoolNum(Long examId) {
		return examStudentDao.getExamSchoolNum(examId);
	}

	@Override
	public StudentCjContainer listStudentWith(Long examId, Param... params) {
		List<ExamStudent> esList = listStudent(examId, params);
		StudentCjContainer result = new StudentCjContainer();
		for (ExamStudent s : esList) {
			result.addStudentCj(s);
		}
		return result;
	}

	@Override
	public List<ExamStudent> listStudent(Long examId, Param... params) {
		return examStudentDao.listStudentWith(examId, params);
	}

	@Override
	public ExamStudent findByExample(ExamStudent example) {
		return examStudentDao.findByExample(example);
	}
	
	public List<Map<String, String>> getStudentList(Long examId){
		return examStudentDao.getStudentList(examId);
	}

	//追加导入相同区域数据时，删除原来该区所有数据
	@Override
	public boolean cleanStudent(Long examid, List<String> studentIds,List<String> schoolIds) throws Exception {
		return examStudentDao.cleanStudent(examid,studentIds, schoolIds);
	}

	@Override
	public void deleteTmpExamStudent(Long examId) {
		examStudentDao.deleteTmpExamStudent(examId);
	}

	@Override
	public List<ExamStudent> examByStudentForList(Long examid) {
		return examStudentDao.examByStudentForList(examid);
	}

	@Override
	public List<String> studentValidate(WebRetrieveResult wrr) {
		return examStudentDao.studentValidate(wrr);
	}

}
