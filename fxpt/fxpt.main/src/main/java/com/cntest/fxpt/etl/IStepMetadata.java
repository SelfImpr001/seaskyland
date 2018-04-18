/**
 * 
 */
package com.cntest.fxpt.etl;

import java.util.HashMap;

import com.cntest.fxpt.etl.domain.RowSet;
import com.cntest.fxpt.etl.domain.StepLog;

/**
 * @author 刘海林 步骤元数据接口
 */
public interface IStepMetadata {
	public void registerParentStepHops(IStepHops stepHops);

	public void registerChildStepHops(IStepHops stepHops);

	public IStep getStep();

	public String getName();

	/**
	 * <Pre>
	 * 判断正果步骤是否完毕
	 * </Pre>
	 * 
	 * @return
	 * @return boolean
	 * @author:刘海林 2014年5月9日 下午2:10:16
	 */
	public boolean isFinish();

	public int getWarnRowNumber();

	public int getFailRowNumber();

	public int getIgnoreRowNumber();

	public int getSuccessRowNumber();

	public void putRow(RowSet row);

	public RowSet getRow();

	public StepLog getStepLog();

	public IEtlContext getContext();

	public HashMap<String, Object> getStepParameter();

	public String getPk();
}
