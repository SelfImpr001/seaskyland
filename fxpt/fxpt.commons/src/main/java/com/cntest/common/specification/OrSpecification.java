/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.common.specification;
/** 
 * <pre>
 * OR 规格实现，实现两种规格的或关系
 * </pre>
 *  
 * @author 李贵庆2014年10月28日
 * @version 1.0
 **/
public class OrSpecification<T> extends AbstractSpecification<T> {

	private Specification<T> spec1;
	
	private Specification<T> spec2;

	public OrSpecification(Specification<T> spec1, Specification<T> spec2) {
		this.spec1 = spec1;
		this.spec2 = spec2;
	}



	@Override
	public boolean isInSatified(T t) {
		return this.spec1.isInSatified(t) && this.spec2.isInSatified(t);
	}

}

