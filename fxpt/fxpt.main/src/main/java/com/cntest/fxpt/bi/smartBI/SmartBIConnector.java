/**
 * 
 */
package com.cntest.fxpt.bi.smartBI;

import org.springframework.stereotype.Component;

import smartbi.sdk.ClientConnector;
import smartbi.sdk.catalog.CatalogService;

import com.cntest.fxpt.bi.BIConnector;

/**
 * @author Administrator
 * 
 */
@Component("bi.BIConnector")
public class SmartBIConnector implements BIConnector {
	private String userName;
	private String password;
	private String url;
	
	ClientConnector clientConnector;
	
	public ClientConnector getClientConnector() {
		return clientConnector;
	}

	public void setClientConnector(ClientConnector clientConnector) {
		this.clientConnector = clientConnector;
	}

	
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String getCookie() {
		return clientConnector.getCookie();
	}
	
	public boolean open(){
		clientConnector = new ClientConnector(url);
		return clientConnector.open(userName, password);
	}
	
	public boolean isValid(){
		boolean result = true;
		try {
			CatalogService scs = new CatalogService(clientConnector);
			scs.getRootElements();
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}

}
