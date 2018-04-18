/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.io.excel;

import java.util.List;

import com.cntest.io.FileReadException;


/** 
 * <pre>
 * .xls文件读取接口
 * </pre>
 *  
 * @author 李贵庆2015年10月30日
 * @version 1.0
 **/
public interface XlsReader {

	public void open() throws FileReadException;
	
	public void close();
	
	public void setSheet(String sheetName);
	
	public List<String> getSheetNames();
	
	public List<String> getRow(int row);
	
	public int rowCount();
}

