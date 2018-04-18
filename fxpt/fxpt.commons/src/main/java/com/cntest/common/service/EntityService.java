/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.common.service;

import java.io.Serializable;
import java.util.List;

import com.cntest.common.page.Page;
import com.cntest.common.query.Query;
import com.cntest.exception.BusinessException;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年6月7日
 * @version 1.0
 **/
public interface EntityService<T,PK extends Serializable> {

	void create(T t) throws BusinessException;
	
	T load(PK pk) throws BusinessException;
	
	void update(T t) throws BusinessException;
	
	void remove(T t) throws BusinessException;
	
	void remove(T... ts) throws BusinessException;
	
	List<T> list() throws BusinessException;
	
	void query(Query<T> query) throws BusinessException;
	
	/**
	 * 验证对象名称的唯一性
	 * @param value 值
	 * @param 主键   新增则给null
	 * @throws BusinessException
	 */
	boolean validateName(String value,Long pk) throws BusinessException;
}

