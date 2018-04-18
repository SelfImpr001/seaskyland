package com.cntest.fxpt.service.bi.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.bi.domain.BiTokens;
import com.cntest.fxpt.repository.bi.ReportBiMenuDao;
import com.cntest.fxpt.service.bi.ReportBiMenuService;
@Service("ReportBiMenuService")
public class ReportBiMenuServiceImpl implements ReportBiMenuService{
	
	@Autowired
	private ReportBiMenuDao reportBiMenuDao;

	@Override
	public void addTokens(BiTokens tokens) {
		reportBiMenuDao.addTokens(tokens);
	}
	
}
