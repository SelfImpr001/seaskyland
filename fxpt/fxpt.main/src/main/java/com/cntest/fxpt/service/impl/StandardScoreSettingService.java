/*
 * @(#)com.cntest.fxpt.service.impl.StandardScoreSettiongService.java	1.0 2015年4月20日:下午5:01:32
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.repository.ILeveScoreSettingDao;
import com.cntest.fxpt.repository.IStandardScoreSettingDao;
import com.cntest.fxpt.service.IStandardScoreSettingService;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2015年4月20日 下午5:01:32
 * @version 1.0
 */
@Service("StandardScoreSettingService")
public class StandardScoreSettingService implements
		IStandardScoreSettingService {
	@Autowired(required = false)
	@Qualifier("StandardScoreSettingDao")
	private IStandardScoreSettingDao standardScoreSettingDao;

	public Map<String, List<Object[]>> findList(Long examid) {
		List<String> wl = standardScoreSettingDao.getWl(examid);
		Map<String, List<Object[]>> map = new  HashMap<String, List<Object[]>>();
		List<Object[]> list = standardScoreSettingDao.findList(examid);
		if(wl!=null && wl.size()>0){
			for (int i = 0; i < wl.size(); i++) {
				List<Object[]> resList= new ArrayList<Object[]>();
				for (int j = 0; j < list.size(); j++) {
					if(wl.get(i).equals(list.get(j)[3].toString()) || list.get(j)[3].toString().equals("0")){
						resList.add(list.get(j));
					}
				}
				map.put(wl.get(i), resList);
			}
		}
		return map;
	}

	public void save(String[] res) {
	
		if(res!=null && res.length>0){
			for (int i = 0; i < res.length; i++) {
				String[] str = res[i].split("_");
				Long examid = Long.parseLong(str[0]);
				int subjectid = Integer.parseInt(str[1]);
				String zvalue = str[2];
				int wl = Integer.parseInt(str[3]);
				standardScoreSettingDao.save(examid, subjectid, zvalue,wl);
			}
		}
		
	}
	
	public String getZvalue(Long examid,int wl){
		return standardScoreSettingDao.getZvalue(examid, wl);
	}

}
