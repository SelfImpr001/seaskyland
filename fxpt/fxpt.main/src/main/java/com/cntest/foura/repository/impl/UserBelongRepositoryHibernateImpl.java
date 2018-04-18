/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cntest.common.query.Query;
import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.exception.BusinessException;
import com.cntest.foura.domain.Organization;
import com.cntest.foura.domain.User;
import com.cntest.foura.domain.UserBelong;
import com.cntest.foura.repository.UserBelongRepository;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 肖 肖 2014年6月18日
 * @version 1.0
 **/
@Repository
@SuppressWarnings({ "unused", "unchecked" })
public class UserBelongRepositoryHibernateImpl extends
		AbstractHibernateDao<UserBelong, Long> implements UserBelongRepository {

	private static Logger logger = LoggerFactory
			.getLogger(UserBelongRepositoryHibernateImpl.class);

	@Override
	protected Class<UserBelong> getEntityClass() {
		return UserBelong.class;
	}

	@Override
	public List<UserBelong> getBelongByUserPk(User user) {
		logger.info("getBelongByUserPk({})", user);
		Criteria criteria = null;
		if (user.getPk() != null && user.getPk() >= 0) {
			criteria = this.getSession().createCriteria(UserBelong.class)
					.add(Restrictions.eq("user.pk", user.getPk()));
		} else {
			criteria = this.getSession().createCriteria(UserBelong.class);
			criteria.createAlias("user", "user", JoinType.LEFT_OUTER_JOIN);
			criteria.add(Restrictions.eq("user.name", user.getName()));
		}

		return criteria.list();
	}
	
	

	@Override
	public Integer removeBelongByUser(User user) {
		logger.info("removeBelongByUserAndOrgs({})", user);
		StringBuffer sb = new StringBuffer(
				"delete FROM 4a_userBelong where user_id = ? ");

		logger.info("executeBySQL({},{})", sb, user.getPk());
		return executeBySQL(sb.toString(), user.getPk());
	}

	@Override
	public List<UserBelong> getBelongByUserAll(String orgIds) {
		String hql="select * from 4a_userBelong where org_id IN("+orgIds+")";
		SQLQuery query = getSession().createSQLQuery(hql);
		return query.list();
	}

	@Override
	public void deleteBelongUser(Long id) {
		String sql ="delete from 4a_userBelong where uBelong_id=?";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setLong(0,id);
		query.executeUpdate();
	}
	@Override
	public List getUserByOrg(String orgList) throws BusinessException {
		List<UserBelong> userList= new ArrayList<UserBelong>();
		if(!"".equals(orgList)){
			String sql = "select DISTINCT(user_id) from 4a_userBelong where org_id in("+orgList
					+ ")";
			SQLQuery query = getSession().createSQLQuery(sql);
			userList=query.list();
		}
		
		return userList;
	}
}
