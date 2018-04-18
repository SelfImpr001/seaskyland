/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.repository.impl;


import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.exception.BusinessException;
import com.cntest.foura.domain.Organization;
import com.cntest.foura.domain.Role;
import com.cntest.foura.domain.URLResource;
import com.cntest.foura.domain.User;
import com.cntest.foura.domain.UserResource;
import com.cntest.foura.domain.UserRole;
import com.cntest.foura.repository.UserRepository;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年6月7日
 * @version 1.0
 **/
@Repository
public class UserRepositoryHibernateImpl extends AbstractHibernateDao<User, Long> implements UserRepository {
	private static Logger logger = LoggerFactory.getLogger(UserRepositoryHibernateImpl.class);
	
	@Override
	protected Class<User> getEntityClass() {
		return User.class;
	}

	@Override
	public User selectUserBy(String userName) {
		logger.debug("Select User by userName :{}",userName);
		return super.findEntityByHql("From User where name=?", userName);
	}

	@Override
	public List<User> list() {
		return findByHql("from User");
	}
	@Override
	public List<User> list(Long role) {
		StringBuffer sql = new StringBuffer(" SELECT * FROM 4a_user  u WHERE u.user_id NOT IN ( SELECT r.user_id FROM 4a_user_role r WHERE role_id = ? ) ");
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setLong(0, role);
		query.addEntity("u", User.class);
		List<User> lists = (List<User>)query.list();
		return lists;
	}
	
//	@Override
//	public Long findUsers(List<User> result, int first, int max, Map<String, String[]> parameters) {
//		Criteria criteria = createCriteria();
//		if(parameters != null) {
//		
//			if(parameters.get("q")!= null) {
//				String q = parameters.get("q")[0];
//				//criteria.add(Restrictions.like("name", "%" + q + "%"));
//				criteria.createAlias("userInfo", "a").add(Restrictions.like("a.realName", "%" + q + "%"));
//				criteria.add(Restrictions.or(Restrictions.like("userInfo.realName", "%" + q + "%"), Restrictions.like("name", "%" + q + "%")));
//				//criteria.add(Restrictions.or(Restrictions.like("userInfo.nickName", "%" + q + "%"), Restrictions.like("userInfo.cellphone", "%" + q + "%")));
//				//criteria.add(Restrictions.or(Restrictions.like("userInfo.email", "%" + q + "%"), Restrictions.like("userInfo.telphone", "%" + q + "%")));
//			}
//		}
//		
//		ProjectionList ps = Projections.projectionList();
//		//ps.add(Projections.distinct(Projections.property("name")));
//		ps.add(Projections.rowCount());
//		
//		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
//		Object o = criteria.setProjection(ps).uniqueResult();
//		//Long rowCount = (Long)((Object[]) o)[1];
//		Long rowCount = (Long)o;
//		criteria.setProjection(null);
//		criteria.setFirstResult(first);
//		criteria.setMaxResults(max);
//		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
//
//		result.addAll(criteria.list());
//		return rowCount;
//	}

	@Override
	public void deleteNullUserRole() {
		String sql = "delete from 4a_user_role where user_id is NULL";
		Query q = getSession().createSQLQuery(sql);
		q.executeUpdate();
	}

	@Override
	public User findSameUser(String name) {
		Criteria criteria = createCriteria();
		criteria.add(Restrictions.eq("name", name));
		User user = (User) criteria.uniqueResult();
		return user;
	}

