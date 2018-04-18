/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.domain.event;

import com.cntest.common.disruptor.PersistanceEvent;
import com.cntest.common.event.RepositoryEvent;
import com.cntest.fxpt.domain.ExamineeCheckin;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年11月7日
 * @version 1.0
 **/
public class ExamineeCheckinEvent extends RepositoryEvent implements PersistanceEvent{

	ExamineeCheckin checkin;
	
	public ExamineeCheckinEvent(ExamineeCheckin checkin) {
		this.checkin = checkin;
	}
	
	public void checkin() {
		this.checkin.start();
		this.save(this.checkin);
		this.save(checkin.getExamCheckin());
	}
}

