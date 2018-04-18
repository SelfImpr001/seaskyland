package com.cntest.data;

import java.io.Closeable;

/**
 * 先验证的数据集
 * @author liguiqing
 *
 */

public interface DataSet extends Closeable{
	/**
	 * 返回数据集的下一行，首次调用时返回第一行
	 * @return row 无数据或者到达最后一个Row返回后返回Null
	 */
	Row next();
	
	void open();
}
