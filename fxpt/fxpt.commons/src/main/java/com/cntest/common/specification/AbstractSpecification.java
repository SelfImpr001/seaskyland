/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.common.specification;
/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年10月28日
 * @version 1.0
 **/
public abstract class AbstractSpecification<T> implements Specification<T>{
	public abstract boolean isInSatified(T t);
	public Specification<T> and(Specification<T> other){
		return new AndSpecification<T>(this,other);
	};
	
	public Specification<T> or(Specification<T> other){
		return new OrSpecification<T>(this,other);
	}
	
	public Specification<T> not(Specification<T> spec){
		return new NotSpecification<T>(spec);
	}
}

