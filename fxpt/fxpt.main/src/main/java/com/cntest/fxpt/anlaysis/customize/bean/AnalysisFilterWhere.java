/*
 * @(#)com.cntest.fxpt.anlaysis.customize.bean.AnalysisFilterWhere.java	1.0 2015年4月22日:上午9:50:47
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.customize.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年4月22日 上午9:50:47
 * @version 1.0
 */
public class AnalysisFilterWhere {
	private int dimesionGroupType;
	private DimesionType dimesionType;
	private String dimesionName;
	private List<String> dimensionAttributeValues;
	private AnalysisFilterWhere nextAnalysisFilterWhere;

	public DimesionType getDimesionType() {
		return dimesionType;
	}

	public String getDimesionName() {
		return dimesionName;
	}

	public List<String> getDimensionAttributeValues() {
		return dimensionAttributeValues;
	}

	public AnalysisFilterWhere getNextAnalysisFilterWhere() {
		return nextAnalysisFilterWhere;
	}

	public int getDimesionGroupType() {
		return dimesionGroupType;
	}

	public AnalysisFilterWhere setDimesionGroupType(int dimesionGroupType) {
		this.dimesionGroupType = dimesionGroupType;
		return this;
	}

	public AnalysisFilterWhere setNextAnalysisFilterWhere(
			AnalysisFilterWhere nextAnalysisFilterWhere) {
		this.nextAnalysisFilterWhere = nextAnalysisFilterWhere;
		return this;
	}

	public AnalysisFilterWhere setDimesionType(DimesionType dimesionType) {
		this.dimesionType = dimesionType;
		return this;
	}

	public AnalysisFilterWhere setDimesionName(String dimesionName) {
		this.dimesionName = dimesionName;
		return this;
	}

	public AnalysisFilterWhere setDimensionAttributeValues(
			List<String> dimensionAttributeValues) {
		this.dimensionAttributeValues = dimensionAttributeValues;
		return this;
	}

	public List<FilterWhereValue> list() {
		ArrayList<FilterWhereValue> result = new ArrayList<>();
		for (String value : dimensionAttributeValues) {
			FilterWhereValue filter = new FilterWhereValue();
			filter.setDimesionGroupType(dimesionGroupType)
					.setDimesionName(dimesionName)
					.setDimesionType(dimesionType)
					.setDimensionAttributeValue(value)
					.setNextAnalysisFilterWhere(nextAnalysisFilterWhere);
			result.add(filter);
		}
		return result;
	}

	private FilterWhereValue alreadyFilterWhereValue;
	private boolean isFirst = true;

	public boolean canExec(FilterWhereValue filterWhereValue) {
		if ((alreadyFilterWhereValue == null && isFirst)
				|| (alreadyFilterWhereValue != null && !alreadyFilterWhereValue
						.equals(filterWhereValue))) {
			alreadyFilterWhereValue = filterWhereValue;
			isFirst = false;
			return true;
		}
		return false;
	}

	@Override
	public String toString() {

		return dimesionName + ":" + dimensionAttributeValues.toString();
	}

}
