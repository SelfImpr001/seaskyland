/**
 * <p><b>© 1997-2013 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/
package com.cntest.common.repository;

import java.io.Serializable;
import java.util.List;

import com.cntest.common.query.Query;



/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆 2013-8-2
 * @version 1.0
 **/
public interface Repository<T,PK extends Serializable> {

	public void save(T t);
	
	public void saveOrUpdate(T t);
	
	public void update(T t);
	
	public T load(PK pk);
	
	public T get(PK pk);
	
	public T findByExample(T t);
	
	public void delete(T t);
	
	public List<T> list();
	public List<T> list(com.cntest.common.page.Page<T> page);
	
	public void query(Query<T> query);
	/**
	 * 验证唯一
	 * @param property 属性名
	 * @param param    值
	 */
	public boolean validateOnly(String property,String param,Long pk);
	/**
	 * 根据属性查找实体
	 * @param property 属性名
	 * @param param    值
	 */
	public T getEntityByProperty(String property,String param);
	
}

