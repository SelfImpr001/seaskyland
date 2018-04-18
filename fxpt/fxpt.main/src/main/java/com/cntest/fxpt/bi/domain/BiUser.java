/**
 * 
 */
package com.cntest.fxpt.bi.domain;

import java.text.MessageFormat;

/**
 * @author Administrator
 * 
 */
public class BiUser {
	private Integer id;
	private String userName;
	private String userPassword;
	private BiInfo biInfo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BiInfo getBiInfo() {
		return biInfo;
	}

	public void setBiInfo(BiInfo biInfo) {
		this.biInfo = biInfo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String toString() {
		return MessageFormat.format("id:{0},name:{1},password:{2}",
				this.getId(), this.getUserName(), this.getUserPassword());
	}

}
