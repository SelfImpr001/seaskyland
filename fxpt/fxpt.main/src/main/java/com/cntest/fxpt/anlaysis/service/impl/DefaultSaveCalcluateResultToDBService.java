/*
 * @(#)com.cntest.fxpt.anlaysis.service.impl.BatchSaveCalcluateResultToDBService.java	1.0 2014年12月12日:上午10:33:28
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.service.impl;

import com.cntest.fxpt.anlaysis.bean.CalculateTask;
import com.cntest.fxpt.anlaysis.bean.TargetResultContainer;
import com.cntest.fxpt.anlaysis.service.ISaveCalcluateResultToDBService;
import com.cntest.util.SpringContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年12月12日 上午10:33:28
 * @version 1.0
 */
public class DefaultSaveCalcluateResultToDBService implements
		ISaveCalcluateResultToDBService {
	private ArrayList<String> sqls = new ArrayList<>();
	private Lock lock = new ReentrantLock();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.anlaysis.service.ISaveCalcluateResultToDBService#save
	 * (com.cntest.fxpt.anlaysis.bean.CalculateTask)
	 */
	@Override
	public void save(CalculateTask event) throws Exception {

		List<String> curEventHasSQLs = getSQL(event);
		ArrayList<String> beExecuteSqls = new ArrayList<>();
		lock.lock();
		try {
			sqls.addAll(curEventHasSQLs);
			if (sqls.size() > 10000) {
				beExecuteSqls.addAll(this.sqls);
				sqls.clear();
			}
		} finally {
			lock.unlock();
		}

		if (beExecuteSqls.size() > 0) {
			saveToDB(beExecuteSqls);
		}
	}

	private List<String> getSQL(CalculateTask event) {
		ArrayList<String> result = new ArrayList<>();
		List<TargetResultContainer> reusltList = event.getCalculateResult(0)
				.getResults();
		for (TargetResultContainer trc : reusltList) {
			result.add(trc.toSQL());
		}
		return result;
	}

	private void saveToDB(List<String> tmpSqls) throws Exception {
		DataSource ds = SpringContext.getBean("ds");
		Connection con = ds.getConnection();
		con.setAutoCommit(false);
		PreparedStatement ps = con.prepareStatement("");
		try {
			for (String sql : tmpSqls) {
//				if(sql.indexOf("0,0.0,0.0")!= -1 )
//				{
//					continue ;
//				}
				ps.addBatch(sql);
			}
			ps.executeBatch();
			con.commit();
			ps.clearBatch();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ps.close();
			con.close();
		}
	}
	
	
//	/**
//	 * ps.executeBatch(); 出错时 单个SQL循环执行定位到对应的SQL语句
//	 */
//	private void saveToDB(List<String> tmpSqls) throws Exception {
//		DataSource ds = SpringContext.getBean("ds");
//		Connection con = ds.getConnection();
//		con.setAutoCommit(false);
//		PreparedStatement ps = con.prepareStatement("");
//
//		int i = 0;
//		String tmpsql = "";
//			try {
//			for (String sql : tmpSqls) {
//				System.out.println(tmpsql);
//				tmpsql = sql;
//				ps.addBatch(sql);
//				ps.executeBatch();
//				con.commit();
//				ps.clearBatch();
//			}
//			}catch (Exception e) {
//				System.out.println("总共执行SQL语句" + (tmpSqls.size()) + "条, 报错SQL语句是第"+(++i)+"条: ");
//				System.out.println(tmpsql);
//				e.printStackTrace();
//				return;
//			} finally {
//				ps.close();
//				con.close();
//			}
//	}

	@Override
	public void clear() throws Exception {
		ArrayList<String> tmpSqls = new ArrayList<>();
		lock.lock();
		try {
			tmpSqls.addAll(this.sqls);
			sqls.clear();
		} finally {
			lock.unlock();
		}

		if (tmpSqls.size() > 0) {
			saveToDB(tmpSqls);
		}
	}

}
