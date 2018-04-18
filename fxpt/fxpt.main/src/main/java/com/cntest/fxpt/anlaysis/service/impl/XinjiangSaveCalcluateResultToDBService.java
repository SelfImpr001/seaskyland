/*
 * @(#)com.cntest.fxpt.anlaysis.service.impl.BatchSaveCalcluateResultToDBService.java	1.0 2014年12月12日:上午10:33:28
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.sql.DataSource;

import com.cntest.fxpt.anlaysis.bean.CalculateTask;
import com.cntest.fxpt.anlaysis.bean.TargetResultContainer;
import com.cntest.fxpt.anlaysis.service.ISaveCalcluateResultToDBService;
import com.cntest.util.SpringContext;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年12月12日 上午10:33:28
 * @version 1.0
 */
public class XinjiangSaveCalcluateResultToDBService implements
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
				ps.addBatch(sql);
			}
			ps.executeBatch();
			con.commit();
			ps.clearBatch();
		} finally {
			ps.close();
			con.close();
		}
		// for (String sql : tmpSqls)
		// System.out.println(sql);
	}

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
