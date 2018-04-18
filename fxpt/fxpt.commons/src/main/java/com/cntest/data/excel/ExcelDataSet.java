/**
 * <p><b>© 1997-2016 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/
package com.cntest.data.excel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.data.DataSet;
import com.cntest.data.Row;
import com.cntest.util.CloseableHelper;
import com.cntest.util.ExceptionHelper;
import com.google.common.io.Files;

/**
 * <pre>
 * Excel数据集读取器
 * </pre>
 * 
 * @author 李贵庆 @2016年1月25日
 * @version 1.0
 **/
public class ExcelDataSet implements DataSet {
	private final static Logger logger = LoggerFactory.getLogger(ExcelDataSet.class);
	
	private InputStream is;

	private Workbook workBook = null;

	private Sheet sheet = null;
	
	private File excelFile;
	
	private int rowNumbers;
	
	//excel文件的首行是定义行
	private int currentRowNumbers;
	
	/**
	 * 从excel文件的第二行开始读取数据
	 * @param excelFile 
	 */
	public ExcelDataSet(File excelFile) {
		this.excelFile = excelFile;
		this.currentRowNumbers = 1;
	}
	
	/**
	 * 从excel指定行开始读取数据
	 * @param excelFile
	 * @param rowOfDataStart
	 */
	public ExcelDataSet(File excelFile,int rowOfDataStart) {
		this.excelFile = excelFile;
		this.currentRowNumbers = rowOfDataStart;
	}
	
	public int size() {
		return this.rowNumbers;
	}

	@Override
	public Row next() {
		org.apache.poi.ss.usermodel.Row row = this.sheet.getRow(this.currentRowNumbers);
		if(row != null) {
			this.currentRowNumbers++;
			return new ExcelRow(row,this.currentRowNumbers);
		}
		return null;
	}

	@Override
	public void close() throws IOException {
		CloseableHelper.close(this.is);
	}

	@Override
	public void open() {
		try {
			this.is = Files.asByteSource(excelFile).openStream();
			if(this.sheet != null)
				return;
			// 判断能否正确解析文件，是否是想要的文件解析内容
			if (!is.markSupported()) {
				is = new PushbackInputStream(is, 8);
			}
			if (POIFSFileSystem.hasPOIFSHeader(is)) {// 2003
				workBook = new HSSFWorkbook(is);
			} else if (POIXMLDocument.hasOOXMLHeader(is)) {// 2007
				// 各种出错，所以手动进行装箱操作
				XSSFWorkbook temp = new XSSFWorkbook(is);
				workBook = temp;
			}
			sheet = workBook.getSheetAt(0);
			
			this.rowNumbers = sheet.getPhysicalNumberOfRows();
		} catch (IOException e) {
			logger.error(ExceptionHelper.trace2String(e));
		}

	}

}
