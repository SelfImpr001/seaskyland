/*
 * @(#)com.cntest.fxpt.anlaysis.filter.OrgFilter.java	1.0 2014年12月1日:上午11:31:50
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.filter;

import com.cntest.fxpt.anlaysis.bean.City;
import com.cntest.fxpt.anlaysis.bean.County;
import com.cntest.fxpt.anlaysis.bean.Province;
import com.cntest.fxpt.anlaysis.bean.StudentCj;
import com.cntest.fxpt.anlaysis.uitl.OrgProxy;
import com.cntest.fxpt.domain.Education;
import com.cntest.fxpt.domain.ExamClass;
import com.cntest.fxpt.domain.School;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年12月1日 上午11:31:50
 * @version 1.0
 */
public class OrgFilter extends AbstractStudentCjFilter {
	private Object obj;

	public OrgFilter(Object obj) {
		this.obj = obj;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.anlaysis.filter.IStudentCjFilter#filter(com.cntest.fxpt
	 * .anlaysis.bean.StudentCj)
	 */
	@Override
	public boolean filter(StudentCj studentCj) {
		if (obj instanceof ExamClass) {
			return validateClass(studentCj.getStudent().getClazz(),
					(ExamClass) obj);
		} else if (obj instanceof School) {
			return validateSchool(studentCj.getStudent().getSchool(),
					(School) obj);
		} else if (obj instanceof County) {
			Education e = studentCj.getStudent().getSchool().getEducation();
			return validateCounty(e, (County) obj);
		} else if (obj instanceof City) {
			Education e = studentCj.getStudent().getSchool().getEducation()
					.getParent();
			return validateCity(e, (City) obj);
		} else if (obj instanceof Province) {
			Education e = studentCj.getStudent().getSchool().getEducation()
					.getParent().getParent();
			return validateProvince(e, (Province) obj);
		}
		return false;
	}

	private boolean validateClass(ExamClass src, ExamClass tar) {
		return src.getId().compareTo(tar.getId()) == 0;
	}

	private boolean validateSchool(School src, School tar) {
		return src.getId().compareTo(tar.getId()) == 0;
	}

	private boolean validateCounty(Education src, County tar) {
		return src.getId().compareTo(tar.getId()) == 0;
	}

	private boolean validateCity(Education src, City tar) {
		return src.getId().compareTo(tar.getId()) == 0;
	}

	private boolean validateProvince(Education src, Province tar) {
		return src.getId().compareTo(tar.getId()) == 0;
	}

	@Override
	public String toString() {
		OrgProxy op = new OrgProxy(obj);
		String text = "[" + op.getLevel() + ":" + op.getName() + "]";
		if (this.filter != null) {
			text += "->" + this.filter.toString();
		}
		return text;
	}
}
