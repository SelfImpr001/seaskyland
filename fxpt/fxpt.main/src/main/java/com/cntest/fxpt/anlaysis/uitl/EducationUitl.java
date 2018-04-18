/*
 * @(#)com.cntest.fxpt.anlaysis.uitl.EducationUitl.java	1.0 2014年12月3日:下午4:27:33
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.uitl;

import com.cntest.fxpt.anlaysis.bean.City;
import com.cntest.fxpt.anlaysis.bean.County;
import com.cntest.fxpt.anlaysis.bean.Province;
import com.cntest.fxpt.domain.Education;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年12月3日 下午4:27:33
 * @version 1.0
 */
public class EducationUitl {
	public static Province createProvince(Education e) {
		Province c = new Province();
		c.setId(e.getId());
		c.setCode(e.getCode());
		c.setName(e.getName());
		return c;
	}

	public static City createCity(Education e) {
		City c = new City();
		c.setId(e.getId());
		c.setCode(e.getCode());
		c.setName(e.getName());
		return c;
	}

	public static County createCounty(Education e) {
		County c = new County();
		c.setId(e.getId());
		c.setCode(e.getCode());
		c.setName(e.getName());
		return c;
	}
}
