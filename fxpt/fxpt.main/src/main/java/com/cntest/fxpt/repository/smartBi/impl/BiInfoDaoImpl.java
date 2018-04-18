/**
 * 
 */
package com.cntest.fxpt.repository.smartBi.impl;

import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.cntest.fxpt.bi.domain.BiInfo;
import com.cntest.fxpt.repository.smartBi.IBiInfoDao;
import com.cntest.common.repository.AbstractHibernateDao;

/**
 * @author Administrator
 * 
 */
@Repository("bi.biInfoDao")
public class BiInfoDaoImpl extends AbstractHibernateDao<BiInfo, Integer>
		implements IBiInfoDao {

	@Override
	public BiInfo getBiInfo(Integer id) {
		return this.load(id);
	}

	@Override
	protected Class<BiInfo> getEntityClass() {
		return BiInfo.class;
	}

	@Override
	public void addBiInfo(BiInfo biInfo) {
		this.save(biInfo);
	}

	@Override
	public void updateBiInfo(BiInfo biInfo) {
		this.update(biInfo);
	}

	@Override
	public void deleteBiInfo(BiInfo biInfo) {
		this.delete(biInfo);
	}

	@Override
	public List<BiInfo> getBiInfoList() {
		String hql="from BiInfo";
		return findByHql(hql);
	}
	
	@Override
	public SQLQuery getSQLQuery(String sql) {
		return getSession().createSQLQuery(sql);
	}

}
