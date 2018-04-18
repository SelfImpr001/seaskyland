/**
 * <p><b>© 1997-2016 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/
package com.cntest.data.excel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;

import com.cntest.data.Column;
import com.cntest.data.Row;
import com.google.common.collect.ImmutableList;

/** 
 * <pre>
 * Excel的列包装
 * </pre>
 *  
 * @author 李贵庆 @2016年1月25日
 * @version 1.0
 **/
public class ExcelRow implements Row {

	private List<Column> cols;
	
	private int size = 0;
	
	private int rowNum = 0;
	
	private org.apache.poi.ss.usermodel.Row excelRow;
	
	public ExcelRow(org.apache.poi.ss.usermodel.Row excelRow) {
		this.excelRow = excelRow;
	}
	
	public ExcelRow(org.apache.poi.ss.usermodel.Row excelRow,int rowNum) {
		this.excelRow = excelRow;
		this.rowNum = rowNum;
	}
	
	@Override
	public List<Column> cols() {
		if(this.cols == null) {
			ArrayList<Column> celss = new ArrayList<>();
			org.apache.poi.ss.usermodel.Row row = this.excelRow;
			for(int col = 0;col<row.getLastCellNum();col++) {
				Cell cell = row.getCell(col);
				this.size++;
				celss.add(new ExcelColumn(cell));
			}
			
			/*
			 * 陈勇改于2017-4-12
			Iterator<Cell> cells =  this.excelRow.iterator();
			while(cells.hasNext()){
				Cell cell = cells.next();
				this.size++;
				celss.add(new ExcelColumn(cell));
			}*/
			
			this.cols = ImmutableList.copyOf(celss);
			celss.clear();
		}
		return this.cols;
	}

	@Override
	public Column colAt(int position) {
		if(position <= this.size && position >0) {
			return this.cols.get(position-1);
		}
		return null;
	}

	@Override
	public int size() {
		return this.size;
	}

	@Override
	public int rowNumber() {
		return rowNum;
	}

	public String toString() {
		return rowNum+"";
	}
}
