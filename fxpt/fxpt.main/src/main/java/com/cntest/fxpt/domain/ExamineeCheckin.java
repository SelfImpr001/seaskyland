/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.domain;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.cntest.common.domain.Entity;
import com.cntest.common.specification.Specification;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2014年10月28日
 * @version 1.0
 **/
public class ExamineeCheckin implements Entity{

	private ExamCheckin examCheckin;

	private ExamStudent examinee;
	
	private Long examineeId;

	private int status;//9成功，８不成功

	private Date checkinDate;
	
	//private ExamineeCheckinHandler handler;
	
	private Specification<ExamStudent> spec;
	
	public ExamineeCheckin(ExamCheckin examCheckin,ExamStudent examinee,Specification<ExamStudent> spec) {
		this.examCheckin = examCheckin;
		this.examinee = examinee;
		this.examineeId = examinee.getId();
		this.status = 0;
		this.checkinDate = Calendar.getInstance().getTime();
		//this.handler = ExamineeCheckinHandler.newInstance(); 
		this.spec = spec;
	}
	
	public boolean start() {
		if(spec.isInSatified(this.examinee)){
			this.status = 9;
			this.examCheckin.addSuccess();
			return true;
		}else {
			this.examCheckin.addFailure();
			this.status = 8;
			return false;
		}
		//DisruptorProxy.getInstance().produce(this, "save");
		
		//AsyncEventProducer.newInstanceFor(handler).produce(this,"checkin");
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.examCheckin).append(examinee).toHashCode();
	}

	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof ExamineeCheckin))
			return false;
		ExamineeCheckin other = (ExamineeCheckin) o;
		return new EqualsBuilder().append(this.examCheckin, other.examCheckin).append(this.examinee, other.examinee).isEquals();
	}

	public String toString() {
		return new ToStringBuilder(this).append("Examinee", this.examinee.getName()).append("For exam")
				.append(this.examCheckin != null ? this.examCheckin.getExam() : "").build();
	}

	private Long pk;

	public ExamineeCheckin() {
		//this.handler = ExamineeCheckinHandler.newInstance();; 
	}
	
	public ExamCheckin getExamCheckin() {
		return examCheckin;
	}

	public void setExamCheckin(ExamCheckin examCheckin) {
		this.examCheckin = examCheckin;
	}

	public ExamStudent getExaminee() {
		return examinee;
	}

	public void setExaminee(ExamStudent examinee) {
		this.examinee = examinee;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCheckinDate() {
		return checkinDate;
	}

	public void setCheckinDate(Date checkinDate) {
		this.checkinDate = checkinDate;
	}

	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}

	public Long getExamineeId() {
		return examineeId;
	}

	public void setExamineeId(Long examineeId) {
		this.examineeId = examineeId;
	}
	
	
}
