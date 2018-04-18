package com.cntest.data;

import java.util.List;

import com.google.common.base.Predicate;

/**
 * 数据集的行
 * 
 * @author liguiqing
 *
 */
public interface Row {
	/**
	 * 讀取行的全部列
	 * 
	 * @return
	 */
	public List<Column> cols();

	/**
	 * 读取行的某列，如果posion超出范围，返回null
	 * 数值为正整数
	 * @param position
	 * @return
	 */
	public Column colAt(int position);

	/**
	 * 读取行的总列数
	 * @return
	 */
	public int size();
	
	public int rowNumber();
}
