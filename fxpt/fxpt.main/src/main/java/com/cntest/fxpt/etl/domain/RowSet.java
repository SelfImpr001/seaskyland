/**
 * 
 */
package com.cntest.fxpt.etl.domain;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 刘海林 描述一行数据
 */
public class RowSet {
	private static final Logger log = LoggerFactory.getLogger(RowSet.class);
	private int rowNumber;
	private RowMetadata rowMetadata = new RowMetadata();
	private List<String> dataSet = new ArrayList<String>();

	public RowSet(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	public RowMetadata getRowMetadata() {
		return rowMetadata;
	}

	public void setRowMetadata(RowMetadata rowMetadata) {
		this.rowMetadata = rowMetadata;
	}

	public void setDataSet(List<String> dataSet) {
		this.dataSet = dataSet;
	}

	public List<String> getDataSet() {
		return dataSet;
	}

	public RowSet append(String data) {
		dataSet.add(data);
		return this;
	}

	public RowSet append(String cellMetadataName, String data) {

		CellMetadata cellMetadata = rowMetadata
				.getcellMetadata(cellMetadataName);

		if (cellMetadata == null) {
			cellMetadata = new CellMetadata(cellMetadataName, dataSet.size());
			rowMetadata.add(cellMetadata);
			append(data);
		} else {
			int columnIndex = cellMetadata.getColumIndex();
			dataSet.set(columnIndex, data);
		}
		return this;
	}

	public String getData(String cellMetadataName) {
		CellMetadata cellMetadata = rowMetadata
				.getcellMetadata(cellMetadataName);

		String data ="";
		if (cellMetadata == null) {
			String message = "存在不匹配字段："+ cellMetadataName +"，请查看导入字段管理中的‘默认对应字段’列与excel字段名称是否匹配！";
			if(!cellMetadataName.equals("languageType")){
				log.debug(message);
				throw new NullPointerException(message);
			}else{
				data = "汉语言";
			}
		}else{
			data = dataSet.get(cellMetadata.getColumIndex());
		}
		return data;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("rowNumber:" + rowNumber);
		for (String key : rowMetadata.keySet()) {
			CellMetadata cellMetadata = rowMetadata.getcellMetadata(key);
			result.append("\n" + cellMetadata.getName() + ":"
					+ dataSet.get(cellMetadata.getColumIndex()));
		}
		return result.toString();
	}

}
