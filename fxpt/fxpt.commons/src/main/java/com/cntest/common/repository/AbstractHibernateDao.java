/**
 * <p><b>© 1997-2013 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/
package com.cntest.common.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;




/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆 2013-8-2
 * @version 1.0
 **/
public abstract class AbstractHibernateDao<T,PK extends Serializable> extends AbastractDao{
	private static Logger logger = LoggerFactory.getLogger(AbstractHibernateDao.class);
	
	SessionFactory sessionFactory;
	
	@Resource(name="sessionFactory")
	public void setSessionFactory0(SessionFactory sessionFactory){
	  this.sessionFactory = sessionFactory;
	 
	}
	
	@Autowired
	protected DynamicHibernateTemplate dynamicHibernateTemplate;
	
	public T findByExample(T t) {
		return null;
	}
	
	protected abstract Class<T> getEntityClass();
	
	protected Session getCurrentSession() {
		return this.sessionFactory.getCurrentSession();
	}
	
	public void save(T t) {
		this.getSession().save(t);
	}
	
	public void saveOrUpdate(T t) {
		this.getSession().saveOrUpdate(t);
	}
	
	public void update(T t) {
		this.getSession().update(t);
	}

	public T load(PK id) {
		Class<T> cls = getEntityClass();
		return (T) this.getSession().load(cls, id);
	}

	public T get(PK id) {
		Class<T> cls = getEntityClass();
		return (T)  this.getSession().get(cls, id);
	}

	public void delete(T t) {
		this.getSession().delete(t);
	}

	public T findEntityByHql(String queryString,Object... params) {

		List<T> objs =  findByHql(queryString,params);
		if(objs != null && objs.size() > 0)
			return objs.get(0);
		return null;
	}
	
	public List<T> findByHql(String queryString,Object... params) {
		Session session = getSession();
		Query query = session.createQuery(queryString);
		if(params !=null) {
			int position = 0;
			for(Object val:params) {
				if(val instanceof String)
					query.setString(position++, val+"");
				else if(val instanceof Long)
					query.setLong(position++, (Long)val);
				else if(val instanceof Integer)
					query.setInteger(position++, (Integer)val);
				else if(val instanceof Date)
					query.setDate(position++, (Date)val);
				else if(val instanceof Boolean)
					query.setBoolean(position++, (Boolean)val);
			}
		}
		return query.list();
	}
	
	public List<T> findByHqlLimit(String queryString,int start,int size,Object... params) {
		Session session = getSession();
		Query query = session.createQuery(queryString);
		if(params !=null) {
			int position = 0;
			for(Object val:params) {
				if(val instanceof String)
					query.setString(position++, val+"");
				else if(val instanceof Long)
					query.setLong(position++, (Long)val);
				else if(val instanceof Integer)
					query.setInteger(position++, (Integer)val);
				else if(val instanceof Date)
					query.setDate(position++, (Date)val);
				else if(val instanceof Boolean)
					query.setBoolean(position++, (Boolean)val);
			}
		}
		query.setMaxResults(size);
		query.setFirstResult(start);
		return query.list();
	}
	
	protected SQLQuery createSQLQuery(String queryString) {
		Session session = getSession();
		return session.createSQLQuery(queryString);
	}
	
	protected Session getSession() {
		try {
			return this.sessionFactory.getCurrentSession();
		}catch(Exception e) {
			
		}

		return this.sessionFactory.openSession();
	}

	 /**
     * 批量创建
     * @param <T>
     * @param entitys
     */
	public <T> void batchSave(List<T> entitys){
		for(T entity : entitys){
			this.getSession().persist(entity);
		}
	}
	/**
     * 执行SQL语句
     * @param sql
     * @param params
     * @return
     */
    public int executeBySQL(String sql, Object... params){
    	Query query = this.getSession().createSQLQuery(sql);
		if (params != null) {
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					logger.debug(i + " " + params[i]);
					query.setParameter(i, params[i]);
				}
			}
		}
		return query.executeUpdate();
    }
    
    /**
     * 根据主键删除实体
     * @param <T>
     * @param id      主键
     */
    public <T> void delete(PK id){
    	Class<T> clazz = (Class<T>) getEntityClass();
    	T t = (T)this.getSession().get(clazz,id);
		this.getSession().delete(id);
    }
   /**
    * 批量删除实体
    * @param ids
    */
    public <T> void deleteAll(List<T> list){
    	Class<T> clazz = (Class<T>) getEntityClass();
    	T t = null;
    	for(Object id : list){
    		t = (T)this.getSession().get(clazz,(Serializable) id);
    		this.getSession().delete(id);
    	}
    }
    /**
     * 获取列表
     * @param page
     * @return
     */
    public List<T> list(){
    	Criteria criteria = this.getSession().createCriteria(
				this.getEntityClass().getName());

		@SuppressWarnings("unchecked")
		List<T> list = criteria.list();

		return list;
    }
    @Deprecated
    public List<T> list(com.cntest.common.page.Page<T> query){
    	
    	return null;
    }
    /**
     * 分页查询列表    按条件查询需重写此方法
     * @param 
     * @return
     */
    public void query(com.cntest.common.query.Query<T> query) {
		Criteria criteria = createCriteria();
		Map<String, String[]> param = query.getParameters();
		ProjectionList ps = Projections.projectionList();
		//ps.add(Projections.distinct(Projections.property("name")));
		ps.add(Projections.rowCount());
		
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		Object o = criteria.setProjection(ps).uniqueResult();
		//Long rowCount = (Long)((Object[]) o)[1];
		Long rowCount = (Long)o;
		query.setTotalRows(rowCount.intValue());
		criteria.setProjection(null);
		if(param.containsKey("order")) {
			/* value name asc  */
			String[] order = param.get("order");
			for (int i = 0; i < order.length; i++) {
				if(order[i].split(" ")[1].toLowerCase().equals("asc")) {
					criteria.addOrder(Order.asc(order[i].split(" ")[0]));
				}else {
					criteria.addOrder(Order.desc(order[i].split(" ")[0]));
				}
			}
		}else {
			criteria.addOrder(Order.desc("pk"));
		}
		criteria.setFirstResult(query.getStartRow());
		criteria.setMaxResults(query.getPagesize());
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		query.setResults(criteria.list());
	}
    protected Criteria createCriteria() {
    	Criteria c= this.getSession().createCriteria(getEntityClass());
    	return c;
    }
    public boolean validateOnly(String property,String param,Long pk) {
    	Criteria criteria = createCriteria();
    	criteria.add(Restrictions.eq(property, param))
    			.add(Restrictions.ne("pk", pk));
    	
    	return criteria.list().size() > 0;
	}
    public T getEntityByProperty(String property,String param) {
    	Criteria criteria = createCriteria();
    	criteria.add(Restrictions.eq(property, param));
		return (T) criteria.uniqueResult();
    }
}

