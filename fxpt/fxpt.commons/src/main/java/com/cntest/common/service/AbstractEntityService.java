/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.common.service;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.cntest.common.query.Query;
import com.cntest.common.repository.Repository;
import com.cntest.exception.BusinessException;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年6月7日
 * @version 1.0
 **/
public abstract class AbstractEntityService<T,PK extends Serializable> {
	private static Logger logger = LoggerFactory.getLogger(AbstractEntityService.class);
	
	private Repository<T, PK> repository;

	@Transactional
	public void create(T t) throws BusinessException{
		logger.debug("Create {} ....",t);
		repository.save(t);
		logger.debug("Create {} success!",t);
	}
	
	@Transactional(readOnly=true)
	public T load(PK pk) throws BusinessException{
		if(pk.toString().equals("-1"))
			return null;
		T t = repository.load(pk);
		if(t == null)
			t = repository.get(pk);
		return t;
	}
	
	@Transactional
	public void update(T t) throws BusinessException{
		logger.debug("Update {} ....",t);
		repository.update(t);
		logger.debug("Update {} success!",t);
	}
	
	@Transactional
	public void remove(T t) throws BusinessException{
		logger.debug("Remove {} ....",t);
		repository.delete(t);
		logger.debug("Remove {} success!",t);
	}
	
	@Transactional
	public void remove(T... ts) throws BusinessException{
		if(ts != null && ts.length >0 ) {
			for(T t:ts) {
				logger.debug("Remove {} ....",t);
				repository.delete(t);
				logger.debug("Remove {} success!",t);
			}			
		}
	}
	
	protected void setRepository(Repository<T, PK> repository) {
		this.repository = repository;
	}
	
	@Transactional(readOnly=true)
	public List<T> list()throws BusinessException{
		return repository.list();
	}
	
	@Transactional(readOnly=true)
	public List<T> list(com.cntest.common.page.Page<T> query)throws BusinessException{
		return repository.list(query);
	}
	
	@Transactional
	public void query(Query<T> query) throws BusinessException{
		repository.query(query);
	}
	@Transactional(readOnly=true)
	public boolean validateName(String value,Long pk) throws BusinessException{
		if(pk == null) 
			pk = -1L;
		return repository.validateOnly("name",value,pk);
	}
}

