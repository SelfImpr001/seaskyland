/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.permission;

import org.apache.shiro.authc.AuthenticationToken;

import com.cntest.fxpt.domain.School;
import com.cntest.fxpt.domain.StudentBase;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2014年11月27日
 * @version 1.0
 **/
public class StudentBaseToken implements AuthenticationToken {

//	private String name;
//
//	private String schoolCode;
//
//	private String identity;
	
	private StudentBase student;

	public StudentBaseToken() {
		this.student = new StudentBase.Builder("").create();
	}

	public StudentBaseToken(String name, String identity, String schoolCode) {
		super();
		this.student = new StudentBase.Builder(name).xh(identity).school(new School.Builder(schoolCode).create()).create();
		
	}

	@Override
	public StudentBase getPrincipal() {
		return student;
	}

	@Override
	public Object getCredentials() {
		return student;
	}

	public String getName() {
		return this.student.getName();
	}

	public void setName(String name) {
		this.student.setName(name);
	}

	public String getSchoolCode() {
		return this.student.getSchool().getCode();
	}

	public void setSchoolCode(String schoolCode) {
		this.student.setSchool(new School.Builder(schoolCode).create());
	}

	public String getIdentity() {
		return this.student.getXh();
	}

	public void setIdentity(String identity) {
		this.student.setXh(identity);
	}
	
	public int hashCode() {
		return this.student.hashCode();
	}
	
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof StudentBaseToken)) 
			return false;
		StudentBaseToken other  = (StudentBaseToken)o;
		return this.student.equals(other.student);
	}
	
	public String toString() {
		return this.student.toString();
	}

}
