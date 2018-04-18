/*
 * @(#)com.cntest.fxpt.etl.EtlContextFactory.java	1.0 2014年5月9日:下午6:34:46
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl;

import com.cntest.fxpt.etl.impl.DefaultEtlContext;
import com.cntest.fxpt.etl.impl.DefaultStepHops;

/**
 * <Pre>
 * 负责创建etl上下文对象
 * </Pre>
 * 
 * @author 刘海林 2014年5月9日 下午6:34:46
 * @version 1.0
 */
public class EtlFactory {
	public static IEtlContext createContext(){
		DefaultEtlContext context = new DefaultEtlContext(); 
		return context;
	}
	public static IStepHops createStepHops(IStepMetadata stepMetadata){
		DefaultStepHops stepHops = new DefaultStepHops(stepMetadata);
		return stepHops;
	}
}
