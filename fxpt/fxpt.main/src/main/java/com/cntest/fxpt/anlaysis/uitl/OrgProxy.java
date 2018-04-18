/*
 * @(#)com.cntest.fxpt.anlaysis.uitl.OrgProxy.java	1.0 2014年12月4日:上午9:32:34
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.uitl;

import com.cntest.fxpt.anlaysis.bean.City;
import com.cntest.fxpt.anlaysis.bean.County;
import com.cntest.fxpt.anlaysis.bean.Province;
import com.cntest.fxpt.domain.ExamClass;
import com.cntest.fxpt.domain.School;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年12月4日 上午9:32:34
 * @version 1.0
 */
public class OrgProxy {

	private Long id;
	private String name;
	private String tableType;
	private boolean isClazz = false;
	private int level;

	public OrgProxy(Object obj) {
		init(obj);
	}

	private void init(Object obj) {
		if(obj==null){
			return;
		}
		if (obj instanceof ExamClass) {
			ExamClass o = (ExamClass) obj;
			name = o.getSchoolName() + ":" + o.getName();
			id = o.getId();
			tableType = "class";
			isClazz = true;
			level = 5;
		} else if (obj instanceof School) {
			School o = (School) obj;
			name = o.getName();
			id = o.getId();
			tableType = "school";
			level = 4;
		} else if (obj instanceof County) {
			County o = (County) obj;
			name = o.getName();
			id = o.getId();
			tableType = "county";
			level = 3;
		} else if (obj instanceof City) {
			City o = (City) obj;
			name = o.getName();
			id = o.getId();
			tableType = "city";
			level = 2;
		} else if (obj instanceof Province) {
			Province o = (Province) obj;
			name = o.getName();
			id = o.getId();
			tableType = "province";
			level = 1;
		}else{
			System.out.println("未定义的组织级别");
		}
	}

	public String getTableType() {
		return tableType;
	}

	public Long getId() {
		return id;
	}

	public String getName() {

		return name;
	}

	public boolean isClazz() {
		return isClazz;
	}

	public int getLevel() {
		return level;
	}

}
