package com.cntest.fxpt.service.etl;

import java.util.List;

import com.cntest.fxpt.bean.DataCategory;


public interface IDataCategoryService {
	
	public void add(DataCategory dataCategory);
	public void delete(DataCategory dataCategory);
	public void update(DataCategory dataCategory);
	public List<DataCategory> list();
	public DataCategory findById(Long dataCategoryId);
	public boolean hasDataField(DataCategory dataCategory);
	public boolean hasTransform(DataCategory dataCategory);
	public boolean hasTableName(DataCategory dataCategory);
}
