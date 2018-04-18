/*
 * @(#)com.cntest.fxpt.etl.module.MapinngFiled.java	1.0 2014年5月10日:上午10:15:13
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.module;

import java.util.ArrayList;
import java.util.HashMap;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import ch.qos.logback.core.joran.action.NewRuleAction;

import com.cntest.fxpt.etl.BaseStep;
import com.cntest.fxpt.etl.IEtlContext;
import com.cntest.fxpt.etl.domain.CellMetadata;
import com.cntest.fxpt.etl.domain.MappingFiledRelation;
import com.cntest.fxpt.etl.domain.RowMetadata;
import com.cntest.fxpt.etl.domain.RowSet;

/**
 * <Pre>
 * 映射字段
 * </Pre>
 * 
 * @author 刘海林 2014年5月10日 上午10:15:13
 * @version 1.0
 */
public class MapinngFiled extends BaseStep {
	private static final Logger log = LoggerFactory
			.getLogger(MapinngFiled.class);
	private ArrayList<MappingFiledRelation> mappingFileds = new ArrayList<MappingFiledRelation>();
	private RowMetadata rowMetadata = new RowMetadata();

	public MapinngFiled(String name, IEtlContext context) {
		super(name, context);
	}

	public MapinngFiled appendMapingFieldRalation(String fromFiledName,
			String toFiledName) {
		mappingFileds.add(MappingFiledRelation.create(fromFiledName,
				toFiledName));
		return this;
	}

	@Override
	public void begin() throws Exception {
		log.debug("初始化步骤"+getName());
		int index = 0;
		log.debug("映射关系：");
		for (MappingFiledRelation mappingFiled : mappingFileds) {
			CellMetadata cellMetadata = mappingFiled.getTo();
			cellMetadata.setColumIndex(index++);
			rowMetadata.add(cellMetadata);
			log.debug(mappingFiled.toString());
		}
		log.debug("初始化成功");
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub

	}

	@Override
	public void excuteStep() throws Exception {
		log.debug("执行步骤" + getName());
		RowSet row = null;
		while ((row = getRow()) != null) {
			RowSet newRow = new RowSet(row.getRowNumber());
			newRow.setRowMetadata(rowMetadata);
			for (MappingFiledRelation mappingFiled : mappingFileds) {
					newRow.append(row.getData(mappingFiled.getFrom().getName()));	
			}
			putRow(newRow);
		}
		super.excuteStep();
//		log.debug("执行步骤成功");
	}

}
