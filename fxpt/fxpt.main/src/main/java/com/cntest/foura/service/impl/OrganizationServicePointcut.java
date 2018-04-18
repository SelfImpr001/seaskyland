/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.service.impl;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.cntest.common.validation.Validator;
import com.cntest.exception.BusinessException;
import com.cntest.foura.domain.Organization;
import com.cntest.foura.repository.impl.OrganizationRepositoryHibernateImpl;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年10月31日
 * @version 1.0
 **/

@Aspect
@Component("OrganizationServicePointcut")
public class OrganizationServicePointcut {
	private static Logger logger = LoggerFactory.getLogger(OrganizationServicePointcut.class);
	
	@Autowired
	private OrganizationRepositoryHibernateImpl orgRepository;
	
	@Autowired(required=false)
	@Qualifier("OrganizationCreateValidationEntry")
	private Validator<Organization> createValidator;
	
	@Autowired(required=false)
	@Qualifier("OrganizationUpdateValidationEntry")
	private Validator<Organization> upateValidator;
	
	@Autowired(required=false)
	@Qualifier("OrganizationRemoveValidationEntry")
	private Validator<Organization> deleteValidator;
	
	@Before("execution(* com.cntest.foura.service..OrganizationService+.create(com.cntest.foura.domain.Organization))")
	public void beforeCreateAdvice(JoinPoint jp) throws BusinessException{
		
		Organization org = (Organization)jp.getArgs()[0];
		logger.debug("create Org validation:{}",org);
		if(createValidator!=null)
			createValidator.validate(org);
		logger.debug("create Org validation success :" + org.toString());
		
	}

	@Before("execution(* com.cntest.foura.service..OrganizationService+.update(com.cntest.foura.domain.Organization))")
	public void beforeUpdateAdvice(JoinPoint jp) throws BusinessException{
		Organization org = (Organization)jp.getArgs()[0];
		logger.debug("upate Org validate:{}",org);
		if(upateValidator!=null)
			upateValidator.validate(org);
		logger.debug("create Org validation success :" + org.toString());
	
	}
	
	@Before("execution(* com.cntest.foura.service..OrganizationService+.remove(com.cntest.foura.domain.Organization))")
	public void beforeRemoveAdvice(JoinPoint jp) throws BusinessException{
		Organization org = (Organization)jp.getArgs()[0];
		logger.debug("delete Org validate:{}",org);
		if(deleteValidator!=null)
			deleteValidator.validate(org);
		logger.debug("delete Org validation success :" + org.toString());
		
	}
}

