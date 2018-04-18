package com.cntest.fxpt.service.etl;

import java.util.List;

import com.cntest.fxpt.bean.DataTransform;


public interface IDataTransformService {

	public void add(DataTransform dataTransform);

	public void delete(DataTransform dataTransform);

	public void update(DataTransform dataTransform);

	public List<DataTransform> list();

	public List<DataTransform> list(Long dataCategoryId);

	public DataTransform findById(Long dataTransformId);
	
	public List<DataTransform> findByTableNameAndType(int schemeType,
			int type, boolean isValid);
}
