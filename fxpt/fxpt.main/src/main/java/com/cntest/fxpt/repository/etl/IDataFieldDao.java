/*
 * @(#)com.cntest.fxpt.repository.etl.IDataCategoryDao.java	1.0 2014年5月13日:下午3:44:32
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository.etl;

import java.util.HashMap;
import java.util.List;

import com.cntest.fxpt.bean.DataField;


/**
 * <Pre>
 * etl操作的数据类型，比如学生信息，成绩，细目表等等
 * </Pre>
 * 
 * @author 刘海林 2014年5月13日 下午3:44:32
 * @version 1.0
 */
public interface IDataFieldDao {
	public void add(DataField dataField);

	public void delete(DataField dataField);

	public void update(DataField dataField);

	public DataField findDataCategoryById(Long dataFieldId);

	public List<DataField> findByDataCategoryId(Long dataCategoryId);

	public List<DataField> findByDataCategoryId(Long dataCategoryId,
			boolean isValid);

	public List<DataField> list();

	public List<DataField> list(int schemeType, boolean isValid);
	
	public List<DataField> list(int schemeType, boolean isValid, boolean isNeed);
	
	public boolean hasFiled(DataField dataField);
	/**
	 * 得要需要显示与否的Map列表
	 * @param type
	 * @return
	 */
	public HashMap getDateByType(String type);
	
	/**
	 * 获取明细表考试名称的字段说明
	 * @param schemeType  类型
	 * @param fieldName   字段名称
	 * @return
	 */
	public String getTestPaperDescription(int schemeType,String fieldName);
	
	/**
	 * 验证导入字段与设置必须导入子段是否匹配
	 * @param listHead
	 * @param schemeType
	 * @return
	 */
	public String initDataHead(List<String> listHead,int schemeType);
	
}
