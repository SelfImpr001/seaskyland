/*
 * @(#)com.cntest.fxpt.anlaysis.customize.bean.AnalysisFilterWhere.java	1.0 2015年4月22日:上午9:50:47
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.customize.bean;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年4月22日 上午9:50:47
 * @version 1.0
 */
public class FilterWhereValue {
	private int dimesionGroupType;
	private DimesionType dimesionType;
	private String dimesionName;
	private String dimensionAttributeValue;
	private AnalysisFilterWhere nextAnalysisFilterWhere;

	public DimesionType getDimesionType() {
		return dimesionType;
	}

	public String getDimesionName() {
		return dimesionName;
	}

	public AnalysisFilterWhere getNextAnalysisFilterWhere() {
		return nextAnalysisFilterWhere;
	}

	public String getDimensionAttributeValue() {
		return dimensionAttributeValue;
	}

	public int getDimesionGroupType() {
		return dimesionGroupType;
	}

	public FilterWhereValue setDimensionAttributeValue(
			String dimensionAttributeValue) {
		this.dimensionAttributeValue = dimensionAttributeValue;
		return this;
	}

	public FilterWhereValue setDimesionGroupType(int dimesionGroupType) {
		this.dimesionGroupType = dimesionGroupType;
		return this;
	}

	public FilterWhereValue setNextAnalysisFilterWhere(
			AnalysisFilterWhere nextAnalysisFilterWhere) {
		this.nextAnalysisFilterWhere = nextAnalysisFilterWhere;
		return this;
	}

	public FilterWhereValue setDimesionType(DimesionType dimesionType) {
		this.dimesionType = dimesionType;
		return this;
	}

	public FilterWhereValue setDimesionName(String dimesionName) {
		this.dimesionName = dimesionName;
		return this;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(dimesionName);
		sb.append("[");
		sb.append(dimensionAttributeValue);
		sb.append("]");
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		FilterWhereValue tmp = (FilterWhereValue) obj;
		return (tmp.dimesionName.compareTo(dimesionName) == 0)
				&& (tmp.dimensionAttributeValue
						.compareTo(dimensionAttributeValue) == 0);
	}

}
