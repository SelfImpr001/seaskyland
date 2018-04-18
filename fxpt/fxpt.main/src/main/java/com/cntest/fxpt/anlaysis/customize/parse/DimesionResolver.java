/*
 * @(#)com.cntest.fxpt.anlaysis.customize.parse.DimesionResolverImpl.java	1.0 2015年4月22日:上午11:23:08
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.customize.parse;

import java.util.List;

import com.cntest.fxpt.anlaysis.customize.bean.FilterWhereValue;
import com.cntest.fxpt.anlaysis.filter.AbstractStudentCjFilter;
import com.cntest.fxpt.anlaysis.filter.StudentAttrFilter;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2015年4月22日 上午11:23:08
 * @version 1.0
 */
public class DimesionResolver {

	public AbstractStudentCjFilter parse(
			List<FilterWhereValue> filterWhereValues) {

		AbstractStudentCjFilter filter = null;
		for (FilterWhereValue filterWhereValue : filterWhereValues) {
			AbstractStudentCjFilter tmpFilter = studentAttr(filterWhereValue);
			if (filter == null) {
				filter = tmpFilter;
			} else {
				filter.next(tmpFilter);
			}
		}

		return filter;
	}

	private AbstractStudentCjFilter studentAttr(
			FilterWhereValue filterWhereValue) {
		AbstractStudentCjFilter filter = null;
		switch (filterWhereValue.getDimesionType()) {
		case PROVINCE:
			filter = createProvinceFilter(filterWhereValue
					.getDimensionAttributeValue());
			break;
		case CITY:
			filter = createCityFilter(filterWhereValue
					.getDimensionAttributeValue());
			break;
		case COUNTY:
			filter = createCountyFilter(filterWhereValue
					.getDimensionAttributeValue());
			break;
		case GENDER:
			filter = createGenerFilter(filterWhereValue
					.getDimensionAttributeValue());
			break;
		case NATION:
			filter = createNationFilter(filterWhereValue
					.getDimensionAttributeValue());
			break;
		case SCHOOL:
			filter = createSchoolFilter(filterWhereValue
					.getDimensionAttributeValue());
			break;
		case WL:
			filter = createWLFilter(filterWhereValue
					.getDimensionAttributeValue());
			break;
		}
		return filter;
	}

	private AbstractStudentCjFilter createProvinceFilter(
			String dimensionAttributeValue) {
		return new StudentAttrFilter("school.education.parent.parent.name",
				dimensionAttributeValue);
	}

	private AbstractStudentCjFilter createCityFilter(
			String dimensionAttributeValue) {
		return new StudentAttrFilter("school.education.parent.name",
				dimensionAttributeValue);
	}

	private AbstractStudentCjFilter createCountyFilter(
			String dimensionAttributeValue) {
		return new StudentAttrFilter("school.education.name",
				dimensionAttributeValue);
	}

	private AbstractStudentCjFilter createWLFilter(
			String dimensionAttributeValue) {
		AbstractStudentCjFilter filter = null;
		if (dimensionAttributeValue.equals("文科")) {
			filter = new StudentAttrFilter("wl", "2");
		} else if (dimensionAttributeValue.equals("理科")) {
			filter = new StudentAttrFilter("wl", "1");
		}
		return filter;
	}

	private AbstractStudentCjFilter createSchoolFilter(
			String dimensionAttributeValue) {
		return new StudentAttrFilter("school.name", dimensionAttributeValue);
	}

	private AbstractStudentCjFilter createNationFilter(
			String dimensionAttributeValue) {
		return new StudentAttrFilter("nation", dimensionAttributeValue);
	}

	private AbstractStudentCjFilter createGenerFilter(
			String dimensionAttributeValue) {
		AbstractStudentCjFilter filter = null;
		if (dimensionAttributeValue.equals("男")) {
			filter = new StudentAttrFilter("gender", "1");
		} else if (dimensionAttributeValue.equals("女")) {
			filter = new StudentAttrFilter("gender", "2");
		}
		return filter;
	}
}
