/*
 * @(#)com.cntest.fxpt.service.impl.SchoolServiceImpl.java	1.0 2014年6月3日:上午8:51:49
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.remote.service.impl;

import java.util.Map;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.exception.BusinessException;
import com.cntest.foura.domain.Sex;
import com.cntest.foura.domain.User;
import com.cntest.foura.domain.UserInfo;
import com.cntest.foura.service.UserService;
import com.cntest.remote.domain.NanShanData;
import com.cntest.remote.repository.INanShanDataDao;
import com.cntest.remote.service.INanShanDataService;

/**
 * @author 吕萌 2016年12月6日
 * @version 1.0
 */
@Service("INanShanDataService")
public class NanShanDataServiceImpl implements INanShanDataService {

	@Autowired(required = false)
	@Qualifier("INanShanDataDao")
	private INanShanDataDao nanShanDataDao;
	
	@Autowired
	private UserService userService;
	
	@Override
	public void add(NanShanData nanShanData) throws BusinessException {
		if(nanShanData.getId() != null){
			updateState(nanShanData, 1);
		}else{
			User user = new User(nanShanData.getUid());
			
			UserInfo userInfo = new UserInfo();
			initUserInfo(userInfo, nanShanData);
			
			user.setUserInfo(userInfo);
			user.setPassword("123456");
			//根据userType获取相应的权限
			String roleCode=getRoleCodeBy(nanShanData.getUserType());
			nanShanData.setRoleId(roleCode);
			userService.createNanshan(user, roleCode);
			
			nanShanData.setUser(user);
			nanShanDataDao.add(nanShanData);
		}
	}
	
	/**
	 * 根据userType获取相应的角色code
	 * @param userType 000：教师   0001：行政人员  0002：技术人员    0100:临时南山工作人员   0200、0201、0202、0203、0204、0205、0206：学生    0207：家长   0300：其他  
	 * @return
	 */
	private String getRoleCodeBy(String userType){
		String students=",0200,0201,0202,0203,0204,0205,0206,";
		String roleCode ="";
		if(!"".equals(userType)){
			if("0000".equals(userType)){
				roleCode="teacher";
			}else if("0001".equals(userType)){
				roleCode="executive";
			}else if("0002".equals(userType)){
				roleCode="artisan";
			}else if("0100".equals(userType)){
				roleCode="work";
			}else if(students.indexOf(","+userType+",")>0){
				roleCode="student";
			}else if("0207".equals(userType)){
				roleCode="parents";
			}else if("0300".equals(userType)){
				roleCode="other";
			}
		}
		return roleCode;
	} 
	public void update(NanShanData nanShanData) throws BusinessException{
		User user = nanShanData.getUser();
		if(user !=null ){
			if(user.getUserInfo() != null){
				UserInfo userInfo = user.getUserInfo();
				initUserInfo(user.getUserInfo(), nanShanData);
				user.setUserInfo(userInfo);
			}
			userService.update(user);
		}
		
		nanShanDataDao.update(nanShanData);
	}
	
	private void initUserInfo(UserInfo userInfo, NanShanData nanShanData){
		Sex sex = Sex.UNKNOW;
		if("2".equals(nanShanData.getSex())){
			sex = Sex.FEMALE;
		}else if("1".equals(nanShanData.getSex())){
			sex = Sex.MALE;
		}
		
		userInfo.setSex(sex);
		userInfo.setNickName(nanShanData.getNickName());
		userInfo.setRealName(nanShanData.getUserName());
		userInfo.setEmail(nanShanData.getEmail());
		userInfo.setCellphone(nanShanData.getMobile());
	}
	
	@Override
	public void updateState(NanShanData nanShanData) throws BusinessException{
		updateState(nanShanData, 0);
	}
	
	private void updateState(NanShanData nanShanData, int status) throws BusinessException{
		/*User user = nanShanData.getUser();
		user.setStatus(status);
		userService.update(user);*/
		
		nanShanData.setStatus(status);
		nanShanDataDao.update(nanShanData);
	}

	@Override
	public void delete(NanShanData nanShanData) {
		nanShanDataDao.delete(nanShanData);
	}

	@Override
	public NanShanData get(Long nanShanDataId) {
		return nanShanDataDao.get(nanShanDataId);
	}

	@Override
	public NanShanData findByUid(String uid) {
		return nanShanDataDao.findByUid(uid);
	}

	@Override
	public boolean isExistData(NanShanData nanShanData) {
		nanShanData = nanShanDataDao.findByUid(nanShanData.getUid());
		return nanShanData == null ? false : true;
	}

	@Override
	public Query getQueryByUser(String loginId, User user,Map<String,String> parameters) {
		return nanShanDataDao.getQueryByUser(loginId,user,parameters);
	}

}
