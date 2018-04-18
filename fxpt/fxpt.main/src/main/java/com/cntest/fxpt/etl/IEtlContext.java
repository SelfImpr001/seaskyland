/**
 * 
 */
package com.cntest.fxpt.etl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator 记录etl过程的全局变量；
 */
public interface IEtlContext {
	public void registerSetp(IStep step);
	public IEtlContext addParameter(String paramName,Object value);
	public<T> T getParameter(String paramName);
	public IScriptContext getScriptContext();
	public List<IStep> getSteps();
}
