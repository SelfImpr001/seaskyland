package com.cntest.data;

import com.google.common.base.Function;

/**
 * 验证数据集中的列
 * @author liguiqing
 *
 */
public interface Column {

	public <T> T value(Function<Object,T> function);
	
	public String value();
}
