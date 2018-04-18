/**
 * 
 */
package com.cntest.fxpt.etl;

import com.cntest.fxpt.etl.domain.RowSet;

/**
 * <pre>
 * 建立步骤之间的关系,连接上一个步骤和他的子步骤;处理步骤之间的数据传送
 * </pre>
 * 
 * @author 刘海林
 */
public interface IStepHops {
	public IStepMetadata getStepMetadata();

	public void putRow(RowSet row);

	public RowSet getRow();

	public void addSetp(IStep step);

	public void execSetp() throws Exception;
}
