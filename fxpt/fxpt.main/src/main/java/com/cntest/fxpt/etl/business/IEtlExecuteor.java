/*
 * @(#)com.cntest.fxpt.etl.business.IEtlExecuteor.java	1.0 2014年10月9日:下午5:09:57
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business;

import com.cntest.fxpt.bean.WebRetrieveResult;
import com.cntest.fxpt.etl.domain.EtlProcessResult;

/**
 * <Pre>
 * 导入数据处理程序
 * </Pre>
 * 
 * @author 刘海林 2014年10月9日 下午5:09:57
 * @version 1.0
 */
public interface IEtlExecuteor {
	public boolean executeClean(WebRetrieveResult webRetrieveResult);
	public EtlProcessResult execute(WebRetrieveResult webRetrieveResult) throws Exception;
}
