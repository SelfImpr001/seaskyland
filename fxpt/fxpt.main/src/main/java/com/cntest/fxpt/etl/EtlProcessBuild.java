/*
 * @(#)com.cntest.fxpt.etl.EtlProcessBuild.java	1.0 2014年5月19日:下午5:05:22
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl;

import com.cntest.fxpt.etl.impl.DefaultStepHops;

/**
 * <Pre>
 * 构建etl过程
 * </Pre>
 * 
 * @author 刘海林 2014年5月19日 下午5:05:22
 * @version 1.0
 */
public abstract class EtlProcessBuild {

	public abstract IStep createInput();

	public abstract IStep createTransform();

	public abstract IStep createMappingField();

	public abstract IStep createValidate();

	public abstract IStep createLoad();

	public IStep build() {
		IStep input = createInput();

		DefaultStepHops hops = new DefaultStepHops(input.getStepMetadata());
		IStep transform = createTransform();
		if (transform != null) {
			hops.addSetp(transform);
			hops = new DefaultStepHops(transform.getStepMetadata());
		}

		IStep mappingField = createMappingField();
		if (mappingField != null) {
			hops.addSetp(mappingField);
			hops = new DefaultStepHops(mappingField.getStepMetadata());
		}

		IStep validate = createValidate();
		if (validate != null) {
			hops.addSetp(validate);
			hops = new DefaultStepHops(validate.getStepMetadata());
		}
		IStep load = createLoad();
		if (validate != null) {
			hops.addSetp(load);
		}
		return input;
	}

	public abstract IEtlContext getContext();
}
