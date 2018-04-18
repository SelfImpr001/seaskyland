/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.io.excel;

import com.cntest.io.FileReadException;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2015年10月30日
 * @version 1.0
 **/
public class ExcelReaderConcreator {

	/**
	 * 
	 * @param file excel文件绝对路径，包含文件本身
	 * @return
	 */
	public static XlsReader creatReader(String file) {
		if(file.endsWith(".xls")) {
			return new XLSFileReader(file);
		}
		if(file.endsWith(".xlsx")) {
			return new XLSXFileReader(file);
		}
		
		throw new FileReadException("非法文件类型");
	}
}

