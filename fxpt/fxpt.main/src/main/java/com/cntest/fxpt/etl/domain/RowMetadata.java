/**
 * 
 */
package com.cntest.fxpt.etl.domain;

import java.util.HashMap;
import java.util.Set;

/**
 * @author 刘海林 行的数据元数据
 */
public class RowMetadata {
	protected HashMap<String, CellMetadata> cellMetadatas = new HashMap<String, CellMetadata>();

	public RowMetadata add(CellMetadata cellMetadata) {
		String key = cellMetadata.getName().toLowerCase();
		cellMetadatas.put(key, cellMetadata);
		return this;
	}

	public RowMetadata add(RowMetadata rowMetadata) {
		cellMetadatas.putAll(rowMetadata.cellMetadatas);
		return this;
	}

	public Set<String> keySet() {
		return cellMetadatas.keySet();
	}

	public CellMetadata getcellMetadata(String key) {
		return cellMetadatas.get(key!=null?key.toLowerCase():"");
	}

}
