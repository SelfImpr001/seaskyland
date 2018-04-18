/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.web.view;

import java.math.BigDecimal;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年11月19日
 * @version 1.0
 **/
public class Progress {
	//private static final int total = 100;
	
	private BigDecimal total;
	
	private BigDecimal completed;
	
	public Progress(Double total,Double completed) {
				
		this.total = new BigDecimal(total);
		this.completed = new BigDecimal(completed);
	}
	
	public Progress(Integer total,Integer completed) {
		this.total = new BigDecimal(total);
		this.completed = new BigDecimal(completed);
	}
	
	public int getTotal() {
		return 100;
	}
	
	public int getLeft() {
		if(completed.compareTo(total)>= 0)
			return 0;
		return new BigDecimal((1 - completed.divide(total,2,2).doubleValue()) * 100).intValue(); 
	}
	
	public int getCompleted() {
		if(total.intValue() <=0)
			return 100;
		return new BigDecimal(completed.divide(total,2,3).doubleValue() * 100).intValue();
	}
	
	public boolean isFinished() {
		return getCompleted() == this.getTotal();
	}
	
	public static void main(String[] args) {
		Progress p1 = new Progress(100,10);
		assert(p1.getLeft()==90);
		assert(p1.getTotal()==100);
		assert(p1.getCompleted()==10);
		
		Progress p2 = new Progress(900,100);
		assert(p2.getLeft()==90);
		assert(p2.getTotal()==100);
		assert(p2.getCompleted()==10);
		
		System.out.println("success");
	}
}

