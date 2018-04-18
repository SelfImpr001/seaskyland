/**
 * 
 */
package com.cntest.fxpt.bi;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * @author Administrator
 *
 */
public class BIConnectorPoolMgr {
	private final String poolName = "default";
	
	private static ConcurrentMap<String, BIConnectorPool> biConnectorPoolMap = new ConcurrentHashMap<String, BIConnectorPool>();
	
	public void setDefaultConnectorPool(BIConnectorPool biConnectorPool){
		setConnectorPool(poolName, biConnectorPool);
	}
	public void setConnectorPool(String poolName,BIConnectorPool biConnectorPool){
		biConnectorPoolMap.put(poolName, biConnectorPool);
	}
	
	public BIConnectorPool getBIConnectorPool(){
		return biConnectorPoolMap.get(poolName);
	}
	public BIConnector getBIConnector(){
		return getBIConnectorPool().getConnector();
	}
	
	
}
