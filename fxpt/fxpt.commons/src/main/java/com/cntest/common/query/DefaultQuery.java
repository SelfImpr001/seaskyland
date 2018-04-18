/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.common.query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年6月23日
 * @version 1.0
 **/
public class DefaultQuery<T> implements Query<T> {
	

	public DefaultQuery() {
		super();
	}

	private int startRow;

	private int curpage;

	private int pagesize;

	private int totalpage;

	private int totalRows;

	private List<T> list;

	private boolean hasNext;

	private boolean hasPre;
	
	private Map<String, String[]> parameters = new HashMap<String, String[]>();

	public Map<String, String[]> getParameters() {
		return parameters;
	}
	
	public static class Builder<T> {
		private DefaultQuery<T> query ;
		
		public Builder() {
			this.query = new DefaultQuery<T>();
		}
		
		public Builder<T> startRow(int startRow) {
			this.query.startRow = startRow;
			return this;
		}
		
		public Builder<T> curpage(int curpage) {
			this.query.curpage = curpage;
			return this;
		}
		
		public Builder<T> pagesize(int pagesize) {
			this.query.pagesize = pagesize;
			return this;
		}
		
		public Builder<T> totalpage(int totalpage) {
			this.query.totalpage = totalpage;
			return this;
		}
		
		public Builder<T> totalRows(int totalRows) {
			this.query.totalRows = totalRows;
			return this;
		}
		
		public DefaultQuery<T> create(){
			return this.query;
		}
	}

	public void setParameters(Map<String, String[]> parameters) {
		this.parameters = parameters;
	}

	public int getCurpage() {
		return curpage;
	}

	public void setCurpage(int curpage) {
		this.curpage = curpage;
	}

	public List<T> getResults() {
		return list;
	}

	public void setResults(List<T> list) {
		this.list = list;
	}

	public int getPagesize() {
		if (this.pagesize == 0) {
			this.pagesize = 10;
		}
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
		this.totalpage = totalRows / getPagesize();
		if (totalRows <= pagesize) {
			this.curpage = 1;
		}
		if (totalRows % pagesize > 0) {
			this.totalpage = this.totalpage + 1;
		}
		if (this.curpage > totalpage) {
			this.curpage = totalpage;
		}
		if (this.totalpage == 0) {
			this.totalpage = 1;
		}

		if (getCurpage() < totalpage) {
			this.hasNext = true;
		}

		if (getCurpage() > 1 && totalpage > 1) {
			this.hasPre = true;
		}
	}

	public int getTotalpage() {
		return totalpage;
	}

	public void setTotalpage(int totalpage) {
		this.totalpage = totalpage;
	}

	public int getStartRow() {
		this.startRow = (this.curpage - 1) * this.pagesize;
		if (this.startRow < 0) {
			this.startRow = 0;
		}
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public boolean isHasNext() {
		return hasNext;
	}

	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}

	public boolean isHasPre() {
		return hasPre;
	}

	public void setHasPre(boolean hasPre) {
		this.hasPre = hasPre;
	}

	public void first() {
		this.curpage = 1;
		this.startRow = 0;
	}

	public void previous() {

		this.curpage--;
		this.startRow = (this.curpage - 1) * this.pagesize;
		check();
	}

	public void next() {
		if (this.curpage < this.totalpage) {
			this.curpage++;
		}
		this.startRow = (this.curpage - 1) * this.pagesize;
		check();
	}

	public void last() {
		this.curpage = this.totalpage;
		this.startRow = (this.curpage - 1) * this.pagesize;
		check();
	}

	public void check() {
		if (this.curpage <= 1) {
			this.curpage = 1;
			this.startRow = 0;
		}

	}
}

