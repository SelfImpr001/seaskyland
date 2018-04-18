/**
 * 
 */
package com.cntest.fxpt.etl.domain;

/**
 * @author 刘海林 一个单元的元数据
 */
public class CellMetadata {
	private String name;
	private int columIndex;// 所在行的列位置

	public CellMetadata() {

	}

	public CellMetadata(String name, int columIndex) {
		this.name = name;
		this.columIndex = columIndex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getColumIndex() {
		return columIndex;
	}

	public void setColumIndex(int columIndex) {
		this.columIndex = columIndex;
	}

}
