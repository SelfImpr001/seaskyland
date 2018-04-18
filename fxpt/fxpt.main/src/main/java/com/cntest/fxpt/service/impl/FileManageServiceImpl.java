package com.cntest.fxpt.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.bean.WebRetrieveResult;
import com.cntest.fxpt.domain.FileManage;
import com.cntest.fxpt.repository.IFileManageDao;
import com.cntest.fxpt.service.IFileManageService;
import com.cntest.util.ExceptionHelper;

/**
 * <Pre>
 * </Pre>
 * 
 * @author cheny 2016年9月1日 上午10:56:39
 */
@Service("IFileManageService")
public class FileManageServiceImpl implements IFileManageService {
	private static final Logger loggr = LoggerFactory
			.getLogger(FileManageServiceImpl.class);
	@Autowired(required = false)
	@Qualifier("IFileManageDao")
	private IFileManageDao fileManageDao;
	
	
	@Override
	public List<FileManage> fileList(Long examId, String type) {
		return fileManageDao.fileList(examId, type);
	}


	@Override
	public void saveFileMsg(WebRetrieveResult wrr,Long type,Long testPaperId,String useranme) {
		fileManageDao.saveFileMsg(wrr,type,testPaperId,useranme);
	}


	@Override
	public FileManage findFileByFileId(Long fileId) {
		return fileManageDao.findFileByFileId(fileId);
	}


	@Override
	public void deleteFileByExamid(Long examId,String type) {
		
		fileManageDao.deleteFileByExamid(examId,type);
	}


	@Override
	public void deleteFileByTestPaperId(Long testPaperId) {
		
		fileManageDao.deleteFileByTestPaperId(testPaperId);
	}


	@Override
	public List<FileManage> findFileByTestPaperId(Long testPaperId) {
		return fileManageDao.findFileByTestPaperId(testPaperId);
	}

	@Override
	public void importFail(WebRetrieveResult wrr) {
		//删除零时表数据
		fileManageDao.deleteLSBorg();
	}
	@Override
	public void importSuccess(WebRetrieveResult wrr) throws Exception  {
		fileManageDao.marchingOrg();
		fileManageDao.copyOrgTo4a();
		fileManageDao.deleteLSBorg();
	}
	@Override
	public List findAllListFromTmp() {
		return fileManageDao.findAllListFromTmp();
	}
	@Override
	public List findOrgListByCodeFrom4a(String org_code) {
		return fileManageDao.findOrgListByCodeFrom4a(org_code);
	}
	@Override
	public List findOrgListByCodeFromTmp(String org_code) {
		return fileManageDao.findOrgListByCodeFromTmp(org_code);
	}


	@Override
	public void marchingOrg() {
		fileManageDao.marchingOrg();
	}


	@Override
	public void deleteLSBorg() {
		fileManageDao.deleteLSBorg();
	}

}
