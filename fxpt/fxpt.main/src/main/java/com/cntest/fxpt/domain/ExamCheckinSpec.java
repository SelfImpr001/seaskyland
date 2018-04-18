/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年11月5日
 * @version 1.0
 **/
public class ExamCheckinSpec {

	private ExamCheckin checkin;
	
	private String specName;
	
	private String specCode;
	
	
	public ExamCheckinSpec(ExamCheckin checkin,String specCode,String specName) {
		this.checkin = checkin;
		this.specCode = specCode;
		this.specName = specName;
	}
	
	
	public int hashCode() {
		return new HashCodeBuilder().append(this.checkin).append(this.specCode).toHashCode();
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof ExamCheckinSpec))
			return false;
		ExamCheckinSpec other = (ExamCheckinSpec) o;
		return new EqualsBuilder().append(this.checkin, other.checkin).append(this.specCode, other.specCode).isEquals();
	}

	public String toString() {
		return new ToStringBuilder(this).append("ExamCheckinSpec For", this.checkin.getExam().getName())
				.append(this.specCode ).build();
	}
	
	private Long pk;
	
	public ExamCheckinSpec() {};

	public ExamCheckin getCheckin() {
		return checkin;
	}

	public void setCheckin(ExamCheckin checkin) {
		this.checkin = checkin;
	}

	public String getSpecName() {
		return specName;
	}

	public void setSpecName(String specName) {
		this.specName = specName;
	}

	public String getSpecCode() {
		return specCode;
	}

	public void setSpecCode(String specCode) {
		this.specCode = specCode;
	}


	public Long getPk() {
		return pk;
	}


	public void setPk(Long pk) {
		this.pk = pk;
	}
}

