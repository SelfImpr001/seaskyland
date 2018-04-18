/*
 * @(#)com.cntest.fxpt.etl.business.IEtlExecuteor.java	1.0 2014年10月9日:下午5:09:57
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.cntest.fxpt.bean.WebRetrieveResult;

/**
 * <Pre>
 * 导入数据处理程序
 * </Pre>
 * 
 * @author 刘海林 2014年10月9日 下午5:09:57
 * @version 1.0
 */
public interface IEtlExecuteorForItem extends IEtlExecuteor {
	public Map<String, String> batchExecute(
			Map<String, WebRetrieveResult> webRetrieveResultMap)
			throws Exception;
}
