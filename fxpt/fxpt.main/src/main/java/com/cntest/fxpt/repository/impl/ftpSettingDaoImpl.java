/*
 * @(#)com.cntest.fxpt.repository.impl.StatisticSettingDaoImpl.java	1.0 2014年10月27日:下午3:10:11
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.FtpSetting;
import com.cntest.fxpt.domain.StatisticSetting;
import com.cntest.fxpt.repository.IFtpSettingDao;
import com.cntest.fxpt.repository.IStatisticSettingDao;
import com.cntest.fxpt.service.IExamService;
import com.cntest.fxpt.service.IExamStudentService;
import com.cntest.fxpt.util.FileFactory;
import com.cntest.fxpt.util.Json;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2014年10月27日 下午3:10:11
 * @version 1.0
 */
@Repository("IFtpSettingDao")
public class ftpSettingDaoImpl extends
		AbstractHibernateDao<FtpSetting, Integer> implements
		IFtpSettingDao {

	
	@Autowired(required = false)
	@Qualifier("IExamService")
	private IExamService examService;
	
	@Autowired(required = false)
	@Qualifier("IExamStudentService")
	private IExamStudentService examStudentService;

	@Override
	public List<FtpSetting> ftpList() {
		String hql = "from FtpSetting";
		Query query = getSession().createQuery(hql);
		return query.list();
	}

	@Override
	public void updateFtp(FtpSetting ftpSetting) {
		this.update(ftpSetting);
	}
	@Override
	protected Class<FtpSetting> getEntityClass() {
		return FtpSetting.class;
	}

	@Override
	public FtpSetting findFtpSetByStatus(int status) {
		String hql = "from FtpSetting where status=?";
		Query query = getSession().createQuery(hql);
		query.setInteger(0, status);
		List<FtpSetting> list = query.list();
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	public boolean upload(Long examid) {
		boolean up = false;
		FtpSetting ftpSetting = this.findFtpSetByStatus(1);
		if(ftpSetting!=null && examid!=null){
			try {
				List<Map<String, String>> listStudent = examStudentService.getStudentList(examid);
				String result = Json.toJson(listStudent);
				InputStream inputStream = new ByteArrayInputStream(result.getBytes("UTF-8"));
				Exam exam = examService.findById(examid);
				up = FileFactory.uploadFiletoFtp(ftpSetting.getUrl(),
						ftpSetting.getPort(), ftpSetting.getUsername(),
						ftpSetting.getPassword(), ftpSetting.getPath(),
						exam.getName() + "-(" + FileFactory.getNowDate()
								+ ").txt", inputStream);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return up;
	}

}