	@Override
	public void findUsers(com.cntest.common.query.Query<User> query) {
		Map<String, String[]> parameters = query.getParameters();
		Criteria criteria = createCriteria();
		if(parameters != null) {
		
			if(parameters.get("q")!= null) {
				String q = parameters.get("q")[0];
				criteria.createAlias("userInfo", "a")
					.add(Restrictions.or(Restrictions.like("a.realName", "%" + q + "%"), 
							Restrictions.or(Restrictions.like("a.cellphone", "%" + q + "%"),
									Restrictions.or(Restrictions.like("a.nickName", "%" + q + "%"),
											Restrictions.or(Restrictions.like("a.email", "%" + q + "%"),
													Restrictions.or(Restrictions.like("a.telphone", "%" + q + "%"),Restrictions.like("name", "%" + q + "%")))))));
				    //.add(Restrictions.or(Restrictions.like("a.nickName", "%" + q + "%"), Restrictions.like("a.cellphone", "%" + q + "%")))
				    //.add(Restrictions.or(Restrictions.like("a.email", "%" + q + "%"), Restrictions.like("a.telphone", "%" + q + "%")));
			}
		}
		
		ProjectionList ps = Projections.projectionList();
		//ps.add(Projections.distinct(Projections.property("name")));
		ps.add(Projections.rowCount());
		criteria.add(Restrictions.isNotNull("userInfo"));
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		Object o = criteria.setProjection(ps).uniqueResult();
		//Long rowCount = (Long)((Object[]) o)[1];
		Long rowCount = (Long)o;
		query.setTotalRows(rowCount.intValue());
		criteria.setProjection(null);
		criteria.addOrder(Order.desc("pk"));
		criteria.setFirstResult(query.getStartRow());
		criteria.setMaxResults(query.getPagesize());
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		
		query.setResults(criteria.list());
	}
	
	@Override
	public void deleteRolesByUserId(String userId) {
		StringBuffer sb=new StringBuffer();
		sb.append("delete from 4a_user_role where user_id in(").append(userId).append(")");
		createSQLQuery(sb.toString()).executeUpdate();
	}
	
	@Override
	public Set<Role> findRolesByUserId(Long userId) {
		Criteria criteria = this.getSession().createCriteria(UserRole.class);
		criteria.add(Restrictions.eq("user.id", userId));
		List<UserRole> userRoles=criteria.list();
		Set<Role> roles=new HashSet<Role>();
		for (UserRole userRole : userRoles) {
			roles.add(userRole.getRole());
		}
		return roles;
	}
	
	@Override
	public Set<URLResource> findUrlsByUserId(Long userId) {
		Criteria criteria = this.getSession().createCriteria(UserResource.class);
		criteria.add(Restrictions.eq("user.id", userId));
		List<UserResource> userResources=criteria.list();
		Set<URLResource> urls=new HashSet<URLResource>();
		for (UserResource userResource : userResources) {
			urls.add(userResource.getResource());
		}
		return urls;
	}
	
	@Override
	public Set<User> findUsersByRoleId(Long roleId) {
		Criteria criteria = this.getSession().createCriteria(UserRole.class);
		criteria.add(Restrictions.eq("role.id", roleId));
		List<UserRole> userRoles=criteria.list();
		Set<User> users=new HashSet<User>();
		for (UserRole userRole : userRoles) {
			users.add(userRole.getUser());
		}
		return users;
	}

	@Override
	public void deleteResByUserId(String  userId) {
		StringBuffer sb=new StringBuffer();
		sb.append("delete from 4a_user_resource where user_id in(").append(userId).append(")");
		createSQLQuery(sb.toString()).executeUpdate();
	}
	
	@Override
	public void findUsersByRoleId(com.cntest.common.query.Query<User> query) {
		Map<String, String[]> parameters = query.getParameters();
		Criteria criteria = createCriteria();
		if(parameters != null) {
			if(parameters.get("roleId")[0]!= null) {
				criteria.createAlias("roles", "roles")
				.add(Restrictions.eq("roles.role.pk", Long.valueOf(parameters.get("roleId")[0].toString())));
			}
		}
		
		ProjectionList ps = Projections.projectionList();
		//ps.add(Projections.distinct(Projections.property("name")));
		ps.add(Projections.rowCount());
		
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		Object o = criteria.setProjection(ps).uniqueResult();
		//Long rowCount = (Long)((Object[]) o)[1];
		Long rowCount = (Long)o;
		query.setTotalRows(rowCount.intValue());
		criteria.setProjection(null);
		criteria.addOrder(Order.asc("name"));
		criteria.setFirstResult(query.getStartRow());
		criteria.setMaxResults(query.getPagesize());
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

		query.setResults(criteria.list());
	}

	@Override
	public void addUserExamByPKandExamids(String examids, Long pk) {
		String[] str = examids.split(",");
		String sql = "insert into kn_user_exam(user_id,exam_id) values(?,?)";
		SQLQuery query =this.getSession().createSQLQuery(sql);
		for(int i=0;i<str.length;i++) {
			query.setLong(0, pk);
			query.setLong(1,Long.parseLong(str[i]));
			query.executeUpdate();
		}
	}

