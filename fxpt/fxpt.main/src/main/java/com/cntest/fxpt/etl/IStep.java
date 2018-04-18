/**
 * 
 */
package com.cntest.fxpt.etl;

/**
 * @author 刘海林 步骤执行接口
 */
public interface IStep {
	public void begin() throws Exception;
	public void end() throws Exception;
	public void excuteStep() throws Exception;
	public IStepMetadata getStepMetadata();
}
