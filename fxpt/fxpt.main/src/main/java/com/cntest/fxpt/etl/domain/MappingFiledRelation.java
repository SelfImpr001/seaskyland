/*
 * @(#)com.cntest.fxpt.etl.domain.MappingFiledRelation.java	1.0 2014年5月10日:上午10:29:47
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.domain;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月10日 上午10:29:47
 * @version 1.0
 */
public class MappingFiledRelation {
	private CellMetadata from;
	private CellMetadata to;

	
	public static MappingFiledRelation create(String fromFiledName, String toFiledName){
		MappingFiledRelation mfr = new MappingFiledRelation(createCellMetadata(fromFiledName), createCellMetadata(toFiledName));
		return mfr;
	}
	public static MappingFiledRelation create(String fromFiledName, CellMetadata to){
		MappingFiledRelation mfr = new MappingFiledRelation(createCellMetadata(fromFiledName), to);
		return mfr;
	}
	public static MappingFiledRelation create(CellMetadata from, CellMetadata to){
		MappingFiledRelation mfr = new MappingFiledRelation(from, to);
		return mfr;
	}
	
	private static CellMetadata createCellMetadata(String name){
		CellMetadata cellMetadata = new CellMetadata();
		cellMetadata.setName(name);
		return cellMetadata;
	}

	private MappingFiledRelation(CellMetadata from, CellMetadata to) {
		this.from = from;
		this.to = to;
	}
	public CellMetadata getFrom() {
		return from;
	}
	public void setFrom(CellMetadata from) {
		this.from = from;
	}
	public CellMetadata getTo() {
		return to;
	}
	public void setTo(CellMetadata to) {
		this.to = to;
	}
	
	@Override
	public String toString(){
		return from.getName()+"->"+to.getName();
	}
}
