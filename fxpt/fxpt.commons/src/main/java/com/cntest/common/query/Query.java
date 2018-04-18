/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.common.query;

import java.util.List;
import java.util.Map;

/** 
 * <pre>
 * 查询器
 * </pre>
 *  
 * @author 李贵庆2014年6月23日
 * @version 1.0
 **/
public interface Query<T> {

	public void setParameters(Map<String, String[]> parameters);
	
	public Map<String, String[]> getParameters();
	
	public List<T> getResults();
	
	public void setResults(List<T> list) ;

	public int getCurpage();

	public void setCurpage(int curpage);
	
	public int getPagesize();

	public void setPagesize(int pagesize) ;
	
	public int getTotalRows();

	public void setTotalRows(int totalRows);
	
	public int getTotalpage();

	public void setTotalpage(int totalpage);

	public int getStartRow();

	public void setStartRow(int startRow);
	
	public boolean isHasNext();

	public void setHasNext(boolean hasNext) ;

	public boolean isHasPre();

	public void setHasPre(boolean hasPre);

	public void first();

	public void previous();

	public void next();

	public void last();

	public void check();
}

