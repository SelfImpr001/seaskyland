package com.cntest.common.page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.util.ExceptionHelper;

public class Page<T> {
	private static final Logger log = LoggerFactory.getLogger(Page.class);

	public Page() {
	}

	private int startRow;

	private int curpage;

	private int pagesize;

	private int totalpage;

	private int totalRows;

	private List<T> list;

	private boolean hasNext;

	private boolean hasPre;

	private Map<String, String> parameter = new HashMap<String, String>();

	public boolean hasNext() {
		if(this.getCurpage() < this.getTotalpage()) {
			this.setCurpage(this.getCurpage() +1);
			return true;
		}
		return false;
	}

	public Map<String, String> getParameter() {
		return parameter;
	}

	public void addParameter(String name, String value) {
		this.parameter.put(name, value);
	}

	public Page setParameter(Map<String, String> parameter) {
		this.parameter = parameter;
		return this;
	}

	public int getCurpage() {
		return curpage;
	}

	public Page setCurpage(int curpage) {
		this.curpage = curpage;
		return this;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public int getPagesize() {
		if (this.pagesize == 0) {
			this.pagesize = 10;
		}
		return pagesize;
	}

	public Page setPagesize(int pagesize) {
		this.pagesize = pagesize;
		return this;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public Page setTotalRows(int totalRows) {
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
		return this;
	}

	public int getTotalpage() {
		return totalpage;
	}

	public boolean hasParam(String paramName) {
		String value = parameter.get(paramName);
		return value != null && !"".equals(value);
	}

	public String getString(String paramName) {
		return parameter.get(paramName);
	}

	public Integer getInteger(String paramName) {
		Integer tmp = 0;
		try {
			tmp = Integer.parseInt(getString(paramName));
		} catch (Exception e) {
			log.error("获取参数的时候，转换" + paramName + "字符串“" + getString(paramName)
					+ "”为int的时候出错");
			log.error(ExceptionHelper.trace2String(e));
		}
		return tmp;
	}

	public Long getLong(String paramName) {
		Long tmp = 0L;
		try {
			tmp = Long.parseLong(getString(paramName));
		} catch (Exception e) {
			log.error("获取参数的时候，转换" + paramName + "字符串“" + getString(paramName)
					+ "”为long的时候出错");
			log.error(ExceptionHelper.trace2String(e));
		}
		return tmp;
	}

	public Boolean getBoolean(String paramName) {
		Boolean tmp = false;
		try {
			tmp = Boolean.valueOf(getString(paramName));
		} catch (Exception e) {
			log.error("获取参数值，转换" + paramName + "字符串“" + getString(paramName)
					+ "”为Boolean的时候出错");
			log.error(ExceptionHelper.trace2String(e));
		}
		return tmp;
	}
}
