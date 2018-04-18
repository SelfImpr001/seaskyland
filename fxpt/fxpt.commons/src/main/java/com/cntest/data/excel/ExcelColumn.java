/**
 * <p><b>© 1997-2016 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/
package com.cntest.data.excel;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;

import com.cntest.data.Column;
import com.google.common.base.Function;

/** s
 * <pre>
 * Excel的一行的中一列
 * </pre>
 *  
 * @author 李贵庆 @2016年1月25日
 * @version 1.0
 **/
public class ExcelColumn implements Column {

	private Cell warperd;
	
	public ExcelColumn(Cell cell) {
		if(cell!=null)
		cell.setCellType(Cell.CELL_TYPE_STRING);
		this.warperd = cell;
	}
	
	@Override
	public <T> T value(Function<Object,T> function) {
		Type[] genType = function.getClass().getGenericInterfaces();
		Type[] types = ((ParameterizedType)genType[0]).getActualTypeArguments();
		if(types[1].equals(Integer.class)){
			int value = new Double(this.warperd.getNumericCellValue()).intValue();
			return function.apply(value);
		}
		
		if(types[1].equals(Double.class)){
			return function.apply(new Double(this.warperd.getNumericCellValue()));
		}
		
		if(types[1].equals(Date.class)){
			Date date = this.warperd.getDateCellValue();
			return function.apply(date);
		}
		
		return function.apply(this.warperd.getStringCellValue());
	}
	
	@Override
	public String value() {
		if(warperd!=null && warperd.getStringCellValue()!=null)
			return warperd.getStringCellValue();
		else
			return "";
	}

}
