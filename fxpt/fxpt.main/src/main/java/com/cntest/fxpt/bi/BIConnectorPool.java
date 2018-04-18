/**
 * 
 */
package com.cntest.fxpt.bi;

/**
 * @author Administrator
 * 
 */
public interface BIConnectorPool {
	public BIConnector getConnector();
	public void clean();
}
