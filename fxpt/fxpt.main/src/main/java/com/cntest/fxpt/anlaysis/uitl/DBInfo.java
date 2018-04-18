/*
 * @(#)com.cntest.fxpt.anlaysis.uitl.DBInfo.java	1.0 2014年12月10日:下午5:18:24
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.uitl;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

import javax.sql.DataSource;

import com.cntest.util.SpringContext;
import com.mysql.jdbc.ConnectionImpl;
import com.mysql.jdbc.JDBC4Connection;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年12月10日 下午5:18:24
 * @version 1.0
 */
public class DBInfo {

	private String user;
	private String password;
	private String dbName;
	private String host;
	private String port;

	public DBInfo() {
		init();
	}

	private void init() {
		DataSource ds = SpringContext.getBean("ds");
		Connection con = null;
		try {
			con = ds.getConnection();
			DatabaseMetaData dmd = con.getMetaData();
			if (dmd.getConnection() instanceof com.mysql.jdbc.JDBC4Connection) {
				JDBC4Connection con2 = (JDBC4Connection) dmd.getConnection();

				Field f = ConnectionImpl.class.getDeclaredField("host");
				f.setAccessible(true);
				host = f.get(con2).toString();
				f = ConnectionImpl.class.getDeclaredField("database");
				f.setAccessible(true);
				dbName = f.get(con2).toString();
				f = ConnectionImpl.class.getDeclaredField("password");
				f.setAccessible(true);
				password = f.get(con2).toString();
				f = ConnectionImpl.class.getDeclaredField("user");
				f.setAccessible(true);
				user = f.get(con2).toString();
				f = ConnectionImpl.class.getDeclaredField("port");
				f.setAccessible(true);
				port = f.get(con2).toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public String getDbName() {
		return dbName;
	}

	public String getHost() {
		return host;
	}

	public String getPort() {
		return port;
	}

	@Override
	public String toString() {
		return "user:" + user + ";password:" + password + ";host:" + host
				+ ";port:" + port + ";dbName" + dbName;
	}
}
