/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.common.specification;
/** 
 * <pre>
 * 规格接口，描述对象是否满足特定的规格
 * </pre>
 *  
 * @author 李贵庆2014年10月28日
 * @version 1.0
 **/
public interface Specification<T> {

	boolean isInSatified(T t);
	
	Specification<T> and(Specification<T> other);
	
	Specification<T> or(Specification<T> other);
	
	Specification<T> not(Specification<T> other);
}

