/*
 * @(#)com.cntest.fxpt.anlaysis.customize.IAnalysisFilterWhereParse.java	1.0 2015年4月22日:上午10:35:02
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.customize.parse;

import java.util.ArrayList;
import java.util.List;

import com.cntest.fxpt.anlaysis.customize.bean.AnalysisFilterWhere;
import com.cntest.fxpt.anlaysis.customize.bean.AnalysisTask;
import com.cntest.fxpt.anlaysis.customize.bean.FilterWhereValue;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年4月22日 上午10:35:02
 * @version 1.0
 */
public class AnalysisFilterWhereResolver {

	private ArrayList<AnalysisTask> analysisTasks = new ArrayList<>();

	public void parse(AnalysisFilterWhere beParse) {
		if (beParse == null) {
			AnalysisTask at = new AnalysisTask();
			at.setFilterWhereValues(null);
			analysisTasks.add(at);
			return;
		}

		List<FilterWhereValue> filterWhereValues = beParse.list();
		for (FilterWhereValue filterWhereValue : filterWhereValues) {
			ArrayList<FilterWhereValue> theFilterWhereValueList = new ArrayList<>();
			theFilterWhereValueList.add(filterWhereValue);
			parseNext(theFilterWhereValueList, filterWhereValue,
					filterWhereValue.getNextAnalysisFilterWhere());
		}
	}

	private void parseNext(List<FilterWhereValue> srcFilterWhereValueList,
			FilterWhereValue filterWhereValue, AnalysisFilterWhere theNext) {
		if (null == theNext) {
			AnalysisTask at = new AnalysisTask();
			at.setFilterWhereValues(srcFilterWhereValueList);
			analysisTasks.add(at);
			return;
		} else if (theNext.getDimesionGroupType() == filterWhereValue
				.getDimesionGroupType()) {

			AnalysisFilterWhere tmpNext = theNext;

			ArrayList<AnalysisFilterWhere> analysisFilterWheres = new ArrayList<>();
			while (tmpNext != null
					&& tmpNext.getDimesionGroupType() == filterWhereValue
							.getDimesionGroupType()) {
				analysisFilterWheres.add(tmpNext);
				tmpNext = tmpNext.getNextAnalysisFilterWhere();
			}

			ArrayList<FilterWhereValue> theFilterWhereValueList = new ArrayList<>();
			theFilterWhereValueList.addAll(srcFilterWhereValueList);
			parseNext(theFilterWhereValueList, filterWhereValue, tmpNext);

			for (AnalysisFilterWhere afw : analysisFilterWheres) {

				FilterWhereValue tmpFilterWhereValue = null;
				if (srcFilterWhereValueList.size() >= 2) {
					tmpFilterWhereValue = srcFilterWhereValueList
							.get(srcFilterWhereValueList.size() - 2);
				}
				if (!afw.canExec(tmpFilterWhereValue)) {
					continue;
				}

				List<FilterWhereValue> filterWhereValues = afw.list();
				for (FilterWhereValue curFilterWhereValue : filterWhereValues) {
					theFilterWhereValueList = new ArrayList<>();
					theFilterWhereValueList.addAll(srcFilterWhereValueList);
					theFilterWhereValueList.set(
							theFilterWhereValueList.size() - 1,
							curFilterWhereValue);
					parseNext(theFilterWhereValueList, curFilterWhereValue,
							tmpNext);
				}
			}
		} else {
			List<FilterWhereValue> filterWhereValues = theNext.list();
			for (FilterWhereValue curFilterWhereValue : filterWhereValues) {
				ArrayList<FilterWhereValue> theFilterWhereValueList = new ArrayList<>();
				theFilterWhereValueList.addAll(srcFilterWhereValueList);
				theFilterWhereValueList.add(curFilterWhereValue);
				parseNext(theFilterWhereValueList, curFilterWhereValue,
						curFilterWhereValue.getNextAnalysisFilterWhere());
			}
		}
	}

	public List<AnalysisTask> getAnalysisTasks() {
		return analysisTasks;
	}
}
