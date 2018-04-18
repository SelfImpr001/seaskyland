package com.cntest.fxpt.bi.smartBI;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.cntest.fxpt.bi.BIConnectionDAO;
import com.cntest.fxpt.bi.domain.BiUser;
import com.cntest.fxpt.repository.smartBi.IBiUserDao;

@Component("bi.BIConnectionDAO")
public class BIConnectionDAOImpl implements BIConnectionDAO{
	@Autowired(required=false)
	@Qualifier("bi.biUserDao")
	private IBiUserDao dao;
	int biUserId = 1;
	public List<BiUser> getBiUsers() {
		return dao.getBiUsersByBiInfoId(biUserId);
	}

}
