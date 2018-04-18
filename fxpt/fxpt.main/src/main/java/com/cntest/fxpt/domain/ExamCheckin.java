/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.cntest.common.disruptor.DisruptorProxy;
import com.cntest.foura.domain.User;
import com.cntest.fxpt.domain.event.ExamCheckinEvent;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年10月13日
 * @version 1.0
 **/
public class ExamCheckin {

	private Exam exam;
	
	private int status = 0;//９完成核对成功；8半成功，0，未核对
	
	private Date begain;
	
	private Date end;
	
	private int examineeTotal = 0;
	
	private int checkedTotal = 0;	
	
	private int failureTotal = 0;	

	private List<ExamCheckinSpec> specs;
	
	public ExamCheckin(Exam exam,String... checkinSpecs) {
		this.exam = exam;
		if(checkinSpecs != null && checkinSpecs.length >0) {
			specs = new ArrayList<ExamCheckinSpec>();
			for(String s:checkinSpecs) {
				specs.add(new ExamCheckinSpec(this,s,s));
			}
		}
		
	}
	
	public void begainCheckin() {
		this.status = 1;
		this.begain = Calendar.getInstance().getTime();
		DisruptorProxy.getInstance().produce(new ExamCheckinEvent(this), "checkin");
	}
	
	public void endCheckin() {
		this.status = 9;
		this.end = Calendar.getInstance().getTime();
	}
	
	public void successChickin() {
		this.status = 9;
	}
	
	public void addFailure() {
		this.failureTotal ++;
	}

	public void addSuccess() {
		this.checkedTotal ++;
	}
	
	public String[] mySpecs() {
		String [] specs = new String[this.specs.size()];
		for(int i=0;i<this.specs.size();i++) {
			specs[i] = this.specs.get(i).getSpecCode();
		}
		
		return specs;
	}
	
	public int hashCode() {
		return new HashCodeBuilder().append(this.exam).toHashCode();
	}
	
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof User)) 
			return false;
		ExamCheckin other  = (ExamCheckin)o;
		return new EqualsBuilder().append(this.exam,other.exam).isEquals();
	}
	
	public String toString() {
		return new ToStringBuilder(this).append("StudentCheckin",this.pk).append("For exam").append(this.exam != null?this.exam.getName():"").build();
	}
	
	private Long pk = 0l;
	
	public ExamCheckin() {
		
	}


	public Exam getExam() {
		return exam;
	}


	public void setExam(Exam exam) {
		this.exam = exam;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}


	public Date getBegain() {
		return begain;
	}


	public void setBegain(Date begain) {
		this.begain = begain;
	}


	public Date getEnd() {
		return end;
	}


	public void setEnd(Date end) {
		this.end = end;
	}


	public Long getPk() {
		return pk;
	}


	public void setPk(Long pk) {
		this.pk = pk;
	}

	public int getExamineeTotal() {
		return examineeTotal;
	}

	public void setExamineeTotal(int examineeTotal) {
		this.examineeTotal = examineeTotal;
	}

	public int getCheckedTotal() {
		return checkedTotal;
	}

	public void setCheckedTotal(int checkedTotal) {
		this.checkedTotal = checkedTotal;
	}

	public List<ExamCheckinSpec> getSpecs() {
		return specs;
	}

	public void setSpecs(List<ExamCheckinSpec> specs) {
		this.specs = specs;
	}

	public int getFailureTotal() {
		return failureTotal;
	}

	public void setFailureTotal(int failureTotal) {
		this.failureTotal = failureTotal;
	}


	
	
}

