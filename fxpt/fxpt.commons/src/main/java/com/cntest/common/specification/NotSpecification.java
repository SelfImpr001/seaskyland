/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.common.specification;
/** 
 * <pre>
 * not 规格实现，实现两种规格的非关系
 * </pre>
 *  
 * @author 李贵庆2014年10月28日
 * @version 1.0
 **/
public class NotSpecification<T> extends AbstractSpecification<T> {

	private Specification<T> spec;
	
	public NotSpecification(Specification<T> spec) {
		this.spec = spec;
	}



	@Override
	public boolean isInSatified(T t) {
		return !this.spec.isInSatified(t);
	}

}

