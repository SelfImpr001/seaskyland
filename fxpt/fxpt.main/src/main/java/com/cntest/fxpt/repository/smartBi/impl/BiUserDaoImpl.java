/**
 * 
 */
package com.cntest.fxpt.repository.smartBi.impl;

import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.cntest.fxpt.bi.domain.BiUser;
import com.cntest.fxpt.repository.smartBi.IBiUserDao;
import com.cntest.common.repository.AbstractHibernateDao;

/**
 * @author Administrator
 *
 */
@Repository("bi.biUserDao")
public class BiUserDaoImpl extends AbstractHibernateDao<BiUser, Integer> implements IBiUserDao{

	@Override
	public BiUser getBiUser(Integer id) {
		return this.load(id);
	}

	
	@Override
	public List<BiUser> getBiUsersByBiInfoId(Integer biInfoId) {
		String hql="from BiUser where biInfo.id=?";
		return findByHql(hql, biInfoId);
	}

	
	@Override
	protected Class<BiUser> getEntityClass() {
		return BiUser.class;
	}


	@Override
	public void addBiUser(BiUser biUser) {
		this.save(biUser);
	}


	@Override
	public void updateBiUser(BiUser biUser) {
		this.update(biUser);
	}


	@Override
	public void deleteBiUser(BiUser biUser) {
		this.delete(biUser);
	}


	@Override
	public List<BiUser> getAllBiUsers() {
		String hql="from BiUser";
		return findByHql(hql);
	}


	@Override
	public SQLQuery getSQLQuery(String sql) {
		return getSession().createSQLQuery(sql);
	}

}
