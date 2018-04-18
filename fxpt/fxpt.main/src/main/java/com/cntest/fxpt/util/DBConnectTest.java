package com.cntest.fxpt.util;

import java.sql.*;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

	/**
	 * 数据库连接测试类
	 * @author chenyou
	 *
	 */
public class DBConnectTest {
	
public static  boolean getConnection(String serviceName,String baseName,String userName,String password) {
	boolean flg=false;
	MysqlConnectionPoolDataSource ds=new MysqlConnectionPoolDataSource();
	ds.setServerName(serviceName);
	ds.setDatabaseName(baseName);
	ds.setUser(userName);
	ds.setPassword(password);
	Connection con = null;
	try {
	    con = ds.getConnection();
	    flg=true;
	} catch (SQLException ex) {
	  //  ex.printStackTrace();
	}
    return flg;
 }
}