	@Override
	public void deleteUserExamByPk(String pk) {
		String sql = "delete from kn_user_exam where user_id in("+pk+")";
		SQLQuery query =this.getSession().createSQLQuery(sql);
		query.executeUpdate();
	}

	@Override
	public String getUserExamByRoleId(Long pk) {
		String examids ="";
		String sql = "select exam_id from kn_user_exam where user_id="+pk;
		SQLQuery query =this.getSession().createSQLQuery(sql);
		List list=query.list();
		if(list.size()>0) {
			examids=list.toString().replace("[", ",").replace("]", ",");
		}
		return examids;
		
	}

	@Override
	public void deleteUserExamByExamId(Long examid) {
		String sql = "delete from kn_user_exam where exam_id="+examid;
		SQLQuery query =this.getSession().createSQLQuery(sql);
		query.executeUpdate();
	}

	@Override
	public com.cntest.common.query.Query<Role> findUserCountByRoleId(com.cntest.common.query.Query<Role> result) {
		int num=0;
		for(int i=0;i<result.getResults().size();i++){
			String sql="select count(1) from 4a_user_role where role_id="+result.getResults().get(i).getPk();
			num=Integer.valueOf(getSession().createSQLQuery(sql).uniqueResult().toString());
			result.getResults().get(i).setRoleCount(num);
		}
		return result;
	}

	@Override
	public List findUserRoleByUserId(String userId) {
		String sql = "select *  from 4a_user_role where user_id="+userId;
		Query q = getSession().createSQLQuery(sql);
		return q.list();
	}
	
	@Override
	public List findUserResourceByUserId(String userId) {
		String sql = "select *  from 4a_user_resource where user_id="+userId;
		Query q = getSession().createSQLQuery(sql);
		return q.list();
	}

	@Override
	public void deleteUserByPk(String pks) {
		StringBuffer sb=new StringBuffer();
		sb.append("delete from 4a_user where user_id in(").append(pks).append(")");
		createSQLQuery(sb.toString()).executeUpdate();
	}
	
	public void deleteUserLink(String pks) {
		StringBuffer sb = new StringBuffer();
		sb.append(" DELETE 4a_data_authorized ,4a_userbelong FROM 4a_data_authorized ");
		sb.append(" LEFT JOIN 4a_userbelong ON 4a_data_authorized.target_id = 4a_userbelong.user_id ");
		sb.append(" WHERE 4a_data_authorized.target_id in(").append(pks).append(") ");
		createSQLQuery(sb.toString()).executeUpdate();
	}


	@Override
	public void findUsersByOrg(com.cntest.common.query.Query<User> query, Organization role) {
		String sql = "select user.* from 4a_user user,4a_userbelong org where user.user_id = org.user_id and org.org_id in("+role.getChildren()+")";
		Query q = getSession().createSQLQuery(sql);
		query.setTotalRows(q.list().size());
		sql+="limit "+query.getStartRow()+","+query.getPagesize();
		 q = getSession().createSQLQuery(sql);
		 ((SQLQuery) q).addEntity(User.class);
		List user =  q.list();
		 query.setResults(user);
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean powerIsExist(String userid, String examid) throws BusinessException {
		boolean flg=false;
		String sql = "select * from kn_user_exam where user_id="+userid+" and exam_id="+examid;
		SQLQuery query =this.getSession().createSQLQuery(sql);
		List list=query.list();
		if(list.size()>0) {
			flg=true;
		}
		return flg;
	}
	
	@Override
	public void evictSession(Object o ) {
		this.getSession().evict(o);
	}

	@Override
	public void deletePublishUserByUserName(String userName) {
		String sql="delete from kn_userandpassword where CODE in("+userName+")";
		createSQLQuery(sql).executeUpdate();
	}

	@Override
	public String selectMoreUserNameByPks(String pks) {
		StringBuffer sb=new StringBuffer();
		sb.append("select user_name from 4a_user where user_id in(").append(pks).append(")");
		List usernames=createSQLQuery(sb.toString()).list();
		return usernames.size()>0?usernames.toString():"";
	}
}

