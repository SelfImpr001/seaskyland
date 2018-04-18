/*
 * @(#)com.cntest.fxpt.etl.business.IImportTemplateService.java	1.0 2014年10月10日:上午10:03:48
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月10日 上午10:03:48
 * @version 1.0
 */
public interface IImportTemplateService {
	public void template(OutputStream out) throws Exception;
	public void template(OutputStream out,HttpServletRequest request,HttpServletResponse response) throws Exception;

}
