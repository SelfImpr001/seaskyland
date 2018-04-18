/**
 * <p><b>© 2001-2013 CC</b></p>
 * 
 **/

package com.cntest.common.event;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;


/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆 2013-8-17
 * @version 1.0
 **/
public abstract class RepositoryEvent {

//	JdbcTemplate jdbcTemplate;
	
	SessionFactory sessionFactory;
	
	protected Session session;
	
	Transaction tx;
	
	int count = 0;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	protected void beginTx() {
		this.session = this.sessionFactory.openSession();
		this.tx = this.session.beginTransaction();
	}
	
	protected void save(Object o) {
		if(this.session == null)
			beginTx();
		this.session.merge(o);
		if(count%50 == 0) {
			this.session.flush();
			this.session.clear();
		}
		count++;
	}
	
	protected void save(String sql) {
		
	}
	
	protected void commit() {
		this.session.flush();
		this.session.clear();
		tx.commit();
		session.close();
	}
	
}
