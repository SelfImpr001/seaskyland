/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.service.impl;

import com.cntest.common.validation.Validator;
import com.cntest.exception.BusinessException;
import com.cntest.foura.domain.Organization;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年10月31日
 * @version 1.0
 **/
public class OrganizationValidationEntry implements Validator<Organization> {

	private Validator<Organization> next;
	
	public OrganizationValidationEntry(Validator<Organization> next) {
		this.next = next;
	}
	
	@Override
	public void validate(Organization t) throws BusinessException {
		if(this.next != null)
			this.next.validate(t);
	}

}

