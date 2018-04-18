/*
 * @(#)com.excle2007.ReaderXlsx.java	1.0 2014年5月28日:下午1:32:16
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.util.excle2007;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.xerces.parsers.IInterrupte;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月28日 下午1:32:16
 * @version 1.0
 */
public class ReaderXlsxUtil extends DefaultHandler implements IInterrupte {

	private OPCPackage pkg;
	private XSSFReader reader;
	private List<String> sheetNames;

	private StringBuffer sbValue = new StringBuffer();
	private SharedStringsTable shareData;
	private String typeType;
	private boolean isInterrupte = false;
	private int curRowIndex;
	private int curCellIndex;

	private int rows;
	private int columns;
	private IRowOpt rowOption = new DefaultRowOpt();

	private HashMap<Integer, String> rowMap = new HashMap<Integer, String>();

	public void setRowOption(IRowOpt rowOption) {
		this.rowOption = rowOption;
	}

	public List<String> getSheetNames() {
		return sheetNames;
	}

	public int getAllRows() {
		return this.rows;
	}
	
	public void open(String filePath) throws Exception {
		pkg = OPCPackage.open(filePath);
		reader = new XSSFReader(pkg);
		shareData = reader.getSharedStringsTable();
		XSSFReader.SheetIterator it = (XSSFReader.SheetIterator) reader
				.getSheetsData();
		sheetNames = new ArrayList<String>();
		while (it.hasNext()) {
			InputStream inStream = it.next();
			sheetNames.add(it.getSheetName());
		}
	}

	public void processSheet(String sheetName) throws Exception {
		checkInterrupte();
		XSSFReader.SheetIterator it = (XSSFReader.SheetIterator) reader
				.getSheetsData();
		while (it.hasNext()) {
			InputStream inStream = it.next();
			if (it.getSheetName().equalsIgnoreCase(sheetName)) {
				process(inStream);
			}
		}
	}

	private void checkInterrupte() {
		isInterrupte = isInterrupte ? false : isInterrupte;
	}

	public void close() throws Exception {
		if (pkg != null) {
			pkg.close();
		}
	}

	private void process(InputStream in) throws Exception {
		InputSource sheetSource = new InputSource(in);
		XMLReader parser = XMLReaderFactory
				.createXMLReader("org.apache.xerces.parsers.SAXParser");
		parser.setContentHandler(this);
		parser.parse(sheetSource);
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		if (qName.equalsIgnoreCase("dimension")) {
			String ref = attributes.getValue("ref");
			calculateRowNumberAndColumnNumber(ref);
		}

		if (qName.equalsIgnoreCase("row")) {
			String rowIndex = attributes.getValue("r");
			curRowIndex = Integer.parseInt(rowIndex);

			rowMap.clear();
		}

		if (qName.equalsIgnoreCase("c")) {
			String cellIndex = attributes.getValue("r");
			curCellIndex = getStrChar(cellIndex);

			typeType = attributes.getValue("t");
			if (typeType == null) {
				typeType = "";
			}
		}

		if (qName.equalsIgnoreCase("v")) {
			sbValue.setLength(0);
		}
	}

	private void calculateRowNumberAndColumnNumber(String ref) {
		String[] refs = ref.split(":");
		rows = getStrNumber(refs[1]);
		columns = getStrChar(refs[1]);
	}

	public int getRows() {
		return rows;
	}

	private int getStrNumber(String str) {
		char[] columnChars = str.toUpperCase().toCharArray();
		int result = 0;
		StringBuffer sb = new StringBuffer();
		for (char tmpChar : columnChars) {
			if (tmpChar >= '0' && tmpChar <= '9') {
				sb.append(tmpChar);
			}
		}
		result = Integer.parseInt(sb.toString());
		return result;
	}

	private int getStrChar(String str) {
		char[] columnChars = str.toUpperCase().toCharArray();

		ArrayList<Integer> nums = new ArrayList<Integer>();
		for (char tmpChar : columnChars) {
			if (tmpChar >= 'A' && tmpChar <= 'Z') {
				int tempIdx = (tmpChar - 64);
				nums.add(tempIdx);
			}
		}
		int result = 0;
		int size = nums.size() - 1;
		for (int num : nums) {
			result += (int) Math.pow(26, size--) * num;
		}
		return result;
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		if (qName.equalsIgnoreCase("v")) {
			System.out.print("\t");
			String tmpValue = "";
			if (typeType.equalsIgnoreCase("s")) {
				int idx = Integer.parseInt(sbValue.toString());
				tmpValue = new XSSFRichTextString(shareData.getEntryAt(idx))
						.toString();
			} else {
				tmpValue = sbValue.toString();
			}
			rowMap.put(curCellIndex, tmpValue);
		}

		if (qName.equalsIgnoreCase("row")) {
			ArrayList<String> rowData = new ArrayList<String>();
			for (int i = 1; i <= columns; i++) {
				String value = rowMap.get(i);
				value = value == null ? "" : value;
				rowData.add(value.trim());
			}
			rowOption.row(curRowIndex, rowData);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		sbValue.append(ch, start, length);
	}

	public void setInterrupted(boolean isInterrupte) {
		this.isInterrupte = isInterrupte;
	}

	@Override
	public boolean interrupted() {
		return isInterrupte;
	}

	@Override
	public void endDocument() throws SAXException {
		System.out.println();
	}

}
