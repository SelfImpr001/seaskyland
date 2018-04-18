package com.cntest.fxpt.service.bi.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.bi.domain.BiInfo;
import com.cntest.fxpt.bi.domain.BiUser;
import com.cntest.fxpt.repository.smartBi.IBiInfoDao;
import com.cntest.fxpt.repository.smartBi.IBiUserDao;
import com.cntest.fxpt.service.bi.BiService;
import com.cntest.common.page.Page;
@Service("BiService")
public class BiServiceImpl implements BiService{
	
	@Autowired(required = false)
	@Qualifier("bi.biUserDao")
	private IBiUserDao biUserDao;
	
	@Autowired(required = false)
	@Qualifier("bi.biInfoDao")
	private IBiInfoDao biInfoDao;
	
	@Override
	public BiUser getBiUser(Integer id) {
		return biUserDao.getBiUser(id);
	}

	@Override
	public List<BiUser> getBiUsersByBiInfoId(Integer biInfoId) {
		return biUserDao.getBiUsersByBiInfoId(biInfoId);
	}

	@Override
	public void addBiUser(BiUser biUser) {
		biUserDao.addBiUser(biUser);
	}

	@Override
	public void updateBiUser(BiUser biUser) {
		biUserDao.updateBiUser(biUser);
	}

	@Override
	public void deleteBiUser(BiUser biUser) {
		biUserDao.deleteBiUser(biUser);
	}
	@Override
	public List<BiUser> getAllBiUsers() {
		return biUserDao.getAllBiUsers();
	}
	
	
	@Override
	public BiInfo getBiInfo(Integer id) {
		return biInfoDao.getBiInfo(id);
	}

	@Override
	public List<BiInfo> getBiInfoList() {
		return biInfoDao.getBiInfoList();
	}

	@Override
	public void addBiInfo(BiInfo biInfo) {
		biInfoDao.addBiInfo(biInfo);
		
	}

	@Override
	public void updateBiInfo(BiInfo biInfo) {
		biInfoDao.updateBiInfo(biInfo);
		
	}

	@Override
	public void deleteBiInfo(BiInfo biInfo) {
		biInfoDao.deleteBiInfo(biInfo);
		
	}

	@Override
	public List<BiUser> findPageByBiUser(Page page) {
		List<BiUser> listUser = new ArrayList<BiUser>();
		String sql = "select * from tb_bi_user";
		SQLQuery query = biUserDao.getSQLQuery(sql);
		if (null != page) {
			int first = (page.getCurpage() - 1) * page.getPagesize();
			first=first<0?0:first;
			query.setFirstResult(first);
			query.setMaxResults(page.getPagesize());
		}
		List list = query.list();
		if(list!=null){
			for (int i = 0; i < list.size(); i++) {
				Object[] obj = (Object[]) list.get(i);
				BiUser biUser = new BiUser();
				biUser.setId((Integer)obj[0]);
				BiInfo biInfo = new BiInfo();
				biInfo = this.getBiInfo((Integer)obj[1]);
				biUser.setBiInfo(biInfo);
				biUser.setUserName((String)obj[2]);
				biUser.setUserPassword((String)obj[3]);
				listUser.add(biUser);
			}
		}
		return listUser;
	}
	
	
	@Override
	public List<BiInfo> findPageByBiInfo(Page page) {
		List<BiInfo> listBiInfo = new ArrayList<BiInfo>();
		String sql = "select * from tb_bi_info";
		SQLQuery query = biInfoDao.getSQLQuery(sql);
		if (null != page) {
			int first = (page.getCurpage() - 1) * page.getPagesize();
			first=first<0?0:first;
			query.setFirstResult(first);
			query.setMaxResults(page.getPagesize());
		}
		List list = query.list();
		if(list!=null){
			for (int i = 0; i < list.size(); i++) {
				Object[] obj = (Object[]) list.get(i);
				BiInfo biInfo = new BiInfo();
				biInfo.setId((Integer)obj[0]);
				biInfo.setName((String)obj[1]);
				biInfo.setUrl((String)obj[2]);
				biInfo.setRemark((String)obj[3]);
				listBiInfo.add(biInfo);
			}
		}
		return listBiInfo;
	}

	/* (non-Javadoc)
	 * @see com.cntest.fxpt.service.bi.BiService#setHideParameForUrl(java.lang.String)
	 */
	@Override
	public String setHideParameForUrl(String url,List<String> listParams) {
		if(listParams!=null && listParams.size()>0){
			for (int i = 0; i < listParams.size(); i++) {
				url+="?"+listParams.get(i)+" ";
			}
		}
		return url;
	}

	

}
